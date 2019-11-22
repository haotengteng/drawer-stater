package com.bee.ms.drawer.ident;

import com.bee.ms.common.client.ClientResult;
import com.bee.ms.drawer.autoconfigure.DrawerProperties;
import com.bee.ms.drawer.exception.ErrorCodeEnum;
import com.bee.ms.drawer.exception.ServiceException;
import com.bee.ms.drawer.filter.LogFilter;
import com.bee.ms.ident.client.IdentClient;
import com.bee.ms.ident.dto.MultiIdDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 取号器
 *
 * @author created by htt on 2018/8/10
 */
@Slf4j
@Configuration
//@ConditionalOnProperty(name = "drawer.ident-tooker.enable", havingValue = "true", matchIfMissing = false)
@ConditionalOnClass(value = IdentClient.class)
public class IdentTooker {


    private static DrawerProperties drawerProperties;

    private static IdentFeignClient identFeignClient;

    @Autowired
    public void setIdentFeignClient(IdentFeignClient identFeignClient) {
        IdentTooker.identFeignClient = identFeignClient;
    }

    @Autowired
    public void setDrawerProperties(DrawerProperties drawerProperties) {
        IdentTooker.drawerProperties = drawerProperties;
    }


    private static final LinkedBlockingQueue<Long> IDENT_POOL = new LinkedBlockingQueue<>();
    private static final LinkedBlockingQueue<Boolean> IDENT_POOL_LISTEN = new LinkedBlockingQueue<>();

    private static final ExecutorService SINGLE_THREAD_EXECUTOR = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(), r -> new Thread(r, "id-took-thread"),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static Long take() {
        log.info("当前取号池剩余:{}个号码", IDENT_POOL.size());
        try {
            // 如果号码池为空，说明出现异常，直接调用id生成器
            if (0 == IDENT_POOL.size()) {
                log.warn("取号线程异常中断，当前系统直接从生成器获取id");
                ClientResult<Long> clientResult = identFeignClient.make();
                if (1 == clientResult.getCode()) {
                    return clientResult.getData();
                }
            }
            Long id = IDENT_POOL.poll(10, TimeUnit.SECONDS);
            if (IDENT_POOL.size() < drawerProperties.getIdentTooker().getLimitAllowExistNumber()) {
                synchronized (listenRunnable) {
                    listenRunnable.notify();
                }
            }
            return id;
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            throw new ServiceException(ErrorCodeEnum.ERROR_INTERNAL_ERROR.customMassage("取号超时,请确保已启用id取号器功能"));
        }
    }

    private static void offer(Long... ids) {
        for (Long id : ids) {
            try {
                IDENT_POOL.offer(id, 1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                log.warn("插入新号码失败");
            }
        }
    }

    private static ListenRunnable listenRunnable = new ListenRunnable();

    @PostConstruct
    public void initIdentPool() {
        SINGLE_THREAD_EXECUTOR.execute(listenRunnable);
    }

    private static class ListenRunnable implements Runnable {
        @Override
        public void run() {
            // 队列里号码小于阀值时，开始下一次获取
            while (true) {
                synchronized (listenRunnable) {
                    if (IDENT_POOL.size() > drawerProperties.getIdentTooker().getLimitAllowExistNumber()) {
                        try {
                            listenRunnable.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        log.info("取号池号码过少，获取新号码");
                        Integer step = drawerProperties.getIdentTooker().getPerRequestStep();
                        step = step > 100 ? 100 : step;
                        // 取到设置的最大个数的号码，可能会超过
                        int count = (int) Math.ceil((drawerProperties.getIdentTooker().getMaxKeepNumber() / step));
                        count = 0 == count ? 1 : count;
                        for (int i = 0; i < count; i++) {
                            ClientResult<MultiIdDTO> clientResult = identFeignClient.make(step);
                            if (1 == clientResult.getCode()) {
                                MultiIdDTO multiIdDTO = clientResult.getData();
                                offer(multiIdDTO.getIds());
                            }
                        }
                    }
                }
            }
        }
    }
}

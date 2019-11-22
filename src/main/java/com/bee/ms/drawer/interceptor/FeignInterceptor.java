package com.bee.ms.drawer.interceptor;

import com.bee.ms.drawer.BeeContext;
import com.bee.ms.drawer.BeeTools;
import com.bee.ms.drawer.PlatformIsTransparent;
import com.bee.ms.drawer.Shop;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * feign 请求头拦截添加
 *
 * @author created by htt on 2018/8/7
 */
@ConditionalOnClass(RequestInterceptor.class)
@Configuration
public class FeignInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {

        BeeContext beeContext = BeeTools.beeContext();
        if (beeContext != null && Objects.nonNull(BeeTools.beeContext().getHeaderTransparentMap())) {
            Map<Class, Boolean> map = BeeTools.beeContext().getHeaderTransparentMap();
            Set<Class> clazzs = map.keySet();
            for (Class clazz : clazzs) {
                if (clazz.getTypeName().equals(Shop.class.getTypeName())) {
                    Long shopId = BeeTools.getExistShopId();
                    List<String> value = new ArrayList<>();
                    value.add(Objects.isNull(shopId) ? "" : String.valueOf(shopId));
                    requestTemplate.header("shop-id", value);
                } else if (clazz.getTypeName().equals(PlatformIsTransparent.class.getTypeName())) {
                    String platform = BeeTools.platForm().getCode();
                    List<String> value = new ArrayList<>();
                    value.add(platform);
                    requestTemplate.header("platform", value);
                }
            }
        }
    }
}

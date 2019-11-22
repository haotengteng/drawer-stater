package com.bee.ms.drawer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bee.ms.common.client.ClientResult;
import com.bee.ms.common.enums.PlatformEnum;
import com.bee.ms.common.portal.core.PlatformContext;
import com.bee.ms.drawer.ident.IdentTooker;
import org.springframework.util.StringUtils;
import com.bee.ms.drawer.exception.ErrorCodeEnum;
import com.bee.ms.drawer.exception.ServiceException;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

/**
 * 系统工具类
 *
 * @author Created by haoteng on 2018/6/28.
 */
public class BeeTools {

    public static ThreadLocal<BeeContext> contextThreadLocal = new ThreadLocal<>();

    /**
     * 获取店铺id
     *
     * @return
     */
    public static Long shopId() {
        BeeContext beeContext = contextThreadLocal.get();
        if (Objects.isNull(beeContext) || Objects.isNull(beeContext.getShopId())) {
            throw new ServiceException(ErrorCodeEnum.ERROR_ILLEGAL_PARAMETER.customMassage("店铺Id未上传"));
        }
        return beeContext.getShopId();
    }


    /**
     * 获取店铺id (有返回 没有返回null)
     *
     * @return
     */
    public static Long getExistShopId() {
        BeeContext beeContext = contextThreadLocal.get();
        if (Objects.isNull(beeContext) || Objects.isNull(beeContext.getShopId())) {
            return null;
        }
        return beeContext.getShopId();
    }

    /**
     * 获取用户Id
     *
     * @return
     */
    public static Long consumerId() {
        // TODO: 2018/8/6
        return null;
    }

    /**
     * 获取平台类型
     *
     * @return
     */
    public static PlatformEnum platForm() {
        BeeContext beeContext = contextThreadLocal.get();
        if (Objects.isNull(beeContext) || Objects.isNull(beeContext.getPlatForm())) {
            throw new ServiceException(ErrorCodeEnum.ERROR_ILLEGAL_PARAMETER.customMassage("平台编号未上传"));
        }
        return beeContext.getPlatForm();
    }

    /**
     * 获取请求业务信息
     *
     * @return
     */
    public static BeeContext beeContext() {
        return contextThreadLocal.get();
    }

    /**
     * id生成器
     */
    public static class IDTooker {
        public static Long getId() {
            return IdentTooker.take();
        }
    }

    public static Long coverString(String l) {
        if (StringUtils.isEmpty(l)) {
            throw new ServiceException(ErrorCodeEnum.ERROR_ILLEGAL_PARAMETER.customMassage("必要参数不能为空"));
        }
        return Long.valueOf(l);
    }

    public static Long coverIgnoreString(String l) {
        return StringUtils.isEmpty(l) ? null : Long.valueOf(l);
    }

    /**
     * 统一返回包装
     */
//    public static String returnSuccess(Object jsonObject) {
//
//        ClientResult clientResult = new ClientResult();
//        clientResult.setCode(1);
//        clientResult.setMessage("请求成功");
//        clientResult.setData(jsonObject);
//        return JSON.toJSONString(clientResult, SerializerFeature.WriteMapNullValue);
//    }
    public static <T> ClientResult<T> returnSuccess(T t) {

        ClientResult<T> clientResult = new ClientResult();
        clientResult.setCode(1);
        clientResult.setMessage("请求成功");
        clientResult.setData(t);
        return clientResult;
    }

    public static ClientResult returnSuccess() {
        ClientResult clientResult = new ClientResult();
        clientResult.setCode(1);
        clientResult.setMessage("请求成功");
        return clientResult;
    }

    public static String returnSuccess(String jsonString) {

        ClientResult clientResult = new ClientResult();
        clientResult.setCode(1);
        clientResult.setMessage("请求成功");
        clientResult.setData(JSON.parseObject(jsonString));
        return JSON.toJSONString(clientResult, SerializerFeature.WriteMapNullValue);
    }

    public static ClientResult returnInfo(ClientResult clientResult) {
        return clientResult;
    }

    public static ClientResult returnAbout() {
        ClientResult clientResult = new ClientResult();
        // 订单模块常规异常码
        clientResult.setCode(601000200);
        clientResult.setMessage("请求失败");
        return clientResult;
    }

    public static ClientResult returnAbout(String errorMessage) {
        ClientResult clientResult = new ClientResult();
        // 订单模块常规异常码
        clientResult.setCode(601000200);
        clientResult.setMessage(errorMessage);
        return clientResult;
    }

    public static ClientResult returnAbout(ErrorCodeEnum errorCodeEnum) {
        ClientResult clientResult = new ClientResult();
        clientResult.setCode(errorCodeEnum.getCode());
        clientResult.setMessage(errorCodeEnum.getMessage());
        return returnAbout();
    }

    /**
     * 获取一天的最后时间点  24点
     *
     * @param date
     * @return
     */
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

}

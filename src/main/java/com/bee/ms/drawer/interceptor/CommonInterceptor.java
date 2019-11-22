package com.bee.ms.drawer.interceptor;


import com.bee.ms.common.enums.PlatformEnum;
import com.bee.ms.common.portal.core.PlatformContext;
import com.bee.ms.common.portal.core.PortalTokenContext;
import com.bee.ms.drawer.*;
import com.bee.ms.drawer.exception.ErrorCodeEnum;
import com.bee.ms.drawer.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author created by htt on 2018/7/23
 */
@Component
@Slf4j
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        BeeContext beeContext = new BeeContext();


        if (handler instanceof HandlerMethod) {
            // 处理平台来源
            PlatformEnum platformEnum = PlatformContext.getInstance().get();
            // feign调用
            if (null == platformEnum) {
                String platform = request.getHeader("platform");
                if (!StringUtils.isEmpty(platform)) {
                    platformEnum = PlatformEnum.getPlatform(platform);
                }
            }
            // 网关调用
            beeContext.setPlatForm(platformEnum);

            // 处理feign消息头
            Map<Class, Boolean> headTransparentMap = new HashMap<>(5);
            if (((HandlerMethod) handler).hasMethodAnnotation(Shop.class)
                    && ((HandlerMethod) handler).getMethodAnnotation(Shop.class).transparent()) {
                headTransparentMap.put(Shop.class, true);
            }
            if (((HandlerMethod) handler).hasMethodAnnotation(PlatformIsTransparent.class)) {
                headTransparentMap.put(PlatformIsTransparent.class, true);
            }
            beeContext.setHeaderTransparentMap(headTransparentMap);

            //处理shop-id 请求头
            if (((HandlerMethod) handler).hasMethodAnnotation(Shop.class)) {
                if (Objects.nonNull(platformEnum) && (PlatformEnum.SAAS_WEB.equals(platformEnum)
                        || PlatformEnum.SAAS_ANDROID.equals(platformEnum)
                        || PlatformEnum.SAAS_IOS.equals(platformEnum))) {
                    String shopId = request.getHeader("shop-id");
                    if (StringUtils.isEmpty(shopId)) {
                        throw new ServiceException(ErrorCodeEnum.ERROR_ILLEGAL_PARAMETER.customMassage("店铺Id未上传"));
                    }
                    try {
                        Long shopIdLong = Long.valueOf(shopId);
                        beeContext.setShopId(shopIdLong);
                    } catch (NumberFormatException e) {
                        throw new ServiceException(ErrorCodeEnum.ERROR_ILLEGAL_PARAMETER.customMassage("店铺Id不合法"));
                    }

                }
            }
            // 登录权限校验
            if (((HandlerMethod) handler).hasMethodAnnotation(Login.class)) {
                if (PortalTokenContext.getInstance().getAccountId() == null) {
                    throw new ServiceException(ErrorCodeEnum.ERROR_NO_LOGIN.customMassage("用户未登录，请登录后重试"));
                }
            }
        }
        BeeTools.contextThreadLocal.set(beeContext);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
        BeeTools.contextThreadLocal.remove();
    }
}

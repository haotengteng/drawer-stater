package com.bee.ms.drawer;

import java.lang.annotation.*;

/**
 * 控制platform 消息头feign调用透传注解
 *
 * @author created by htt on 2018/8/28
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PlatformIsTransparent {
}

package com.bee.ms.drawer;

import java.lang.annotation.*;

/**
 * @author created by htt on 2018/7/23
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Shop {
    /**
     * feign 调用传递shop-id请求头,true 透传，false 不透传
     */
    boolean transparent() default true;

    boolean require() default false;

}

package com.bee.ms.drawer;

import java.lang.annotation.*;

/**
 * 登录校验
 * @author Created by haoteng on 2019/2/22.
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Login {
}

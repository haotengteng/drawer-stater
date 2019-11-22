package com.bee.ms.drawer.autoconfigure;

import com.bee.ms.drawer.BeeContext;
import com.bee.ms.drawer.filter.LogFilter;
import com.bee.ms.drawer.ident.IdentFeignClient;
import com.bee.ms.drawer.ident.IdentTooker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author created by htt on 2018/8/10
 */
@Configuration
@EnableConfigurationProperties({DrawerProperties.class})
@ConditionalOnClass(BeeContext.class)
public class DrawerAutoConfiguration {
    @Autowired
    DrawerProperties drawerProperties;

    @Autowired(required = false)
    LogFilter logFilter;

    @Autowired(required = false)
    IdentTooker identTooker;

    @Autowired(required = false)
    IdentFeignClient identFeignClient;
}

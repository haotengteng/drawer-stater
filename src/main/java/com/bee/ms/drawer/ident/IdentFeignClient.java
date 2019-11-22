package com.bee.ms.drawer.ident;

import com.bee.ms.ident.client.IdentClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

/**
 * @author created by htt on 2018/8/10
 */
@Service
@FeignClient(value = "ident")
public interface IdentFeignClient extends IdentClient {
}

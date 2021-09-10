package cn.itcast.order.clients;

import cn.itcast.order.config.DefaultFeignConfiguration;
import cn.itcast.order.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Halo
 * @create 2021/09/10 上午 11:58
 * @description
 */
@FeignClient(value = "user-service", configuration = DefaultFeignConfiguration.class)
public interface UserClient {
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}

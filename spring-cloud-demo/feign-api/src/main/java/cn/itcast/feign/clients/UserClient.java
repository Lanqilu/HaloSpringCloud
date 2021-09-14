package cn.itcast.feign.clients;

import cn.itcast.feign.clients.fallback.UserClientFallbackFactory;
import cn.itcast.feign.config.DefaultFeignConfiguration;
import cn.itcast.feign.pojo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author Halo
 * @create 2021/09/10 上午 11:58
 * @description
 */
@FeignClient(value = "user-service",
        configuration = DefaultFeignConfiguration.class,
        fallbackFactory = UserClientFallbackFactory.class)
public interface UserClient {
    @GetMapping("/user/{id}")
    User findById(@PathVariable("id") Long id);
}

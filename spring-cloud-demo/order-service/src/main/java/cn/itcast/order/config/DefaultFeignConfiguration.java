package cn.itcast.order.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @author Halo
 * @create 2021/09/10 下午 12:26
 * @description
 */
public class DefaultFeignConfiguration  {
    @Bean
    public Logger.Level feignLogLevel(){
        return Logger.Level.BASIC; // 日志级别为BASIC
    }
}
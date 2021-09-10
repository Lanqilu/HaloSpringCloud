package cn.itcast.user.confog;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Halo
 * @create 2021/09/09 下午 11:04
 * @description
 */

@Data
@Component
@ConfigurationProperties(prefix = "pattern")
public class PatternProperties {
    private String dateformat;
    private String envShareValue;
}

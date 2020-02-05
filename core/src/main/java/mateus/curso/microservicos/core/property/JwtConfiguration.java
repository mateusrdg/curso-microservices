package mateus.curso.microservicos.core.property;

import com.netflix.ribbon.proxy.annotation.Http;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt.config")
@Getter
@Setter
@ToString
public class JwtConfiguration {
    private String loginUrl = "/login/**";
    @NestedConfigurationProperty
    private Header header = new Header();
    private Integer expiration = 3600;
    private String privateKey = "CCJIrZW6MvrpPBj96SCeoUwcsY2A8RQP";
    private String type = "encrypted";

    @Getter
    @Setter
    private class Header {
        private String name = "Authorization";
        private String prefix = "Bearer ";

    }
}

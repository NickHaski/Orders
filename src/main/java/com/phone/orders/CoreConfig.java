package com.phone.orders;

import com.phone.orders.profile.Profiles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.client.RestTemplate;

@Configuration
@PropertySource({"classpath:core.properties"})
@ComponentScan(basePackages = {Constants.CORE_PACKAGE})
@Import({JpaConfig.class})
public class CoreConfig {

    @Configuration
    @Profile(Profiles.LOCAL)
    @PropertySource("classpath:local.properties")
    static class LocalConfig {
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}

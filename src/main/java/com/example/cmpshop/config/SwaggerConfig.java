package com.example.cmpshop.config;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI cusOpenAPI(){
        return new OpenAPI().info(new Info().title("CMP Shop APi's").description("Shop CMP Application")
                .version("1.0")
                .contact(new Contact().name("locnguyendev")));
    }

}

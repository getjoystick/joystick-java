package com.getjoystick.examples;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.or;
import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .groupName("joystick-examples-api")
            .apiInfo(apiInfo())
            .select()
            .paths(appPaths())
            .build();
    }

    private Predicate<String> appPaths() {
        return or(regex("/single/.*"),
            regex("/multi/.*"));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Joystick Usage Examples API")
            .description("Joystick Examples reference for developers")
            .version("1.0").build();
    }

}

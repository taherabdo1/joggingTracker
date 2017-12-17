package com.toptal.demo;

import java.util.Arrays;
import java.util.Map;

import javax.servlet.RequestDispatcher;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.ApiKeyVehicle;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class ToptalApplication extends WebMvcConfigurerAdapter {

    public static void main(final String[] args) {
        SpringApplication.run(ToptalApplication.class, args);
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {

            @Override
            public Map<String, Object> getErrorAttributes(final RequestAttributes requestAttributes, final boolean includeStackTrace) {
                final Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
                final Object errorMessage = requestAttributes.getAttribute(RequestDispatcher.ERROR_MESSAGE, RequestAttributes.SCOPE_REQUEST);
                if (errorMessage != null) {
                    errorAttributes.put("message", errorMessage);
                }
                return errorAttributes;

            }

        };
    }

    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage(getClass().getPackage().getName()))
                .paths(PathSelectors.any()).build().securitySchemes(Arrays.asList(apiKey())).apiInfo(generateApiInfo());
    }

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");

    }

    private ApiInfo generateApiInfo() {
        return new ApiInfo("Jogging Tracking Service", "", "Version 1.0", "", "taher.ayoub90@gmail.com", "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0");
    }

    /*
     * replace api key with AUTHORIZATION header
     */
    private ApiKey apiKey() {
        return new ApiKey("AUTHORIZATION", "api_key", "header");
    }

    @Bean
    SecurityConfiguration security() {
        return new SecurityConfiguration(null, null, null, null, "Bearer access_token", ApiKeyVehicle.HEADER, "Authorization", ",");
    }

    @Bean
    RestTemplate restTamplate() {
        return new RestTemplate();
    }

}

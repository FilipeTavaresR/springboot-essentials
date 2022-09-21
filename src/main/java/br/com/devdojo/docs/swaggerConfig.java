package br.com.devdojo.docs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class swaggerConfig {
    @Bean
    public Docket apiDoc(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                    .apis(RequestHandlerSelectors.basePackage("br.com.devdojo.endpoint"))
                    .paths(regex("/v1.*"))
                    .build()
                .apiInfo(metaData());
    }
    private ApiInfo metaData(){
        return new ApiInfoBuilder()
                .title("Spring Boot Essentials By DevDojo")
                .description("The best spring course out there")
                .version("1.0")
                .contact(new Contact("Filipe Tavares","teste http://localhost/", "filipe.tavares@invillia.com"))
                .license("Apache License Version 2.0")
                .licenseUrl("Http://whathever.com")
                .build();
    }
}

package br.com.jrafael.infrastructure.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class SwaggerConfiguration {

    protected String projectPackage = "br.com.jrafael.infrastructure";
    protected String titleApplication = "Todo";
    protected String descriptionApplication = "Todo";
    protected String licenseApplication = "Todo";
    protected String licenseUrlApplication = "Todo";
    protected String termsOfServiceUrlApplication = "Todo";
    protected String versionApplication = "1.0.0";
    protected String contatoName = "José Rafael";
    protected String contatoUrl = "Todo";
    protected String contatoEmail = "jose.rafael.feitosa@gmail.com";

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage(projectPackage))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(this.apiInfo())
                .securityContexts(Arrays.asList(securityContext()));
//                .securitySchemes(Collections.singletonList(HttpAuthenticationScheme
//                        .JWT_BEARER_BUILDER
//                        .name("JWT")
//                        .build()));
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title (this.titleApplication)
                .description (this.descriptionApplication)
                .license(this.licenseApplication)
                .licenseUrl(this.licenseUrlApplication)
                .termsOfServiceUrl(this.termsOfServiceUrlApplication)
                .version(this.versionApplication)
                .contact(new Contact(this.contatoName,this.contatoUrl, this.contatoEmail))
                .build();
    }


    private SecurityContext securityContext() {
        return SecurityContext.builder().securityReferences(defaultAuth()).build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("JWT", authorizationScopes));
    }
}

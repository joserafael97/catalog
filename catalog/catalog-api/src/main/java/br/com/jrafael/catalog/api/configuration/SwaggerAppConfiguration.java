package br.com.jrafael.catalog.api.configuration;


import br.com.jrafael.infrastructure.configuration.SwaggerConfiguration;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerAppConfiguration extends SwaggerConfiguration {

    public SwaggerAppConfiguration(){
        this.projectPackage = "br.com.jrafael.catalog.api.controller";
        this.titleApplication = "Catalog";
    }
}

package br.com.jrafael.catalog.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"br.com.jrafael.catalog.api"})
@EnableJpaRepositories(basePackages = {"br.com.jrafael.catalog.repository"})
@EntityScan(basePackages = {"br.com.jrafael.catalog.model"})
@ComponentScan(basePackages = {"br.com.jrafael.infrastructure", "br.com.jrafael.catalog.api"})
public class CatalogApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatalogApiApplication.class, args);
    }

}

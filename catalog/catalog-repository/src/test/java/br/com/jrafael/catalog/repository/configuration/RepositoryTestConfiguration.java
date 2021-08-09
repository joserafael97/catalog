package br.com.jrafael.catalog.repository.configuration;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@TestConfiguration
@EnableJpaRepositories(basePackages = {"br.com.jrafael.catalog.repository"})
@EntityScan(basePackages = {"br.com.jrafael.catalog.model"})
@ComponentScan(basePackages = {"br.com.jrafael.infrastructure"})
public class RepositoryTestConfiguration {

}

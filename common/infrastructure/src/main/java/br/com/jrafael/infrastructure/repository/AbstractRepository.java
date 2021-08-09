package br.com.jrafael.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AbstractRepository<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}

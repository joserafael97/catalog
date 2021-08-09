package br.com.jrafael.infrastructure.repository.specification.factory;

import br.com.jrafael.infrastructure.repository.specification.builder.GenericSpecificationsBuilder;
import br.com.jrafael.infrastructure.repository.specification.util.enums.SearchOperation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SpecificationFactory<T> {

    public Specification<T> isEqual(String key, Object arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.EQUALITY, false, Collections.singletonList(arg))
                .with(key, SearchOperation.EQUALITY, false, Collections.singletonList(arg)).build();
    }

    public Specification<T> isGreaterThan(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.GREATER_THAN, Collections.singletonList(arg)).build();
    }

    public Specification<T> isLessThan(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.LESS_THAN, Collections.singletonList(arg)).build();
    }

    public Specification<T> isLike(String key, Comparable arg) {
        GenericSpecificationsBuilder<T> builder = new GenericSpecificationsBuilder<>();
        return builder.with(key, SearchOperation.LIKE, Collections.singletonList(arg)).build();
    }
}

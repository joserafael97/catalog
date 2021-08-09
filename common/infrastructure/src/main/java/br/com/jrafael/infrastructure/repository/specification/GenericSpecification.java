


package br.com.jrafael.infrastructure.repository.specification;

import br.com.jrafael.infrastructure.repository.specification.util.SearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Slf4j
public class GenericSpecification<T> implements Specification<T> {

    private SearchCriteria searchCriteria;

    public GenericSpecification(final SearchCriteria searchCriteria){
        super();
        this.searchCriteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        List<Object> arguments = searchCriteria.getArguments();
        Object arg = arguments.get(0);

        switch (searchCriteria.getSearchOperation()) {
            case EQUALITY:
                return criteriaBuilder.equal(root.get(searchCriteria.getKey()), arg);
            case GREATER_THAN:
                return criteriaBuilder.greaterThan(root.get(searchCriteria.getKey()), (Comparable) arg);
            case LESS_THAN:
                return criteriaBuilder.lessThan(root.get(searchCriteria.getKey()), (Comparable) arg);
            case LIKE:
                return criteriaBuilder.like(criteriaBuilder.lower(root.get(searchCriteria.getKey())),  "%"+((String) arg).toLowerCase()+"%");
            case IN:
                return root.get(searchCriteria.getKey()).in(arguments);
        }

        return null;
    }
}



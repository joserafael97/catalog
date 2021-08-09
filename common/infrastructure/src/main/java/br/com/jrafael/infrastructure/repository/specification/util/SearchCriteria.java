package br.com.jrafael.infrastructure.repository.specification.util;

import br.com.jrafael.infrastructure.repository.specification.util.enums.SearchOperation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {

    private String key;
    private SearchOperation searchOperation;
    private boolean isOrOperation;
    private List<Object> arguments;
}

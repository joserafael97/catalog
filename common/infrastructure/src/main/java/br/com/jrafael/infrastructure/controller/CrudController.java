package br.com.jrafael.infrastructure.controller;

import br.com.jrafael.infrastructure.dto.BaseDto;
import br.com.jrafael.infrastructure.exception.BusinessException;
import br.com.jrafael.infrastructure.repository.AbstractRepository;
import br.com.jrafael.infrastructure.util.MyBeansUtil;
import br.com.jrafael.infrastructure.util.NullAwareBeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;



public abstract class CrudController<T, K extends BaseDto<T>, F extends BaseDto<T>, ID> {

    protected static final String PAGE_NUMBER_PARAM = "page";
    protected static final String PAGE_SIZE_PARAM = "size";

    protected AbstractRepository<T, ID> repository;

    protected final Logger LOGGER = Logger.getLogger(CrudController.class.getName());

    public CrudController(AbstractRepository<T, ID> repository) {
        this.repository = repository;
    }

    protected abstract K convert(T entity);

    @GetMapping(params = { PAGE_NUMBER_PARAM, PAGE_SIZE_PARAM })
    public Page<K> getPaginated(@PageableDefault(direction = Sort.Direction.DESC, page = 0, size = 100) Pageable page, @RequestParam(required = false) Map<String, String> requestParams) {
        Page<T> entities = this.repository.findAll(page);
        return entities.map(entity -> this.convert(entity));
    }

    @GetMapping
    public List<K> get(@RequestParam(required = false) Map<String, String> requestParams) {
        List<T> entities = this.repository.findAll();

        return entities.stream().map(entity -> this.convert(entity)).collect(Collectors.toList());
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<K> getById(@PathVariable ID id) {
        Optional<T> optional = this.repository.findById(id);

        if (!optional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        K result = this.convert(optional.get());

        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<K> post(@RequestBody @Valid F form) throws BusinessException {
        T entity = this.repository.save(form.convert());
        return ResponseEntity.ok(this.convert(entity));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<K> put(@PathVariable ID id, @RequestBody @Valid F form) throws BusinessException {
        Optional<T> optional = this.repository.findById(id);

        if (optional.isPresent()) {
            T entity = optional.get();
            MyBeansUtil<T> myBeansUtil = new MyBeansUtil<T>();
            myBeansUtil.copyPropertiesNonNull(entity, form.convert());
            entity = this.repository.save(entity);
            return ResponseEntity.ok(this.convert(entity));
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    @Transactional
    public ResponseEntity<K> patch(@PathVariable ID id, @RequestBody K form) throws BusinessException {
        Optional<T> optional = this.repository.findById(id);

        if (optional.isPresent()) {
            T entity = optional.get();
            try {
                NullAwareBeanUtils.getInstance().copyProperties(entity, form.convert());
            } catch (IllegalAccessException | InvocationTargetException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
            entity = this.repository.save(entity);
            return ResponseEntity.ok(this.convert(entity));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> delete(@PathVariable ID id) throws BusinessException {
        Optional<T> optional = this.repository.findById(id);
        if (optional.isPresent()) {
            this.repository.deleteById(id);
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.notFound().build();
    }

}

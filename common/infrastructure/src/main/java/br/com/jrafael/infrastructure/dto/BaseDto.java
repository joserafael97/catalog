package br.com.jrafael.infrastructure.dto;

import java.io.Serializable;

public abstract class BaseDto<T> implements Serializable {

    public abstract T convert();

}

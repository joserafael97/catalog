package br.com.jrafael.infrastructure.exception;

public class BusinessException extends Exception {
    public BusinessException(String mensagem) {
        super(mensagem);
    }
}

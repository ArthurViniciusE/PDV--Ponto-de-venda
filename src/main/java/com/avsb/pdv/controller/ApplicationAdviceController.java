package com.avsb.pdv.controller;

import com.avsb.pdv.dto.ResponseDTO;
import com.avsb.pdv.exceptions.InvalidOperationException;
import com.avsb.pdv.exceptions.NoItemException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApplicationAdviceController {

    @ExceptionHandler(NoItemException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handlerNoItemException(NoItemException exception) {
        String messageError = exception.getMessage();
        return new ResponseDTO(messageError);
    }

    @ExceptionHandler(InvalidOperationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseDTO handlerInvalidOperationException(InvalidOperationException exception) {
        String messageError = exception.getMessage();
        return new ResponseDTO(messageError);
    }
}

package com.upgrad.quora.api.exception;


import com.upgrad.quora.api.model.ErrorResponse;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.SignUpRestrictedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(SignUpRestrictedException.class)

    public ResponseEntity<ErrorResponse> signupRestrictedException(SignUpRestrictedException exe, WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public  ResponseEntity<ErrorResponse> authnticationFailedException(AuthenticationFailedException exe,WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
                HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(AuthorizationFailedException.class)
    public ResponseEntity<ErrorResponse> authorizationFailedException(AuthorizationFailedException exe,WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> userNotFountException(UserNotFoundException exe,WebRequest request){
        return new ResponseEntity<ErrorResponse>(new ErrorResponse().code(exe.getCode()).message(exe.getErrorMessage()),
                HttpStatus.NOT_FOUND);
    }

}


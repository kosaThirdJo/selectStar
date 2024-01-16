package com.threestar.selectstar.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MeetingNotFoundException extends RuntimeException{
    public MeetingNotFoundException(String message){
        super(message);
    }
}

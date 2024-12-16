package ru.ism.task_test.exception;

import lombok.Getter;

@Getter
public class ErrResponse {
    private final String error;

    public ErrResponse(String error) {
        this.error = error;
    }

}

package ru.ism.task_test.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@RequiredArgsConstructor
@ToString
public class Error {
    private final String reason;
    private final String message;
}

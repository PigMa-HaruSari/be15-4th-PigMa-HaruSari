package com.pigma.harusari.task.exception;

import lombok.Getter;

@Getter
public class CategoryNotFoundException extends RuntimeException {

    private final TaskErrorCode taskErrorCode;

    public CategoryNotFoundException(TaskErrorCode categoryNotFound) {
        super(TaskErrorCode.CATEGORY_NOT_FOUND.getMessage());
        this.taskErrorCode = TaskErrorCode.CATEGORY_NOT_FOUND;
    }
}
package com.pigma.harusari.category.exception;

import lombok.Getter;

@Getter
public class CategoryException extends RuntimeException {

    private final CategoryErrorCode errorCode;

    public CategoryException(CategoryErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.errorCode = errorCode;
    }
}

package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.ErrorCategory;

public abstract class AppException extends RuntimeException 
{
    private final String code;
    private final String defaultMessage;
    private final ErrorCategory category;

    protected AppException(String code, String defaultMessage, ErrorCategory category) 
    {
        super(defaultMessage);
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.category = category;
    }

    protected AppException(String code, String defaultMessage, ErrorCategory category, String customMessage) 
    {
        super(customMessage);
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.category = category;
    }

    public String getCode() { return code; }
    public String getDefaultMessage() { return defaultMessage; }
    public ErrorCategory getCategory() { return category; }

    public String getDisplayMessage() { return getMessage(); }
}

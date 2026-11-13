package io.github.roony11_1.error.core.exceptions;

import java.util.Objects;

import io.github.roony11_1.error.core.ErrorCategory;

public abstract class AppException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    private final String code;
    private final String defaultMessage;
    private final ErrorCategory category;

    protected AppException(
        String code,
        String defaultMessage,
        ErrorCategory category,
        String customMessage,
        Throwable cause)
    {
        super(customMessage != null ? customMessage : defaultMessage, cause);
        this.code = Objects.requireNonNull(code, "code");
        this.defaultMessage = Objects.requireNonNull(defaultMessage, "defaultMessage");
        this.category = Objects.requireNonNull(category, "category");
    }

    protected AppException(String code, String defaultMessage, ErrorCategory category)
    {
        this(code, defaultMessage, category, null, null);
    }

    protected AppException(String code, String defaultMessage, ErrorCategory category,
                           String customMessage)
    {
        this(code, defaultMessage, category, customMessage, null);
    }

    protected AppException(String code, String defaultMessage, ErrorCategory category,
                           Throwable cause)
    {
        this(code, defaultMessage, category, null, cause);
    }

    public String getCode() { return code; }
    public String getDefaultMessage() { return defaultMessage; }
    public ErrorCategory getCategory() { return category; }
    public String getDisplayMessage() { return getMessage(); }
}
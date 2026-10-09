
package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.ErrorCategory;

/**
 * Base class for application exceptions.
 *
 * <p>Provides a standardized representation of application errors through
 * an error code, a default message, and an error category.</p>
 *
 * <p>Supports custom display messages and exception causes, allowing
 * application errors to preserve their original context.</p>
 */
public abstract class AppException extends RuntimeException
{
    private final String code;
    private final String defaultMessage;
    private final ErrorCategory category;

    /**
     * Creates an application exception using the default message as its
     * runtime exception message.
     *
     * @param code the application error code
     * @param defaultMessage the default error message
     * @param category the category associated with this error
     */
    protected AppException(
        String code,
        String defaultMessage,
        ErrorCategory category
    )
    {
        super(defaultMessage);
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.category = category;
    }

    /**
     * Creates an application exception with a custom runtime exception
     * message.
     *
     * @param code the application error code
     * @param defaultMessage the default error message
     * @param category the category associated with this error
     * @param customMessage the custom message exposed by the exception
     */
    protected AppException(
        String code,
        String defaultMessage,
        ErrorCategory category,
        String customMessage
    )
    {
        super(customMessage);
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.category = category;
    }

    /**
     * Creates an application exception with an underlying cause.
     *
     * @param code the application error code
     * @param defaultMessage the default error message
     * @param category the category associated with this error
     * @param cause the underlying cause of the exception
     */
    protected AppException(
        String code,
        String defaultMessage,
        ErrorCategory category,
        Throwable cause
    )
    {
        super(defaultMessage, cause);
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.category = category;
    }

    /**
     * Creates an application exception with a custom message and an
     * underlying cause.
     *
     * @param code the application error code
     * @param defaultMessage the default error message
     * @param category the category associated with this error
     * @param customMessage the custom runtime exception message
     * @param cause the underlying cause of the exception
     */
    protected AppException(
        String code,
        String defaultMessage,
        ErrorCategory category,
        String customMessage,
        Throwable cause
    )
    {
        super(customMessage, cause);
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.category = category;
    }

    /**
     * Returns the application error code.
     *
     * @return the error code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * Returns the default message associated with this error.
     *
     * @return the default error message
     */
    public String getDefaultMessage()
    {
        return defaultMessage;
    }

    /**
     * Returns the category associated with this error.
     *
     * @return the error category
     */
    public ErrorCategory getCategory()
    {
        return category;
    }

    /**
     * Returns the message intended to be displayed to the consumer.
     *
     * @return the display message
     */
    public String getDisplayMessage()
    {
        return getMessage();
    }
}
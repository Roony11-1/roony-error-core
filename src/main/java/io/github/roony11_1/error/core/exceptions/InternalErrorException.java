
package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

/**
 * Exception representing an internal application error.
 *
 * <p>Uses error code {@code ERR-0005} and the
 * {@link StandardErrorCategories#INTERNAL_ERROR} category.</p>
 */
public class InternalErrorException extends AppException
{
    /**
     * Creates an internal error associated with a specific operation.
     *
     * @param operation the operation in which the error occurred
     */
    public InternalErrorException(String operation)
    {
        super(
            "ERR-0005",
            "Error interno en " + operation,
            StandardErrorCategories.INTERNAL_ERROR,
            "Error interno en " + operation
        );
    }

    /**
     * Creates an internal error associated with a specific operation
     * and preserves its underlying cause.
     *
     * @param operation the operation in which the error occurred
     * @param cause the underlying cause of the error
     */
    public InternalErrorException(String operation, Throwable cause)
    {
        super(
            "ERR-0005",
            "Error interno en " + operation,
            StandardErrorCategories.INTERNAL_ERROR,
            "Error interno en " + operation,
            cause
        );
    }
}
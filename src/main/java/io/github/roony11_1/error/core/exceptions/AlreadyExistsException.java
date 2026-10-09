
package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

/**
 * Exception thrown when an attempt is made to create a resource that
 * already exists.
 *
 * <p>Uses error code {@code ERR-0004} and the
 * {@link StandardErrorCategories#ALREADY_EXISTS} category.</p>
 */
public class AlreadyExistsException extends AppException
{
    /**
     * Creates an exception for a resource identified by its name and ID.
     *
     * @param resourceName the name of the resource
     * @param id the identifier of the existing resource
     */
    public AlreadyExistsException(String resourceName, Object id)
    {
        super(
            "ERR-0004",
            resourceName + " ya existe: " + id,
            StandardErrorCategories.ALREADY_EXISTS,
            resourceName + " ya existe: " + id
        );
    }

    /**
     * Creates an exception with a custom message.
     *
     * @param message the error message
     */
    public AlreadyExistsException(String message)
    {
        super(
            "ERR-0004",
            message,
            StandardErrorCategories.ALREADY_EXISTS,
            message
        );
    }
}
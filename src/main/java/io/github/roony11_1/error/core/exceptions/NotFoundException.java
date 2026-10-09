
package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

/**
 * Exception thrown when a requested resource cannot be found.
 *
 * <p>Uses error code {@code ERR-0003} and the
 * {@link StandardErrorCategories#NOT_FOUND} category.</p>
 */
public class NotFoundException extends AppException
{
    /**
     * Creates an exception for a resource identified by its name and ID.
     *
     * @param resourceName the name of the resource
     * @param id the identifier of the missing resource
     */
    public NotFoundException(String resourceName, Object id)
    {
        super(
            "ERR-0003",
            resourceName + " no encontrado: " + id,
            StandardErrorCategories.NOT_FOUND,
            resourceName + " no encontrado: " + id
        );
    }

    /**
     * Creates an exception with a custom message.
     *
     * @param message the error message
     */
    public NotFoundException(String message)
    {
        super(
            "ERR-0003",
            message,
            StandardErrorCategories.NOT_FOUND,
            message
        );
    }
}
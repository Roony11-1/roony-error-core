
package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

/**
 * Exception thrown when the supplied input is invalid.
 *
 * <p>Uses error code {@code ERR-0002} and the
 * {@link StandardErrorCategories#INVALID_INPUT} category.</p>
 */
public class InvalidInputException extends AppException
{
    /**
     * Creates an exception describing the invalid input.
     *
     * @param details a description of the validation or input error
     */
    public InvalidInputException(String details)
    {
        super(
            "ERR-0002",
            "Entrada inválida: " + details,
            StandardErrorCategories.INVALID_INPUT,
            "Entrada inválida: " + details
        );
    }
}
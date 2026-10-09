package io.github.roony11_1.error.core;

/**
 * Defines the standard categories used to classify application errors.
 *
 * <p>These categories provide a common vocabulary for frequent error
 * scenarios. Applications can define additional categories by implementing
 * {@link ErrorCategory}.</p>
 *
 * <p>The category name is provided by {@link Enum#name()}, while the
 * description provides a human-readable explanation.</p>
 */
public enum StandardErrorCategories implements ErrorCategory 
{
    /** Indicates that the requested resource could not be found. */
    NOT_FOUND("Resource not found"),

    /** Indicates that the resource already exists. */
    ALREADY_EXISTS("The resource already exists"),

    /** Indicates that the supplied input is invalid. */
    INVALID_INPUT("Invalid input"),

    /** Indicates that an unexpected internal error occurred. */
    INTERNAL_ERROR("Internal error"),

    /** Indicates that authentication is required or has failed. */
    UNAUTHORIZED("Unauthenticated"),

    /** Indicates that access to the requested resource is denied. */
    FORBIDDEN("Access denied"),

    /** Indicates that the current operation is not permitted. */
    ACCESS_DENIED("Access denied");

    private final String description;

    StandardErrorCategories(String description)
    {
        this.description = description;
    }

    /**
     * Returns the human-readable description of this category.
     *
     * @return the category description
     */
    @Override
    public String description()
    {
        return description;
    }
}
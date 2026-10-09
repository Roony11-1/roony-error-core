
package io.github.roony11_1.error.core;

/**
 * Represents a category used to classify application errors.
 *
 * <p>Implementations can provide application-specific categories without
 * modifying the standard categories defined by this library.</p>
 */
public interface ErrorCategory
{
    /**
     * Returns the name of this category.
     *
     * @return the category name
     */
    String name();

    /**
     * Returns a human-readable description of this category.
     *
     * @return the category description
     */
    String description();
}
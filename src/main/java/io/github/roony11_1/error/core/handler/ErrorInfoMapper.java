package io.github.roony11_1.error.core.handler;

import io.github.roony11_1.error.core.StandardErrorCategories;
import io.github.roony11_1.error.core.dto.ErrorInfo;
import io.github.roony11_1.error.core.exceptions.AppException;

/**
 * Convierte excepciones a {@link ErrorInfo} en su forma canónica,
 * sin enriquecimiento de perfil ni logging. Esas son
 * responsabilidades de {@link ErrorHandler}.
 */
public final class ErrorInfoMapper
{
    private static final String UNEXPECTED_CODE  = "UNK-001";
    private static final String UNEXPECTED_TITLE = "Error interno inesperado";

    public ErrorInfo toInfo(AppException ex)
    {
        return new ErrorInfo(
            ex.getCode(),
            ex.getCategory().description(),
            ex.getDisplayMessage(),
            ex.getCategory());
    }

    public ErrorInfo toInfo(Throwable ex)
    {
        return new ErrorInfo(
            UNEXPECTED_CODE,
            UNEXPECTED_TITLE,
            null,
            StandardErrorCategories.INTERNAL_ERROR);
    }
}
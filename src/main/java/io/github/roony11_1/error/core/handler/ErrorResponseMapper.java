package io.github.roony11_1.error.core.handler;

import io.github.roony11_1.error.core.dto.ErrorResponse;
import io.github.roony11_1.error.core.exceptions.AppException;

/**
 * Convierte excepciones a ErrorResponse en su forma canónica,
 * sin enriquecimiento de perfil ni logging. Esas son
 * responsabilidades de ErrorHandler.
 */
public class ErrorResponseMapper 
{
    private static final String UNEXPECTED_CODE = "UNK-001";
    private static final String UNEXPECTED_TITLE = "Error interno inesperado";

    public ErrorResponse toResponse(AppException ex)
    {
        return new ErrorResponse(ex.getCode(), ex.getCategory().description(), ex.getDisplayMessage());
    }

    public ErrorResponse toResponse(Throwable ex)
    {
        return new ErrorResponse(UNEXPECTED_CODE, UNEXPECTED_TITLE, null);
    }
}

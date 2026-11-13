package io.github.roony11_1.error.core.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.roony11_1.error.core.StandardErrorCategories;
import io.github.roony11_1.error.core.dto.ErrorResponse;
import io.github.roony11_1.error.core.exceptions.AppException;
import io.github.roony11_1.error.support.TestAppException;

class ErrorResponseMapperTest
{
    private final ErrorResponseMapper mapper = new ErrorResponseMapper();

    @Test
    void mapsAppExceptionToResponse()
    {
        AppException ex = new TestAppException(
            "TEST-001", "Default", StandardErrorCategories.NOT_FOUND, "No encontrado");

        ErrorResponse r = mapper.toResponse(ex);

        assertEquals("TEST-001", r.code());
        assertEquals("Recurso no encontrado", r.title());
        assertEquals("No encontrado", r.detail());
        assertTrue(r.errors().isEmpty());
    }

    @Test
    void mapsGenericThrowableToUnexpected()
    {
        ErrorResponse r = mapper.toResponse(new RuntimeException("boom"));

        assertEquals("UNK-001", r.code());
        assertEquals("Error interno inesperado", r.title());
        assertNull(r.detail());
    }
}

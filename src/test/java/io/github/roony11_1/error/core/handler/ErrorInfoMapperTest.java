package io.github.roony11_1.error.core.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import io.github.roony11_1.error.core.StandardErrorCategories;
import io.github.roony11_1.error.core.dto.ErrorInfo;
import io.github.roony11_1.error.core.exceptions.AppException;
import io.github.roony11_1.error.support.TestAppException;

public class ErrorInfoMapperTest
{
    private final ErrorInfoMapper mapper = new ErrorInfoMapper();

    @Test
    void mapsAppExceptionToInfo()
    {
        AppException ex = new TestAppException(
            "TEST-001", "Default", StandardErrorCategories.NOT_FOUND, "No encontrado");

        ErrorInfo r = mapper.toInfo(ex);

        assertEquals("TEST-001", r.code());
        assertEquals("Recurso no encontrado", r.title());
        assertEquals("No encontrado", r.detail());
        assertEquals(StandardErrorCategories.NOT_FOUND, r.category());
        assertTrue(r.errors().isEmpty());
    }

    @Test
    void mapsGenericThrowableToUnexpected()
    {
        ErrorInfo r = mapper.toInfo(new RuntimeException("boom"));

        assertEquals("UNK-001", r.code());
        assertEquals("Error interno inesperado", r.title());
        assertNull(r.detail());
        assertEquals(StandardErrorCategories.INTERNAL_ERROR, r.category());
    }
}
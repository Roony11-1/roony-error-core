package io.github.roony11_1.error.core.handler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.roony11_1.error.core.StandardErrorCategories;
import io.github.roony11_1.error.core.dto.ErrorResponse;
import io.github.roony11_1.error.core.exceptions.AppException;
import io.github.roony11_1.error.support.TestAppException;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest
{
    @Mock
    private IErrorEnricher enricher;

    @Mock
    private ErrorResponseMapper mapper;

    private ErrorHandler handler;

    private static final ErrorResponse APP_RESPONSE =
        new ErrorResponse("TEST-001", "Not found", "No encontrado");

    private static final ErrorResponse UNEXPECTED_RESPONSE =
        new ErrorResponse("UNK-001", "Error interno inesperado", null);

    @BeforeEach
    void setUp()
    {
        handler = new ErrorHandler(enricher, mapper);

        when(enricher.enrich(any(), any())).thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void nullThrowable_returnsUnexpectedResponse()
    {
        when(mapper.toResponse((Throwable) null)).thenReturn(UNEXPECTED_RESPONSE);

        ErrorResponse r = handler.toErrorResponse(null);

        assertSame(UNEXPECTED_RESPONSE, r);
        verify(mapper).toResponse((Throwable) null);
        verify(enricher).enrich(UNEXPECTED_RESPONSE, null);
    }

    @Test
    void appExceptionAtRoot_mapsAndEnriches()
    {
        AppException ex = new TestAppException(
            "TEST-001", "Default", StandardErrorCategories.NOT_FOUND, "No encontrado");
        when(mapper.toResponse((AppException) ex)).thenReturn(APP_RESPONSE);

        ErrorResponse r = handler.toErrorResponse(ex);

        assertSame(APP_RESPONSE, r);
        verify(mapper).toResponse((AppException) ex);
        verify(enricher).enrich(APP_RESPONSE, ex);
    }

    @Test
    void appExceptionWrappedOnce_isFoundThroughInspector()
    {
        AppException domain = new TestAppException(
            "TEST-002", "Default", StandardErrorCategories.INVALID_INPUT, "Inválido");
        Throwable wrapper = new RuntimeException("infra", domain);
        when(mapper.toResponse((AppException) domain)).thenReturn(APP_RESPONSE);

        ErrorResponse r = handler.toErrorResponse(wrapper);

        assertSame(APP_RESPONSE, r);
        verify(mapper).toResponse((AppException) domain);
        verify(mapper, never()).toResponse(wrapper);
    }

    @Test
    void genericException_mapsAsUnexpected()
    {
        RuntimeException ex = new RuntimeException("boom");
        when(mapper.toResponse((Throwable) ex)).thenReturn(UNEXPECTED_RESPONSE);

        ErrorResponse r = handler.toErrorResponse(ex);

        assertSame(UNEXPECTED_RESPONSE, r);
        verify(mapper).toResponse((Throwable) ex);
        verify(enricher).enrich(UNEXPECTED_RESPONSE, ex);
    }

    @Test
    void enricherIsCalledOnce_perResponse()
    {
        RuntimeException ex = new RuntimeException("boom");
        when(mapper.toResponse((Throwable) ex)).thenReturn(UNEXPECTED_RESPONSE);

        handler.toErrorResponse(ex);

        verify(enricher, times(1)).enrich(any(), any());
    }

    @Test
    void enrichedResponse_isReturnedAsIs()
    {
        RuntimeException ex = new RuntimeException("boom");
        ErrorResponse enriched = UNEXPECTED_RESPONSE.withError("stacktrace");
        when(mapper.toResponse((Throwable) ex)).thenReturn(UNEXPECTED_RESPONSE);
        when(enricher.enrich(UNEXPECTED_RESPONSE, ex)).thenReturn(enriched);

        ErrorResponse r = handler.toErrorResponse(ex);

        assertSame(enriched, r);
        assertFalse(r.errors().isEmpty());
    }

    @Test
    void nullThrowable_doesNotCallInspector()
    {
        when(mapper.toResponse((Throwable) null)).thenReturn(UNEXPECTED_RESPONSE);

        handler.toErrorResponse(null);

        verify(mapper).toResponse((Throwable) null);
    }
}
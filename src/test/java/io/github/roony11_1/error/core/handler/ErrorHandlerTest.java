package io.github.roony11_1.error.core.handler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.github.roony11_1.error.core.StandardErrorCategories;
import io.github.roony11_1.error.core.dto.ErrorInfo;
import io.github.roony11_1.error.core.exceptions.AppException;
import io.github.roony11_1.error.support.TestAppException;

@ExtendWith(MockitoExtension.class)
class ErrorHandlerTest
{
    @Mock private IErrorEnricher enricher;
    @Mock private ErrorResponseMapper mapper;
    @Mock private ErrorListener listener;

    private ErrorHandler handler;

    private static final ErrorInfo APP_RESPONSE =
        new ErrorInfo("TEST-001", "Not found", "No encontrado");

    private static final ErrorInfo UNEXPECTED_RESPONSE =
        new ErrorInfo("UNK-001", "Error interno inesperado", null);

    @BeforeEach
    void setUp()
    {
        handler = new ErrorHandler(enricher, mapper, listener);
        when(enricher.enrich(any(), any())).thenAnswer(inv -> inv.getArgument(0));
    }

    // ---------------- casos base (adaptados) ----------------

    @Test
    void nullThrowable_returnsUnexpectedResponse()
    {
        when(mapper.toResponse((Throwable) null)).thenReturn(UNEXPECTED_RESPONSE);

        ErrorInfo r = handler.toErrorInfo(null);

        assertSame(UNEXPECTED_RESPONSE, r);
        verify(mapper).toResponse((Throwable) null);
        verify(enricher).enrich(UNEXPECTED_RESPONSE, null);
    }

    @Test
    void nullThrowable_doesNotCallInspector()
    {
        when(mapper.toResponse((Throwable) null)).thenReturn(UNEXPECTED_RESPONSE);

        handler.toErrorInfo(null);

        verify(mapper).toResponse((Throwable) null);
    }

    @Test
    void nullThrowable_doesNotNotifyListener()
    {
        when(mapper.toResponse((Throwable) null)).thenReturn(UNEXPECTED_RESPONSE);

        handler.toErrorInfo(null);

        verifyNoInteractions(listener);
    }

    @Test
    void appExceptionAtRoot_mapsAndEnriches()
    {
        AppException ex = new TestAppException(
            "TEST-001", "Default", StandardErrorCategories.NOT_FOUND, "No encontrado");
        when(mapper.toResponse((AppException) ex)).thenReturn(APP_RESPONSE);

        ErrorInfo r = handler.toErrorInfo(ex);

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

        ErrorInfo r = handler.toErrorInfo(wrapper);

        assertSame(APP_RESPONSE, r);
        verify(mapper).toResponse((AppException) domain);
        verify(mapper, never()).toResponse(wrapper);
    }

    @Test
    void genericException_mapsAsUnexpected()
    {
        RuntimeException ex = new RuntimeException("boom");
        when(mapper.toResponse((Throwable) ex)).thenReturn(UNEXPECTED_RESPONSE);

        ErrorInfo r = handler.toErrorInfo(ex);

        assertSame(UNEXPECTED_RESPONSE, r);
        verify(mapper).toResponse((Throwable) ex);
        verify(enricher).enrich(UNEXPECTED_RESPONSE, ex);
    }

    @Test
    void enricherIsCalledOnce_perResponse()
    {
        RuntimeException ex = new RuntimeException("boom");
        when(mapper.toResponse((Throwable) ex)).thenReturn(UNEXPECTED_RESPONSE);

        handler.toErrorInfo(ex);

        verify(enricher, times(1)).enrich(any(), any());
    }

    @Test
    void enrichedResponse_isReturnedAsIs()
    {
        RuntimeException ex = new RuntimeException("boom");
        ErrorInfo enriched = UNEXPECTED_RESPONSE.withError("stacktrace");
        when(mapper.toResponse((Throwable) ex)).thenReturn(UNEXPECTED_RESPONSE);
        when(enricher.enrich(UNEXPECTED_RESPONSE, ex)).thenReturn(enriched);

        ErrorInfo r = handler.toErrorInfo(ex);

        assertSame(enriched, r);
        assertFalse(r.errors().isEmpty());
    }

    // ---------------- nuevos: notificación al listener ----------------

    @Test
    void appException_notifiesListenerAsHandled()
    {
        AppException ex = new TestAppException(
            "TEST-001", "Default", StandardErrorCategories.NOT_FOUND, "No encontrado");
        when(mapper.toResponse((AppException) ex)).thenReturn(APP_RESPONSE);

        handler.toErrorInfo(ex);

        verify(listener).onError(ex, APP_RESPONSE, ErrorListener.Severity.HANDLED);
    }

    @Test
    void genericException_notifiesListenerAsUnexpected()
    {
        RuntimeException ex = new RuntimeException("boom");
        when(mapper.toResponse((Throwable) ex)).thenReturn(UNEXPECTED_RESPONSE);

        handler.toErrorInfo(ex);

        verify(listener).onError(
            ex, UNEXPECTED_RESPONSE, ErrorListener.Severity.UNEXPECTED);
    }

    @Test
    void wrappedAppException_notifiesListenerWithResolvedAppException()
    {
        AppException domain = new TestAppException(
            "TEST-002", "Default", StandardErrorCategories.INVALID_INPUT, "Inválido");
        Throwable wrapper = new RuntimeException("infra", domain);
        when(mapper.toResponse((AppException) domain)).thenReturn(APP_RESPONSE);

        handler.toErrorInfo(wrapper);

        verify(listener).onError(domain, APP_RESPONSE, ErrorListener.Severity.HANDLED);
        verify(listener, never()).onError(
            wrapper, APP_RESPONSE, ErrorListener.Severity.UNEXPECTED);
    }

    @Test
    void listenerIsNotifiedOnce_perResponse()
    {
        RuntimeException ex = new RuntimeException("boom");
        when(mapper.toResponse((Throwable) ex)).thenReturn(UNEXPECTED_RESPONSE);

        handler.toErrorInfo(ex);

        verify(listener, times(1)).onError(any(), any(), any());
    }

    @Test
    void listenerReceivesEnrichedInfo_notRawMappedInfo()
    {
        RuntimeException ex = new RuntimeException("boom");
        ErrorInfo enriched = UNEXPECTED_RESPONSE.withTraceId("trace-123");
        when(mapper.toResponse((Throwable) ex)).thenReturn(UNEXPECTED_RESPONSE);
        when(enricher.enrich(UNEXPECTED_RESPONSE, ex)).thenReturn(enriched);

        handler.toErrorInfo(ex);

        verify(listener).onError(
            ex, enriched, ErrorListener.Severity.UNEXPECTED);
    }

    // ---------------- compatibilidad ----------------

    @Test
    void twoArgConstructor_defaultsToNoOpListener()
    {
        var noOpHandler = new ErrorHandler(enricher, mapper);
        when(mapper.toResponse((Throwable) null)).thenReturn(UNEXPECTED_RESPONSE);

        // no debe lanzar por listener nulo
        assertSame(UNEXPECTED_RESPONSE, noOpHandler.toErrorInfo(null));
    }

    @Test
    void nullListenerInConstructor_fallsBackToNoOp()
    {
        var h = new ErrorHandler(enricher, mapper, null);
        RuntimeException ex = new RuntimeException("boom");
        when(mapper.toResponse((Throwable) ex)).thenReturn(UNEXPECTED_RESPONSE);

        assertSame(UNEXPECTED_RESPONSE, h.toErrorInfo(ex));
    }
}
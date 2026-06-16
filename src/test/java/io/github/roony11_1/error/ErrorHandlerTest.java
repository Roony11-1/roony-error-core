package io.github.roony11_1.error;

import io.github.roony11_1.error.core.ErrorCategory;
import io.github.roony11_1.error.core.ErrorHandler;
import io.github.roony11_1.error.core.ErrorResponse;
import io.github.roony11_1.error.core.StandardErrorCategories;
import io.github.roony11_1.error.core.exceptions.AppException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ErrorHandlerTest 
{

    static class TestAppException extends AppException 
    {
        public TestAppException(String code, String defaultMessage, ErrorCategory category, String customMessage) 
        {
            super(code, defaultMessage, category, customMessage);
        }
        public TestAppException(String code, String defaultMessage, ErrorCategory category) 
        {
            super(code, defaultMessage, category);
        }
        public TestAppException(String code, String defaultMessage, ErrorCategory category, Throwable cause) 
        {
            super(code, defaultMessage, category, cause);
        }
    }

    private static final String PROFILE_KEY = "app.profile";
    private String originalProfile;

    @BeforeEach
    void setUp() 
    {
        originalProfile = System.getProperty(PROFILE_KEY);
        System.clearProperty(PROFILE_KEY);
    }

    @AfterEach
    void tearDown() 
    {
        if (originalProfile != null) 
        {
            System.setProperty(PROFILE_KEY, originalProfile);
        } 
        else 
        {
            System.clearProperty(PROFILE_KEY);
        }
    }

    @Test
    void toErrorResponse_withAppException_shouldReturnCorrectCodeAndMessage() 
    {
        AppException ex = new TestAppException(
            "TEST-001",
            "Mensaje por defecto",
            StandardErrorCategories.ALREADY_EXISTS,
            "Mensaje personalizado"
        );

        ErrorResponse response = ErrorHandler.toErrorResponse(ex);

        assertEquals("TEST-001", response.getCode());
        assertEquals("Mensaje personalizado", response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void toErrorResponse_withAppException_shouldNotContainDetailsInProduction() 
    {
        AppException ex = new TestAppException(
            "TEST-002", "Default", StandardErrorCategories.NOT_FOUND, "Not found"
        );

        ErrorResponse response = ErrorHandler.toErrorResponse(ex);

        assertTrue(response.getDetails().isEmpty());
    }

    @Test
    void toErrorResponse_withAppException_shouldContainDetailsInDevelopmentMode()
    {
        System.setProperty(PROFILE_KEY, "dev");
        AppException ex = new TestAppException(
            "TEST-003", "Default dev", StandardErrorCategories.INVALID_INPUT, "Invalid input"
        );

        ErrorResponse response = ErrorHandler.toErrorResponse(ex);

        assertFalse(response.getDetails().isEmpty());
        assertEquals(1, response.getDetails().size());
        assertTrue(response.getDetails().get(0).contains("TestAppException"));
    }

    @Test
    void toErrorResponse_withGenericException_shouldReturnInternalErrorCode() 
    {
        RuntimeException genericEx = new RuntimeException("Algo explotó");

        ErrorResponse response = ErrorHandler.toErrorResponse(genericEx);

        assertEquals("ERR-0005", response.getCode());
        assertEquals("Error interno inesperado", response.getMessage());
    }

    @Test
    void toErrorResponse_withGenericException_inDevelopment_shouldAddDetails() 
    {
        System.setProperty(PROFILE_KEY, "dev");
        RuntimeException genericEx = new RuntimeException("Error de red");

        ErrorResponse response = ErrorHandler.toErrorResponse(genericEx);

        assertFalse(response.getDetails().isEmpty());
        assertTrue(response.getDetails().get(0).contains("RuntimeException"));
    }

    @Test
    void toErrorResponse_withNull_shouldNotThrowException() 
    {
        assertDoesNotThrow(() -> ErrorHandler.toErrorResponse(null));
    }

    @Test
    void toErrorResponse_withAppExceptionAndCause_shouldPreserveCause() 
    {
        Throwable cause = new IllegalStateException("Causa raíz");
        AppException ex = new TestAppException("TEST-004", "Con causa", StandardErrorCategories.INTERNAL_ERROR, cause);

        ErrorResponse response = ErrorHandler.toErrorResponse(ex);

        assertEquals("TEST-004", response.getCode());
        assertEquals("Con causa", response.getMessage());
        assertTrue(response.getDetails().isEmpty());
        assertEquals(cause, ex.getCause());
    }

    @Test
    void appException_shouldBeExtensibleAndWorkWithErrorHandler() 
    {
        class PaymentFailedException extends AppException 
        {
            public PaymentFailedException(String transactionId, String reason, Throwable cause) 
            {
                super("PAY-001",
                    "Pago fallido para la transacción " + transactionId,
                    StandardErrorCategories.INTERNAL_ERROR,
                    "Pago fallido: " + reason,
                    cause);
            }
        }

        Throwable rootCause = new IllegalStateException("Fondos insuficientes");
        PaymentFailedException ex = new PaymentFailedException("TXN-123", "Fondos insuficientes", rootCause);

        assertEquals("PAY-001", ex.getCode());
        assertEquals("Pago fallido para la transacción TXN-123", ex.getDefaultMessage());
        assertEquals(StandardErrorCategories.INTERNAL_ERROR, ex.getCategory());
        assertEquals("Pago fallido: Fondos insuficientes", ex.getDisplayMessage());
        assertEquals(rootCause, ex.getCause());

        ErrorResponse response = ErrorHandler.toErrorResponse(ex);

        assertEquals("PAY-001", response.getCode());
        assertEquals("Pago fallido: Fondos insuficientes", response.getMessage());
    }
}
package io.github.roony11_1.error.support;

import io.github.roony11_1.error.core.ErrorCategory;
import io.github.roony11_1.error.core.exceptions.AppException;

public class TestAppException extends AppException
{
    public TestAppException(String code, String defaultMessage,
                            ErrorCategory category, String customMessage)
    {
        super(code, defaultMessage, category, customMessage);
    }

    public TestAppException(String code, String defaultMessage,
                            ErrorCategory category)
    {
        super(code, defaultMessage, category);
    }

    public TestAppException(String code, String defaultMessage,
                            ErrorCategory category, Throwable cause)
    {
        super(code, defaultMessage, category, cause);
    }
}

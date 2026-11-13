package io.github.roony11_1.error.core.handler;

import io.github.roony11_1.error.core.StandardErrorCategories;
import io.github.roony11_1.error.core.exceptions.AppException;
import io.github.roony11_1.error.support.TestAppException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionInspectorTest
{
    @Test
    void returnsNullWhenThrowableIsNull()
    {
        assertNull(ExceptionInspector.find(null));
    }

    @Test
    void returnsNullWhenChainHasNoAppException()
    {
        Throwable root = new RuntimeException("boom",
            new IllegalStateException("nested"));

        assertNull(ExceptionInspector.find(root));
    }

    @Test
    void findsAppExceptionAtRoot()
    {
        AppException ex = new TestAppException(
            "TEST-001", "Default", StandardErrorCategories.NOT_FOUND, "No encontrado");

        assertSame(ex, ExceptionInspector.find(ex));
    }

    @Test
    void findsAppExceptionWrappedOnce()
    {
        AppException domain = new TestAppException(
            "TEST-002", "Default", StandardErrorCategories.INVALID_INPUT, "Inválido");
        Throwable wrapper = new RuntimeException("infra", domain);

        assertSame(domain, ExceptionInspector.find(wrapper));
    }

    @Test
    void findsAppExceptionDeepInChain()
    {
        AppException domain = new TestAppException(
            "TEST-003", "Default", StandardErrorCategories.INTERNAL_ERROR, "Interno");
        Throwable deep = new RuntimeException("nivel1",
            new IllegalArgumentException("nivel2", domain));

        assertSame(domain, ExceptionInspector.find(deep));
    }

    @Test
    void terminatesOnCyclicCauseChain()
    {
        Throwable a = new RuntimeException("a");
        Throwable b = new RuntimeException("b", a);
        a.initCause(b);   // a → b → a → b → ...

        assertDoesNotThrow(() -> ExceptionInspector.find(a));
        assertNull(ExceptionInspector.find(a));
    }
}
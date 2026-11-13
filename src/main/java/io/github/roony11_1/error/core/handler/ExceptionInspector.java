package io.github.roony11_1.error.core.handler;

import io.github.roony11_1.error.core.exceptions.AppException;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Busca una {@link AppException} en la cadena de causas de un throwable.
 *
 * <p>Permite que las excepciones de dominio se reconozcan aunque estén
 * envueltas por excepciones de infraestructura.</p>
 *
 * <p>La búsqueda usa identidad de referencia para detectar ciclos,
 * de modo que una cadena circular termina en lugar de colgarse.</p>
 */
public final class ExceptionInspector
{
    private ExceptionInspector() {}

    /**
     * Recorre la cadena de causas desde {@code throwable} hacia atrás
     * y devuelve la primera {@link AppException} encontrada.
     *
     * @param throwable el throwable desde el cual empezar; puede ser {@code null}
     * @return la primera {@code AppException} de la cadena, o {@code null}
     *         si no existe o si {@code throwable} es {@code null}
     */
    public static AppException find(Throwable throwable)
    {
        Set<Throwable> seen = Collections.newSetFromMap(new IdentityHashMap<>());
        Throwable current = throwable;

        while (current != null && seen.add(current))
        {
            if (current instanceof AppException appEx)
                return appEx;

            current = current.getCause();
        }

        return null;
    }
}
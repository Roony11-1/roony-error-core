package io.github.roony11_1.error.core.exceptions;

import io.github.roony11_1.error.core.StandardErrorCategories;

public class InvalidInputException extends AppException 
{
    public InvalidInputException(String details) 
    {
        super("ERR-0002", "Entrada inválida: " + details, StandardErrorCategories.INVALID_INPUT, "Entrada inválida: " + details);
    }
}
package io.github.roony11_1.error.core;

import java.util.function.UnaryOperator;

public class DefaultProfileResolver implements IProfileResolver
{
    private final String[] PROPERTY_KEYS = 
    {
        "app.profile",
        "spring.profiles.active"
    };
    
    private final UnaryOperator<String> systemProperty;
    private final UnaryOperator<String> environment;

    public DefaultProfileResolver()
    {
        this(System::getProperty, System::getenv);
    }

    DefaultProfileResolver(UnaryOperator<String> systemProperty,
                           UnaryOperator<String> environment)
    {
        this.systemProperty = systemProperty;
        this.environment = environment;
    }

    @Override
    public String resolve()
    {
        for (String property : PROPERTY_KEYS)
        {
            String value = systemProperty.apply(property);

            if (value != null && !value.isEmpty())
                return value;
        }

        String env = environment.apply("SPRING_PROFILES_ACTIVE");
        
        return (env == null || env.isEmpty())
            ? environment.apply("APP_PROFILE")
            : env;
    }
}
package io.github.roony11_1.error.core.handler;

import io.github.roony11_1.error.core.dto.ErrorResponse;

public final class DevelopmentDetailsEnricher implements IErrorEnricher
{
    private final IProfileResolver profileResolver;

    public DevelopmentDetailsEnricher(IProfileResolver profileResolver)
    {
        this.profileResolver = profileResolver;
    }

    @Override
    public ErrorResponse enrich(ErrorResponse response, Throwable ex)
    {
        if (ex != null && isDevelopment())
            return response.withError(String.valueOf(ex));
        
        return response;
    }

    private boolean isDevelopment()
    {
        String profile = profileResolver.resolve();
        return profile != null && profile.toLowerCase().contains("dev");
    }
}
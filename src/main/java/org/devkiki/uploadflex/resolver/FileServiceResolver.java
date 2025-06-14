package org.devkiki.uploadflex.resolver;

import org.devkiki.uploadflex.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FileServiceResolver {
    private final Map<String, FileService> serviceMap;
    private final String defaultProvider;

    public FileServiceResolver(
            Map<String, FileService> serviceMap,
            @Value("${app.file.provider}") String defaultProvider
    ) {
        this.serviceMap = serviceMap;
        this.defaultProvider = defaultProvider;
    }

    public FileService resolve() {
        FileService service = serviceMap.get(defaultProvider);
        if (service == null) {
            throw new IllegalArgumentException("No file service found for provider: " + defaultProvider);
        }
        return service;
    }

    public FileService resolve(String providerKey) {
        if (providerKey == null || providerKey.isBlank()) {
            return resolve();
        }

        FileService service = serviceMap.get(providerKey);
        if (service == null) {
            throw new IllegalArgumentException("No file service found for provider: " + providerKey);
        }
        return service;
    }

}

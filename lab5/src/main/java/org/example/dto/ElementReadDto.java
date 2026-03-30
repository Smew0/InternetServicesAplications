package org.example.dto;

import java.util.UUID;

public record ElementReadDto(UUID id, String name, double price, UUID categoryId) {}

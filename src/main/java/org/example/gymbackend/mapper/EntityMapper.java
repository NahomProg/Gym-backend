package org.example.gymbackend.mapper;

public interface EntityMapper<E, D> {
    D toResponse(E entity);
}

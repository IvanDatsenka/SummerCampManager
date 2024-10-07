package org.example.dao;

import org.example.entity.Child;

import java.util.List;
import java.util.Optional;

public interface Dao<K, E> {
    E save(E e);
    Optional<E> findById(K id);
    boolean update (E e);
    boolean delete(K id);
    List<E> findAll();
}


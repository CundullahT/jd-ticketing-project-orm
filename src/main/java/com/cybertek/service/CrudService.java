package com.cybertek.service;

import java.util.List;

public interface CrudService<T,ID> {

    List<T> findAll();
    T findById(ID id);
    void delete(T object);
    void deleteById(ID id);
    T save(T object);
    void update(T object);

}

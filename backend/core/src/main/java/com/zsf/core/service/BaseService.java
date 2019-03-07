package com.zsf.core.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author yyq
 */
public interface BaseService<T, ID> {

    T findOne(ID id);

    T findOne(Example<T> example);

    List<T> findAll();

    List<T> findAll(Example<T> example);

    List<T> findAll(Example<T> example, Sort sort);

    Page<T> findAll(Example<T> example, Pageable pageable);

    boolean exist(ID id);

    long count();

    T save(T object);

    List<T> save(List<T> list);

    void deleteById(ID id);


    void delete(T object);

    void delete(List<T> list);

    void deleteAll();
}

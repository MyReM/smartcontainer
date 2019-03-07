package com.zsf.core.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author yyq
 */
@NoRepositoryBean
public interface BaseDAO<T, ID> extends JpaRepository<T, ID> {


}

package com.zsf.core.service.impl;

import com.zsf.core.config.shiro.UserInfo;
import com.zsf.core.dao.BaseDAO;
import com.zsf.core.service.BaseService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author yyq
 */
@Service
public class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

    private UserInfo userInfo = new UserInfo();

    public UserInfo getUserInfo() {
        Subject subject = SecurityUtils.getSubject();
        BeanUtils.copyProperties(subject.getPrincipal(), userInfo);
        return userInfo;
    }

    private BaseDAO<T, ID> baseDAO;


    public void setBaseDAO(BaseDAO<T, ID> baseDAO) {
        this.baseDAO = baseDAO;
    }

    @Override
    public T findOne(ID id) {
        return baseDAO.findById(id).orElseThrow(() -> new NullPointerException("未找到该记录！"));
    }

    @Override
    public T findOne(Example<T> example) {
        Optional<T> optional = baseDAO.findOne(example);
        return optional.orElse(null);
    }

    @Override
    public List<T> findAll(){
        return baseDAO.findAll();
    }

    @Override
    public List<T> findAll(Example<T> example){
        return baseDAO.findAll(example);
    }

    @Override
    public List<T> findAll(Example<T> example, Sort sort){
        return baseDAO.findAll(example,sort);
    }

    @Override
    public Page<T> findAll(Example<T> example, Pageable pageable){
        return baseDAO.findAll(example, pageable);
    }

    @Override
    public boolean exist(ID id){
        return baseDAO.existsById(id);
    }

    public boolean exist(Example<T> example){
        return baseDAO.exists(example);
    }

    @Override
    public long count(){
        return baseDAO.count();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public T save(T object) {
        return baseDAO.save(object);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<T> save(List<T> list) {
        return baseDAO.saveAll(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteById(ID id) {
        baseDAO.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(T object) {
        baseDAO.delete(object);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(List<T> list) {
        baseDAO.deleteAll(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAll() {
        baseDAO.deleteAll();
    }

}

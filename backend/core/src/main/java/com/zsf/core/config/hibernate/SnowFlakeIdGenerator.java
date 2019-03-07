package com.zsf.core.config.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * 生成id的方法
 */
@Service
@Component
@Transactional
public class SnowFlakeIdGenerator implements IdentifierGenerator, Configurable {

    public static final String TYPE = "com.zsf.core.config.hibernate.SnowFlakeIdGenerator";

    public String workId;

    public String dataId;

    public SnowFlakeIdWorker snowFlakeIdWorker;

    /**
     * hibernate自定义主键生成规则必须实现 IdentifierGenerator  generate 为默认方法
     */
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {

        Long id = snowFlakeIdWorker.nextId();
        if (id != null) {
            return id;
        }else {
            return null;
        }
    }

    /**
     * 加载配置文件中的数据初始化snowflakeworker类
     */
    @Override
    public void configure(Type type, Properties properties, ServiceRegistry registry) throws MappingException {

        //加载配置文件
        InputStream is = SnowFlakeIdGenerator.class.getResourceAsStream("/snowflake.properties");
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        workId = properties.getProperty("workId");
        dataId = properties.getProperty("dataId");

        if (StringUtils.hasLength(dataId) && StringUtils.hasLength(workId)) {
            snowFlakeIdWorker = new SnowFlakeIdWorker(Long.valueOf(workId), Long.valueOf(dataId));
        }
    }

    public String getWorkId() {
        return workId;
    }

    public void setWorkId(String workId) {
        this.workId = workId;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }
}

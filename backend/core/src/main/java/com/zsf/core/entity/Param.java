package com.zsf.core.entity;

import com.zsf.core.config.hibernate.SnowFlakeIdGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yyq
 */
@Data
@Entity(name = "sys_param")
@Table(name = "sys_param")
public class Param implements Serializable {

    private static final long serialVersionUID = 4019162931458814913L;

    @Id
    @GeneratedValue(generator = "snowFlake")
    @GenericGenerator(name = "snowFlake", strategy = SnowFlakeIdGenerator.TYPE)
    @Column(nullable = false)
    private Long paramId;

    private String paramName;

    private String paramValue;


}

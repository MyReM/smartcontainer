package com.zsf.core.entity;

import com.zsf.core.config.hibernate.SnowFlakeIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yyq
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "sys_dept")
@Table(name = "sys_dept")
public class Dept implements Serializable {

    private static final long serialVersionUID = 5633809583694409995L;

    @Id
    @GeneratedValue(generator = "snowFlake")
    @GenericGenerator(name = "snowFlake", strategy = SnowFlakeIdGenerator.TYPE)
    @Column(nullable = false)
    private Long deptId;

    private String simpleName;

    private String fullName;

    private Integer sortNum;

    private Integer level;

    private Integer status;

    private String comment;

    private Long parentId;

}

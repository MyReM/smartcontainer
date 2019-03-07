package com.zsf.core.entity;

import com.zsf.core.config.hibernate.SnowFlakeIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * @author yyq
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "sys_resource")
@Table(name = "sys_resource")
public class Resource implements Serializable {

    private static final long serialVersionUID = -2759997732985473883L;

    @Id
    @GeneratedValue(generator = "snowFlake")
    @GenericGenerator(name = "snowFlake", strategy = SnowFlakeIdGenerator.TYPE)
    @Column(nullable = false)
    private Long resourceId;

    private String resourceName;

    private String resourceValue;

    private String comment;

}

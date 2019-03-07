package com.zsf.core.entity;

import com.zsf.core.config.hibernate.SnowFlakeIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author yyq
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "sys_menu")
@Table(name = "sys_menu")
public class Menu implements Serializable {

    private static final long serialVersionUID = -5417810396919706043L;

    @Id
    @GeneratedValue(generator = "snowFlake")
    @GenericGenerator(name = "snowFlake", strategy = SnowFlakeIdGenerator.TYPE)
    @Column(nullable = false)
    private Long menuId;

    private String menuCode;

    private String menuName;

    private String menuValue;

    private Long parentId;

    private Integer status;

    private Boolean hidden;

    private Boolean fullScreen;

    private Integer level;

    private Integer sortNum;

    private String icon;

    private String comment;
}

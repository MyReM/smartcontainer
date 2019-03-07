package com.zsf.core.entity;

import com.zsf.core.config.hibernate.SnowFlakeIdGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author yyq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sys_role")
@Table(name = "sys_role")
public class Role implements Serializable {

    private static final long serialVersionUID = -5500972104927566654L;

    @Id
    @GeneratedValue(generator = "snowFlake")
    @GenericGenerator(name = "snowFlake", strategy = SnowFlakeIdGenerator.TYPE)
    @Column(nullable = false)
    private Long roleId;

    private String roleName;

    private Integer level;

    private Integer status;

    private String comment;

    private Date createTime;

    private Long parentId;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "sys_role_permission",
            joinColumns =@JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "permissionId")
    )
    private Set<Permission> permissions;

    @ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "sys_role_menu",
            joinColumns =@JoinColumn(name = "roleId"),
            inverseJoinColumns = @JoinColumn(name = "menuId")
    )
    private Set<Menu> menuList;

}

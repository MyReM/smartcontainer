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
import java.util.Set;

/**
 * @author yyq
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "sys_user")
@Table(name = "sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = -3996337648338308459L;

    @Id
    @GeneratedValue(generator = "snowFlake")
    @GenericGenerator(name = "snowFlake", strategy = SnowFlakeIdGenerator.TYPE)
    @Column(nullable = false)
    private Long userId;

    private String userCode;

    private String userName;

    private String password;

    private String salt;

    private String description;

    private String avator;

    private Integer status;

    private Date birthday;

    private Integer sex;

    private String email;

    private String phone;

    private Date createTime;

    private Long deptId;

    private Long roleId;

    public static class Status {

        public static final Integer FORBIDDEN = 0;

        public static final Integer NORMAL = 1;
    }

}

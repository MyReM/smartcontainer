package com.zsf.container.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "goods")
@Table(name = "goods")
public class Goods {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;
    private String name;//物品名称
    private Integer count;//物品总数
    private String address;//生产地
    private String remark;//物品备注
    private int status;//物品状态
    private String describe;//物品描述
    private Long userId;//用户id
    private Integer type;//物品类型
    private String reserver1;
    private String reserver2;
    private String reserver3;
    private String reserver4;
}

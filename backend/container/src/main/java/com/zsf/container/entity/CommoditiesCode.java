package com.zsf.container.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author ReM
 * 商品编码
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "commodity_commodities_code")
public class CommoditiesCode {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id; // 唯一id

    private String commoditiesCode; //编码

    private Integer commoditiesType; // 商品类型

    private Integer isIn; // 是否在库

    private String reserve1; // 原预留1

    private String reserve2; // 预留字段

    private String reserve3; // 预留字段

    private String reserve4; // 预留字段
}

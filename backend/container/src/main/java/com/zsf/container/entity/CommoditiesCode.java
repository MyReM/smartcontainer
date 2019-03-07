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

    private Integer searchCount; // 判断是否拿出去过，显示一次后无论是否归还，不再显示

    private Integer reserve1; // 原预留1,现改为是否在库

    private Integer reserve2; // 预留字段,现改为统计不在库次数，达到一定次数修改状态

    private Integer reserve3; // 预留字段,现改为统计扫描次数

    private String reserve4; // 预留字段
}

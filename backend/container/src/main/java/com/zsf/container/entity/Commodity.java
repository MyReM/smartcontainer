package com.zsf.container.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author ReM
 * 商品种类
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "commodity_commodity")
public class Commodity {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id; // 唯一id

    private String commoditiesName; //商品名称

    private Integer commoditiesType; // 商品类型

    private String commoditiesTypeName; // 商品类型名称

    private Integer sum; // 在库数量

    private Integer totalNumber; // 总数量

    private Integer isShow; // 是否显示商品

    @Transient
    private GoodsImg goodsImg; // 图片

    @Transient
    private Object object; // 子类型

    private String reserve1; // 预留字段

    private String reserve2; // 预留字段

    private String reserve3; // 预留字段

    private String reserve4; // 预留字段

}

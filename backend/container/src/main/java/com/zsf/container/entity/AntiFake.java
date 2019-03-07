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
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_anti_fake")
public class AntiFake {

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(strategy = "increment",name = "increment")
    private Long id;

    private String epc;

    private String qrCode;

    private String url;

    private Long typeId;

    private String reserved1;
}

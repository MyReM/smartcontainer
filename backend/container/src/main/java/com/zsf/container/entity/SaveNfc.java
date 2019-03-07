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
@Table(name = "nfc_save")
public class SaveNfc {
    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(strategy = "increment",name = "increment")
    private Long id;

    private String nfc;
}

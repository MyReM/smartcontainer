package com.zsf.core.model;

import com.zsf.core.entity.Dept;
import lombok.Data;

import java.util.List;

/**
 * @author yyq
 */
@Data
public class DeptModel extends Dept {

    private Dept parent;

    private List<Dept> children;

}

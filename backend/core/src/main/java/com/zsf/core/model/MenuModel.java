package com.zsf.core.model;

import com.zsf.core.entity.Menu;
import lombok.Data;

import java.util.List;

/**
 * @author yyq
 */
@Data
public class MenuModel extends Menu {

    private Menu parent;

    private List<Menu> children;

}

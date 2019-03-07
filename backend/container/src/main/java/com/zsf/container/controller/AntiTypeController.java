package com.zsf.container.controller;

import com.zsf.container.entity.AntiType;
import com.zsf.container.service.AntiTypeService;
import com.zsf.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author ReM
 */
@RestController
@RequestMapping("/api/antiType")
public class AntiTypeController extends BaseController {

    @Autowired
    private AntiTypeService antiTypeService;

    @RequestMapping("/getPage")
    public Page<AntiType> getPage(@RequestParam(value = "page",defaultValue = "0") Integer page,
                                  @RequestParam(value = "size",defaultValue = "10") Integer size,
                                  AntiType antiType) {
        Page<AntiType> pages = antiTypeService.getPage(page,size,antiType);
        return pages;
    }

    @GetMapping("/getAll")
    public List<AntiType> getAllAntiType() {
        return antiTypeService.findAll();
    }

    @PostMapping("/add")
    public String saveCommodities(@RequestBody(required = false) AntiType antiType) {

        // 新增后返回一个对象
        AntiType antiType1 = antiTypeService.save(antiType);
        // 通过返回的对象获取id并设置到typ中
        antiType1.setType(antiType1.getId());
        // 执行保存
        antiTypeService.save(antiType1);
        return SUCCESS;
    }

    @PutMapping("/update")
    public String updateCommoditiesCode(@RequestBody(required = false) AntiType antiType) {

        antiTypeService.save(antiType);
        return SUCCESS;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCommoditiesCode(@PathVariable(value = "id") Long id) {

        antiTypeService.deleteById(id);
        return SUCCESS;
    }
}

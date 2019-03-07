package com.zsf.container.controller;

import com.zsf.container.service.ImgPathService;
import com.zsf.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ReM
 */
@RestController
@RequestMapping("/api/imgPath")
public class ImgPathController extends BaseController {

    @Autowired
    private ImgPathService imgPathService;
}

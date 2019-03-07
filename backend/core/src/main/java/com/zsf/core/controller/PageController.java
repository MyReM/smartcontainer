package com.zsf.core.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yyq
 */
@Controller
@RequestMapping(value = "/api/page")
public class PageController {

    @GetMapping(value = "/druid")
    public ModelAndView druid(HttpServletResponse response) throws IOException {

        return new ModelAndView("/druid/login.html");

    }
}

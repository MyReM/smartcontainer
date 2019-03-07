package com.zsf.core.controller;

import com.zsf.core.entity.Log;
import com.zsf.core.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yyq
 */
@RestController
@RequestMapping(value = "/api/log")
public class LogController {

    @Autowired
    private LogService logService;

    @GetMapping(value = "/filter")
    public Map<String, Object> filter(){
        Map<String, Object> map = new HashMap<>();

        Map<Integer, String> levelMap = Log.LogLevel.getLogLevel();
        map.put("level", levelMap);

        Map<Integer, String> typeMap = Log.LogType.getLogType();
        map.put("type", typeMap);
        return map;
    }

    @GetMapping
    public Page findAll(@RequestParam(value = "page", defaultValue = "0") Integer page, @RequestParam(value = "size", defaultValue = "10") Integer size, Log log) {

        if (!Objects.equals(page, 0)) {
            page = page - 1;
        }

        Pageable pageable = PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "logId"));

        ExampleMatcher matcher = ExampleMatcher.matching();

        Example<Log> example = Example.of(log, matcher);
        Page<Log> pages = logService.findAll(example, pageable);

        return pages;
    }

    @DeleteMapping(value = "/{logId}")
    public String delete(@PathVariable(value = "logId") Long logId){


        Log log = logService.findOne(logId);

        Objects.requireNonNull(log, "该记录不存在");

        logService.delete(log);

        return "success";
    }

    @DeleteMapping
    public String deleteAll(){

        logService.deleteAll();

        return "success";
    }

}

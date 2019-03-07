package com.zsf.container.controller;

import com.yang.serialport.manager.SerialPortManager;
import com.zsf.container.entity.AntiFake;
import com.zsf.container.serialutils.FindByHandleUtils;
import com.zsf.container.serialutils.GetOutRFID;
import com.zsf.container.service.AntiFakeService;
import com.zsf.core.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@RestController
@RequestMapping("/api/antiFake")
public class AntiFakeController extends BaseController {

    @Autowired
    private AntiFakeService antiFakeService;

    private FindByHandleUtils findByHandleUtils = new FindByHandleUtils();

    @RequestMapping("/getPage")
    public Page<AntiFake> getPage(@RequestParam(value = "page",defaultValue = "0") Integer page,
                                  @RequestParam(value = "size",defaultValue = "10") Integer size,
                                  AntiFake antiFake) {
        Page<AntiFake> pages = antiFakeService.getPages(page,size,antiFake);
        return pages;
    }

    @PostMapping("/add")
    public String saveCommodities(@RequestBody(required = false) AntiFake antiFake) {

        // 新增后返回一个对象
        antiFakeService.save(antiFake);
        return SUCCESS;
    }

    @PutMapping("/update")
    public String updateCommoditiesCode(@RequestBody(required = false) AntiFake antiFake) {

        antiFakeService.save(antiFake);
        return SUCCESS;
    }

    @DeleteMapping("/delete/{id}")
    public String deleteCommoditiesCode(@PathVariable(value = "id") Long id) {

        antiFakeService.deleteById(id);
        return SUCCESS;
    }
    // 获取串口信息
    @GetMapping("/getSerial")
    public List<String> getSerial() {
        List<String> li = SerialPortManager.findPorts();
        return li;
    }
    // 开启串口
    @GetMapping("/openNFC/{portName}")
    public Integer openNFC(@PathVariable(value = "portName") String portName) {
        Integer status = findByHandleUtils.open(portName);
        return status;
    }
    // 扫描nfc
    @GetMapping("/searchNFC")
    public String searchNFC() {
        String nfc = findByHandleUtils.getEPCUid();
        return nfc;
    }
}

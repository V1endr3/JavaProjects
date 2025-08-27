package org.example.controller;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.example.constant.BizAspectConstant;
import org.example.service.AnyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class InvokeController {

    @Autowired
    private AnyService anyService;

    @LogRecord(
            bizNo = "{{T(org.example.util.RandUtil).getName()}}",
            type = BizAspectConstant.FIRST_CONTROLLER,
            success = "请求到了第一个控制器, 拿到了变量{{#hello}}"
    )
    @GetMapping("/hello")
    public String greetingForName() {
        LogRecordContext.putVariable("hello", "world");
        return anyService.getName();
    }

    @GetMapping("/location")
    public String greetingForAddress() {
        return anyService.getAddress();
    }
}

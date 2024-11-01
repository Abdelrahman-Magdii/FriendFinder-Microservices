package com.spring.usermanagement.controller;

import com.spring.commonlib.config.translate.BundleTranslator;
import com.spring.commonlib.model.bundle.BundleErrorMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final BundleTranslator bundleTranslator;

    public TestController(BundleTranslator bundleTranslator) {
        this.bundleTranslator = bundleTranslator;
    }

    @GetMapping("/start")
    public String test() {
        return bundleTranslator.getMessage("error.loginNameOrEmail.invalid");
    }

    @GetMapping("/start/list")
    public BundleErrorMessage testList() {

        return bundleTranslator.getMessages("error.loginNameOrEmail.invalid");
    }
}

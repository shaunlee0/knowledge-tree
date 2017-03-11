package com.shaun.knowledgetree.frontend;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller("error")
public class ExceptionController implements ErrorController {

    private static final String PATH = "/error";

    @RequestMapping(value = PATH, method = RequestMethod.GET)
    public String error() {
        return "error";
    }

    @Override
    public String getErrorPath() {
        return PATH;
    }
}
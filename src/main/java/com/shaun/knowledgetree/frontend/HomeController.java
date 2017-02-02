package com.shaun.knowledgetree.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class HomeController {

//	@RequestMapping(value = "/test", method = RequestMethod.GET)
//	public ModelAndView init(@ModelAttribute("model") ModelMap model){
//		model.addAttribute("foo","bar");
//		return new ModelAndView("/WEB-INF/views/ftl/home.jsp",model);
//	}
//
//	@RequestMapping("/test2")
//	public String testMethodTwo(){
//		return "/WEB-INF/views/ftl/home.jsp";
//	}

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home() {
		return "home";
	}

	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public ModelAndView testViewAndModel() {
		HashMap<String,Object> model = new HashMap<>();
		model.put("foo","bar");
		return new ModelAndView("home",model);
	}
}

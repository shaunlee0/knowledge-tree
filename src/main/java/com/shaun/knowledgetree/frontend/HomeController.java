package com.shaun.knowledgetree.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
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

	@RequestMapping(value = "/knowledge-tree")
	public String home() {
		return "home";
	}

	@RequestMapping(value = "/validate", method = RequestMethod.POST, params = "searchTerm")
	@ResponseBody
	public String validateSearch(@RequestParam("searchTerm") String searchTerm, HttpServletRequest request) {
		System.out.println(searchTerm);
//		return new SearchTermValidation("failure");
		//TODO test with postman before integrating.
		return "{\"status\":\"failure\"}";

	}
}

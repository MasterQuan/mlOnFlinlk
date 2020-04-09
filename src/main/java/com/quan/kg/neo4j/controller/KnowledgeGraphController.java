package com.quan.kg.neo4j.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/neo4j")
public class KnowledgeGraphController {
	@RequestMapping(value = "/showMessage", method = RequestMethod.GET)
	String getUserByGet(@RequestParam(value = "userName") String userName){
		return "Hello " + userName;
	}

	@RequestMapping("/hello")
	public String index() {
		return "Hello World";
	}
}

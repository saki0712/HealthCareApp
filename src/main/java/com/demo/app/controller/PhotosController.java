package com.demo.app.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PhotosController {
	@Value("${credential_path}") 
	private String credential_path;
	
	public PhotosController() {
	}
	
	@RequestMapping(value = {"/", "/photos"})
	public String photos(Model model) {
		return "app/photos";
	}
}

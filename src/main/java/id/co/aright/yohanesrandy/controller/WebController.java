package id.co.aright.yohanesrandy.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;

import id.co.aright.yohanesrandy.dto.Response;
import id.co.aright.yohanesrandy.service.AppService;

@Controller
@RequestMapping("web")
public class WebController {

	@Autowired
	AppService service;

	@GetMapping("load-data")
	public String loadData(Model model) {
		try {
			model.addAttribute("data", service.loadData(null));
		} catch (Exception ex) {
			model.addAttribute("message", ex.getLocalizedMessage());
		}
		return "load_data";
	}

	@GetMapping("push-notif")
	public String pushNotif(Model model, String token) {
		try {
			model.addAttribute("message", service.pushNotif(token));
		} catch (Exception ex) {
			model.addAttribute("message", ex.getLocalizedMessage());
		}
		return "push_notif";
	}
}

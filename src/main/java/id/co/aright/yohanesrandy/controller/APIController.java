package id.co.aright.yohanesrandy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.co.aright.yohanesrandy.dto.Response;
import id.co.aright.yohanesrandy.service.AppService;

@RestController
@RequestMapping("api")
public class APIController {
	@Autowired
	AppService service;

	@GetMapping("load-data")
	public Response loadData(String provider_id) {
		Response response = new Response();
		try {
			response.setCode("200");
			response.setData(service.loadData(provider_id));
		} catch (Exception ex) {
			response.setCode("500");
			response.setMessage(ex.getLocalizedMessage());
		}
		return response;
	}
}

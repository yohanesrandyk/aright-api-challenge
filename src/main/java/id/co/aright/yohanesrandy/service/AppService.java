package id.co.aright.yohanesrandy.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.methods.HttpPost;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.gson.Gson;

import id.co.aright.yohanesrandy.helper.Helper;
import id.co.aright.yohanesrandy.service.http.HttpService;

@Service
public class AppService {
	@Value("${resources}")
	private String resources;

	Helper helper = new Helper();
	HttpService http = new HttpService();
	Gson gson = new Gson();

	public List<HashMap<String, String>> loadData(String provider_id) throws Exception {
		System.out.println(provider_id);

		if (provider_id != null && !provider_id.matches("") && !provider_id.matches("null")
				&& provider_id.matches("[a-zA-Z]+")) {
			throw new Exception("Invalid Input");
		}

		List<HashMap<String, String>> data = new ArrayList();
		String json = helper.readFile(resources + "/data.json");

		JsonNode list = helper.jsonToNode(json);
		for (JsonNode row : list) {
			HashMap<String, String> col = new HashMap<String, String>();
			col.put("Service", row.get("Service").get("Name").asText());
			col.put("Provider", row.get("Provider").get("Name").asText());
			col.put("Begin Time", row.get("BeginTime").asText());
			col.put("End Time", row.get("EndTime").asText());

			if (provider_id != null && !provider_id.matches("") && !provider_id.matches("null")) {
				if (row.get("Provider").get("ProviderId").asText().matches(provider_id)) {
					data.add(col);
				}
			} else {
				data.add(col);
			}
		}

		return data;
	}

	public String pushNotif(String token) throws Exception {
		GoogleCredentials googleCredentials = GoogleCredentials
				.fromStream(new FileInputStream(
						resources + "/mobile-banking-f1a91-firebase-adminsdk-46m33-271c159a55.json"))
				.createScoped(Arrays.asList("https://www.googleapis.com/auth/firebase.messaging"));
		googleCredentials.refresh();
		String oauthToken = googleCredentials.getAccessToken().getTokenValue();

		HttpPost request = new HttpPost("https://fcm.googleapis.com/v1/projects/mobile-banking-f1a91/messages:send");
		request.setHeader("User-Agent", "Mozilla/5.0");
		request.setHeader("Content-Type", "application/json");
		request.setHeader("Accept", "application/json");
		request.setHeader("Authorization", "Bearer " + oauthToken);

		Map<String, Object> body = new HashMap<>();
		Map<String, Object> message = new HashMap<>();
		message.put("token", token);
		Map<String, Object> notification = new HashMap<>();
		notification.put("title", "THIS IS TITLE");
		notification.put("body", "THIS IS NOTIF");
		message.put("notification", notification);
		body.put("message", message);

		return http.sendHTTPRequest(request, gson.toJson(body));
	}
}

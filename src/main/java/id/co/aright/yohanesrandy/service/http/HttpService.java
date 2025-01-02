/*******************************************************************************
 * Copyright 2019 Yohanes Randy Kurnianto
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package id.co.aright.yohanesrandy.service.http;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.gson.Gson;

@Repository
public class HttpService {
	Logger logger = LoggerFactory.getLogger(HttpService.class);

	HttpClientFactory clientFactory = new HttpClientFactory();
	private final static String USER_AGENT = "Mozilla/5.0";

	private String buildResponse(CloseableHttpResponse httpResponse) throws Exception {
		String response = null;
		try {
			if (httpResponse.getEntity() != null) {
				BufferedReader rd = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

				StringBuffer result = new StringBuffer();
				String line = "";
				while ((line = rd.readLine()) != null) {
					result.append(line);
				}

				response = result.toString();
			} else {
				throw new Exception("Response null");
			}
		} finally {
			httpResponse.close();
		}
		return response;
	}

	public String sendHTTPRequest(HttpPost request, String body) throws Exception {
		CloseableHttpClient client = clientFactory.getHttpsClient();
		String response = "";

		StringEntity entity = new StringEntity(body, ContentType.APPLICATION_JSON);
		request.setEntity(entity);
		CloseableHttpResponse httpResponse = client.execute(request);
		response = buildResponse(httpResponse);

		return response;
	}
}

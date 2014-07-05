package com.github.theholywaffle.lolchatapi.riotapi;

/*
 * #%L
 * League of Legends XMPP Chat Library
 * %%
 * Copyright (C) 2014 Bert De Geyter
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.util.StringUtils;

import com.github.theholywaffle.lolchatapi.ChatServer;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class RiotApi {

	private static Map<String, RiotApi> instances = new HashMap<>();

	private String apiKey;
	private ChatServer server;
	private List<RateLimiter> rateLimiters = new ArrayList<>();

	private RiotApi(String apiKey, ChatServer server) {
		this.apiKey = apiKey;
		this.server = server;
		rateLimiters.add(new RateLimiter(10, 10_000));
		rateLimiters.add(new RateLimiter(500, 600_000));
	}

	public static RiotApi build(String apiKey, ChatServer server) {
		RiotApi api = instances.get(apiKey);
		if (api == null) {
			api = new RiotApi(apiKey, server);
			instances.put(apiKey, api);
		}
		return api;
	}

	private String request(String URL) throws IOException {
		for (RateLimiter limiter : rateLimiters) {
			limiter.acquire();
		}
		for (RateLimiter limiter : rateLimiters) {
			limiter.enter();
		}
		String requestURL = URL;
		URL url = new URL(requestURL);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setInstanceFollowRedirects(false);

		if (connection.getResponseCode() != 200) {
			throw new IOException("Response code is "
					+ connection.getResponseCode() + " instead of 200.");
		}

		InputStream is = connection.getInputStream();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is,
				"utf-8"));
		String line;
		StringBuffer response = new StringBuffer();
		while ((line = rd.readLine()) != null) {
			response.append(line);
		}
		connection.disconnect();
		return response.toString();
	}

	public String getName(String userId) throws IOException {
		String summonerId = StringUtils.parseName(userId).replace("sum", "");
		String response = request("https://" + server.api + "/api/lol/"
				+ server + "/v1.4/summoner/" + summonerId + "/name?api_key="
				+ apiKey);
		Map<String, String> summoner = new GsonBuilder().create().fromJson(
				response, new TypeToken<Map<String, String>>() {
				}.getType());
		return summoner.get(summonerId);
	}

	public long getSummonerId(String name) throws IOException,
			URISyntaxException {
		URI uri = new URI("https", server.api, "/api/lol/" + server
				+ "/v1.4/summoner/by-name/" + name, "api_key=" + apiKey, null);
		String response = request(uri.toASCIIString());
		Map<String, SummonerDto> summoner = new GsonBuilder().create()
				.fromJson(response, new TypeToken<Map<String, SummonerDto>>() {
				}.getType());
		return summoner.entrySet().iterator().next().getValue().getId();
	}

}

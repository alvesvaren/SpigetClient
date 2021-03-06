package org.spiget.client.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

@Log4j2
public class JsonClient {

	public static String  userAgent = "";
	public static boolean logConn   = false;
	public static Gson    gson      = new GsonBuilder().create();

	public static JsonResponse get(String url) throws IOException {
		if (logConn) {
			log.info("GET " + url);
		}
		Connection connection = Jsoup.connect(url).method(Connection.Method.GET).userAgent(userAgent);
		connection.followRedirects(true);
		connection.ignoreHttpErrors(true);
		connection.ignoreContentType(true);
		connection.timeout(5000);
		Connection.Response response = connection.execute();
		String body = response.body();
		JsonElement json;
		try {
			json = gson.fromJson(body, JsonElement.class);
		} catch (Exception e) {
			log.log(Level.ERROR, "Failed to parse json body", e);
			log.log(Level.WARN, url);
			log.log(Level.WARN, body);
			return null;
		}
		return new JsonResponse(response.statusCode(), json);
	}

}

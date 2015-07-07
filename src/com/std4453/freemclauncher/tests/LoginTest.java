package com.std4453.freemclauncher.tests;

import java.io.IOException;
import java.util.Scanner;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class LoginTest {
	/**
	 * Input your user name and password, this program tries to authenticate you
	 * to Minecraft and retrives the response.<br>
	 * The response content and status code is printed out.
	 * <p>
	 * You can see the authenticating JSON format and the response format. <br>
	 * <a href="http://wiki.vg/Authentication">See wiki.vg for more
	 * information</a>
	 * 
	 * @param args
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static void main(String[] args) throws ClientProtocolException,
			IOException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(
				"https://authserver.mojang.com/authenticate");

		Scanner scanner = new Scanner(System.in);
		System.out.print("Input your user name:");
		String userName = scanner.nextLine();
		System.out.print("Input your password:");
		String password = scanner.nextLine();
		scanner.close();

		JSONObject root = new JSONObject();
		JSONObject agent = new JSONObject();
		agent.put("name", "Minecraft").put("version", 1);
		root.put("agent", agent);
		root.put("username", userName);
		root.put("password", password);

		StringEntity entity = new StringEntity(root.toString(),
				ContentType.create("application/json", "UTF-8"));
		post.setEntity(entity);

		CloseableHttpResponse response = client.execute(post);
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(new JSONObject(EntityUtils.toString(response
				.getEntity())).toString(4));
	}
}

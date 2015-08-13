package com.std4453.freemclauncher.auth;

import static com.std4453.freemclauncher.logging.Logger.*;

import java.io.IOException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.std4453.freemclauncher.gui.DialogBoxHelper;
import com.std4453.freemclauncher.i18n.I18NHelper;
import com.std4453.freemclauncher.util.Pair;
import com.std4453.freemclauncher.util.StructuredDataHelper;
import com.std4453.freemclauncher.util.StructuredDataObject;

public class AuthUtils {
	public static Pair<Integer, StructuredDataObject> sendJSONData(
			StructuredDataObject sdo, String url) {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url);

		JSONObject root = StructuredDataHelper.toJSONObject(sdo);

		StringEntity entity = new StringEntity(root.toString(),
				ContentType.create("application/json", "UTF-8"));
		post.setEntity(entity);

		CloseableHttpResponse response = null;
		try {
			response = client.execute(post);
		} catch (IOException e) {
			log(ERROR, "Error while executing post request to \"" + url + "\"!");
			log(ERROR, e);

			try {
				client.close();
			} catch (Throwable t) {
				log(ERROR, "Error while closing HttpClient!");
				log(ERROR, t);

				throw new RuntimeException(t);
			}

			return new Pair<>(-1, null);
		}
		HttpEntity responseEntity = response.getEntity();

		String data = null;
		try {
			data = EntityUtils.toString(responseEntity);
		} catch (ParseException | IOException e) {
			log(ERROR, "Error while getting response entity!");
			log(ERROR, e);

			try {
				client.close();
			} catch (Throwable t) {
				log(ERROR, "Error while closing HttpClient!");
				log(ERROR, t);

				throw new RuntimeException(t);
			}

			return new Pair<>(-1, null);
		}

		StructuredDataObject retSdo = null;
		try {
			JSONObject json = new JSONObject(data);
			retSdo = StructuredDataHelper.fromJSONObject(json)
					.toStructuredDataObject();
		} catch (Throwable t) {
			log(ERROR, "Error while parsing response data!");
			log(ERROR, t);
		}

		int responseCode = response.getStatusLine().getStatusCode();

		try {
			response.close();
			client.close();
		} catch (IOException e) {
			log(ERROR, "Error while closing response and client!");
			log(ERROR, e);
		}

		return new Pair<>(responseCode, retSdo);
	}

	public static Pair<String, String> handleError(String url,
			Pair<Integer, StructuredDataObject> response) {
		if (response == null)
			return null;

		int code = response.getLeft();
		StructuredDataObject sdo = response.getRight();

		String mappedErrorStr = "general.unknownError";
		String mappedErrorMsg = "auth.error.msg.unknownError";

		String origErrorStr = I18NHelper
				.getFormattedLocalization("general.none");
		String origErrorMsg = I18NHelper
				.getFormattedLocalization("general.none");

		if (code == 200)
			return null;

		if (code == -1) {
			mappedErrorStr = "auth.error.error.noConnection";
			mappedErrorMsg = "auth.error.msg.noConnection";
		} else if (sdo == null) {
			mappedErrorStr = "auth.error.error.parseError";
			mappedErrorMsg = "auth.error.msg.parseError";
		} else {
			try {
				origErrorStr = sdo.getString("error");
				origErrorMsg = sdo.getString("errorMessage");

				Pair<String, String> mapped = AuthErrorMapper
						.authErrorStr2ResourceStr(origErrorStr, origErrorMsg);
				mappedErrorStr = mapped.getLeft();
				mappedErrorMsg = mapped.getRight();
			} catch (Exception e) {
				log(ERROR, "Error while parsing error json!");
				log(ERROR, e);

				mappedErrorStr = "auth.error.error.parseError";
				mappedErrorMsg = "auth.error.msg.parseError";

				origErrorStr = I18NHelper
						.getFormattedLocalization("general.none");
				origErrorMsg = I18NHelper
						.getFormattedLocalization("general.none");
			}
		}

		String formattedErrorStr = I18NHelper
				.getFormattedLocalization(mappedErrorStr);
		String formattedErrorMsg = I18NHelper.getFormattedLocalization(
				mappedErrorMsg, url, code, origErrorStr, origErrorMsg);

		return new Pair<>(formattedErrorStr, formattedErrorMsg);
	}

	/**
	 * Just for testing.. You can copy these codes to made a proper request with
	 * its errors handled or test to login using the given variable
	 * {@code jsonStr} and your user name and password filled in.
	 * <p>
	 * The first 5 lines are not required. They are only used to load Swing
	 * LookAndFeel. When you call {@code DialogBoxHelper}, the LAF must have
	 * already been loaded.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException
				| IllegalAccessException | UnsupportedLookAndFeelException e) {
			throw new RuntimeException(e);
		}

		String url = "https://authserver.mojang.com/authenticate";
		String jsonStr = "{\"agent\": {\"name\": \"Minecraft\",\"version\": 1},\"username\": \"YOUR USERNAME HERE\",\"password\": \"YOUR PASSWORD HERE\"}";

		JSONObject json = new JSONObject(jsonStr);
		StructuredDataObject sdo = StructuredDataHelper.fromJSONObject(json)
				.toStructuredDataObject();

		Pair<Integer, StructuredDataObject> response = sendJSONData(sdo, url);
		Pair<String, String> error = handleError(url, response);

		if (error != null)
			DialogBoxHelper.error(error.getLeft(), error.getRight());
		else {
			StructuredDataObject responseSdo = response.getRight();
			JSONObject responseJSON = StructuredDataHelper
					.toJSONObject(responseSdo);

			DialogBoxHelper.info(
					"Response",
					String.format("Status code: %d\nData:\n%s",
							response.getLeft(), responseJSON.toString(3)));
		}
	}
}

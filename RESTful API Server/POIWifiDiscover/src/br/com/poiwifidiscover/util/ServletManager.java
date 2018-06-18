package br.com.poiwifidiscover.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

public class ServletManager {

	public static JSONObject readJSON(HttpServletRequest request) {

		StringBuffer jb = new StringBuffer();
		String line = null;
		try {
			request.setCharacterEncoding("UTF8");
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null)
				jb.append(line);
		} catch (Exception e) {
			/* report an error */
		}

		JSONObject jsonObject = new JSONObject(jb.toString());

		return jsonObject;
	}

	public static void writeObject(Object object, HttpServletResponse response) throws IOException {

		writeJSON(new Gson().toJson(object), response);
	}
	
	public static String objectToJSON(Object object) {
		
		return new Gson().toJson(object);
	}
	
	public static void writeJSON(Object object, HttpServletResponse response) throws IOException {

		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");

		PrintWriter out = response.getWriter();
		out.print(object);
		out.flush();
	}

	public static void successResponse(HttpServletResponse response) throws IOException {

		writeObject(new MessageJSON(), response);
	}

	public static void failureResponse(String message, HttpServletResponse response) throws IOException {
		writeObject(new MessageJSON(message), response);
	}
}

class MessageJSON {

	String status;
	String message;

	public MessageJSON() {
		this.status = "success";
	}

	public MessageJSON(String error) {
		this.status = "error";
		this.message = error;
	}
}

package br.com.poiwifidiscover.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.google.gson.Gson;

import br.com.poiwifidiscover.dao.WifiDataDAO;
import br.com.poiwifidiscover.model.WifiData;
import br.com.poiwifidiscover.util.ServletManager;

@WebServlet("/wifiCollector")
public class WifiCollector extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public WifiCollector() {
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		JSONObject jsonObject = ServletManager.readJSON(request);
		
		WifiData wifiData = new Gson().fromJson(jsonObject.get("wifiData").toString(), WifiData.class);
		WifiDataDAO.insert(wifiData);
		
		ServletManager.successResponse(response);
	}

}

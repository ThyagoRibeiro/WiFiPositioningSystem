package br.com.poiwifidiscover.servlet;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import br.com.poiwifidiscover.dao.ThingDAO;
import br.com.poiwifidiscover.model.Thing;
import br.com.poiwifidiscover.model.WifiData;
import br.com.poiwifidiscover.util.ClassificationAlgorithm;
import br.com.poiwifidiscover.util.Classifiers;
import br.com.poiwifidiscover.util.ServletManager;

/**
 * Servlet implementation class POIDiscover
 */
@WebServlet("/poiDiscover")
public class POIDiscover extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Default constructor.
	 */
	public POIDiscover() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		long inicio = System.currentTimeMillis();

		JSONObject jsonObject = ServletManager.readJSON(request);

		ClassificationAlgorithm algorithm = ClassificationAlgorithm.valueOf(jsonObject.getString("algorithm"));
		WifiData wifiData = new Gson().fromJson(jsonObject.get("wifiData").toString(), WifiData.class);

		LinkedHashMap<Integer, Double> probabilityMap = Classifiers.buildClassifier(wifiData, algorithm);

		if (probabilityMap.entrySet().size() != 0) {
			JSONArray thingArray = new JSONArray();

			for (Entry<Integer, Double> entry : probabilityMap.entrySet()) {
//				long inicioBusca = System.currentTimeMillis();
				Thing newThing = ThingDAO.select(entry.getKey());
				if (newThing != null) {
					JSONObject thingJSon = new JSONObject();
					thingJSon.put("thing", new JSONObject(newThing));
					thingJSon.put("probability", entry.getValue());
					thingArray.put(thingJSon);
					// System.out.println("buscou " + newThing.getNome().getTexto() + " em "
					// + (System.currentTimeMillis() - inicioBusca) + "ms");
				}
			}

			ServletManager.writeJSON(new JSONObject().put("thingProbability", thingArray), response);
		} else {
			ServletManager.failureResponse("object doesn't exist", response);
		}

//		System.out.println("encerrou em " + (System.currentTimeMillis() - inicio) + "ms");
	}

}

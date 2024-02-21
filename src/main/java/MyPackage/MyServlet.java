package MyPackage;

import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Servlet implementation class MyServlet
 */
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public MyServlet() {
		super();
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

//		API setup
		String apiKey = "32339cb4244bf757fe7b097ab9b15cc7";
		String city = request.getParameter("city");
		String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
//		API Integration
		URL url = new URL(apiUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
//		Reading the data from network
		InputStream inputstream = connection.getInputStream();
		InputStreamReader reader = new InputStreamReader(inputstream);
//		Store the data in string

		StringBuilder responseContent = new StringBuilder();
//		creating scanner object to take input from reader
		Scanner scanner = new Scanner(reader);

		while (scanner.hasNext()) {
			responseContent.append(scanner.nextLine());
		}

		scanner.close();
//	typeCasting the data into JSON
		Gson gson = new Gson();
		JsonObject jsonObject = gson.fromJson(responseContent.toString(), JsonObject.class);
//		Date & Time
		long dateTimeStamp = jsonObject.get("dt").getAsLong() * 1000;
		String date = new Date(dateTimeStamp).toString();
//		Temperature
		double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
		int temperatureCelcius = (int) (temperatureKelvin - 273.15);
//		Humidity
		int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
//		wind Speed
		double windSpeed = jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
//		Weather Condition 
		String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main")
				.getAsString();
		request.setAttribute("date", date);
		request.setAttribute("city", city);
		request.setAttribute("temperature", temperatureCelcius);
		request.setAttribute("weatherCondition", weatherCondition);
		request.setAttribute("humidity", humidity);
		request.setAttribute("windSpeed", windSpeed);
		request.setAttribute("weatherDate", responseContent.toString());
		connection.disconnect();
		
//		Forwars the request to the index.jsp
		request.getRequestDispatcher("index.jsp").forward(request, response);

	}

}

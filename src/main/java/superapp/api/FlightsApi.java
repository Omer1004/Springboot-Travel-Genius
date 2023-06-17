package superapp.api;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FlightsApi {

	private static String XRapidAPIKey = "b4e59d9512msh2df3806d470ec4ap10fda7jsna6e05bc97cff";
	private static final String RAPID_API_HOST = "booking-com.p.rapidapi.com";
//-------------------FLIGHTS FINAL VARIABLES-----------------------------------------------

	private static final String SORT_ORDER = "ML_BEST_VALUE";
	private static final int NUM_SENIORS = 0;
	private static final String CLASS_OF_SERVICE = "ECONOMY";
	private static final int PAGE_NUMBER_FLIGHTS = 1;
	private static final String NONSTOP = "yes";
	private static final String CURRENCY_CODE = "USD";

//-------------------------FLIGHTS------------------------------------------------------------	

	public static String searchFlightsOneWay(String sourceAirportCode, String destinationAirportCode, String date,
			String itineraryType, String sortOrder, int numAdults, int numSeniors, String classOfService,
			int pageNumber, String nonstop, String currencyCode) {
		String url = "https://tripadvisor16.p.rapidapi.com/api/v1/flights/searchFlights" + "?sourceAirportCode="
				+ sourceAirportCode + "&destinationAirportCode=" + destinationAirportCode + "&date=" + date
				+ "&itineraryType=" + itineraryType + "&sortOrder=" + sortOrder + "&numAdults=" + numAdults
				+ "&numSeniors=" + numSeniors + "&classOfService=" + classOfService + "&pageNumber=" + pageNumber
				+ "&nonstop=" + nonstop + "&currencyCode=" + currencyCode;

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("X-RapidAPI-Key", XRapidAPIKey)
				.header("X-RapidAPI-Host", "tripadvisor16.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
			System.out.println(response.body());
			response.body();
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}
		return null;
	}

	public static String searchFlightsRoundTrip(String sourceAirportCode, String destinationAirportCode, String date,
			String returnDate, String itineraryType, String sortOrder, int numAdults, int numSeniors,
			String classOfService, int pageNumber, String nonstop, String currencyCode) {
		String url = "https://tripadvisor16.p.rapidapi.com/api/v1/flights/searchFlights" + "?sourceAirportCode="
				+ sourceAirportCode + "&destinationAirportCode=" + destinationAirportCode + "&date=" + date
				+ "&itineraryType=" + itineraryType + "&sortOrder=" + sortOrder + "&numAdults=" + numAdults
				+ "&numSeniors=" + numSeniors + "&classOfService=" + classOfService + "&pageNumber=" + pageNumber
				+ "&nonstop=" + nonstop + "&currencyCode=" + currencyCode + "&returnDate=" + returnDate;

		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("X-RapidAPI-Key", XRapidAPIKey)
				.header("X-RapidAPI-Host", "tripadvisor16.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response;
		try {
			response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

			return response.body();
		} catch (IOException | InterruptedException e) {
			System.out.println(e);
		}
		return null;
	}

}

package superapp.api;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class HotelsApi {

	private static String XRapidAPIKey = "b4e59d9512msh2df3806d470ec4ap10fda7jsna6e05bc97cff";
	private static final String RAPID_API_HOST = "booking-com.p.rapidapi.com";
//--------------------HOTEL FINAL VARIABLES-----------------------------------------------
	private static final String FILTER_BY_CURRENCY = "USD";
	private static final String CATEGORIES_FILTER_IDS = "class::2,class::4,free_cancellation::1";
	private static final String ORDER_BY = "popularity";
	private static final int PAGE_NUMBER_HOTELS = 0;
	private static final boolean INCLUDE_ADJACENCY = true;

	// -------------------------------------HOTELS---------------------------------------------------------------

	public static String searchLocations(String name, String locale) throws Exception {
		if (locale == "") {
			locale = "en-gb";
		}
		name = name.replace(" ", "%20");
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(
						"https://booking-com.p.rapidapi.com/v1/hotels/locations?name=" + name + "&locale=" + locale))
				.header("X-RapidAPI-Key", XRapidAPIKey).header("X-RapidAPI-Host", RAPID_API_HOST)
				.method("GET", HttpRequest.BodyPublishers.noBody()).build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		return response.body();
	}

	public static String extractDestId(String responseBody) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode locationsArray = mapper.readTree(responseBody);
			JsonNode firstLocation = locationsArray.get(0);
			return firstLocation.get("dest_id").textValue();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public static String extractDestType(String responseBody) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode locationsArray = mapper.readTree(responseBody);
			JsonNode firstLocation = locationsArray.get(0);
			return firstLocation.get("dest_type").textValue();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public static String searchHotels(String destId, String destType, String checkinDate, String checkoutDate,
			int adultsNumber, int roomNumber, String orderBy, String filterByCurrency, String categoriesFilterIds,
			int pageNumber, boolean includeAdjacency) {
		try {
			String url = String.format(
					"https://booking-com.p.rapidapi.com/v1/hotels/search?checkin_date=%s&dest_type=%s&units=metric&checkout_date=%s&adults_number=%d&order_by=%s&dest_id=%s&filter_by_currency=%s&locale=en-gb&room_number=%d&categories_filter_ids=%s&page_number=%d&include_adjacency=%b",
					checkinDate, destType, checkoutDate, adultsNumber, orderBy, destId, filterByCurrency, roomNumber,
					categoriesFilterIds, pageNumber, includeAdjacency);

			HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).header("X-RapidAPI-Key", XRapidAPIKey)
					.header("X-RapidAPI-Host", "booking-com.p.rapidapi.com")
					.method("GET", HttpRequest.BodyPublishers.noBody()).build();

			HttpResponse<String> response = HttpClient.newHttpClient().send(request,
					HttpResponse.BodyHandlers.ofString());

			return response.body();
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	public static String searchHotelsByLocation(String locationName, String checkinDate, String checkoutDate,
			int adultsNumber, int roomNumber, String orderBy, String filterByCurrency, String categoriesFilterIds,
			int pageNumber, boolean includeAdjacency) {
		try {
			// Search for the location and get the response
			System.out.println(locationName);
			String locationSearchResponse = searchLocations(locationName, "en-gb");

			// Extract the destId and destType from the location search response
			String destId = extractDestId(locationSearchResponse);
			String destType = extractDestType(locationSearchResponse);
			System.out.println(locationSearchResponse);

			// Now use the destId and destType to search for hotels
			String hotelSearchResponse = searchHotels(destId, destType, checkinDate, checkoutDate, adultsNumber,
					roomNumber, orderBy, filterByCurrency, categoriesFilterIds, pageNumber, includeAdjacency);

			// Return the response from the hotel search
			return hotelSearchResponse;

		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

}

package superapp.restAPI;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import superapp.api.*;
import superapp.data.CommandId;
import superapp.data.UserId;
import superapp.data.UserRole;
import superapp.exception.NotFound;
import superapp.exception.UnAuthorized;
import superapp.logic.MiniAppCommandQueries;
import superapp.logic.ObjectQueries;
import superapp.logic.UserQueries;

@RestController
public class MiniAppController {

	private MiniAppCommandQueries miniAppCommandService;
	private UserQueries userService;
	private ObjectQueries objectService;

	// --------------------HOTEL FINAL
	// VARIABLES-----------------------------------------------
	private static final String FILTER_BY_CURRENCY = "USD";
	private static final String CATEGORIES_FILTER_IDS = "class::2,class::4,free_cancellation::1";
	private static final String ORDER_BY = "popularity";
	private static final int PAGE_NUMBER_HOTELS = 0;
	private static final boolean INCLUDE_ADJACENCY = true;

	// -------------------FLIGHTS FINAL
	// VARIABLES-----------------------------------------------

	private static final String SORT_ORDER = "ML_BEST_VALUE";
	private static final int NUM_SENIORS = 0;
	private static final String CLASS_OF_SERVICE = "ECONOMY";
	private static final int PAGE_NUMBER_FLIGHTS = 1;
	private static final String NONSTOP = "yes";
	private static final String CURRENCY_CODE = "USD";
	private static final String ITINERARYTYPEONEWAY = "ONE_WAY";
	private static final String ITINERARYTYPEROUNDTRIP = "ROUND_TRIP";

	@Autowired
	private Environment env;

	@Autowired
	public MiniAppController(MiniAppCommandQueries miniAppCommandService, UserQueries userService,
			ObjectQueries objectService) {

		this.miniAppCommandService = miniAppCommandService;
		this.userService = userService;
		this.objectService = objectService;

	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(path = { "/superapp/miniapp/{miniAppName}" }, method = { RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public String invokeMiniAppCommand(@RequestBody MiniAppCommandBoundary miniAppCommandBoundary,
			@RequestParam(name = "async", required = false, defaultValue = "false") boolean async,
			@PathVariable("miniAppName") String miniAppName) {

		System.err.println(miniAppCommandBoundary);
		if (isMiniApp(miniAppCommandBoundary.getInvokedBy().getUserId().getSuperapp(),
				miniAppCommandBoundary.getInvokedBy().getUserId().getEmail())) {

			SuperAppObjectBoundary object = isSuperappObjectExistAndActive(
					miniAppCommandBoundary.getTargetObject().getObjectId().getSuperapp(),
					miniAppCommandBoundary.getTargetObject().getObjectId().getInternalObjectId());

			System.err.println(miniAppCommandBoundary);
			miniAppCommandBoundary.setCommandId(new CommandId(env.getProperty("spring.application.name"), miniAppName));
			miniAppCommandBoundary.setInvocationTimestamp(new Date());

			if (object != null) {

				String jsonResponse = "";
				String commandName = miniAppCommandBoundary.getCommand();
				Map<String, Object> objectDetails = object.getObjectDetails();

				switch (miniAppName) {
				case "flights":
					if ("searchflightoneway".equals(commandName)) {
						jsonResponse = FlightsApi.searchFlightsOneWay((String) objectDetails.get("fromWhere"),
								(String) objectDetails.get("whereTo"), (String) objectDetails.get("departureDate"),
								ITINERARYTYPEONEWAY, SORT_ORDER, 1, // Assuming there is 1 adult
								NUM_SENIORS, CLASS_OF_SERVICE, PAGE_NUMBER_FLIGHTS, NONSTOP, CURRENCY_CODE);
					} else if ("searchflightsroundtrip".equals(commandName)) {
						jsonResponse = FlightsApi.searchFlightsRoundTrip((String) objectDetails.get("fromWhere"),
								(String) objectDetails.get("whereTo"), (String) objectDetails.get("departureDate"),
								(String) objectDetails.get("returnDate"), ITINERARYTYPEROUNDTRIP, SORT_ORDER, 1, // Assuming
																													// there
																													// is
																													// 1
																													// adult
								NUM_SENIORS, CLASS_OF_SERVICE, PAGE_NUMBER_FLIGHTS, NONSTOP, CURRENCY_CODE);
					}
					break;
				case "hotels":
					if ("searchhotelbylocation".equals(commandName)) {
						System.err.print((String) objectDetails.get("destenationCity"));
						jsonResponse = HotelsApi.searchHotelsByLocation((String) objectDetails.get("destenationCity"),
								(String) objectDetails.get("departureDate"), (String) objectDetails.get("returnDate"),
								1, // Assuming there is 1 adult
								1, // Assuming there is 1 room
								ORDER_BY, FILTER_BY_CURRENCY, CATEGORIES_FILTER_IDS, PAGE_NUMBER_HOTELS,
								INCLUDE_ADJACENCY);
					}
					break;
				case "budget":
					if ("getbudgetplanner".equals(commandName)) {
						jsonResponse = BudgetManagerContainer.getInstance()
								.getBudgetManagerJson(miniAppCommandBoundary.getInvokedBy().getUserId().getEmail());
						break;
					} else if ("addflight".equals(commandName)) {
						String email = miniAppCommandBoundary.getInvokedBy().getUserId().getEmail();
						String from = (String) object.getObjectDetails().get("fromWhere");
						String destination = (String) object.getObjectDetails().get("destenationCity");
						String departureDate = (String) object.getObjectDetails().get("departureDate");
						String returnDate = (String) object.getObjectDetails().get("returnDate");
						double flightPrice = Double
								.valueOf(miniAppCommandBoundary.getCommandAttributes().get("flightPrice").toString());
						String flightPurchaseLink = (String) miniAppCommandBoundary.getCommandAttributes()
								.get("flightPurchaseLink");
						double minBudget = (Integer) object.getObjectDetails().get("minValue");
						double maxBudget = (Integer) object.getObjectDetails().get("maxValue");
						BudgetManagerContainer.getInstance().addFlight(email, from, destination, departureDate,
								returnDate, flightPrice, flightPurchaseLink, minBudget, maxBudget);
						jsonResponse = "{response : 'flight added'}";
						break;
					} else if ("removeflight".equals(commandName)) {
						String email = miniAppCommandBoundary.getInvokedBy().getUserId().getEmail();
						String from = (String) object.getObjectDetails().get("fromWhere");
						String destination = (String) object.getObjectDetails().get("destenationCity");
						String departureDate = (String) object.getObjectDetails().get("departureDate");
						String returnDate = (String) object.getObjectDetails().get("returnDate");
						double minBudget = (Integer) object.getObjectDetails().get("minValue");
						double maxBudget = (Integer) object.getObjectDetails().get("maxValue");
						BudgetManagerContainer.getInstance().removeFlight(email, from, destination, departureDate,
								returnDate, minBudget, maxBudget);
						jsonResponse = "{response : 'flight removed'}";
						break;
					} else if ("addhotel".equals(commandName)) {
						String email = miniAppCommandBoundary.getInvokedBy().getUserId().getEmail();
						String from = (String) object.getObjectDetails().get("fromWhere");
						String destination = (String) object.getObjectDetails().get("destenationCity");
						String departureDate = (String) object.getObjectDetails().get("departureDate");
						String returnDate = (String) object.getObjectDetails().get("returnDate");
						double hotelPrice = Double
								.valueOf(miniAppCommandBoundary.getCommandAttributes().get("hotelPrice").toString());
						String hotelPurchaseLink = (String) miniAppCommandBoundary.getCommandAttributes()
								.get("hotelPurchaseLink");
						double minBudget = (Integer) object.getObjectDetails().get("minValue");
						double maxBudget = (Integer) object.getObjectDetails().get("maxValue");
						BudgetManagerContainer.getInstance().addHotel(email, from, destination, departureDate,
								returnDate, hotelPrice, hotelPurchaseLink, minBudget, maxBudget);
						jsonResponse = "{response : 'hotel added'}";
						break;
					} else if ("removehotel".equals(commandName)) {
						String email = miniAppCommandBoundary.getInvokedBy().getUserId().getEmail();
						String from = (String) object.getObjectDetails().get("fromWhere");
						String destination = (String) object.getObjectDetails().get("destenationCity");
						String departureDate = (String) object.getObjectDetails().get("departureDate");
						String returnDate = (String) object.getObjectDetails().get("returnDate");
						double minBudget = (Integer) object.getObjectDetails().get("minValue");
						double maxBudget = (Integer) object.getObjectDetails().get("maxValue");
						BudgetManagerContainer.getInstance().removeHotel(email, from, destination, departureDate,
								returnDate, minBudget, maxBudget);
						jsonResponse = "{response : 'hotel removed'}";
						break;
					}
				}

				if (async == false) {

					this.miniAppCommandService.invokeCommand(miniAppCommandBoundary);

				} else {

					this.miniAppCommandService.invokeCommandLater(miniAppCommandBoundary);
				}
				System.err.println("Command invoked\n" + jsonResponse);
				return jsonResponse;

			} else {
				throw new NotFound("object not exist or not active");
			}

		} else {
			throw new UnAuthorized("You are not MiniApp user");
		}

	}

	private boolean isMiniApp(String superapp, String email) {

		UserId userToCheck = new UserId(superapp, email);

		return this.userService.getUserById(userToCheck).getRole().equals(UserRole.MINIAPP_USER.toString());
	}

	private SuperAppObjectBoundary isSuperappObjectExistAndActive(String objectSupperApp, String internalObjectId) {

		SuperAppObjectBoundary existing = this.objectService.getSpecificObject(objectSupperApp, internalObjectId);

		if (existing != null && existing.getActive() == true) {
			return existing;
		} else
			throw new UnAuthorized("its false");

	}

}

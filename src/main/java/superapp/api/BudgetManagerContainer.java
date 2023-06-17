package superapp.api;

import java.util.Map;
import java.util.HashMap;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BudgetManagerContainer {
	private static BudgetManagerContainer instance;
	private Map<String, BudgetManager> budgetManagers;

	private BudgetManagerContainer() {
		this.budgetManagers = new HashMap<String, BudgetManager>();
	}

	// done as singeltone
	public static synchronized BudgetManagerContainer getInstance() {
		if (instance == null) {
			instance = new BudgetManagerContainer();
		}
		return instance;
	}

	public void addBudgetManager(BudgetManager budgetManager) {
		this.budgetManagers.put(budgetManager.getEmail(), budgetManager);
	}

	public void removeBudgetManager(String email) {
		this.budgetManagers.remove(email);
	}

	public BudgetManager getBudgetManager(String email, String from, String destination, String departureDate,
			String returnDate, double minBudget, double maxBudget) {
		getInstance();// for error prevention
		BudgetManager bm = this.budgetManagers.get(email);
		if (bm == null) {
			bm = new BudgetManager(email, from, destination, departureDate, returnDate, 0.0, "", 0.0, "", minBudget,
					maxBudget);
			this.budgetManagers.put(email, bm);
		}
		return bm;
	}

	public String getBudgetManagerJson(String email) {
		getInstance();// for error prevention
		BudgetManager bm = this.budgetManagers.get(email);
		if (bm == null) {
			return "{}"; // or some error message or status indicating no BudgetManager found
		}

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(bm);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{}"; // or some error message or status indicating a conversion failure
		}
	}

	public void addFlight(String email, String from, String destination, String departureDate, String returnDate,
			double flightPrice, String flightPurchaseLink, double minBudget, double maxBudget) {
		getInstance();// for error prevention
		BudgetManager bm = this.budgetManagers.get(email);
		if (bm == null) {
			bm = new BudgetManager(email, from, destination, departureDate, returnDate, 0.0, "", flightPrice,
					flightPurchaseLink, minBudget, maxBudget);
			this.budgetManagers.put(email, bm);
		} else {
			bm.setFrom(from);
			bm.setDestination(destination);
			bm.setDepartureDate(departureDate);
			bm.setReturnDate(returnDate);
			bm.setMinBudget(minBudget);
			bm.setMaxBudget(maxBudget);
			bm.setFlightPrice(flightPrice);
			bm.setFlightPurchaseLink(flightPurchaseLink);
		}
	}

	public void removeFlight(String email, String from, String destination, String departureDate, String returnDate,
			double minBudget, double maxBudget) {
		getInstance();// for error prevention
		BudgetManager bm = this.budgetManagers.get(email);
		if (bm == null) {
			bm = new BudgetManager(email, from, destination, departureDate, returnDate, 0.0, "", 0.0, "", minBudget,
					maxBudget);
			this.budgetManagers.put(email, bm);
		} else {
			bm.setFrom(from);
			bm.setDestination(destination);
			bm.setDepartureDate(departureDate);
			bm.setReturnDate(returnDate);
			bm.setMinBudget(minBudget);
			bm.setMaxBudget(maxBudget);
			bm.setFlightPrice(0.0);
			bm.setFlightPurchaseLink("");
		}
	}

	public void addHotel(String email, String from, String destination, String departureDate, String returnDate,
			double hotelPrice, String hotelPurchaseLink, double minBudget, double maxBudget) {
		getInstance();// for error prevention
		BudgetManager bm = this.budgetManagers.get(email);
		if (bm == null) {
			bm = new BudgetManager(email, from, destination, departureDate, returnDate, hotelPrice, hotelPurchaseLink,
					0.0, "", minBudget, maxBudget);
			this.budgetManagers.put(email, bm);
		} else {
			bm.setFrom(from);
			bm.setDestination(destination);
			bm.setDepartureDate(departureDate);
			bm.setReturnDate(returnDate);
			bm.setMinBudget(minBudget);
			bm.setMaxBudget(maxBudget);
			bm.setHotelPrice(hotelPrice);
			bm.setHotelPurchaseLink(hotelPurchaseLink);
		}
	}

	public void removeHotel(String email, String from, String destination, String departureDate, String returnDate,
			double minBudget, double maxBudget) {
		getInstance();// for error prevention
		BudgetManager bm = this.budgetManagers.get(email);
		if (bm == null) {
			bm = new BudgetManager(email, from, destination, departureDate, returnDate, 0.0, "", 0.0, "", minBudget,
					maxBudget);
			this.budgetManagers.put(email, bm);
		} else {
			bm.setFrom(from);
			bm.setDestination(destination);
			bm.setDepartureDate(departureDate);
			bm.setReturnDate(returnDate);
			bm.setMinBudget(minBudget);
			bm.setMaxBudget(maxBudget);
			bm.setHotelPrice(0.0);
			bm.setHotelPurchaseLink("");
		}
	}
}

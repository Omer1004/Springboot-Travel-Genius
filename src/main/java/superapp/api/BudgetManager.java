package superapp.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class BudgetManager {

	private String email;
	private String from;
	private String destination;
	private String departureDate;
	private String returnDate;
	private double hotelPrice;
	private String hotelPurchaseLink;
	private double flightPrice;
	private String flightPurchaseLink;
	private double minBudget;
	private double maxBudget;
	private double totalPrice;
	private BudgetStatus status;

	public enum BudgetStatus {
		UNDER_BUDGET, WITHIN_BUDGET, OVER_BUDGET
	}

	public BudgetManager() {

	}

	public BudgetManager(String email, String from, String destination, String departureDate, String returnDate,
			double hotelPrice, String hotelPurchaseLink, double flightPrice, String flightPurchaseLink,
			double minBudget, double maxBudget) {

		this.email = email;
		this.from = from;
		this.destination = destination;
		this.departureDate = departureDate;
		this.returnDate = returnDate;
		this.hotelPrice = hotelPrice;
		this.hotelPurchaseLink = hotelPurchaseLink;
		this.flightPrice = flightPrice;
		this.flightPurchaseLink = flightPurchaseLink;
		this.minBudget = minBudget;
		this.maxBudget = maxBudget;
		this.status = calculateStatus(hotelPrice + flightPrice, minBudget, maxBudget);
		this.totalPrice = flightPrice + hotelPrice;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getDepartureDate() {
		return departureDate;
	}

	public void setDepartureDate(String departureDate) {
		this.departureDate = departureDate;
	}

	public String getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(String returnDate) {
		this.returnDate = returnDate;
	}

	public double getHotelPrice() {
		return hotelPrice;
	}

	public String getHotelPurchaseLink() {
		return hotelPurchaseLink;
	}

	public void setHotelPurchaseLink(String hotelPurchaseLink) {
		this.hotelPurchaseLink = hotelPurchaseLink;
	}

	public double getFlightPrice() {
		return flightPrice;
	}

	public void setHotelPrice(double hotelPrice) {
		this.hotelPrice = hotelPrice;
		this.totalPrice = this.hotelPrice + this.flightPrice;
		this.status = calculateStatus(this.totalPrice, this.minBudget, this.maxBudget);
	}

	public void setFlightPrice(double flightPrice) {
		this.flightPrice = flightPrice;
		this.totalPrice = this.hotelPrice + this.flightPrice;
		this.status = calculateStatus(this.totalPrice, this.minBudget, this.maxBudget);
	}

	public String getFlightPurchaseLink() {
		return flightPurchaseLink;
	}

	public void setFlightPurchaseLink(String flightPurchaseLink) {
		this.flightPurchaseLink = flightPurchaseLink;
	}

	public double getMinBudget() {
		return minBudget;
	}

	public void setMinBudget(double minBudget) {
		this.minBudget = minBudget;
	}

	public double getMaxBudget() {
		return maxBudget;
	}

	public void setMaxBudget(double maxBudget) {
		this.maxBudget = maxBudget;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public BudgetStatus getStatus() {
		return status;
	}

	private BudgetStatus calculateStatus(double currentBudget, double minBudget, double maxBudget) {
		if (currentBudget < minBudget) {
			return BudgetStatus.UNDER_BUDGET;
		} else if (currentBudget > maxBudget) {
			return BudgetStatus.OVER_BUDGET;
		} else {
			return BudgetStatus.WITHIN_BUDGET;
		}
	}

	public String getJsonToString() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		ObjectNode rootNode = mapper.createObjectNode();
		rootNode.put("email", email);
		rootNode.put("from", from);
		rootNode.put("destination", destination);
		rootNode.put("departureDate", departureDate);
		rootNode.put("returnDate", returnDate);
		rootNode.put("hotelPrice", hotelPrice);
		rootNode.put("hotelPurchaseLink", hotelPurchaseLink);
		rootNode.put("flightPrice", flightPrice);
		rootNode.put("flightPurchaseLink", flightPurchaseLink);
		rootNode.put("minBudget", minBudget);
		rootNode.put("maxBudget", maxBudget);
		rootNode.put("totalPrice", totalPrice);
		rootNode.put("status", status.name());
		return mapper.writeValueAsString(rootNode);
	}

	@Override
	public String toString() {
		return "BudgetManager [destination=" + destination + ", departureDate=" + departureDate + ", returnDate="
				+ returnDate + ", hotelPrice=" + hotelPrice + ", hotelPurchaseLink=" + hotelPurchaseLink
				+ ", flightPrice=" + flightPrice + ", flightPurchaseLink=" + flightPurchaseLink + ", minBudget="
				+ minBudget + ", maxBudget=" + maxBudget + ", totalprice=" + totalPrice + ", status=" + status + "]";
	}

}

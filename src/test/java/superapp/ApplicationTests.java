package superapp;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Date;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;
import jakarta.annotation.PostConstruct;
import superapp.dal.*;
import superapp.data.*;
import superapp.restAPI.*;

@SpringBootTest(classes = Application.class, webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTests {
	ObjectServiceDB objectDb;
	private int port;
	private String url;
	private RestTemplate client;

// get the random port that was generated
	@LocalServerPort
	public void setPort(int port) {
		this.port = port;
	}

	@PostConstruct
	public void init() {
		this.url = "Http://localhost:" + this.port + "/superapp";
		this.client = new RestTemplate();
	}

	@AfterEach
// make sure each test cleans up any data it generates
	public void tearDownUsers() {
// WHEN I POST /users with legal user:

		postPlayerAdminUser();
		// delete objects
		this.client.delete(this.url + "/admin/objects?userSuperapp=2023b.TravelGenuis&userEmail=admin@player.com");
		// delete commands
		this.client.delete(this.url + "/admin/miniapp?userSuperapp=2023b.TravelGenuis&userEmail=admin@player.com");
		// delete users
		this.client.delete(this.url + "/admin/users?userSuperapp=2023b.TravelGenuis&userEmail=admin@player.com");

	}

// check if the server goes to air
	@Test
	void contextLoads() {
		System.err.println("contextLoads()");

	}

	@Test
	public void createNewUserTest() throws Exception {
		System.err.println("createNewUserTest()");

		postPlayerAdminUser();

// THEN the database contains a single user
// UserBoundary actual = getAllUsers();
		UserBoundary[] actual = getAllUsers();
// System.err.println(actua2);
// System.err.println(actual);
		if ((actual.length - 1) != 0) {
			throw new Exception(
					"expected to receive an array of size 1, but the server returned array of size: " + actual.length);
		}

	}

	@Test
	public void putMethodUserCheck() {
		System.err.println("putMethodUserCheck()");

		UserBoundary expected = postPlayerAdminUser();
		expected.setUsername("bla-bla");
		client.put(url + "/users/" + "2023b.TravelGenuis/" + "admin@player.com", expected);
		assertThat(expected).isNotNull();
		assertThat(expected.getUsername()).isEqualTo("bla-bla");
	}

	@Test
	public void testGetParentsofObject() {
		System.err.println("testGetParentsofObject()");

		SuperAppObjectBoundary o1 = postObject();
		// create another object and post it

		SuperAppObjectBoundary o2 = postObject2();
		// CreatedBy create = new CreatedBy(o2.getCreatedBy().getUserId());

		// bind two objects
		this.client.put(this.url + "/objects" + "/2023b.TravelGenuis/" + o2.getObjectId().getInternalObjectId()
				+ "/children?userSuperapp=2023b.TravelGenuis&userEmail=super2@player.com", o1.getObjectId());
		System.err.println(o1);
		// check if two objects are binded to each other
		SuperAppObjectBoundary[] actualResponses = this.client.getForObject(
				this.url + "/objects/2023b.TravelGenuis/" + o2.getObjectId().getInternalObjectId()
						+ "/parents?userSuperapp=2023b.TravelGenuis&userEmail=super2@player.com",
				SuperAppObjectBoundary[].class);

		assertThat(actualResponses[0].toString()).isEqualTo(o1.toString());

	}

	@Test
	public void testGetByType() {
		System.err.println("testGetByType()");

		SuperAppObjectBoundary o1 = postObject();
		o1.setType("test");
		SuperAppObjectBoundary[] o2 = this.client.getForObject(
				url + "/objects/search/byType/test?userSuperapp=2023b.TravelGenuis&userEmail=super@player.com",
				SuperAppObjectBoundary[].class);
		System.err.println(o2);
		assertThat(o1.getType()).isEqualTo(o2[0].getType());
	}

	@Test
	public void testGetByAlias() {
		System.err.println("testGetByAlias()");

		SuperAppObjectBoundary o1 = postObject();

		SuperAppObjectBoundary[] o2 = this.client.getForObject(
				url + "/objects/search/byAlias/demo alias?userSuperapp=2023b.TravelGenuis&userEmail=super@player.com",
				SuperAppObjectBoundary[].class);
		System.err.println(o2);
		assertThat(o1.getAlias()).isEqualTo(o2[0].getAlias());
	}

	@Test
	public void testPostUserToCleanServerAndValidateItExistsOnDb() throws Exception {
		System.err.println("testPostUserToCleanServerAndValidateItExistsOnDb()");

// POST of new legal user

// GIVEN the server is up
// do nothing
// AND the database is clean
// do nothing

// WHEN I POST /users legal user:
// UserBoundary result = postPlayerUser();
		UserBoundary result = postPlayerAdminUser();
// THEN the database contains the user that was posted
		UserBoundary actual = getPlayerUser();

		assertThat(actual).isNotNull();

		assertThat(actual.getUserId().getEmail()).isEqualTo(result.getUserId().getEmail());

	}

	@Test
	public void testObjectsCanBeRelated() {
		System.err.println("testObjectsCanBeRelated()");

		// post object origin
		SuperAppObjectBoundary o1 = postObject();
		// create another object and post it

		SuperAppObjectBoundary o2 = postObject2();
		CreatedBy create = new CreatedBy(o2.getCreatedBy().getUserId());

		// objectBoundary.setObjectId(new ObjectId("2023b.TravelGenuis"));
		// post second object
		o2 = client.postForObject(this.url + "/objects", o2, SuperAppObjectBoundary.class);
		// bind two objects
		this.client.put(this.url + "/objects" + "/2023b.TravelGenuis/" + o2.getObjectId().getInternalObjectId()
				+ "/children?userSuperapp=2023b.TravelGenuis&userEmail=super2@player.com", o1.getObjectId());
		System.err.println(o1);
		// check if two objects are binded to each other by getting children
		SuperAppObjectBoundary[] actualResponses = this.client.getForObject(
				this.url + "/objects/2023b.TravelGenuis/" + o1.getObjectId().getInternalObjectId()
						+ "/children?userSuperapp=2023b.TravelGenuis&userEmail=super@player.com",
				SuperAppObjectBoundary[].class);

		assertThat(actualResponses[0].getObjectId().getInternalObjectId())
				.isEqualTo(o2.getObjectId().getInternalObjectId());

	}

	@Test
	public void testCreateNewObject() throws Exception {
		System.err.println("testCreateNewObject()");
//post object
		SuperAppObjectBoundary objectBoundary = postObject();
//get object
		SuperAppObjectBoundary[] res = getAllObjects();
		assertThat(res).isNotNull();

		assertThat(objectBoundary.equals(res[0]));
	}

	@Test
	public void testUpdateObject() {
		System.err.println("testUpdateObject()");
//
// Create a new object
		SuperAppObjectBoundary objectBoundary = postObject();
		System.err.println(objectBoundary.getObjectId().getInternalObjectId());

// Update the object's alias
		objectBoundary.setAlias("new alias");

// Send a PUT request to update the object
		this.client.put(url + "/objects/" + "2023b.TravelGenuis/" + objectBoundary.getObjectId().getInternalObjectId()
				+ "?userSuperapp=2023b.TravelGenuis&userEmail=super@player.com", objectBoundary);

// Get the updated object from the server
		SuperAppObjectBoundary[] updatedObject = getAllObjects();

// Assert that the object was updated correctly
		assertThat(updatedObject).isNotNull();
		assertThat(updatedObject[0].getAlias()).isEqualTo("new alias");

	}

	@Test
	public void postMiniAppMethodCheck() {
		System.err.println("postMiniAppMethodCheck()");

		SuperAppObjectBoundary boundaryTest = postObject();
		System.err.println("for testing , Active  value is true but can change to false in postObject() then "
				+ "it will not work as it needed to" + boundaryTest);

		MiniAppCommandBoundary input = new MiniAppCommandBoundary();
		postPlayerMiniAppUser();// user with MiniApp_user role
		postPlayerAdminUser();
		input.setCommand("some-command");
		input.setCommandId(new CommandId("2023b.TravelGenuis", "some-mini-app"));
		input.setCommandAttributes(new HashMap<>());
		input.setInvocationTimestamp(new Date());
		InvokedBy inv = new InvokedBy(new UserId("2023b.TravelGenuis", "mini@player.com"));
		input.setInvokedBy(inv);
		TargetObject trg = new TargetObject(boundaryTest.getObjectId());
		input.setTargetObject(trg);

		this.client.postForObject(this.url + "/miniapp/" + "some-mini-app", input, MiniAppCommandBoundary.class);
		MiniAppCommandBoundary[] output;
		output = client.getForObject(
				url + "/admin/miniapp/" + "some-mini-app?userSuperapp=2023b.TravelGenuis&userEmail=admin@player.com",
				MiniAppCommandBoundary[].class);
		System.err.println(output[0].toString());
	}

	public SuperAppObjectBoundary postObject() {
		// post user
		postPlayerSuperAppUser();

		SuperAppObjectBoundary objectBoundary = new SuperAppObjectBoundary();
		// objectBoundary.setObjectId(new ObjectId("2023b.TravelGenuis"));
		objectBoundary.setType("test");
		objectBoundary.setActive(true);
		objectBoundary.setLocation(new Location(10, 12));

		CreatedBy create = new CreatedBy(new UserId("2023b.TravelGenuis", "super@player.com"));
		objectBoundary.setCreatedBy(create);
		objectBoundary.setAlias("demo alias");
		objectBoundary.setCreationTimestamp(new Date());

//pass SuperAppObjectBoundary to controller
		return client.postForObject(this.url + "/objects", objectBoundary, SuperAppObjectBoundary.class);
	}

	public SuperAppObjectBoundary postObject2() {
		// post user
		this.client.postForObject(this.url + "/users",
				new NewUserBoundary("super2@player.com", UserRole.SUPERAPP_USER.name(), "username2", "avatar2"),
				UserBoundary.class);

		SuperAppObjectBoundary objectBoundary = new SuperAppObjectBoundary();
		// objectBoundary.setObjectId(new ObjectId("2023b.TravelGenuis"));
		objectBoundary.setType("test");
		objectBoundary.setActive(true);
		objectBoundary.setLocation(new Location(10, 12));

		CreatedBy create = new CreatedBy(new UserId("2023b.TravelGenuis", "super2@player.com"));
		objectBoundary.setCreatedBy(create);
		objectBoundary.setAlias("demo alias");
		objectBoundary.setCreationTimestamp(new Date());

//pass SuperAppObjectBoundary to controller
		return client.postForObject(this.url + "/objects", objectBoundary, SuperAppObjectBoundary.class);
	}

	public SuperAppObjectBoundary[] getAllObjects() {
		return this.client.getForObject(
				this.url + "/objects?userSuperapp=2023b.TravelGenuis&userEmail=super@player.com",
				SuperAppObjectBoundary[].class);
	}

	public UserBoundary postPlayerAdminUser() {

		return this.client.postForObject(this.url + "/users",
				new NewUserBoundary("admin@player.com", UserRole.ADMIN.name(), "username", "avatar"),
				UserBoundary.class);
	}

	public UserBoundary postPlayerMiniAppUser() {

		return this.client.postForObject(this.url + "/users",
				new NewUserBoundary("mini@player.com", UserRole.MINIAPP_USER.name(), "mini", "avat"),
				UserBoundary.class);
	}

	public UserBoundary postPlayerSuperAppUser() {

		return this.client.postForObject(this.url + "/users",
				new NewUserBoundary("super@player.com", UserRole.SUPERAPP_USER.name(), "username2", "avatar2"),
				UserBoundary.class);
	}

	public UserBoundary[] getAllUsers() {
// /superapp/admin/users
		return this.client.getForObject(
				this.url + "/admin/users?userSuperapp=2023b.TravelGenuis&userEmail=admin@player.com",
				UserBoundary[].class);
	}

	public UserBoundary getPlayerUser() {
		return this.client.getForObject(this.url + "/users/login/2023b.TravelGenuis/" + "admin@player.com",
				UserBoundary.class);
	}

}
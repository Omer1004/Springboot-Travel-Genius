package superapp.restAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import superapp.data.UserId;
import superapp.logic.UserQueries;

@RestController
public class UserController {

	private UserQueries userService;
	@Autowired
	private Environment env;

	@Autowired
	public UserController(UserQueries userService) {
		this.userService = userService;
	}

	@CrossOrigin(origins = "http://localhost:3000") // Replace with the origin of the React app
	@RequestMapping(path = { "/superapp/users" }, method = { RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public UserBoundary createNewUser(@RequestBody NewUserBoundary newUser) {
		// Logic to create a new UserBoundary object based on the NewUserBoundary data
		System.err.println(newUser);
		UserBoundary user = new UserBoundary();
		user.setUserId(new UserId(env.getProperty("spring.application.name"), newUser.getEmail()));
		user.setRole(newUser.getRole());
		user.setUsername(newUser.getUsername());
		user.setAvatar(newUser.getAvatar());
		System.err.println("after added all user parameters :");
		System.err.println(user);
		return this.userService.createUser(user);
	}

	@CrossOrigin(origins = "http://localhost:3000") // Replace with the origin of the React app
	@RequestMapping(path = { "/superapp/users/login/{superapp}/{email}" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public UserBoundary loginUserCheck(@PathVariable("superapp") String superApp, @PathVariable("email") String email) {

		UserBoundary existUser = this.userService.login(superApp, email);
		System.err.println("Login details : ");
		System.err.println(existUser);

		return existUser;
	}

	@CrossOrigin(origins = "http://localhost:3000") // Replace with the origin of the React app
	@RequestMapping(path = { "/superapp/users/{superapp}/{userEmail}" }, method = { RequestMethod.PUT }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public void updateUserDetails(

			@PathVariable("superapp") String superApp, @PathVariable("userEmail") String userEmail,
			@RequestBody UserBoundary update) {

		System.err.println("updating email id: " + userEmail);
		System.err.println("using update: " + update);

		UserBoundary afterChanges = this.userService.updateUser(superApp, userEmail, update);
		System.err.println("after Changes :");
		System.err.println(afterChanges);
	}
}

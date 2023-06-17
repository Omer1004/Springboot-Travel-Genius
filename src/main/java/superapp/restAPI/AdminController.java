package superapp.restAPI;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import superapp.data.UserId;
import superapp.data.UserRole;
import superapp.exception.UnAuthorized;
import superapp.logic.MiniAppCommandQueries;
import superapp.logic.ObjectQueries;
import superapp.logic.UserQueries;

@RestController
public class AdminController {

	private UserQueries userService;
	private ObjectQueries objectService;
	private MiniAppCommandQueries miniAppCommandService;

	@Autowired
	public AdminController(UserQueries userService, ObjectQueries objectService,
			MiniAppCommandQueries miniAppCommandService) {
		this.userService = userService;
		this.objectService = objectService;
		this.miniAppCommandService = miniAppCommandService;
	}

	@RequestMapping(path = { "/superapp/admin/users" }, method = { RequestMethod.DELETE })
	public void deleteAllUsers(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail) {

		if (isAdmin(userSuperapp, userEmail)) {
			System.err.println("deleteAllUsers");
			userService.deleteAllUsers();
		} else {
			throw new UnAuthorized("You are not Admin user");
		}

	}

	@RequestMapping(path = { "/superapp/admin/objects" }, method = { RequestMethod.DELETE })
	public void deleteAllObjects(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail) {

		if (isAdmin(userSuperapp, userEmail)) {
			System.err.println("deleteAllObjects");
			objectService.deleteAllObjects();
		} else {
			throw new UnAuthorized("You are not Admin user");
		}

	}

	@RequestMapping(path = { "/superapp/admin/miniapp" }, method = { RequestMethod.DELETE })
	public void deleteAllCommands(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail) {

		if (isAdmin(userSuperapp, userEmail)) {
			System.err.println("deleteAllCommands");
			miniAppCommandService.deleteAllCommands();
		} else {
			throw new UnAuthorized("You are not Admin user");
		}

	}

	// change here
	@RequestMapping(path = { "/superapp/admin/users" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public UserBoundary[] exportAllUsers(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		if (isAdmin(userSuperapp, userEmail)) {
			System.err.println("exportAllUsers");
			List<UserBoundary> boundarys = userService.getAllUsersWithPagination(page, size);
			return boundarys.toArray(new UserBoundary[0]);
		} else {
			throw new UnAuthorized("You are not Admin user");
		}
	}

	@RequestMapping(path = { "/superapp/admin/miniapp" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public MiniAppCommandBoundary[] exportAllCommands(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		if (isAdmin(userSuperapp, userEmail)) {
			System.err.println("exportAllCommands");
			List<MiniAppCommandBoundary> boundarys = miniAppCommandService.getAllCommandsWithPagination(page, size);
			return boundarys.toArray(new MiniAppCommandBoundary[0]);
		} else {
			throw new UnAuthorized("You are not Admin user");
		}

	}

	@RequestMapping(path = { "/superapp/admin/miniapp/{miniAppName}" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })

	public MiniAppCommandBoundary[] exportSpecificCommands(@PathVariable("miniAppName") String miniAppName,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		if (isAdmin(userSuperapp, userEmail)) {
			System.err.println("exportSpecificCommands");
			List<MiniAppCommandBoundary> boundarys = miniAppCommandService.getAllCommandsWithPagination(page, size);
			List<MiniAppCommandBoundary> filteredCommands = new ArrayList<>();
			for (MiniAppCommandBoundary command : boundarys) {
				if (command.getCommandId().getMiniapp().equals(miniAppName)) {
					filteredCommands.add(command);
				}
			}
			return filteredCommands.toArray(new MiniAppCommandBoundary[0]);
		} else {
			throw new UnAuthorized("You are not Admin user");
		}

	}

	private boolean isAdmin(String superapp, String email) {

		UserId userToCheck = new UserId(superapp, email);

		return this.userService.getUserById(userToCheck).getRole().equals(UserRole.ADMIN.toString());
	}

}

package superapp.restAPI;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import superapp.data.ObjectId;
import superapp.data.UserId;
import superapp.data.UserRole;
import superapp.exception.UnAuthorized;
import superapp.logic.ObjectServiceWithBindingCapabilities;
import superapp.logic.UserQueries;

@RestController
public class RelationshipsController {

	private ObjectServiceWithBindingCapabilities object;
	private UserQueries userService;

	@Autowired
	public RelationshipsController(ObjectServiceWithBindingCapabilities object, UserQueries userService) {
		super();
		this.object = object;
		this.userService = userService;
	}

	@RequestMapping(path = { "/superapp/objects/{superapp}/{internalObjectId}/children" }, method = {
			RequestMethod.PUT }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void bindAnObjectToChildren(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@PathVariable("superapp") String superapp, @PathVariable("internalObjectId") String internalObjectId,
			@RequestBody ObjectId children) {

		if (isSuperApp(userSuperapp, userEmail)) {
			this.object.bind(children, superapp, internalObjectId);
		} else {
			throw new UnAuthorized("You are not SuperApp user");
		}

	}

	@RequestMapping(path = { "/superapp/objects/{superapp}/{internalObjectId}/children" }, method = {
			RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public SuperAppObjectBoundary[] getAllChildrenObjects(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@PathVariable("superapp") String superapp, @PathVariable("internalObjectId") String internalObjectId) {

		if (isSuperApp(userSuperapp, userEmail)) {
			List<SuperAppObjectBoundary> objectBoundaries = this.object.getAllChildrenWithPagination(superapp,
					internalObjectId, size, page);
			return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);
		}

		if (isActive(userSuperapp, internalObjectId)) {

			if (isMiniApp(userSuperapp, userEmail)) {

				List<SuperAppObjectBoundary> objectBoundaries = this.object
						.getAllChildrenWithPagination(superapp, internalObjectId, size, page).stream()
						.filter(t -> t.getActive().equals(true)).toList();

				return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);

			} else {
				throw new UnAuthorized("You are not SuperApp/MiniApp user");
			}
		} else {
			return new SuperAppObjectBoundary[0];
		}
	}

	@RequestMapping(path = { "/superapp/objects/{superapp}/{internalObjectId}/parents" }, method = {
			RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public SuperAppObjectBoundary[] getAllParentsObjects(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@PathVariable("superapp") String superapp, @PathVariable("internalObjectId") String internalObjectId) {

		if (isSuperApp(userSuperapp, userEmail)) {
			List<SuperAppObjectBoundary> objectBoundaries = this.object.getAllParentsWithPagination(superapp,
					internalObjectId, size, page);
			return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);
		}

		if (isActive(userSuperapp, internalObjectId)) {

			if (isMiniApp(userSuperapp, userEmail)) {

				List<SuperAppObjectBoundary> objectBoundaries = this.object
						.getAllChildrenWithPagination(superapp, internalObjectId, size, page).stream()
						.filter(t -> t.getActive().equals(true)).toList();

				return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);

			} else {
				throw new UnAuthorized("You are not SuperApp/MiniApp user");
			}

		} else {
			return new SuperAppObjectBoundary[0];
		}

	}

	private boolean isSuperApp(String superapp, String email) {

		UserId userToCheck = new UserId(superapp, email);

		return this.userService.getUserById(userToCheck).getRole().equals(UserRole.SUPERAPP_USER.toString());
	}

	private boolean isMiniApp(String superapp, String email) {

		UserId userToCheck = new UserId(superapp, email);

		return this.userService.getUserById(userToCheck).getRole().equals(UserRole.MINIAPP_USER.toString());
	}

	private boolean isActive(String superapp, String internalObjectId) {

		return this.object.getSpecificObject(superapp, internalObjectId).getActive().equals(true);
	}
}

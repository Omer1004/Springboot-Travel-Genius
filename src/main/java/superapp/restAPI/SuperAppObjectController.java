package superapp.restAPI;

import java.util.List;
import java.util.Date;
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
import superapp.data.*;
import superapp.exception.NotFound;
import superapp.exception.UnAuthorized;
import superapp.logic.ObjectQueries;
import superapp.logic.UserQueries;

@RestController
public class SuperAppObjectController {

	private ObjectQueries objectService;
	private UserQueries userService;

	@Autowired
	private Environment env;

	@Autowired
	public SuperAppObjectController(ObjectQueries objectService, UserQueries userService) {
		this.objectService = objectService;
		this.userService = userService;
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(path = { "/superapp/objects" }, method = { RequestMethod.POST }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public SuperAppObjectBoundary createNewObject(@RequestBody SuperAppObjectBoundary object) {

//		if (isSuperApp(object.getCreatedBy().getUserId().getSuperapp(), object.getCreatedBy().getUserId().getEmail())) {
		System.err.println("object boundary");
		System.err.println(object);
		ObjectId objId = new ObjectId(env.getProperty("spring.application.name"));
		Date creationTimestamp = new Date();
		object.setObjectId(objId);
		object.setCreationTimestamp(creationTimestamp);
		System.err.println("object boundary after setting Id and TimeCreation : ");
		System.err.println(object);

		return this.objectService.createObject(object);
//		} else {
//			throw new UnAuthorized("You are not SuperApp user");
//		}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(path = { "/superapp/objects/{superapp}/{InternalObjectId}" }, method = {
			RequestMethod.PUT }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void updateObject(@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@PathVariable("superapp") String superApp, @PathVariable("InternalObjectId") String InternalObjectId,
			@RequestBody SuperAppObjectBoundary update) {

		if (isSuperApp(userSuperapp, userEmail)) {
			if (update.getObjectId() != null) {
				System.err.println("updating object by id : " + update.getObjectId().getInternalObjectId());
			}
			System.err.println("using update: " + update);

			SuperAppObjectBoundary afterChanges = this.objectService.updateObject(superApp, InternalObjectId, update);

			System.err.println("after Changes :");
			System.err.println(afterChanges);
		} else {
			throw new UnAuthorized("You are not SuperApp user");
		}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(path = { "/superapp/objects/{superapp}/{InternalObjectId}" }, method = {
			RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public SuperAppObjectBoundary retriveObject(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@PathVariable("superapp") String superapp, @PathVariable("InternalObjectId") String internalObjectId) {

		if (isSuperApp(userSuperapp, userEmail)) {
			System.out.println(superapp + " " + internalObjectId);
			SuperAppObjectBoundary existObject = this.objectService.getSpecificObject(superapp, internalObjectId);
			System.err.println("details : ");
			System.err.println(existObject);
			return existObject;

		} else if (isMiniApp(userSuperapp, userEmail)) {

			SuperAppObjectBoundary existObject = this.objectService.getSpecificObject(superapp, internalObjectId);
			if (existObject.getActive()) {
				return existObject;
			} else
				throw new NotFound("Object not found");

		} else
			throw new UnAuthorized("You are not SuperApp user");
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(path = { "/superapp/objects" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public SuperAppObjectBoundary[] getAllObjects(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {

		if (isSuperApp(userSuperapp, userEmail)) {
			List<SuperAppObjectBoundary> objectBoundaries = this.objectService.getAllObjectsWithPagintion(size, page);
			System.out.print("geting all objects : ");
			System.out.print(objectBoundaries);
			return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);

		} else if (isMiniApp(userSuperapp, userEmail)) {

			List<SuperAppObjectBoundary> objectBoundaries = this.objectService.getAllObjectsWithPagintion(size, page)
					.stream().filter(t -> t.getActive().equals(true)).toList();

			return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);

		} else {
			throw new UnAuthorized("You are not SuperApp/MiniApp user");
		}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(path = { "/superapp/objects/search/byType/{type}" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public SuperAppObjectBoundary[] seacrhByType(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@PathVariable("type") String type) {

		if (isSuperApp(userSuperapp, userEmail)) {
			List<SuperAppObjectBoundary> objectBoundaries = this.objectService
					.getSpecificObjectByTypeWithPagination(userSuperapp, userEmail, type, size, page);
			System.out.print("geting all objects : ");
			System.out.print(objectBoundaries);

			return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);

		} else if (isMiniApp(userSuperapp, userEmail)) {

			List<SuperAppObjectBoundary> objectBoundaries = this.objectService
					.getSpecificObjectByTypeWithPagination(userSuperapp, userEmail, type, size, page).stream()
					.filter(t -> t.getActive().equals(true)).toList();

			return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);

		} else {
			throw new UnAuthorized("You are not SuperApp/MiniApp user");
		}

	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(path = { "/superapp/objects/search/byAlias/{alias}" }, method = { RequestMethod.GET }, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public SuperAppObjectBoundary[] seacrhByAlias(
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@PathVariable("alias") String alias) {

		if (isSuperApp(userSuperapp, userEmail)) {
			List<SuperAppObjectBoundary> objectBoundaries = this.objectService
					.getSpecificObjectByAliasWithPagination(userSuperapp, userEmail, alias, size, page);
			System.out.print(objectBoundaries);

			return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);

		} else if (isMiniApp(userSuperapp, userEmail)) {

			List<SuperAppObjectBoundary> objectBoundaries = this.objectService
					.getSpecificObjectByAliasWithPagination(userSuperapp, userEmail, alias, size, page).stream()
					.filter(t -> t.getActive().equals(true)).toList();

			return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);

		} else {
			throw new UnAuthorized("You are not SuperApp/MiniApp user");
		}
	}

	@CrossOrigin(origins = "http://localhost:3000")
	@RequestMapping(path = { "/superapp/objects/search/byLocation/{lat}/{lng}/{distance}" }, method = {
			RequestMethod.GET }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public SuperAppObjectBoundary[] seacrhByLocation(
			@RequestParam(name = "units", required = false, defaultValue = "NEUTRAL") String units,
			@RequestParam(name = "userSuperapp", required = true) String userSuperapp,
			@RequestParam(name = "userEmail", required = true) String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page,
			@PathVariable("lat") double lat, @PathVariable("lng") double lng,
			@PathVariable("distance") double distance) {

		if (isSuperApp(userSuperapp, userEmail)) {
			List<SuperAppObjectBoundary> objectBoundaries = this.objectService
					.getSpecificObjectByLocationWithPagination(lat, lng, Math.abs(distance), units, size, page);
			System.out.print(objectBoundaries);

			return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);

		} else if (isMiniApp(userSuperapp, userEmail)) {

			List<SuperAppObjectBoundary> objectBoundaries = this.objectService
					.getSpecificObjectByLocationWithPagination(lat, lng, Math.abs(distance), units, size, page).stream()
					.filter(t -> t.getActive().equals(true)).toList();

			return objectBoundaries.toArray(new SuperAppObjectBoundary[0]);

		} else {
			throw new UnAuthorized("You are not SuperApp/MiniApp user");
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

}

package superapp.dal;

import java.util.List;
import java.util.TreeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;
import superapp.exception.OutOfUseOperation;
import superapp.exception.UnAuthorized;
import superapp.data.*;
import superapp.logic.ObjectQueries;
import superapp.restAPI.*;

@Service
public class ObjectServiceDB implements ObjectQueries {

	private ObjectCRUD objectsCRUD;
	private UserServiceDB userServiceDB;

	@Autowired
	public void setObjectsCRUD(ObjectCRUD objectsCRUD) {
		this.objectsCRUD = objectsCRUD;
	}

	@Autowired
	public void setUserServiceDB(UserServiceDB userServiceDB) {
		this.userServiceDB = userServiceDB;
	}

	@Override
	public SuperAppObjectBoundary createObject(SuperAppObjectBoundary object) {

		if (isSuperApp(object.getCreatedBy().getUserId().getSuperapp(), object.getCreatedBy().getUserId().getEmail())) {

			validateObject(object);
			SuperAppObjectEntity entity = this.toEntity(object);
			entity = this.objectsCRUD.save(entity);
			return this.toBoundary(entity);

		} else {
			throw new UnAuthorized("You are not SuperApp user");
		}
	}

	private SuperAppObjectBoundary toBoundary(SuperAppObjectEntity entity) {

		SuperAppObjectBoundary boundary = new SuperAppObjectBoundary();

		boundary.setObjectId(entity.getObjectId());
		boundary.setType(entity.getType());
		boundary.setAlias(entity.getAlias());
		boundary.setActive(entity.getActive());
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		boundary.setLocation(new Location(entity.getLat(), entity.getLng()));
		boundary.setCreatedBy(entity.getCreatedBy());
		boundary.setObjectDetails(entity.getObjectDetails());

		return boundary;
	}

	private SuperAppObjectEntity toEntity(SuperAppObjectBoundary boundary) {

		SuperAppObjectEntity entity = new SuperAppObjectEntity();

		entity.setObjectId(boundary.getObjectId());
		entity.setCreationTimestamp(boundary.getCreationTimestamp());

		if (boundary.getType() != null) {
			entity.setType(boundary.getType());
		}

		if (boundary.getAlias() != null) {
			entity.setAlias(boundary.getAlias());
		}

		if (boundary.getActive() != null) {
			entity.setActive(boundary.getActive());
		} else {
			entity.setActive(true);
		}

		if (boundary.getLocation() != null) {
			entity.setLocation(boundary.getLocation().getLat(), boundary.getLocation().getLng());
		}

		if (boundary.getCreatedBy() != null) {
			entity.setCreatedBy(boundary.getCreatedBy());
		}

		if (boundary.getObjectDetails() != null) {
			entity.setObjectDetails(boundary.getObjectDetails());
		} else {
			entity.setObjectDetails(new TreeMap<>());
		}

		return entity;
	}

	@Override
	public SuperAppObjectBoundary updateObject(String objectSuperApp, String internalObjectId,
			SuperAppObjectBoundary update) {

		SuperAppObjectEntity existing = this.objectsCRUD.findById(new ObjectId(objectSuperApp, internalObjectId))
				.orElseThrow(
						() -> new RuntimeException("could not find  object for update by ID: " + internalObjectId));

		if (update.getType() != null) {
			existing.setType(update.getType());
		}

		if (update.getAlias() != null) {
			existing.setAlias(update.getAlias());
		}

		if (update.getLocation() != null) {
			existing.setLocation(update.getLocation().getLat(), update.getLocation().getLng());
		}

		if (update.getActive() != null) {
			existing.setActive(update.getActive());
		}

		if (update.getObjectDetails() != null) {
			existing.setObjectDetails(update.getObjectDetails());
		}

		existing = this.objectsCRUD.save(existing);
		return this.toBoundary(existing);

	}

	@Override
	public SuperAppObjectBoundary getSpecificObject(String objectSupperApp, String internalObjectId) {

		SuperAppObjectEntity existing = this.objectsCRUD.findById(new ObjectId(objectSupperApp, internalObjectId))
				.orElseThrow(() -> new RuntimeException("could not find object with keyWord: " + internalObjectId));

		return this.toBoundary(existing);
	}

	@Deprecated
	@Override
	public List<SuperAppObjectBoundary> getAllObjects() {

		throw new OutOfUseOperation("Out of use method - see new spec");
	}

	@Override
	public void deleteAllObjects() {

		this.objectsCRUD.deleteAll();
	}

	@Override
	public void bind(ObjectId originId, String superappChild, String internalObjectIdChild) {

		SuperAppObjectEntity child = this.objectsCRUD.findById(originId)
				.orElseThrow(() -> new RuntimeException("could not find object id: " + originId.getInternalObjectId()));

		SuperAppObjectEntity parent = this.objectsCRUD.findById(new ObjectId(superappChild, internalObjectIdChild))
				.orElseThrow(() -> new RuntimeException("could not find object id: " + internalObjectIdChild));

		if (!parent.getChildren().contains(child) && !child.getParents().contains(parent)
				&& !parent.getChildren().contains(parent) && !child.getParents().contains(child)) {

			parent.addChild(child);
			child.addParent(parent);

			this.objectsCRUD.save(child);
			this.objectsCRUD.save(parent);
		}
	}

	@Deprecated
	@Override
	public List<SuperAppObjectBoundary> getAllChildren(String superapp, String internalObjectId) {

		throw new OutOfUseOperation("Out of use method - see new spec");
	}

	@Deprecated
	@Override
	public List<SuperAppObjectBoundary> getAllParents(String superapp, String internalObjectId) {

		throw new OutOfUseOperation("Out of use method - see new spec");
	}

	private void validateObject(SuperAppObjectBoundary object) {

		if (isNullOrEmpty(object.getType())) {
			throw new IllegalArgumentException("Object type cannot be null or empty.");
		}

		if (isNullOrEmpty(object.getAlias())) {
			throw new IllegalArgumentException("Object alias cannot be null or empty.");
		}

		if (isNullOrEmpty(object.getActive().toString())
				|| !((object.getActive().toString().equals("true")) || (object.getActive().toString().equals("false"))))
			throw new IllegalArgumentException("object active status must be deteminated by true or false");

	}

	private boolean isNullOrEmpty(String str) {

		return str == null || str.trim().isEmpty();
	}

	@Override
	public List<SuperAppObjectBoundary> getSpecificObjectByTypeWithPagination(String objectSupperApp,
			String internalObjectId, String type, int size, int page) {

		// Add checking on user type

		return this.objectsCRUD
				.findAllByType(type, PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "type", "id"))
				.stream() // Stream<SuperAppObjectEntity>
				.map(this::toBoundary) // Stream<SuperAppObjectBoundary>
				.toList();
	}

	@Override
	public List<SuperAppObjectBoundary> getSpecificObjectByAliasWithPagination(String userSuperapp, String userEmail,
			String alias, int size, int page) {

		// Add checking on user type

		return this.objectsCRUD
				.findAllByAlias(alias, PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "alias", "id"))
				.stream() // Stream<SuperAppObjectEntity>
				.map(this::toBoundary) // Stream<SuperAppObjectBoundary>
				.toList();
	}

	@Override
	public List<SuperAppObjectBoundary> getSpecificObjectByLocationWithPagination(double lat, double lng,
			double distance, String units, int size, int page) {

		Metrics metricsType = this.toEnumFromString(units);

		return this.objectsCRUD
				.findByLocationNear(new Point(lng, lat), new Distance(distance, metricsType),
						PageRequest.of(page, size, Direction.DESC, "creationTimestamp", "objectId"))
				.stream().map(this::toBoundary).toList();
	}

	@Override
	public List<SuperAppObjectBoundary> getAllChildrenWithPagination(String superapp, String internalObjectId, int size,
			int page) {
		return this.objectsCRUD
				.findByObjectId(new ObjectId(superapp, internalObjectId),
						PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "id"))
				.get(0).getChildren().stream() // Stream<SuperAppObjectEntity>
				.map(this::toBoundary) // Stream<SuperAppObjectBoundary>
				.toList(); // List<SuperAppObjectBoundary>
	}

	@Override
	public List<SuperAppObjectBoundary> getAllParentsWithPagination(String superapp, String internalObjectId, int size,
			int page) {
		return this.objectsCRUD
				.findByObjectId(new ObjectId(superapp, internalObjectId),
						PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "id"))
				.get(0).getParents().stream() // Stream<SuperAppObjectEntity>
				.map(this::toBoundary) // Stream<SuperAppObjectBoundary>
				.toList(); // List<SuperAppObjectBoundary>
	}

	@Override
	public List<SuperAppObjectBoundary> getAllObjectsWithPagintion(int size, int page) {

		return this.objectsCRUD.findAll(PageRequest.of(page, size, Direction.ASC, "createdTimestamp", "id")).stream() // Stream<SuperAppObjectEntity>
				.map(this::toBoundary) // Stream<SuperAppObjectBoundary>
				.toList(); // List<SuperAppObjectBoundary>
	}

	private Metrics toEnumFromString(String value) {
		if (value != null) {
			for (Metrics role : Metrics.values())
				if (value.equals(role.name()))
					return Metrics.valueOf(value);
		}
		return null;
	}

	private boolean isSuperApp(String superapp, String email) {

		UserId userToCheck = new UserId(superapp, email);
		return this.userServiceDB.getUserById(userToCheck).getRole().equals(UserRole.SUPERAPP_USER.toString());

	}

}

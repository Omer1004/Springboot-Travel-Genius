package superapp.logic;

import java.util.List;
import superapp.restAPI.SuperAppObjectBoundary;

public interface ObjectService {

	public SuperAppObjectBoundary createObject(SuperAppObjectBoundary object);

	public SuperAppObjectBoundary updateObject(String objectSuperApp, String internalObjectId,
			SuperAppObjectBoundary update);

	public SuperAppObjectBoundary getSpecificObject(String objectSupperApp, String internalObjectId);

	public List<SuperAppObjectBoundary> getAllObjects();

	public void deleteAllObjects();

}

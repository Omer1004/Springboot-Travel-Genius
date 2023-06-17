package superapp.logic;

import java.util.List;
import superapp.restAPI.SuperAppObjectBoundary;

public interface ObjectQueries extends ObjectServiceWithBindingCapabilities {

	public List<SuperAppObjectBoundary> getSpecificObjectByTypeWithPagination(String objectSupperApp,
			String internalObjectId, String type, int size, int page);

	public List<SuperAppObjectBoundary> getSpecificObjectByAliasWithPagination(String userSuperapp, String userEmail,
			String alias, int size, int page);

	public List<SuperAppObjectBoundary> getAllObjectsWithPagintion(int size, int page);

	public List<SuperAppObjectBoundary> getSpecificObjectByLocationWithPagination(double lat, double lng,
			double distance, String units, int size, int page);

}

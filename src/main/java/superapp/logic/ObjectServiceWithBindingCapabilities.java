package superapp.logic;

import java.util.List;
import superapp.data.ObjectId;
import superapp.restAPI.SuperAppObjectBoundary;

public interface ObjectServiceWithBindingCapabilities extends ObjectService {

	public void bind(ObjectId originId, String superappChild, String internalObjectIdChild);

	@Deprecated
	public List<SuperAppObjectBoundary> getAllChildren(String superapp, String internalObjectId);

	@Deprecated
	public List<SuperAppObjectBoundary> getAllParents(String superapp, String internalObjectId);

	public List<SuperAppObjectBoundary> getAllChildrenWithPagination(String superapp, String internalObjectId, int size,
			int page);

	public List<SuperAppObjectBoundary> getAllParentsWithPagination(String superapp, String internalObjectId, int size,
			int page);

}

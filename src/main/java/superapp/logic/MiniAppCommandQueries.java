package superapp.logic;

import java.util.List;
import superapp.restAPI.MiniAppCommandBoundary;

public interface MiniAppCommandQueries extends MiniAppCommandWithAsyncService {

	public List<MiniAppCommandBoundary> getAllCommandsWithPagination(int page, int size);

	public List<MiniAppCommandBoundary> getAllMiniAppCommandsWithPagination(String miniAppName, int page, int size);

}

package superapp.logic;

import superapp.restAPI.MiniAppCommandBoundary;

public interface MiniAppCommandWithAsyncService extends MiniAppCommandService {

	public MiniAppCommandBoundary invokeCommandLater(MiniAppCommandBoundary command);

}

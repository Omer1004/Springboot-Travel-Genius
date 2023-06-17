package superapp.logic;

import java.util.List;

//import superapp.restAPI.CommandResponse;
import superapp.restAPI.MiniAppCommandBoundary;

public interface MiniAppCommandService {

	public MiniAppCommandBoundary invokeCommand(MiniAppCommandBoundary command);

	@Deprecated
	public List<MiniAppCommandBoundary> getAllCommands();

	@Deprecated
	public List<MiniAppCommandBoundary> getAllMiniAppCommands(String miniAppName);

	public void deleteAllCommands();
}

package superapp.logic;

import java.util.List;
import superapp.restAPI.UserBoundary;

public interface UserService {

	public UserBoundary createUser(UserBoundary user);

	public UserBoundary login(String userSuperApp, String userEmail);

	public UserBoundary updateUser(String userSuperApp, String userEmail, UserBoundary update);

	public List<UserBoundary> getAllUsers();

	public void deleteAllUsers();

}

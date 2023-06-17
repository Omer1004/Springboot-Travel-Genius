package superapp.logic;

import java.util.List;
import superapp.data.UserId;
import superapp.restAPI.UserBoundary;

public interface UserQueries extends UserService {

	public List<UserBoundary> getAllUsersWithPagination(int page, int size);

	public UserBoundary getUserById(UserId user);

}

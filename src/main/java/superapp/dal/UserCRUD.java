package superapp.dal;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import superapp.data.UserEntity;
import superapp.data.UserId;

public interface UserCRUD extends ListCrudRepository<UserEntity, UserId> {

	public List<UserEntity> findAll(Pageable pageable);

}

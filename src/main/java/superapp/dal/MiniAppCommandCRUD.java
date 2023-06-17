package superapp.dal;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;

import superapp.data.CommandId;
import superapp.data.MiniAppCommandEntity;

public interface MiniAppCommandCRUD extends ListCrudRepository<MiniAppCommandEntity, CommandId> {

	public List<MiniAppCommandEntity> findAll(Pageable pageable);

}

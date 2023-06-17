package superapp.dal;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import superapp.data.ObjectId;
import superapp.data.SuperAppObjectEntity;

public interface ObjectCRUD extends ListCrudRepository<SuperAppObjectEntity, ObjectId> {

	public List<SuperAppObjectEntity> findAllByType(@Param("type") String type, Pageable pageable);

	public List<SuperAppObjectEntity> findAllByAlias(@Param("alias") String alias, Pageable pageable);

	public List<SuperAppObjectEntity> findAll(Pageable pageable);

	public List<SuperAppObjectEntity> findByObjectId(@Param("objectId") ObjectId objectId, Pageable pageable);

	public List<SuperAppObjectEntity> findByLocationNear(@Param("location") Point location,
			@Param("distance") Distance distance, Pageable pageable);

}

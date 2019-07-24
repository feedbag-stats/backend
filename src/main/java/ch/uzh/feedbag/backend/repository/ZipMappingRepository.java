package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.entity.ZipMapping;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository("zipMapping")
public interface ZipMappingRepository extends CrudRepository<ZipMapping, Long> {

	@Query(value = "SELECT z FROM ZipMapping z WHERE z.user = :user AND z.markedForDelete = false ORDER BY z.day DESC")
	List<ZipMapping> findByUser(@Param("user") User user);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ZipMapping z")
	void truncate();
}

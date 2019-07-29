package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.User;
import ch.uzh.feedbag.backend.entity.ZipMapping;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository("zipMapping")
public interface ZipMappingRepository extends CrudRepository<ZipMapping, Long> {

	@Query(value = "SELECT z FROM ZipMapping z WHERE z.user = :user ORDER BY z.day DESC")
	List<ZipMapping> findByUser(@Param("user") User user);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM ZipMapping z")
	void truncate();

	@Query(value = "SELECT MAX(z.day) FROM ZipMapping z WHERE z.user = :user")
	LocalDate findLatestByUser(User user);

	@Query(value = "SELECT z FROM ZipMapping z WHERE z.user = :user AND z.markedForDelete = true")
	List<ZipMapping> findByMarkedForDeleteUser(User user);
}

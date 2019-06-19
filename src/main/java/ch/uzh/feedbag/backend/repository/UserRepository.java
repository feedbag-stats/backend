package ch.uzh.feedbag.backend.repository;

import ch.uzh.feedbag.backend.entity.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {
	User findByName(String name);
	User findByUsername(String username);
	User findByToken(String token);

	@Modifying
	@Transactional
	@Query(value = "DELETE FROM User u")
	void truncate();
}

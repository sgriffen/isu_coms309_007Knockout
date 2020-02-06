package application.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Objects that are or extend {@code User}
 * 
 * @author Sean Griffen
 */
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}

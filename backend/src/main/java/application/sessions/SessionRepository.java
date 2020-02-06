package application.sessions;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for Objects that are or extend {@code Session}
 * 
 * @author Sean Griffen
 */
public interface SessionRepository extends JpaRepository<Session, Integer> {
	
}

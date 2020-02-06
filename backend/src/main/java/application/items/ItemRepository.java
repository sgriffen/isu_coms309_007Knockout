package application.items;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Objects that are or extend {@code Session}
 * 
 * @author Theodore Davis
 */
@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>{

}

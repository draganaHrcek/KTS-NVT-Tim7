package tim7.TIM7.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.Cenovnik;
@Repository
public interface CenovnikRepository extends JpaRepository<Cenovnik, Long> {

}

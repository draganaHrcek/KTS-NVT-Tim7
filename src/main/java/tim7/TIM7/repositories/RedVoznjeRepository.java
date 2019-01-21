package tim7.TIM7.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tim7.TIM7.model.RedVoznje;

@Repository
public interface RedVoznjeRepository extends JpaRepository<RedVoznje, Long>{
	
	public List<RedVoznje> findByObrisanFalse();
	public RedVoznje findByIdNotAndObrisanFalse(Long id);
}

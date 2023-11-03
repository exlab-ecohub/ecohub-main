package team.exlab.ecohub.recyclingpoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.exlab.ecohub.recyclingpoint.model.RecyclableType;

import java.util.Optional;

@Repository
public interface RecyclableTypeRepository extends JpaRepository<RecyclableType, Long> {
    Optional<RecyclableType> findByRusName(String rusName);

}
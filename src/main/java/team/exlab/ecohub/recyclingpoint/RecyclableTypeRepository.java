package team.exlab.ecohub.recyclingpoint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.exlab.ecohub.recyclingpoint.model.ERecyclableType;
import team.exlab.ecohub.recyclingpoint.model.RecyclableType;

@Repository
public interface RecyclableTypeRepository extends JpaRepository<RecyclableType, Long> {
    RecyclableType findByName(ERecyclableType name);
}
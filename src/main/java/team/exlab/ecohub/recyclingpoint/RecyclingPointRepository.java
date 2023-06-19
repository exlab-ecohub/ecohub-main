package team.exlab.ecohub.recyclingpoint;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.exlab.ecohub.recyclingpoint.model.RecyclableType;
import team.exlab.ecohub.recyclingpoint.model.RecyclingPoint;

import java.util.List;
import java.util.Set;

@Repository
public interface RecyclingPointRepository extends JpaRepository<RecyclingPoint, Long> {
    //todo На данный момент сделано как на сайте: выдача пунктов, содержащих ХОТЯ БЫ один из запрошенных типов
    List<RecyclingPoint> findAllDistinctByRecyclableTypesIn(Set<RecyclableType> types);

    List<RecyclingPoint> findAllDistinctByRecyclableTypesIn(Set<RecyclableType> types, Pageable pageable);
}

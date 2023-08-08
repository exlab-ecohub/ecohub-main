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

    List<RecyclingPoint> findAllDistinctByRecyclableTypesInOrderById(Set<RecyclableType> types, Pageable pageable);
    List<RecyclingPoint> findAllDistinctByDisplayedAndRecyclableTypesInOrderById(boolean displayed, Set<RecyclableType> types, Pageable pageable);
    List<RecyclingPoint> findAllByOrderById(Pageable pageable);
    List<RecyclingPoint> findAllByDisplayedOrderById(boolean displayed, Pageable pageable);
}

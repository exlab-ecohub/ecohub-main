package team.exlab.ecohub.recyclingpoint.model;

import lombok.*;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.util.Map;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "recycling_points")
public class RecyclingPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String website;
    @Embedded
    private Location location;
    @ElementCollection
    @CollectionTable(name = "working_hours",
            joinColumns = {@JoinColumn(name = "recycling_point_id", referencedColumnName = "id")})
    @MapKeyEnumerated(EnumType.ORDINAL)
    @MapKeyColumn(name = "day_of_week")
    private Map<DayOfWeek, WorkingHours> workingHours;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "recycling_points_recyclable_types",
            joinColumns = {@JoinColumn(name = "recycling_point_id")},
            inverseJoinColumns = {@JoinColumn(name = "recyclable_type_id")})
    private Set<RecyclableType> recyclableTypes;
}

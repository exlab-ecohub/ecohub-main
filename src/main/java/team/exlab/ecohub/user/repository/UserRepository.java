package team.exlab.ecohub.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.exlab.ecohub.user.model.Role;
import team.exlab.ecohub.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    List<User> findUsersByRole(Role role);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

package by.vstu.zamok.lab4.repository;

import by.vstu.zamok.lab4.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Optional<Group> findByGrName(String grName);
}
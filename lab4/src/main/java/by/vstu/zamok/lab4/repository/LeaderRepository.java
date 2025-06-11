package by.vstu.zamok.lab4.repository;

import by.vstu.zamok.lab4.entity.Leader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeaderRepository extends JpaRepository<Leader, Long> {
    List<Leader> findByPosition(String position);
    List<Leader> findBySurname(String surname);
    Optional<Leader> findByName(String name);
    Leader findByPhoneNumber(String phoneNumber);

}
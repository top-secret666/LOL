package by.vstu.zamok.lab4.repository;

import by.vstu.zamok.lab4.entity.Group;
import by.vstu.zamok.lab4.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByGroup(Group group);
    List<Student> findByGroupId(long groupId);
    List<Student> findBySurname(String surname);
}


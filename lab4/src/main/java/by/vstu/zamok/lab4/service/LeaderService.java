package by.vstu.zamok.lab4.service;

import by.vstu.zamok.lab4.entity.Group;
import by.vstu.zamok.lab4.entity.Leader;
import by.vstu.zamok.lab4.entity.Student;

import java.util.List;
import java.util.Optional;


public interface LeaderService extends Service<Leader> {
    List<Leader> findByPosition(String position);
    List<Leader> findBySurname(String surname);
    List<Student> getStudentsByLeader(Long leaderId);
    void assignStudentToLeader(Long leaderId, Long studentId);
    void removeStudentFromLeader(Long leaderId, Long studentId);
    Leader readByName(String name);
}
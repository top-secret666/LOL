package by.vstu.zamok.lab4.service.impl;

import by.vstu.zamok.lab4.entity.Leader;
import by.vstu.zamok.lab4.entity.Student;
import by.vstu.zamok.lab4.repository.LeaderRepository;
import by.vstu.zamok.lab4.repository.StudentRepository;
import by.vstu.zamok.lab4.service.LeaderService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LeaderServiceImpl implements LeaderService {

    private final LeaderRepository leaderRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public LeaderServiceImpl(LeaderRepository leaderRepository, StudentRepository studentRepository) {
        this.leaderRepository = leaderRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public Leader read(Long id) {
        return leaderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leader not found with id: " + id));
    }

    @Override
    public Leader readByName(String name) {
        return leaderRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Leader not found with name: " + name));
    }

    @Override
    public List<Leader> read() {
        return leaderRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Leader entity) {
        try {

            List<Leader> existingLeaders = leaderRepository.findBySurname(entity.getSurname());

            for (Leader existingLeader : existingLeaders) {
                if (existingLeader.getName().equals(entity.getName())) {
                    existingLeader.setPhoneNumber(entity.getPhoneNumber());
                    existingLeader.setPosition(entity.getPosition());
                    leaderRepository.save(existingLeader);
                    return;
                }
            }

            leaderRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error saving leader: " + e.getMessage(), e);
        }
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Leader leader = leaderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Leader not found with id: " + id));

        List<Student> studentsToUpdate = new ArrayList<>(leader.getStudents());

        for (Student student : studentsToUpdate) {
            student.setLeader(null);
            studentRepository.save(student);
        }

        leader.getStudents().clear();

        leaderRepository.delete(leader);
    }

    @Override
    public void edit(Leader entity) {
        Leader leader = leaderRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Leader not found with id: " + entity.getId()));

        leader.setName(entity.getName());
        leader.setSurname(entity.getSurname());
        leader.setPhoneNumber(entity.getPhoneNumber());
        leader.setPosition(entity.getPosition());

        leaderRepository.save(leader);
    }

    public List<Leader> findByPosition(String position) {
        return leaderRepository.findByPosition(position);
    }

    public List<Leader> findBySurname(String surname) {
        return leaderRepository.findBySurname(surname);
    }

    public List<Student> getStudentsByLeader(Long leaderId) {
        Leader leader = leaderRepository.findById(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("Leader not found with id: " + leaderId));
        return leader.getStudents();
    }

    public void assignStudentToLeader(Long leaderId, Long studentId) {
        Leader leader = leaderRepository.findById(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("Leader not found with id: " + leaderId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        student.setLeader(leader);
        leader.getStudents().add(student);
        leaderRepository.save(leader);
    }

    public void removeStudentFromLeader(Long leaderId, Long studentId) {
        Leader leader = leaderRepository.findById(leaderId)
                .orElseThrow(() -> new IllegalArgumentException("Leader not found with id: " + leaderId));
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + studentId));

        leader.getStudents().remove(student);
        student.setLeader(null);
        leaderRepository.save(leader);
    }
}

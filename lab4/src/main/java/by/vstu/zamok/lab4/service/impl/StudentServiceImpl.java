package by.vstu.zamok.lab4.service.impl;

import by.vstu.zamok.lab4.entity.Group;
import by.vstu.zamok.lab4.entity.Leader;
import by.vstu.zamok.lab4.entity.Student;
import by.vstu.zamok.lab4.repository.GroupRepository;
import by.vstu.zamok.lab4.repository.LeaderRepository;
import by.vstu.zamok.lab4.repository.StudentRepository;
import by.vstu.zamok.lab4.service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository sRepository;
    private final GroupRepository gRepository;
    private final LeaderRepository lRepository;

    @Autowired
    public StudentServiceImpl(StudentRepository sRepository, GroupRepository gRepository, LeaderRepository lRepository) {
        this.sRepository = sRepository;
        this.gRepository = gRepository;
        this.lRepository = lRepository;

    }

    @Override
    public Student read(Long id) {
        return sRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
    }

    @Override
    public List<Student> read() {
        return sRepository.findAll();
    }

    @Override
    @Transactional
    public void save(Student entity) {
        try {
            System.out.println("Student received: " + entity);
            System.out.println("Group in student: " + entity.getGroup());

            if (entity.getGroup() == null) {
                throw new IllegalArgumentException("Student must have a group");
            }

            Leader leader = null;
            if (entity.getLeader() != null) {
                leader = lRepository.findById(entity.getLeader().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Leader not found with id: " + entity.getLeader().getId()));
            }

            Group group = gRepository.findById(entity.getGroup().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + entity.getGroup().getId()));

            List<Student> existingStudents = sRepository.findBySurname(entity.getSurname());

            for (Student existingStudent : existingStudents) {
                if (existingStudent.getName().equals(entity.getName())) {
                    existingStudent.setPhoneNumber(entity.getPhoneNumber());

                    Leader oldLeader = existingStudent.getLeader();
                    Group oldGroup = existingStudent.getGroup();

                    if (oldLeader != null && leader != null && !oldLeader.getId().equals(leader.getId())) {
                        existingStudent.setLeader(leader);
                    } else if (oldLeader == null && leader != null) {
                        existingStudent.setLeader(leader);
                    }

                    if (oldGroup != null && !oldGroup.getId().equals(group.getId())) {
                        existingStudent.setGroup(group);

                        if (!group.getStudents().contains(existingStudent)) {
                            group.getStudents().add(existingStudent);
                            gRepository.save(group);
                        }
                    } else {
                        existingStudent.setGroup(group);
                    }

                    // Save the updated student
                    sRepository.save(existingStudent);
                    return;
                }
            }

            entity.setGroup(group);
            if (leader != null) {
                entity.setLeader(leader);
            }
            Student savedStudent = sRepository.save(entity);

            if (!group.getStudents().contains(savedStudent)) {
                group.getStudents().add(savedStudent);
                gRepository.save(group);
            }
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("Error saving student: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        Student student = sRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));
        Group group = student.getGroup();
        group.getStudents().remove(student);
        sRepository.delete(student);
        gRepository.save(group);
    }

    @Override
    public List<Student> readByGroupId(long groupId) {
        return sRepository.findByGroupId(groupId);
    }

    @Override
    public List<Student> readByGroup(Long groupId) {
        Group group = gRepository.findById(groupId)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + groupId));
        return sRepository.findByGroup(group);
    }


    @Override
    public List<Student> readBySurname(String surname) {
        return sRepository.findBySurname(surname);
    }

    @Override
    public void edit(Student entity) {
        Group group = gRepository.findById(entity.getGroup().getId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + entity.getGroup().getId()));
        Student student = sRepository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + entity.getId()));

        student.setGroup(group);
        student.setName(entity.getName());
        student.setSurname(entity.getSurname());
        student.setPhoneNumber(entity.getPhoneNumber());

        if (entity.getLeader() != null) {
            Leader leader = lRepository.findById(entity.getLeader().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Leader not found with id: " + entity.getLeader().getId()));
            student.setLeader(leader);
        } else {
            student.setLeader(null);
        }

        sRepository.save(student);
    }
}

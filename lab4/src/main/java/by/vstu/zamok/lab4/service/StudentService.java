package by.vstu.zamok.lab4.service;

import by.vstu.zamok.lab4.entity.Student;

import java.util.List;

public interface StudentService extends Service<Student> {
    List<Student> readBySurname(String surname);
    List<Student> readByGroupId(long groupId);
    List<Student> readByGroup(Long groupId);
}
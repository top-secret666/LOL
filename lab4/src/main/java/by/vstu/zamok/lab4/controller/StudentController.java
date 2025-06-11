package by.vstu.zamok.lab4.controller;

import by.vstu.zamok.lab4.entity.Group;
import by.vstu.zamok.lab4.entity.Student;
import by.vstu.zamok.lab4.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/student", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentController extends AbstractController<Student> {

    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @Override
    public StudentService getService() {
        return service;
    }

    // GET запросы доступны всем (guest, manager, admin)
    @GetMapping()
    public ResponseEntity<List<Student>> get() {
        List<Student> entities = service.read();
        if (entities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable long id) {
        Student entity = service.read(id);
        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<List<Student>> getStudentsByGroup(@PathVariable long id) {
        List<Student> students = service.readByGroupId(id);
        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students, headers, HttpStatus.OK);
    }

    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<Student>> getStudentsBySurname(@PathVariable String surname) {
        List<Student> students = service.readBySurname(surname);
        if (students.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(students, headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> put(@RequestBody Student entity) {
        service.save(entity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> post(@RequestBody Student entity){
        service.save(entity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete( @PathVariable long id){
        service.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

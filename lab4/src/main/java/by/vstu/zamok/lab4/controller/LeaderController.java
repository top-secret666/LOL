package by.vstu.zamok.lab4.controller;

import by.vstu.zamok.lab4.entity.Leader;
import by.vstu.zamok.lab4.entity.Student;
import by.vstu.zamok.lab4.service.LeaderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/leader")
@RequiredArgsConstructor
public class LeaderController extends AbstractController<Leader> {

    private final LeaderService service;

    @Override
    public LeaderService getService() {
        return service;
    }

    @GetMapping()
    public ResponseEntity<List<Leader>> get() {
        List<Leader> entities = service.read();
        if (entities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Leader> getById(@PathVariable long id) {
        Leader entity = service.read(id);
        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping("/position/{position}")
    public ResponseEntity<List<Leader>> getLeadersByPosition(@PathVariable String position) {
        List<Leader> leaders = service.findByPosition(position);
        if (leaders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(leaders, headers, HttpStatus.OK);
    }

    @GetMapping("/surname/{surname}")
    public ResponseEntity<List<Leader>> getLeadersBySurname(@PathVariable String surname) {
        List<Leader> leaders = service.findBySurname(surname);
        if (leaders.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(leaders, headers, HttpStatus.OK);
    }

    @GetMapping("/{leaderId}/students")
    public ResponseEntity<List<Student>> getStudentsByLeader(@PathVariable Long leaderId) {
        try {
            List<Student> students = service.getStudentsByLeader(leaderId);
            if (students.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(students, headers, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Операции назначения/удаления студентов доступны только ADMIN
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{leaderId}/students/{studentId}")
    public ResponseEntity<Void> assignStudentToLeader(@PathVariable Long leaderId, @PathVariable Long studentId) {
        try {
            service.assignStudentToLeader(leaderId, studentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{leaderId}/students/{studentId}")
    public ResponseEntity<Void> removeStudentFromLeader(@PathVariable Long leaderId, @PathVariable Long studentId) {
        try {
            service.removeStudentFromLeader(leaderId, studentId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Модифицирующие операции доступны только ADMIN
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> put(@RequestBody Leader entity) {
        service.save(entity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> post(@RequestBody Leader entity){
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

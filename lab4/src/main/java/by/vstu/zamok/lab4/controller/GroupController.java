package by.vstu.zamok.lab4.controller;

import by.vstu.zamok.lab4.entity.Group;
import by.vstu.zamok.lab4.entity.Student;
import by.vstu.zamok.lab4.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/group", produces = MediaType.APPLICATION_JSON_VALUE)
public class GroupController extends AbstractController<Group> {

    private final GroupService service;

    public GroupController(GroupService service) {
        this.service = service;
    }

    @Override
    public GroupService getService() {
        return service;
    }

    // GET запросы доступны всем (guest, manager, admin)
    @GetMapping()
    public ResponseEntity<List<Group>> get() {
        List<Group> entities = service.read();
        if (entities.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Group> getById(@PathVariable long id) {
        Group entity = service.read(id);
        if (entity == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<Group> getGroupByName(@PathVariable String name) {
        Group group = service.readByName(name);
        if (group == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(group, headers, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> put(@RequestBody Group entity) {
        service.save(entity);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> post(@RequestBody Group entity){
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

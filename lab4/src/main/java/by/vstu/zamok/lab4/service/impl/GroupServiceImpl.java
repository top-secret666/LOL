package by.vstu.zamok.lab4.service.impl;

import by.vstu.zamok.lab4.entity.Group;
import by.vstu.zamok.lab4.repository.GroupRepository;
import by.vstu.zamok.lab4.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    private final GroupRepository repository;

    @Autowired
    public GroupServiceImpl(GroupRepository repository) {
        this.repository = repository;
    }

    @Override
    public Group read(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + id));
    }

    @Override
    public List<Group> read() {
        return repository.findAll();
    }

    @Override
    public void save(Group entity) {
        try {
            Group existingGroup = repository.findByGrName(entity.getGrName()).orElse(null);

            if (existingGroup != null) {
                throw new IllegalArgumentException("Group with name '" + entity.getGrName() + "' already exists");
            }

            repository.save(entity);
        } catch (Exception e) {
            if (e instanceof IllegalArgumentException) {
                throw e;
            }
            throw new RuntimeException("Error saving group: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Group not found with id: " + id);
        }
    }

    @Override
    public Group readByName(String name) {
        return repository.findByGrName(name)
                .orElseThrow(() -> new IllegalArgumentException("Group not found with name: " + name));
    }

    @Override
    public void edit(Group entity) {
        Group group = repository.findById(entity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Group not found with id: " + entity.getId()));
        group.setGrName(entity.getGrName());
        repository.save(group);
    }
}

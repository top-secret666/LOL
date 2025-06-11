package by.vstu.zamok.lab4.service;

import by.vstu.zamok.lab4.entity.Group;

public interface GroupService extends Service<Group> {
    Group readByName(String name);
}

package com.example.hrautomationbackend.model;

import java.util.List;

public class UsersWithCount {
    private List<UserForAll> users;
    private Long count;

    public UsersWithCount() {
    }

    public static UsersWithCount toModel(List<UserForAll> users, Long count) {
        UsersWithCount model = new UsersWithCount();
        model.setUsers(users);
        model.setCount(count);
        return model;
    }

    public List<UserForAll> getUsers() {
        return users;
    }

    public void setUsers(List<UserForAll> users) {
        this.users = users;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}

package com.example.hrautomationbackend.model;

import java.util.List;

public class UsersWithPages {
    private List<UserForAll> users;
    private int pages;

    public UsersWithPages() {
    }

    public static UsersWithPages toModel(List<UserForAll> users, int pages) {
        UsersWithPages model = new UsersWithPages();
        model.setUsers(users);
        model.setPages(pages);
        return model;
    }

    public List<UserForAll> getUsers() {
        return users;
    }

    public void setUsers(List<UserForAll> users) {
        this.users = users;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}

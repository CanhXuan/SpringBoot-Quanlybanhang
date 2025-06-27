package canhxuan.quanlybanhang.service;

import canhxuan.quanlybanhang.entity.User;

import java.util.List;

public interface UserService {

    public List<User> getAll();

    public User getById(int id);

    public void create(User user);

    public void update(int id, User user);

    public void delete(int id);
}

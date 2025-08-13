package canhxuan.quanlybanhang.service.impl;

import canhxuan.quanlybanhang.entity.User;
import canhxuan.quanlybanhang.repository.UserRepository;
import canhxuan.quanlybanhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User getById(int id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void create(User user) {
        userRepository.save(user);
    }

    public void update(int id, User user) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setId(id);
        userRepository.save(user);
    }

    public void delete(int id) {
        userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.deleteById(id);
    }
}

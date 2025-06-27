package canhxuan.quanlybanhang.controller;

import canhxuan.quanlybanhang.entity.User;
import canhxuan.quanlybanhang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quanlybanhang/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        List<User> users = userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable int id) {
        User user = userService.getById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/create")
    public String create(@RequestBody User user) {
        userService.create(user);
        return "Create user successfully";
    }

    @PutMapping("/update/{id}")
    public String update(@PathVariable int id, @RequestBody User user) {
        userService.update(id, user);
        return "Update user successfully";
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        userService.delete(id);
        return "Delete user successfully";
    }
}

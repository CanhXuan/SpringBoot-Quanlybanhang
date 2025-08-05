package canhxuan.quanlybanhang.controller;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quanlybanhang/policy")
public class PolicyController {
    @Autowired
    private Enforcer enforcer;

    @GetMapping
    public ResponseEntity<List<List<String>>> getAllPolicies() {
        return ResponseEntity.ok(enforcer.getPolicy());
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPolicy(@RequestParam String sub,
                                       @RequestParam String obj,
                                       @RequestParam String act) {
        boolean added = enforcer.addPermissionForUser(sub, obj, act);
        if (added) {
            return ResponseEntity.ok("Policy added");
        } else {
            return ResponseEntity.badRequest().build();
        }
    }
}

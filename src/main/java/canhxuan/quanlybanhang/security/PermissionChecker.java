package canhxuan.quanlybanhang.security;

import org.casbin.jcasbin.main.Enforcer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PermissionChecker {
    @Autowired
    private Enforcer enforcer;

    public boolean hasPermission(String username, String resource, String action) {
        return enforcer.enforce(username,resource,action);
    }
}

package canhxuan.quanlybanhang.config;

import org.casbin.adapter.JDBCAdapter;
import org.casbin.jcasbin.main.Enforcer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class CasbinConfig {

    @Bean
    public Enforcer casbinEnforcer(DataSource dataSource) throws Exception {
        JDBCAdapter adapter = new JDBCAdapter(dataSource);
        Enforcer enforcer = new Enforcer("src/main/resources/model.conf", adapter);
        enforcer.loadPolicy();
        return enforcer;
    }
}

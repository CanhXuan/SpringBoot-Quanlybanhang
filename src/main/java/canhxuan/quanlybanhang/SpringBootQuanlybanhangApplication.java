package canhxuan.quanlybanhang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringBootQuanlybanhangApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootQuanlybanhangApplication.class, args);
	}

}

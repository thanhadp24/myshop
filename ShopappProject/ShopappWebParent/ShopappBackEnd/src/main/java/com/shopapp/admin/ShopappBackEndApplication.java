package com.shopapp.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan({"com.shopapp.common.entity", "com.shopapp.admin.user"})
public class ShopappBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShopappBackEndApplication.class, args);
	}

}

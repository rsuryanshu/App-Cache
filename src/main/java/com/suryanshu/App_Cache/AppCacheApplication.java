package com.suryanshu.App_Cache;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class AppCacheApplication {

	public static void main(String[] args) {
		SpringApplication.run(AppCacheApplication.class, args);
	}

}

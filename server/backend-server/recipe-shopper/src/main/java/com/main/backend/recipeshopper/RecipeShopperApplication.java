package com.main.backend.recipeshopper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// TODO Uncomment to enable scheduled task to run
// @EnableScheduling
public class RecipeShopperApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipeShopperApplication.class, args);
	}

}

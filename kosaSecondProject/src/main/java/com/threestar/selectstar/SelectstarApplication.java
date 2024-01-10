package com.threestar.selectstar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling // 스케 줄링
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.threestar.selectstar.repository"})
@EntityScan(basePackages = {"com.threestar.selectstar.domain.entity"})
public class SelectstarApplication {
	public static void main(String[] args) {
		SpringApplication.run(SelectstarApplication.class, args);
	}

}
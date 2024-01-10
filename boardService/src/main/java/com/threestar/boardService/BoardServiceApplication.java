package com.threestar.boardService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class BoardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardServiceApplication.class, args);
	}

}

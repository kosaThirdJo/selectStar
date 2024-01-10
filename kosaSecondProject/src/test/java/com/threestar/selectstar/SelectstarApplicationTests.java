package com.threestar.selectstar;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.sql.DriverManager;

@SpringBootTest
class SelectstarApplicationTests {


/*
	@Bean
	@Profile("test")
	public DataSource dataSource(){
		DriverManagerDataSource source = new DriverManagerDataSource();
		source.setDriverClassName("org.h2.Driver");
		source.setUrl("jdbc:h2:mem:testdb");
		source.setUsername("sa");
		source.setPassword("password");
		return source;
	}
*/

	@Test
	void contextLoads() {
	}

}

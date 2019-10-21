package com.stackroute;

import com.stackroute.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.sql.DataSource;

@SpringBootApplication
@EnableJpaRepositories("com.stackroute.repository")
@EntityScan("com.stackroute.domain")
//@EnableEurekaClient
public class DemoApplication implements CommandLineRunner{
	@Autowired
	DataSource dataSource;
	@Autowired
	UserRepository userRepository;
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class,args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Our DataSource is = " + dataSource);
		Iterable<com.stackroute.domain.User> userlist = userRepository.findAll();
		for(com.stackroute.domain.User usermodel:userlist){
			System.out.println("Here is a user: " + usermodel.toString());
		}
	}

}


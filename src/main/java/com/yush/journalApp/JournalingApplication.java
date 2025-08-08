package com.yush.journalApp;

import org.bson.codecs.pojo.annotations.BsonRepresentation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JournalingApplication {

	public static void main(String[] args) {
		SpringApplication.run(JournalingApplication.class, args);
	}

	@Bean
	public PlatformTransactionManager manager(MongoDatabaseFactory dbFactory){
		return new MongoTransactionManager(dbFactory);
	}

}

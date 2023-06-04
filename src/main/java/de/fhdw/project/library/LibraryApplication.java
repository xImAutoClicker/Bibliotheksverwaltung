package de.fhdw.project.library;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class LibraryApplication {

	@Getter
	private final static Gson gson = new GsonBuilder().create();

	public static void main(String[] args) {
		SpringApplication.run(LibraryApplication.class, args);
	}

}

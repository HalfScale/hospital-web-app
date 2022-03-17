package io.muffin.inventoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.muffin.inventoryservice.model.Roles;
import io.muffin.inventoryservice.model.User;
import io.muffin.inventoryservice.repository.RolesRepository;
import io.muffin.inventoryservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@SpringBootApplication
@RequiredArgsConstructor
public class TodoServiceApplication {

	private final UserRepository userRepository;
	private final RolesRepository rolesRepository;

	public static void main(String[] args) {
		SpringApplication.run(TodoServiceApplication.class, args);
	}

	@PostConstruct
	public void initData() {
		rolesRepository.saveAll(Arrays.asList(new Roles(1L, "ROLES_USER"), new Roles(2L, "ROLES_ADMIN")));
		Roles admin = rolesRepository.findById(2L).get();
		userRepository.save(new User(1L, "admin@gmail.com", "$2a$10$KKcVr1d6QIMv4SiiA6HP5uj8prhpadSvJItggd3mKDGV4sddwrfQC", admin));
	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper();
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**");
			}
		};
	}

}

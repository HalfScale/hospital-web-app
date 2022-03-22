package io.muffin.inventoryservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@RequiredArgsConstructor
public class HospitalWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(HospitalWebApplication.class, args);
	}

//	@PostConstruct
//	public void initData() {
//		rolesRepository.saveAll(Arrays.asList(new Roles(1L, "ROLES_USER"), new Roles(2L, "ROLES_ADMIN")));
//		Roles admin = rolesRepository.findById(2L).get();
//		userRepository.save(new Users(1L, "admin@gmail.com", "$2a$10$KKcVr1d6QIMv4SiiA6HP5uj8prhpadSvJItggd3mKDGV4sddwrfQC", admin));
//	}

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper().findAndRegisterModules();
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

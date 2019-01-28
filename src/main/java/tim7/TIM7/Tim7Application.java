package tim7.TIM7;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import tim7.TIM7.storage.StorageProperties;
import tim7.TIM7.storage.StorageService;

@SpringBootApplication(scanBasePackages={"tim7.TIM7", "tim7.TIM7.storage"})
@ComponentScan("tim7.TIM7.storage")
@EnableConfigurationProperties(StorageProperties.class)
public class Tim7Application {

	public static void main(String[] args) {
		SpringApplication.run(Tim7Application.class, args);
	}
	
	@Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }
}

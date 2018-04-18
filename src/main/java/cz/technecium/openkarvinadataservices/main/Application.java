package cz.technecium.openkarvinadataservices.main;

import cz.technecium.openkarvinadataservices.repository.CzechRatingListRepository;
import cz.technecium.openkarvinadataservices.repository.FideRatingListRepository;
import cz.technecium.openkarvinadataservices.repository.PlayerRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 *
 *
 * @author Martin.Sobek
 */
@SpringBootApplication
@Configuration
@ComponentScan(basePackages = "cz.technecium")
public class Application {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public PlayerRepository czechRatingListRepository() {
        return new CzechRatingListRepository(new ClassPathResource("ratinglists/lok_sm_cz.xls"));
    }

    @Bean
    public PlayerRepository fideRatingListRepository() {
        return new FideRatingListRepository(new ClassPathResource("ratinglists/standard_rating_list.xml"));
    }

}

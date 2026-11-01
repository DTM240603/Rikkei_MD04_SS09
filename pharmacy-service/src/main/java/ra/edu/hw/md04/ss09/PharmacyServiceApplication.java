package ra.edu.hw.md04.ss09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PharmacyServiceApplication {

    private static final Logger logger = LoggerFactory.getLogger(PharmacyServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(PharmacyServiceApplication.class, args);
    }

    @Bean
    ApplicationRunner logBranchConfiguration(
            @Value("${app.branch-name}") String branchName,
            @Value("${app.hotline}") String hotline) {
        return args -> logger.info("Pharmacy branch: {}, hotline: {}", branchName, hotline);
    }
}

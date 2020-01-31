package mateus.curso.microservicos.curso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.Entity;

@SpringBootApplication
@EntityScan({"mateus.curso.microservicos.core.model"})
@EnableJpaRepositories({"mateus.curso.microservicos.core.repository"})
public class CursoApplication {

    public static void main(String[] args) {
        SpringApplication.run(CursoApplication.class, args);
    }

}

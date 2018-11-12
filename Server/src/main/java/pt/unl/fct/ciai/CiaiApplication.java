package pt.unl.fct.ciai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.hateoas.core.EvoInflectorRelProvider;

@EnableHypermediaSupport(type = { HypermediaType.HAL })
@SpringBootApplication
public class CiaiApplication {

	//TODO adicionar application.properties ao repositorio bitbucket
//TODO adicionar badrequests aos puts (path != id) e posts (id > 0)

	public static void main(String[] args) {
		SpringApplication.run(CiaiApplication.class, args);
	}
	
	// Bean necessario para tornar os nomes gerados pelo HATEOAS mais sugestivos
	@Bean
	EvoInflectorRelProvider relProvider() {
		return new EvoInflectorRelProvider();
	}
	
}

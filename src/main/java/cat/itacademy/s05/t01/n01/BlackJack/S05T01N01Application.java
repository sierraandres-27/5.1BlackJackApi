package cat.itacademy.s05.t01.n01.BlackJack;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
	info = @Info(
			title = "Reactive BlackJackGame",
			version = "1.0.",
			description = "Reactive blackjack game created using spring webflux"
	)
)
public class S05T01N01Application {

	public static void main(String[] args) {
		SpringApplication.run(S05T01N01Application.class, args);
	}

}

package br.com.alura.scrennmatch;

import br.com.alura.scrennmatch.principal.Principal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrennmatchApplication implements CommandLineRunner {

	@Override //novo main
	public void run(String... args) throws Exception {
		Principal principal = new Principal();
		principal.exibirMenu();
	}

	public static void main(String[] args) {
		SpringApplication.run(ScrennmatchApplication.class, args);
	}

}

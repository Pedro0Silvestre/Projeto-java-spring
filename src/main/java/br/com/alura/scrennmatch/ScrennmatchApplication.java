package br.com.alura.scrennmatch;

import br.com.alura.scrennmatch.model.DadosSerie;
import br.com.alura.scrennmatch.service.ConsumoApi;
import br.com.alura.scrennmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScrennmatchApplication implements CommandLineRunner {

	@Override //novo main
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("http://www.omdbapi.com/?t=game+of+thrones&apikey=457dea58");
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obtenerDados(json,DadosSerie.class);
		System.out.println(dados);
	}

	public static void main(String[] args) {
		SpringApplication.run(ScrennmatchApplication.class, args);
	}

}

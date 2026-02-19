package br.com.screenmatch;

import br.com.screenmatch.model.DadosSerie;
import br.com.screenmatch.service.ConsumoApi;
import br.com.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectscreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ProjectscreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=game+of+thrones&apikey=d1cc2652");
		System.out.println(json);
//		var json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
//		System.out.println(json);
		ConverteDados converteDados = new ConverteDados();
		DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
		System.out.println(dadosSerie);
	}
}

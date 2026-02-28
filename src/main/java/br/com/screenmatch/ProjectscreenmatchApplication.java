package br.com.screenmatch;

import br.com.screenmatch.main.Main;
import br.com.screenmatch.main.NovaMain;
import br.com.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProjectscreenmatchApplication implements CommandLineRunner {

	@Autowired
	private SerieRepository repositorio;

	public static void main(String[] args) {
		SpringApplication.run(ProjectscreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

//		Main main = new Main();
		NovaMain main = new NovaMain(repositorio);
		main.exibeMenu();
	}
}

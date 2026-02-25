package br.com.screenmatch.main;

import br.com.screenmatch.model.DadosEpisodio;
import br.com.screenmatch.model.DadosSerie;
import br.com.screenmatch.model.DadosTemporada;
import br.com.screenmatch.model.Episodio;
import br.com.screenmatch.service.ConsumoApi;
import br.com.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();
    private final String ENDERECO_API = "https://www.omdbapi.com/?t=" ;
    private final String API_KEY = "&apikey=d1cc2652";

    public void exibeMenu() {
        System.out.println("Digite o nome da serie que deseja buscar:");

        var nomeSerie = scanner.nextLine();
        var json = consumoApi.obterDados(ENDERECO_API + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dadosSerie = converteDados.obterDados(json, DadosSerie.class);
        System.out.println(dadosSerie);

        List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i = 1; i <=dadosSerie.totalTemporadas(); i++) {
			json = consumoApi.obterDados(ENDERECO_API + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
			DadosTemporada dadosTemporada = converteDados.obterDados(json, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);

        for(int i = 0; i < dadosSerie.totalTemporadas(); i++) {
            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).numerosEpisodios();
            for(int j = 0; j < episodiosTemporada.size(); j++) {
                System.out.println(episodiosTemporada.get(j).titulo());
            }
        }

        temporadas.forEach(t -> t.numerosEpisodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.numerosEpisodios().stream())
                .collect(Collectors.toList());

//        System.out.println("top 10 episódios:");
//        dadosEpisodios.stream()
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                .peek(e -> System.out.println("Primeiro filtro(N/A) " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenacao " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limite " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento " + e))
//                .forEach(System.out::println);

        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.numerosEpisodios().stream()
                        .map(d -> new Episodio(t.numero(), d)))
                .collect(Collectors.toList());

        episodios.forEach(System.out::println);

//        System.out.println("Digite o trecho do titulo do episodio: ");
//
//        var trechoTitulo = scanner.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//
//        if (episodioBuscado.isPresent()) {
//            System.out.println("Episodio encontrado!");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//        } else {
//            System.out.println("Episodio não encontrado!");
//        }
//
//        System.out.println("De que ano você quer os episódios?");
//        var ano = scanner.nextLine();
//        scanner.nextLine();
//
//        LocalDate dataBusca = LocalDate.of(Integer.parseInt(ano), 1, 1);
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        episodios.stream().filter(e -> e.getLancamento() != null && e.getLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                        " Episódio: " + e.getNumeroEpisodio() +
//                        " Título: " + e.getTitulo() +
//                        " Lançamento: " + e.getLancamento().format(formatter)
//                ));

        Map<Integer, Double> avaliacaoPorTemporada = episodios.stream()
                .filter(e -> e.getAvaliacao() > 8.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacaoPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Media: " + est.getAverage());
        System.out.println("Melhor episodio: " + est.getMax());
        System.out.println("Pior episodio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());
    }
}

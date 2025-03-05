package br.com.alura.scrennmatch.principal;

import br.com.alura.scrennmatch.model.DadosEpisodio;
import br.com.alura.scrennmatch.model.DadosSerie;
import br.com.alura.scrennmatch.model.DadosTemporada;
import br.com.alura.scrennmatch.model.Episodio;
import br.com.alura.scrennmatch.service.ConsumoApi;
import br.com.alura.scrennmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=457dea58";
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    List<DadosTemporada> temporadas = new ArrayList<>();

    public void exibirMenu(){
        Scanner sc = new Scanner(System.in);
        System.out.println("""
                Menu ScrennMatch
                Por favor selecione o nome da série que deseja assistir:
                """);
        String nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.toLowerCase().replace(" ","+") + API_KEY);
        DadosSerie dados = conversor.obtenerDados(json,DadosSerie.class);
        System.out.println(dados);



		//Modelando Temporada e exibindo as informações de todas as temporadas
		for (int i = 1; i <= dados.totalDeTemporadas() ; i++) {
			json = consumo.obterDados(ENDERECO + nomeSerie.toLowerCase().replace(" ","+")+ "&season="+ i + API_KEY);
			DadosTemporada dadosTemporada = conversor.obtenerDados(json,DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		//temporadas.forEach(System.out::println);

        //temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo() + " Avaliação:"+e.avaliacao())));
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());// cria lista n imutavel
        //.toList();  criar lista imutavel

        System.out.println("\nTop 5 episodios");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        //Lista de episodios para podermos fazer analise apartir dos dados dos episodios
        List<Episodio> episodios = temporadas.stream().flatMap(t -> t.episodios().stream()
                .map(e -> new Episodio(t.numeroTemporada(),e))
        ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

        //identificando episodio pelo trecho do nome

        System.out.println("Qual episodio você deseja ver?");
        var trechoTitulo = leitura.nextLine();
       Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
                .findFirst();// esse metodo retorna um optinal ou seja o termo buscado pode ou não estar aqui assim ele evita respostas null
                if(episodioBuscado.isPresent()){
                    System.out.println("episodio encontrado");
                    System.out.println("Temporada: " + episodioBuscado.get().getTemporada() ); //episodio buscado apenas é o conteiner de um possivel dado para acessar alguma informação do dado em si deveos usar um get antes
                }else {
                    System.out.println("Episodio nao encontrado");
                }

//        System.out.println("A partir de qual ano você deseja ver os episódios?");
//        int ano = leitura.nextInt();
//        leitura.nextLine(); // apos nextint use next line para n dar erro sla pq
//
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1); // definindo uma data de busca apartir do ano passado a data ela vem no padrão americano então podemos usar um formatador para alterar isso
//
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");//definindo padrão brasileiro para formatar as dadtas
//
//        episodios.stream()
//                .filter(e -> e.getDataDeLancamento() != null  && e.getDataDeLancamento().isAfter(dataBusca))
//                .forEach(e -> System.out.println(
//                        "Temporada: " + e.getTemporada() +
//                                " Episodio: " + e.getNumeroEpisodio() +
//                                " Data de Lancamento: " + e.getDataDeLancamento().format(formatador)// formatando a data para o padrão do formatador
//                ));


        Map<Integer,Double> classficacaoPorTemporada = episodios.stream() // criando uma dicionario de cada temporada e sua avaliação média
                .filter(e -> e.getAvaliacao() > 0.0)//filtrando por episodios com classificação maior que zero
                .collect(Collectors.groupingBy(Episodio::getTemporada,//define a chave
                        Collectors.averagingDouble(Episodio::getAvaliacao))); //define valor que é a média dos valores agrupados
        System.out.println(classficacaoPorTemporada);

        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        System.out.println("Média dos episódios: " + est.getAverage());
        System.out.println("Pior episódio: " + est.getMin());
        System.out.println("Melhor episódio: " + est.getMax());
    }
}


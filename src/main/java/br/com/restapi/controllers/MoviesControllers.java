package br.com.restapi.controllers;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.opencsv.bean.CsvToBeanBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.restapi.model.Movie;
import br.com.restapi.model.ProducerTO;
import br.com.restapi.model.ResponseProducers;
import br.com.restapi.repository.MovieRepository;
import br.com.restapi.utils.FileUtils;

@RestController
@RequestMapping("/movie")
public class MoviesControllers {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    ResourceLoader resourceLoader;

    @PostConstruct
    public void Init() {
        try {
            var sucess = readFileCSV();
            // logger....
            System.out.println("Leitura CSV completo sucess:" + sucess);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean readFileCSV() throws IOException {
        try {

            // check movies exists
            var movieExist = movieRepository.existsById(1L);
            System.out.println("movies ja cadastrados :" + movieExist);
            if (!movieExist) {

                movieRepository.deleteAll();

                var csvFile = FileUtils.readDictionaryAsJson("movielist.csv");
                Reader reader = new FileReader(csvFile.getAbsolutePath());
                List<Movie> files = new CsvToBeanBuilder<Movie>(reader).withType(Movie.class).withSeparator(';').build()
                        .parse();

                movieRepository.saveAll(files);

                System.out.println("Total movies :" + files.size());

            }
            return true;
        } catch (IOException e) {
            throw e;
        }
    }

    @GetMapping("/")
    public List<Movie> getAll() {
        var movies = movieRepository.findAll();
        return movies;
    }

    @GetMapping("/{id}")
    public Movie getMovieById(@PathVariable("id") Long id) {
        var fOpt = movieRepository.findById(id);
        return fOpt.isPresent() ? fOpt.get() : null;
    }

    /*
     * Retorno Max = Produtor com maior intervalo entre dois prêmios consecutivos,
     * Min = Produtor que obteve dois prêmios mais rápido
     */
    @GetMapping("/producers")
    public ResponseProducers findAllWinner() {

        List<ProducerTO> list = new ArrayList<>();

        // a lista ja esta ordenada por ano by ASC do menor para o maior
        // recupera todos os movies com produtores vencedores
        var resultMoviesOrden = movieRepository.findAllMoviesWinner();

        for (Movie movie : resultMoviesOrden) {
            //
            // pesquisar se o mesmo ja esta na lista
            var jaAdd = list.stream().filter(t -> t.getProducer().equals(movie.getProducers()))
                    .collect(Collectors.toList());

            // caso ja tenha esse movie ja tenha sido processado
            if (jaAdd.size() > 0)
                continue;

            // todos os movies desse mesmo produtor e incluindo o current
            var moviesProdut = resultMoviesOrden.stream().filter(f -> f.getProducers().equals(movie.getProducers()))
                    .collect(Collectors.toList());

            ProducerTO prodTO = null;

            // criar pares para calculo do intervalo
            for (int i = 0; i < moviesProdut.size(); i++) {
                var first = moviesProdut.get(i);

                // verificar se existe proximo
                if (moviesProdut.size() - 1 > i) {
                    var next = moviesProdut.get(i + 1);
                    first.setIntervalo(next.getYear() - first.getYear());
                    if (prodTO == null || prodTO.getInterval() < first.getIntervalo()) {
                        prodTO = new ProducerTO(movie.getProducers(), first.getIntervalo(), first.getYear(),
                                next.getYear());
                        list.add(prodTO);
                    }
                }
            }
        }
        // ordena por intervalo do menor para o maior
        Comparator<ProducerTO> c = (s1, s2) -> s1.getInterval().compareTo(s2.getInterval());
        list.sort(c);

        var minVal = list.get(0).getInterval(); // primeiro
        var maxVal = list.get(list.size() - 1).getInterval();// ultimo

        // recuperar todos os registros com esse mesmo valor
        var listMin = list.stream().filter(f -> f.getInterval().equals(minVal)).collect(Collectors.toList());

        var listMax = list.stream().filter(f -> f.getInterval().equals(maxVal)).collect(Collectors.toList());

        ResponseProducers responseProducers = new ResponseProducers(listMin, listMax);

        return responseProducers;
    }

    @PostMapping("/")
    public String newMovie(@RequestBody Movie movie) {

        var movieExist = movieRepository.findByName(movie.getTitle());
        if (movieExist == null) {
            movieRepository.save(movie);
            return "Cadastro realizado com sucesso!";
        } else
            return "Já existe um movie com esse titulo";
    }

    @PutMapping("/")
    public String updateMovie(@RequestBody Movie movie) {

        var movieExist = movieRepository.findById(movie.getId());
        if (movieExist == null) {
            return "Esse movie não foi encontrado";
        } else {
            movieRepository.save(movie);
            return "Atualização realizada com sucesso!";
        }
    }

    @DeleteMapping("/{id}")
    public void deleteMovieByID(@PathVariable("id") Long id) {
        movieRepository.deleteById(id);
    }

}

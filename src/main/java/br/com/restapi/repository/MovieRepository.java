package br.com.restapi.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.restapi.model.Movie;
 

public interface MovieRepository extends JpaRepository<Movie, Long> {

    //
    @Query(value = "SELECT f FROM movie as f WHERE f.producers in (SELECT f2.producers  FROM movie f2 WHERE f2.winner ='yes' GROUP BY f2.producers  HAVING COUNT(*) > 1 ) ORDER BY f.year ASC")
    List<Movie> findAllMoviesWinner();

    @Query(value = "SELECT f FROM movie as f WHERE f.title = :title ORDER BY f.year ASC")
    Movie findByName(String title);

}

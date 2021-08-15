package br.com.restapi.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.opencsv.bean.CsvBindByName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "movie")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @CsvBindByName
    private int year;

    @CsvBindByName
    private String title;

    @CsvBindByName
    private String studios;

    @CsvBindByName
    private String producers;

    @CsvBindByName
    private String winner;

    @Transient
    private Integer intervalo;

    @Override
    public String toString() {
        return "id:" + id + ",year:" + year + ",title" + title + ",producers:" + producers + ",winner:" + winner
                + ",intervalo:" + intervalo;
    }
}

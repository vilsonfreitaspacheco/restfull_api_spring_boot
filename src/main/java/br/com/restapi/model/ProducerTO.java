package br.com.restapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor 
public class ProducerTO {
    private String producer;
    private Integer interval;
    private Integer previousWin;
    private Integer followingWin;//

    @Override
    public String toString() {
        return "producer:" + producer + ", interval:" + interval + ",previousWin:" + previousWin + ",followingWin:"
                + followingWin;
    } 
}

 
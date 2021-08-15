package br.com.restapi.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class ResponseProducers {
    private List<ProducerTO> min;
    private List<ProducerTO> max;
    
}

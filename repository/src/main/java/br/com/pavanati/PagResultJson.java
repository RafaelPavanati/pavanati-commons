package br.com.pavanati;

import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PagResultJson<T> {

    private List<Object> register;
    private int size;
    private int totalPag;
    private int pos;
    private long totalRegister;


    public PagResultJson<?> setFactory(FactoryPagResultResponse factoryPagResultResponse) {
        this.register = register.stream().map(o -> factoryPagResultResponse.toResponse(o)).collect(Collectors.toList());
        return this;
    }
}

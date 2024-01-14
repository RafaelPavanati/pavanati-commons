package br.com.pavanati;

public interface FactoryPagResultResponse<K, T> {
    T toResponse(K object);
}

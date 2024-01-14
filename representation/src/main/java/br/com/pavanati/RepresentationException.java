package br.com.pavanati;


public class RepresentationException extends RuntimeException {

    public RepresentationException(Exception e) {
        super("Erro ao gerar filtros ", e);
    }

}

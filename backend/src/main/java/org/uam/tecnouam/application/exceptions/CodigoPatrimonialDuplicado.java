package org.uam.tecnouam.application.exceptions;

public class CodigoPatrimonialDuplicado extends RuntimeException {
    public CodigoPatrimonialDuplicado(String codigoPatrimonial) {
        super("Ya existe un equipo con el código patrimonial %s.".formatted(
            codigoPatrimonial));
    }
}

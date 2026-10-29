package org.uam.tecnouam.domain.exceptions;

public class FechaDevolucionInvalida extends RuntimeException {
    public FechaDevolucionInvalida() {
        super("La fecha prevista de devolución no puede ser anterior a la fecha del préstamo.");
    }
}

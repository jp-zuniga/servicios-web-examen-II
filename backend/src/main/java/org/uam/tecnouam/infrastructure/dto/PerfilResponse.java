package org.uam.tecnouam.infrastructure.dto;

import java.util.List;

public record PerfilResponse(String usuario, List<String> roles) {
}

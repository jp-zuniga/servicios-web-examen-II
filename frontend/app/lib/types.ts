export type TipoEquipo = "PORTATIL" | "PROYECTOR" | "TABLETA";
export type EstadoEquipo = "DISPONIBLE" | "PRESTADO";

export interface Equipo {
  id: number;
  codigoPatrimonial: string;
  nombre: string;
  tipo: TipoEquipo;
  estado: EstadoEquipo;
}

export interface LoginResponse {
  token: string;
  type: string;
}

export interface Sesion {
  usuario: string;
  roles: string[];
}

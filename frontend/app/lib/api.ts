import axios from "axios";

import type { Sesion } from "./types";

const TOKEN_KEY = "tecnouam.token";

export const API_URL = import.meta.env.VITE_API_URL ?? "http://localhost:9090/api";

export const api = axios.create({ baseURL: API_URL });

// Adjunta el JWT guardado a cada petición: Authorization: Bearer <token>
api.interceptors.request.use((config) => {
  const token = getToken();

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

// localStorage solo existe en el navegador; durante el render del servidor no hay token.
export function getToken(): string | null {
  return typeof window === "undefined" ? null : localStorage.getItem(TOKEN_KEY);
}

export function guardarToken(token: string) {
  localStorage.setItem(TOKEN_KEY, token);
}

export function borrarToken() {
  localStorage.removeItem(TOKEN_KEY);
}

// Lee (sin verificar) el payload del JWT para mostrar el usuario y su rol.
export function leerSesion(token: string): Sesion | null {
  try {
    const base64 = token.split(".")[1].replace(/-/g, "+").replace(/_/g, "/");
    const payload = JSON.parse(atob(base64));

    return { usuario: payload.sub, roles: payload.roles ?? [] };
  } catch {
    return null;
  }
}

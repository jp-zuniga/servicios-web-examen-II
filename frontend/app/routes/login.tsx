import { useEffect, useState, type FormEvent } from "react";
import { useNavigate } from "react-router";
import axios from "axios";

import type { Route } from "./+types/login";
import { Encabezado } from "~/components/Encabezado";
import { API_URL, api, getToken, guardarToken } from "~/lib/api";
import type { LoginResponse } from "~/lib/types";

export function meta({}: Route.MetaArgs) {
  return [{ title: "Iniciar sesión | TecnoUAM" }];
}

export default function Login() {
  const navigate = useNavigate();

  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [enviando, setEnviando] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // Si ya hay un token guardado, va directo a la lista.
  useEffect(() => {
    if (getToken()) {
      navigate("/", { replace: true });
    }
  }, [navigate]);

  async function iniciarSesion(evento: FormEvent<HTMLFormElement>) {
    evento.preventDefault();
    setEnviando(true);
    setError(null);

    try {
      const respuesta = await api.post<LoginResponse>("/auth/login", {
        username,
        password,
      });

      guardarToken(respuesta.data.token);
      navigate("/", { replace: true });
    } catch (e) {
      if (axios.isAxiosError(e) && e.response?.status === 401) {
        setError("Usuario o contraseña incorrectos.");
      } else if (axios.isAxiosError(e) && e.response?.status === 400) {
        setError("Escriba el usuario y la contraseña.");
      } else {
        setError(`No se pudo conectar con la API en ${API_URL}.`);
      }
    } finally {
      setEnviando(false);
    }
  }

  const campo =
    "mt-1.5 block w-full rounded border border-line bg-white px-3 py-2 text-ink";

  return (
    <>
      <Encabezado />

      <main className="mx-auto max-w-5xl px-5 py-12">
        <div className="max-w-sm">
          <h1 className="text-3xl font-semibold tracking-tight">Iniciar sesión</h1>
          <p className="mt-1 text-muted">
            Use su cuenta del centro para consultar los equipos.
          </p>

          <form onSubmit={iniciarSesion} className="mt-8 space-y-5" noValidate>
            <label className="block">
              <span className="text-sm font-medium">Usuario</span>
              <input
                name="username"
                autoComplete="username"
                required
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                className={campo}
              />
            </label>

            <label className="block">
              <span className="text-sm font-medium">Contraseña</span>
              <input
                name="password"
                type="password"
                autoComplete="current-password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className={campo}
              />
            </label>

            {error && (
              <p role="alert" className="text-sm font-medium text-error">
                {error}
              </p>
            )}

            <button
              type="submit"
              disabled={enviando}
              className="w-full rounded bg-ink px-4 py-2.5 font-medium text-white hover:bg-ink/90 disabled:opacity-60"
            >
              {enviando ? "Iniciando sesión…" : "Iniciar sesión"}
            </button>
          </form>
        </div>
      </main>
    </>
  );
}

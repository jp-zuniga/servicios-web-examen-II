import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router";
import axios from "axios";

import type { Route } from "./+types/home";
import { Encabezado } from "~/components/Encabezado";
import { TablaEquipos } from "~/components/TablaEquipos";
import { API_URL, api, borrarToken, getToken, leerSesion } from "~/lib/api";
import type { Equipo, Sesion } from "~/lib/types";

export function meta({}: Route.MetaArgs) {
  return [{ title: "Equipos | TecnoUAM" }];
}

export default function Home() {
  const navigate = useNavigate();

  const [sesion, setSesion] = useState<Sesion | null>(null);
  const [equipos, setEquipos] = useState<Equipo[]>([]);
  const [cargando, setCargando] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [sesionVencida, setSesionVencida] = useState(false);
  const [recargas, setRecargas] = useState(0);

  useEffect(() => {
    const token = getToken();

    if (!token) {
      navigate("/login", { replace: true });
      return;
    }

    setSesion(leerSesion(token));

    let cancelado = false;

    async function cargarEquipos() {
      setCargando(true);
      setError(null);

      try {
        // GET protegido: el interceptor de api.ts agrega el token.
        const respuesta = await api.get<Equipo[]>("/equipos");

        if (!cancelado) {
          setEquipos(respuesta.data);
        }
      } catch (e) {
        if (cancelado) {
          return;
        }

        if (axios.isAxiosError(e) && e.response?.status === 401) {
          borrarToken();
          setSesionVencida(true);
          setError("Su sesión venció o el token no es válido. Inicie sesión de nuevo.");
        } else if (axios.isAxiosError(e) && e.response?.status === 403) {
          setError("Su cuenta no tiene permiso para ver los equipos.");
        } else if (axios.isAxiosError(e) && !e.response) {
          setError(
            `No se pudo conectar con la API en ${API_URL}. Verifique que el backend esté en ejecución.`,
          );
        } else {
          setError("La API respondió con un error al cargar los equipos.");
        }
      } finally {
        if (!cancelado) {
          setCargando(false);
        }
      }
    }

    cargarEquipos();

    return () => {
      cancelado = true;
    };
  }, [navigate, recargas]);

  function cerrarSesion() {
    borrarToken();
    navigate("/login");
  }

  const disponibles = equipos.filter((e) => e.estado === "DISPONIBLE").length;

  return (
    <>
      <Encabezado sesion={sesion} onCerrarSesion={cerrarSesion} />

      <main className="mx-auto max-w-5xl px-5 py-10">
        <div className="mb-6 flex flex-wrap items-end justify-between gap-4">
          <div>
            <h1 className="text-3xl font-semibold tracking-tight">Equipos</h1>
            <p className="mt-1 text-muted" aria-live="polite">
              {cargando || error
                ? "Inventario de portátiles, proyectores y tabletas."
                : `${disponibles} de ${equipos.length} equipos disponibles para préstamo.`}
            </p>
          </div>

          <button
            type="button"
            onClick={() => setRecargas((n) => n + 1)}
            disabled={cargando}
            className="rounded border border-line bg-white px-4 py-2 text-sm font-medium hover:bg-paper disabled:opacity-60"
          >
            {cargando ? "Actualizando…" : "Actualizar lista"}
          </button>
        </div>

        {cargando ? (
          <p
            role="status"
            className="rounded-md border border-line bg-white px-5 py-10 text-muted"
          >
            Cargando equipos…
          </p>
        ) : error ? (
          <div
            role="alert"
            className="rounded-md border border-error/40 bg-white px-5 py-6"
          >
            <p className="font-medium text-error">{error}</p>
            {sesionVencida ? (
              <Link
                to="/login"
                className="mt-4 inline-block rounded bg-ink px-4 py-2 text-sm font-medium text-white"
              >
                Iniciar sesión
              </Link>
            ) : (
              <button
                type="button"
                onClick={() => setRecargas((n) => n + 1)}
                className="mt-4 rounded bg-ink px-4 py-2 text-sm font-medium text-white"
              >
                Reintentar
              </button>
            )}
          </div>
        ) : equipos.length === 0 ? (
          <div className="rounded-md border border-dashed border-line bg-white px-5 py-10">
            <p className="font-medium">No hay equipos registrados.</p>
            <p className="mt-1 text-muted">
              Cuando un administrador registre un equipo, aparecerá en esta lista.
            </p>
          </div>
        ) : (
          <TablaEquipos equipos={equipos} />
        )}
      </main>
    </>
  );
}

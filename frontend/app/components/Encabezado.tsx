import type { Sesion } from "~/lib/types";

const nombresRol: Record<string, string> = {
  ADMIN: "Administrador",
  USER: "Usuario",
};

interface Props {
  sesion?: Sesion | null;
  onCerrarSesion?: () => void;
}

export function Encabezado({ sesion, onCerrarSesion }: Props) {
  return (
    <header className="border-b border-line bg-white">
      <div className="mx-auto flex max-w-5xl flex-wrap items-center justify-between gap-x-6 gap-y-2 px-5 py-4">
        <p className="leading-tight">
          <span className="block font-semibold">TecnoUAM</span>
          <span className="block text-sm text-muted">Préstamo de equipos</span>
        </p>

        {sesion && (
          <div className="flex items-center gap-4 text-sm">
            <span>
              {sesion.usuario}{" "}
              <span className="text-muted">
                ({sesion.roles.map((r) => nombresRol[r] ?? r).join(", ")})
              </span>
            </span>
            <button
              type="button"
              onClick={onCerrarSesion}
              className="rounded border border-line px-3 py-1.5 font-medium hover:bg-paper"
            >
              Cerrar sesión
            </button>
          </div>
        )}
      </div>
    </header>
  );
}

import type { EstadoEquipo as Estado } from "~/lib/types";

const estilos: Record<Estado, { texto: string; color: string; punto: string }> = {
  DISPONIBLE: { texto: "Disponible", color: "text-disponible", punto: "bg-disponible" },
  PRESTADO: { texto: "Prestado", color: "text-prestado", punto: "bg-prestado" },
};

export function EstadoEquipo({ estado }: { estado: Estado }) {
  const { texto, color, punto } = estilos[estado];

  return (
    <span className={`inline-flex items-center gap-2 font-medium ${color}`}>
      <span aria-hidden="true" className={`size-2 rounded-full ${punto}`} />
      {texto}
    </span>
  );
}

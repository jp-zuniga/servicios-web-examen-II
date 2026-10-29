import type { Equipo, TipoEquipo } from "~/lib/types";
import { EstadoEquipo } from "./EstadoEquipo";
import { EtiquetaActivo } from "./EtiquetaActivo";

const tipos: Record<TipoEquipo, string> = {
  PORTATIL: "Portátil",
  PROYECTOR: "Proyector",
  TABLETA: "Tableta",
};

export function TablaEquipos({ equipos }: { equipos: Equipo[] }) {
  return (
    <div className="overflow-x-auto rounded-md border border-line bg-white">
      <table className="w-full min-w-xl text-left text-[0.9375rem]">
        <thead className="border-b border-line text-sm text-muted">
          <tr>
            <th scope="col" className="px-5 py-3 font-medium">Código</th>
            <th scope="col" className="px-5 py-3 font-medium">Nombre</th>
            <th scope="col" className="px-5 py-3 font-medium">Tipo</th>
            <th scope="col" className="px-5 py-3 font-medium">Estado</th>
          </tr>
        </thead>
        <tbody className="divide-y divide-line">
          {equipos.map((equipo) => (
            <tr key={equipo.id}>
              <td className="px-5 py-3.5">
                <EtiquetaActivo codigo={equipo.codigoPatrimonial} />
              </td>
              <td className="px-5 py-3.5 text-ink">{equipo.nombre}</td>
              <td className="px-5 py-3.5 text-muted">{tipos[equipo.tipo]}</td>
              <td className="px-5 py-3.5">
                <EstadoEquipo estado={equipo.estado} />
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

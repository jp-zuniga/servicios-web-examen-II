// Código patrimonial con el aspecto de la etiqueta de activo fijo pegada al equipo.
export function EtiquetaActivo({ codigo }: { codigo: string }) {
  return (
    <span className="inline-flex items-center gap-2 whitespace-nowrap rounded-[3px] border border-tag-edge bg-tag py-0.5 pr-2.5 pl-1.5 font-condensed text-[0.9375rem] font-semibold tracking-wide text-ink shadow-[inset_0_-1px_0_rgb(0_0_0/0.12)]">
      <span
        aria-hidden="true"
        className="size-1.5 rounded-full bg-ink/70 ring-2 ring-tag-edge/60"
      />
      {codigo}
    </span>
  );
}

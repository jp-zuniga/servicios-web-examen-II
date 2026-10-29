INSERT INTO equipo (codigo_patrimonial, nombre, tipo, estado)
SELECT v.codigo, v.nombre, v.tipo, v.estado FROM (
VALUES
    ('UAM-LAP-001', 'Laptop Dell Latitude 5440', 'PORTATIL', 'DISPONIBLE'),
    ('UAM-LAP-002', 'Laptop Lenovo ThinkPad E14', 'PORTATIL', 'DISPONIBLE'),
    ('UAM-PRY-001', 'Proyector Epson PowerLite X49', 'PROYECTOR', 'PRESTADO'),
    ('UAM-PRY-002', 'Proyector BenQ MS560', 'PROYECTOR', 'DISPONIBLE'),
    ('UAM-TAB-001', 'Tableta Samsung Galaxy Tab S9', 'TABLETA', 'DISPONIBLE')
) AS v (codigo, nombre, tipo, estado)
WHERE NOT EXISTS (SELECT 1 FROM equipo);

INSERT INTO prestamo (equipo_id, nombre_solicitante, fecha_prestamo, fecha_devolucion_prevista, estado)
SELECT e.id, 'María López', CURRENT_DATE, CURRENT_DATE + 3, 'ACTIVO' FROM equipo e
WHERE e.codigo_patrimonial = 'UAM-PRY-001' AND NOT EXISTS (SELECT 1 FROM prestamo);

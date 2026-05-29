# Reporte de Refactorización Semántica

## Resumen
Este documento detalla todos los cambios realizados durante la refactorización semántica del proyecto `ParkUQ`, orientada a mejorar la legibilidad del código sin alterar la lógica, el flujo de control ni la funcionalidad existente.

*   **Total de identificadores renombrados:** 12
*   **Estado:** Aplicado con éxito.

## Tabla de Cambios Aplicados

| # | Tipo | Nombre anterior | Nombre nuevo | Archivo(s) afectado(s) | Motivo |
|---|---|---|---|---|---|
| 1 | Parámetro | `id` | `identificacion` | `Parqueadero.java` (`registrarIngreso`) | Evitar abreviaturas para mayor claridad |
| 2 | Variable de ciclo | `v` | `vehiculo` | `Parqueadero.java`, `OperadorControlador.java` | Variable de una sola letra poco descriptiva |
| 3 | Variable de ciclo | `e` | `espacio` | `Parqueadero.java`, `AdminControlador.java` | Variable de una sola letra poco descriptiva |
| 4 | Variable de ciclo | `u` | `usuario` | `Parqueadero.java` | Variable de una sola letra poco descriptiva |
| 5 | Variable de ciclo | `t` | `tarifa` | `Parqueadero.java` | Variable de una sola letra poco descriptiva |
| 6 | Parámetro | `us` | `usuarioNuevo` | `Parqueadero.java` (`agregarUsuarioSistema`) | Abreviatura poco clara |
| 7 | Parámetro | `user` | `nombreUsuario` | `Parqueadero.java` (`autenticarUsuarioSistema`) | Uso de palabra en inglés en código español |
| 8 | Parámetro | `pass` | `contrasena` | `Parqueadero.java` (`autenticarUsuarioSistema`) | Uso de abreviatura en inglés |
| 9 | Variable de ciclo | `r` | `registro` | `GestorReportes.java` | Variable de una sola letra poco descriptiva |
| 10 | Variable local | `p` | `nuevoParqueadero` | `AppParkUQ.java` (`inicializarParqueadero`) | Abreviatura poco descriptiva |
| 11 | Variable local | `tipoStr` | `tipoTexto` | `AdminControlador.java`, `OperadorControlador.java` | Mezcla de español con abreviatura inglesa (`Str`) |
| 12 | Variable local | `estadoStr` | `estadoTexto` | `AdminControlador.java` (`modificarEstadoEspacio`) | Mezcla de español con abreviatura inglesa (`Str`) |

## Archivos Modificados

1.  `src/trabajoFinal/servicio/Parqueadero.java`
2.  `src/trabajoFinal/servicio/GestorReportes.java`
3.  `src/trabajoFinal/ui/AppParkUQ.java`
4.  `src/trabajoFinal/ui/controlador/AdminControlador.java`
5.  `src/trabajoFinal/ui/controlador/OperadorControlador.java`

> **Confirmación Oficial:**
> La lógica, las validaciones y la funcionalidad general de la aplicación no sufrieron ninguna alteración. La refactorización fue 100% semántica (solo nombres de variables y parámetros).

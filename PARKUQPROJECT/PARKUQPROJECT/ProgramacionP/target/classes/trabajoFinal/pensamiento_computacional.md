# Pensamiento Computacional — ParkUQ
## Sistema de Gestión de Parqueadero Inteligente
### Universidad del Quindío — Programación I

---

## 1. Descomposición del Problema

El problema general es: *"El parqueadero opera manualmente, lo que genera errores en el control del tiempo, disponibilidad y tarifas"*.

Para abordarlo, se divide en cinco subproblemas independientes:

### Subproblema 1 — Gestión del espacio físico
**Problema:** El parqueadero tiene espacios limitados de tres tipos (carro, moto, bicicleta). Cada espacio puede estar disponible, ocupado o fuera de servicio.

**Solución computacional:** Representar cada espacio como un objeto con estado mutable. El sistema consulta la lista de espacios para encontrar uno compatible y disponible antes de permitir un ingreso.

---

### Subproblema 2 — Ciclo de vida del vehículo
**Problema:** Un vehículo entra, ocupa un espacio y eventualmente sale. En ese intervalo se debe llevar registro de quién entró, cuándo y dónde está.

**Solución computacional:** Modelar el ciclo como una máquina de estados: `DENTRO → SALIÓ`. Al ingresar se registra la hora con precisión; al salir se calcula el tiempo transcurrido automáticamente.

---

### Subproblema 3 — Cálculo económico
**Problema:** El valor a pagar depende del tipo de vehículo, el tiempo de permanencia y el perfil del conductor (puede tener descuento si es usuario autorizado).

**Solución computacional:** Separar la tarifa (costo por hora por tipo) del descuento (porcentaje según tipo de usuario). El cálculo combina ambos: `total = (horas × valorPorHora) × (1 − descuento)`.

---

### Subproblema 4 — Control de acceso por rol
**Problema:** El personal del parqueadero tiene dos roles con permisos distintos: el operador registra ingresos y salidas; el administrador configura espacios, tarifas y usuarios autorizados.

**Solución computacional:** Autenticación mediante usuario y contraseña. Una vez autenticado, el sistema expone solo las funcionalidades del rol correspondiente.

---

### Subproblema 5 — Reportes y estadísticas
**Problema:** La administración necesita conocer cuántos vehículos pasaron, cuánto dinero generó el día y cuánto tiempo permanecieron en promedio.

**Solución computacional:** Guardar un historial de salidas del día con todos los datos calculados. Los reportes se derivan procesando esa colección.

---

## 2. Reconocimiento de Patrones

Al analizar el problema se identificaron los siguientes patrones recurrentes que guían el diseño:

### Patrón 1 — Entidades con estructura común pero comportamiento diferente
**Observación:** Un carro, una moto y una bicicleta comparten placa, conductor y hora de ingreso, pero se comportan diferente en el sistema (diferente tarifa, diferentes espacios compatibles).

**Decisión:** Clase abstracta `Vehiculo` con subclases `Carro`, `Motocicleta` y `Bicicleta`. El método abstracto `getTipoDescripcion()` permite que cada subclase se identifique en reportes sin condiciones (`if/else`) en el código que las usa.

---

### Patrón 2 — Categorías cerradas de valores
**Observación:** El tipo de vehículo, el estado de un espacio y el rol del usuario son conjuntos fijos que no cambian en tiempo de ejecución.

**Decisión:** Enumeraciones (`enum`) para `TipoVehiculo`, `EstadoEspacio`, `EstadoVehiculo`, `TipoUsuario` y `RolSistema`. Esto elimina los "strings mágicos" y hace que el compilador detecte valores inválidos.

---

### Patrón 3 — Descuento variable según tipo de usuario
**Observación:** Estudiante, docente, administrativo y visitante tienen descuentos distintos, pero todos responden a la misma pregunta: *"¿cuánto descuento aplica?"*.

**Decisión:** Clase abstracta `Usuario` con método abstracto `getDescuento()`. Cada subclase devuelve su porcentaje sin que el código del parqueadero necesite saber de qué tipo es el usuario.

---

### Patrón 4 — Un objeto central que coordina todo
**Observación:** Todas las operaciones (ingresos, salidas, consultas, reportes) necesitan acceso a los mismos datos: espacios, vehículos activos, tarifas, usuarios.

**Decisión:** Clase `Parqueadero` como raíz de composición. Contiene todas las listas y ejecuta las reglas de negocio. Es el único punto de acceso a los datos del sistema.

---

### Patrón 5 — Errores predecibles del dominio
**Observación:** Hay situaciones que el sistema debe rechazar de forma controlada: placa duplicada, sin espacio disponible, vehículo no encontrado.

**Decisión:** Excepciones personalizadas que heredan de una clase base `ParkUQException`. Permiten capturar errores del dominio con mensajes claros en la interfaz de usuario.

---

## 3. Abstracción

La abstracción consiste en quedarse con lo esencial y ocultar los detalles de implementación. En ParkUQ se aplicó en tres niveles:

### Nivel 1 — Qué es un vehículo (sin importar el tipo)
El código que registra un ingreso no necesita saber si es un carro o una moto. Solo necesita: placa, tipo (para buscar espacio compatible), conductor y hora. El resto (`getTipoDescripcion()`) lo resuelve cada subclase internamente.

**Atributos omitidos de la abstracción:** color, marca, modelo, año. No son relevantes para las reglas de negocio del parqueadero.

### Nivel 2 — Qué hace un espacio (sin importar su código)
El método `estaDisponible()` oculta la lógica de comparar el estado con el enum. El método `asignarVehiculo()` oculta que se deben actualizar dos campos y el estado. Quien llama a estos métodos no necesita conocer esos detalles.

### Nivel 3 — Cómo se calcula el costo (sin importar el tipo de vehículo)
`Tarifa.calcularCosto(minutos)` encapsula la regla del mínimo de una hora y la conversión de minutos a horas. El parqueadero solo llama al método; la fórmula vive en un solo lugar.

### Nivel 4 — Cómo se genera un reporte (sin contaminar el parqueadero)
`GestorReportes` recibe el parqueadero como dependencia y centraliza toda la lógica de agregación. El parqueadero no necesita saber cómo se calcula el tiempo promedio; solo provee los datos.

---

## 4. Algoritmos

Para cada proceso principal del sistema se define: entradas, pasos ordenados, salida y casos de error.

---

### Algoritmo A — Registro de ingreso de vehículo

**Precondición:** El sistema tiene espacios configurados y tarifas cargadas.

**Entradas:**
- Objeto `Vehiculo` con placa, tipo, nombre del conductor e identificación
- Identificación del conductor (para buscar si es usuario autorizado)

**Proceso:**
```
1. Verificar si existe algún vehículo activo con la misma placa
   → Si existe: lanzar PlacaDuplicadaException("placa")

2. Buscar el primer espacio donde:
   a. tipoEspacio sea compatible con tipoVehiculo
   b. estado sea DISPONIBLE
   → Si no hay ninguno: lanzar SinEspacioDisponibleException("tipoVehiculo")

3. Marcar el espacio:
   a. espacio.setVehiculoAsignado(vehiculo)
   b. espacio.setEstado(OCUPADO)

4. Registrar datos del vehículo:
   a. vehiculo.setHoraIngreso(ahora)
   b. vehiculo.setEspacioAsignado(espacio)
   c. vehiculo.setEstado(DENTRO)

5. Agregar vehículo a la lista de vehículos activos

6. Retornar el espacio asignado (para mostrar al operador)
```

**Salida:** `EspacioParqueadero` asignado.

**Casos de error:** `PlacaDuplicadaException`, `SinEspacioDisponibleException`.

---

### Algoritmo B — Registro de salida y cálculo de tarifa

**Precondición:** El vehículo fue registrado en el ingreso.

**Entradas:** Placa del vehículo que sale.

**Proceso:**
```
1. Buscar vehículo en la lista de vehículos activos por placa
   → Si no existe: lanzar VehiculoNoEncontradoException("placa")

2. Registrar hora de salida = ahora

3. Calcular tiempo de estadía:
   minutos = diferencia en minutos entre horaIngreso y horaSalida

4. Obtener tarifa del tipo de vehículo

5. Calcular costo base:
   horasEfectivas = max(1.0, minutos / 60.0)   ← mínimo 1 hora
   tarifaBase = horasEfectivas × valorPorHora

6. Buscar usuario autorizado por identificacionConductor
   Si existe: descuento = usuario.getDescuento()
   Si no existe: descuento = 0.0

7. Calcular total:
   totalCobrado = tarifaBase × (1 − descuento)

8. Crear RegistroSalida con todos los valores calculados

9. Liberar el espacio:
   a. espacio.setVehiculoAsignado(null)
   b. espacio.setEstado(DISPONIBLE)

10. Actualizar estado del vehículo:
    vehiculo.setEstado(SALIÓ)

11. Remover vehículo de la lista de activos

12. Agregar RegistroSalida al historial del día

13. Retornar RegistroSalida (para mostrar el recibo al operador)
```

**Salida:** `RegistroSalida` con el recibo detallado.

**Casos de error:** `VehiculoNoEncontradoException`.

---

### Algoritmo C — Generación de reporte del día

**Precondición:** El historial del día contiene al menos un registro.

**Entradas:** Lista `historialDia` de objetos `RegistroSalida`.

**Proceso:**
```
1. totalVehiculos = cantidad de registros en historialDia

2. ingresosTotales = suma de totalCobrado de cada registro

3. tiempoPromedio = promedio de minutosEstadia de cada registro
   Si historialDia está vacío: tiempoPromedio = 0

4. contarPorTipo = agrupar registros por tipoVehiculo y contar cada grupo

5. vehiculosConDescuento = contar registros donde descuentoAplicado > 0

6. vehiculosSobreTiempo(limite) = filtrar vehículos ACTIVOS donde
   (ahora − horaIngreso) en minutos > limite

7. Construir y retornar el texto del reporte con todos los valores
```

**Salida:** Texto con el resumen del día.

---

### Algoritmo D — Asignación de espacio (detalle interno)

Este algoritmo es interno al método `registrarIngreso`. Se hace explícito porque es el núcleo de la lógica de disponibilidad:

```
buscarEspacioDisponible(tipoVehiculo):
  Para cada espacio en la lista de espacios:
    Si espacio.getTipoEspacio().name() == tipoVehiculo.name()
    Y  espacio.getEstado() == DISPONIBLE:
      Retornar ese espacio
  Lanzar SinEspacioDisponibleException(tipoVehiculo.name())
```

**Complejidad:** O(n) donde n es el número total de espacios. Aceptable para el tamaño real de un parqueadero universitario.

---

## 5. Resumen de decisiones de diseño

| Concepto OOP | Aplicación en ParkUQ |
|---|---|
| **Herencia** | `Vehiculo` → `Carro`, `Motocicleta`, `Bicicleta`; `Usuario` → `Estudiante`, `Docente`, `Administrativo`, `Visitante`; `ParkUQException` → subexcepciones |
| **Polimorfismo** | `getTipoDescripcion()` en vehículos; `getDescuento()` en usuarios |
| **Encapsulamiento** | Todos los atributos `private` con getters/setters; lógica interna en `Parqueadero` no expuesta |
| **Abstracción** | Clases abstractas `Vehiculo` y `Usuario`; interfaz uniforme independiente del subtipo |
| **Composición** | `Parqueadero` contiene espacios, vehículos, tarifas, usuarios y registros |
| **Excepciones** | `ParkUQException` y tres subclases para errores del dominio |

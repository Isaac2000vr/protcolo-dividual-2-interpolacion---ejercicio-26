# Ejercicio 26 - Buscador por interpolación

**Asignatura:** Estructuras de datos  
**Programa:** Ingeniería del software  
**Tutor:** John Carlos Arrieta Arrieta  
**Universidad:** IES Universidad de Cartagena

---

## Descripción del ejercicio

Generar un arreglo ordenado de valores numéricos aproximadamente uniformes e implementar tres algoritmos de búsqueda:

1. **Búsqueda lineal** — recorre elemento por elemento (O(n)), no requiere ordenamiento.
2. **Búsqueda binaria** — divide y vencerás sobre datos ordenados (O(log n)).
3. **Búsqueda por interpolación** — estima la posición del valor mediante interpolación lineal (O(log log n) promedio en datos uniformes; O(n) en el peor caso).

Se ejecutan pruebas sobre:
- **Datos uniformemente distribuidos.**
- **Datos con distribución agrupada (no uniforme).**

Para cada caso se buscan valores ubicados al inicio, centro, final y valores inexistentes, registrando el índice encontrado, la cantidad de comparaciones y el tiempo de ejecución.

Finalmente se explica **cómo la distribución de los datos afecta al algoritmo de interpolación**.

---

## Estructura del proyecto

```
ejercicio26-interpolacion/
├── README.md
└── src/
    └── InterpolationSearchDemo.java
```

---

## Requisitos

- **Java 8 o superior** (JDK instalado).
- No se requieren librerías externas (solo `java.util.Arrays` y `java.util.Random` de la JDK).

---

## Cómo compilar y ejecutar

Desde la carpeta raíz del proyecto (`ejercicio26-interpolacion`):

```bash
# Compilar
javac src/InterpolationSearchDemo.java -d out

# Ejecutar
java -cp out InterpolationSearchDemo
```

Si no usas carpeta de salida:

```bash
javac src/InterpolationSearchDemo.java
java -cp src InterpolationSearchDemo
```

---

## Salida esperada

El programa imprime dos bloques de resultados:

1. **PRUEBA A - Datos uniformemente distribuidos:** compara los tres algoritmos sobre un arreglo ordenado con valores uniformes.
2. **PRUEBA B - Datos agrupados / distribución no uniforme:** repite las pruebas sobre un arreglo con valores concentrados en un rango reducido.

Para cada bloque se muestra, por algoritmo y valor buscado:
- Índice encontrado (`-1` si no existe).
- Cantidad de comparaciones realizadas.
- Tiempo de ejecución en nanosegundos.
- Si fue encontrado o no.

Al final se imprime una conclusión sobre el efecto de la distribución en la búsqueda por interpolación.

---

## Conclusión (respuesta a la pregunta del ejercicio)

- Con **datos uniformemente distribuidos**, la búsqueda por interpolación estima muy bien la posición del objetivo y generalmente requiere menos comparaciones que la búsqueda binaria (complejidad promedio cercana a **O(log log n)**).
- Con **datos agrupados o no uniformes**, la estimación de posición es menos precisa; el algoritmo puede degradarse hacia **O(n)** en el peor caso y realizar más comparaciones que la binaria.
- La **búsqueda lineal** es independiente de la distribución (siempre O(n)).
- La **búsqueda binaria** también es independiente de la distribución sobre datos ordenados (siempre O(log n)).

Por tanto, la búsqueda por interpolación es la más eficiente solo cuando se cumple la suposición de distribución aproximadamente uniforme de los valores.

---

## Entrega de la actividad (según instrucciones del taller)

1. **Repositorio público de GitHub:**
   - Crear un repositorio específico para la actividad.
   - Desarrollar en una rama de trabajo (ej. `desarrollo-ejercicio-26`).
   - Hacer commits periódicos con mensajes claros (`feat: ...`, `fix: ...`, `docs: ...`).
   - Hacer push al repositorio remoto y finalmente integrar a `main`.

2. **Video de sustentación:** grabar un video presentando el ejercicio, explicando el código, mostrando la ejecución y los casos de prueba, y mostrando el historial de commits del repositorio.

3. **Documento PDF:** desarrollo de la actividad (código, capturas de ejecución, tabla de comparaciones, respuestas teóricas).

4. **Forma de entrega:** enviar por SIMA-PESAD el enlace al repositorio público, el enlace al video y el documento PDF.
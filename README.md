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
- **Datos uniformemente distribuidos:** cada valor avanza un paso cercano a 10.
- **Datos con distribución agrupada (no uniforme):** la mitad de los valores se concentra entre 0 y 99 y la otra mitad entre 100000 y 100099, con un salto enorme entre ambos grupos.

Para cada caso se buscan valores ubicados al inicio, centro y final de cada grupo, además de valores inexistentes. Se registra el índice encontrado, la cantidad de comparaciones, el tiempo promedio y el **rango completo de ocurrencias**, porque los datos agrupados contienen valores repetidos.

La métrica de comparaciones cuenta únicamente las comparaciones entre el valor buscado y un elemento del arreglo, de modo que los conteos de los tres algoritmos sean comparables entre sí. Los tiempos son el promedio de 200 ejecuciones después de un calentamiento de la JVM.

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
javac -encoding UTF-8 src/InterpolationSearchDemo.java -d out

# Ejecutar
java -cp out InterpolationSearchDemo
```

Si no usas carpeta de salida:

```bash
javac -encoding UTF-8 src/InterpolationSearchDemo.java
java -cp src InterpolationSearchDemo
```

En Windows, si los acentos no se ven bien en la consola, ejecuta antes
`chcp 65001` o usa una terminal con UTF-8.

---

## Salida esperada

El programa imprime dos bloques de resultados:

1. **PRUEBA A - Datos uniformemente distribuidos:** compara los tres algoritmos sobre un arreglo ordenado con paso casi constante.
2. **PRUEBA B - Datos agrupados / distribución no uniforme:** repite las pruebas sobre dos grupos densos separados por un salto de 100000.

Para cada bloque se muestra, por algoritmo y valor buscado:
- Índice encontrado (`-1` si no existe).
- Cantidad de comparaciones entre el objetivo y un elemento del arreglo.
- Tiempo promedio en nanosegundos.
- Si fue encontrado o no.
- Cantidad de ocurrencias y rango de índices donde aparece el valor.

Al final se valida el comportamiento con:
- un arreglo desordenado (la binaria y la interpolación deben rechazarlo),
- un arreglo nulo (los tres algoritmos deben rechazarlo),
- un arreglo vacío (los tres deben devolver `-1`).

Resultados representativos obtenidos:

| Caso | Algoritmo | Comparaciones |
|---|---|---|
| Uniforme, valor central | Binaria | 19 |
| Uniforme, valor central | Interpolación | 5 |
| Agrupado, centro del grupo alto | Binaria | 3 |
| Agrupado, centro del grupo alto | Interpolación | 501 |
| Agrupado, inicio del grupo alto | Binaria | 15 |
| Agrupado, inicio del grupo alto | Interpolación | 869 |

La diferencia en el caso agrupado es la evidencia experimental de la degradación: la estimación lineal supone un crecimiento constante que esos datos no cumplen.

---

## Conclusión (respuesta a la pregunta del ejercicio)

- Con **datos uniformemente distribuidos**, la búsqueda por interpolación estima muy bien la posición del objetivo y requiere menos comparaciones que la búsqueda binaria (complejidad promedio cercana a **O(log log n)**). En las pruebas, la interpolación necesitó entre 3 y 5 comparaciones donde la binaria necesitó entre 19 y 22.
- Con **datos agrupados o no uniformes**, la estimación de posición es muy imprecisa; el algoritmo se degrada hacia **O(n)** en el peor caso y realiza muchas más comparaciones que la binaria. En el grupo alto de la PRUEBA B, la interpolación necesitó entre 501 y 875 comparaciones donde la binaria necesitó entre 3 y 20.
- La **búsqueda lineal** es independiente de la distribución (siempre O(n)).
- La **búsqueda binaria** también es independiente de la distribución sobre datos ordenados (siempre O(log n)).
- Cuando hay **valores repetidos**, los tres algoritmos pueden devolver un índice distinto pero válido. Por eso el programa informa la primera y la última ocurrencia de cada valor.

Por tanto, la búsqueda por interpolación es la más eficiente solo cuando se cumple la suposición de distribución aproximadamente uniforme de los valores. También exige que el arreglo esté ordenado; por eso la implementación valida esa precondición antes de buscar.

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
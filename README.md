# Ejercicio 26 - Buscador por Interpolación

Solución al ejercicio 26 del taller práctico de Algoritmos de Búsqueda para la asignatura de Estructuras de Datos (Universidad de Cartagena).

## Descripción del Ejercicio

El proyecto implementa y compara tres algoritmos de búsqueda sobre arreglos ordenados:
1. **Búsqueda Lineal:** Recorre la estructura elemento por elemento hasta encontrar el valor o llegar al final.
2. **Búsqueda Binaria:** Aplica la estrategia de divide y vencerás dividiendo el rango de búsqueda a la mitad.
3. **Búsqueda por Interpolación:** Estima la posición del elemento objetivo utilizando una fórmula basada en los valores de los extremos del arreglo.

Para evaluar el comportamiento de la búsqueda por interpolación, el programa realiza pruebas sobre dos tipos de distribuciones de datos:
* **Datos uniformemente distribuidos:** Elementos con incrementos o pasos aproximadamente constantes.
* **Datos no uniformes o agrupados:** Elementos concentrados en rangos específicos.

## Estructura del Código

Toda la solución se encuentra en la clase `InterpolationSearchDemo.java` dentro de la carpeta `src/`:

* `linearSearch`: Método para realizar la búsqueda lineal.
* `binarySearchIter`: Método iterativo para la búsqueda binaria.
* `interpolationSearch`: Método para la búsqueda por interpolación con control de límites y división por cero.
* `generateUniform` y `generateClustered`: Funciones para generar los arreglos de prueba.
* `SearchResult`: Clase para empaquetar el índice, las comparaciones realizadas y el tiempo de ejecución.

## Conclusiones

* **En datos uniformes:** La búsqueda por interpolación es muy eficiente y encuentra los elementos en pocas comparaciones, logrando un rendimiento cercano a O(log log n).
* **En datos no uniformes:** La estimación de posición pierde precisión, incrementando las comparaciones y pudiendo degradarse hasta un caso de O(n).
* **Búsqueda binaria:** Mantiene un rendimiento constante de O(log n) sobre datos ordenados, independientemente de cómo estén distribuidos.
import java.util.Arrays;
import java.util.Random;

/**
 * Ejercicio 26 - Buscador por interpolación
 * ---------------------------------------------------------------
 * Genera un arreglo ordenado de valores numéricos aproximadamente
 * uniformes e implementa y compara tres algoritmos de búsqueda:
 *
 *  1. Búsqueda lineal (O(n)) - no requiere ordenamiento.
 *  2. Búsqueda binaria iterativa (O(log n)) - requiere datos ordenados.
 *  3. Búsqueda por interpolación (O(log log n) promedio en datos
 *     uniformes; O(n) en el peor caso) - requiere datos ordenados y
 *     funciona mejor cuando los valores están distribuidos de forma
 *     aproximadamente uniforme.
 *
 * Se prueban los algoritmos con:
 *  a) Datos uniformemente distribuidos.
 *  b) Datos con distribución agrupada (no uniforme).
 *
 * Para cada prueba se registran: índice encontrado, cantidad de
 * comparaciones y tiempo de ejecución. Además se incluye un valor
 * inexistente para validar el comportamiento cuando el elemento no
 * está presente.
 *
 * Autor: Estudiante - Estructuras de datos (Unidad 2)
 */
public class InterpolationSearchDemo {

    /**
     * Resultado de una búsqueda. Empaqueta el índice encontrado, la cantidad
     * de comparaciones realizadas, el tiempo transcurrido y si se encontró.
     */
    static class SearchResult {
        String algorithm;
        int index;           // índice encontrado o -1 si no existe
        long comparisons;    // cantidad de comparaciones realizadas
        long nanos;          // tiempo de ejecución en nanosegundos
        boolean found;

        SearchResult(String algorithm, int index, long comparisons,
                     long nanos, boolean found) {
            this.algorithm = algorithm;
            this.index = index;
            this.comparisons = comparisons;
            this.nanos = nanos;
            this.found = found;
        }
    }

    // ------------------------------------------------------------------
    // 1. BÚSQUEDA LINEAL
    // ------------------------------------------------------------------
    static SearchResult linearSearch(int[] a, int target) {
        long comps = 0;
        long start = System.nanoTime();
        for (int i = 0; i < a.length; i++) {
            comps++;
            if (a[i] == target) {
                return new SearchResult("Lineal", i, comps,
                        System.nanoTime() - start, true);
            }
        }
        return new SearchResult("Lineal", -1, comps,
                System.nanoTime() - start, false);
    }

    // ------------------------------------------------------------------
    // 2. BÚSQUEDA BINARIA (iterativa)
    // Precondición: el arreglo debe estar ordenado de forma no decreciente.
    // ------------------------------------------------------------------
    static SearchResult binarySearchIter(int[] a, int target) {
        long comps = 0;
        long start = System.nanoTime();
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            int mid = lo + ((hi - lo) >>> 1); // evita overflow
            comps++; // comparar a[mid] == target
            if (a[mid] == target) {
                return new SearchResult("Binaria", mid, comps,
                        System.nanoTime() - start, true);
            }
            comps++; // comparar a[mid] < target
            if (a[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return new SearchResult("Binaria", -1, comps,
                System.nanoTime() - start, false);
    }

    // ------------------------------------------------------------------
    // 3. BÚSQUEDA POR INTERPOLACIÓN
    // Precondición: el arreglo debe estar ordenado. Funciona mejor cuando
    // los valores están distribuidos de forma aproximadamente uniforme.
    // Fórmula de interpolación:
    //   pos = lo + ((target - a[lo]) * (hi - lo)) / (a[hi] - a[lo])
    // ------------------------------------------------------------------
    static SearchResult interpolationSearch(int[] a, int target) {
        long comps = 0;
        long start = System.nanoTime();
        int lo = 0, hi = a.length - 1;

        while (lo <= hi && target >= a[lo] && target <= a[hi]) {
            comps++; // comparar que target está dentro del rango [a[lo], a[hi]]

            // Caso base: un solo elemento
            if (lo == hi) {
                comps++; // comparar a[lo] == target
                if (a[lo] == target) {
                    return new SearchResult("Interpolación", lo, comps,
                            System.nanoTime() - start, true);
                }
                return new SearchResult("Interpolación", -1, comps,
                        System.nanoTime() - start, false);
            }

            // Evitar división por cero cuando todos los valores del rango son iguales
            if (a[hi] == a[lo]) {
                comps++; // comparar a[lo] == target
                if (a[lo] == target) {
                    return new SearchResult("Interpolación", lo, comps,
                            System.nanoTime() - start, true);
                }
                return new SearchResult("Interpolación", -1, comps,
                        System.nanoTime() - start, false);
            }

            // Fórmula de interpolación para estimar la posición
            comps++; // cálculo de la posición estimada
            int pos = lo + ((target - a[lo]) * (hi - lo)) / (a[hi] - a[lo]);

            // Seguridad: si la estimación sale del rango, usar punto medio
            if (pos < lo || pos > hi) {
                pos = lo + (hi - lo) / 2;
            }

            comps++; // comparar a[pos] == target
            if (a[pos] == target) {
                return new SearchResult("Interpolación", pos, comps,
                        System.nanoTime() - start, true);
            }
            comps++; // comparar a[pos] < target
            if (a[pos] < target) {
                lo = pos + 1;
            } else {
                hi = pos - 1;
            }
        }

        return new SearchResult("Interpolación", -1, comps,
                System.nanoTime() - start, false);
    }

    // ------------------------------------------------------------------
    // Generadores de datos
    // ------------------------------------------------------------------

    /**
     * Genera un arreglo ordenado de tamaño `size` con valores
     * aproximadamente uniformemente distribuidos.
     */
    static int[] generateUniform(int size, long seed) {
        Random r = new Random(seed);
        int[] a = new int[size];
        for (int i = 0; i < size; i++) {
            // paso base de 10 + pequeña variación aleatoria -> distribución uniforme
            a[i] = i * 10 + r.nextInt(5);
        }
        Arrays.sort(a);
        return a;
    }

    /**
     * Genera un arreglo ordenado de tamaño `size` con valores concentrados
     * en un rango reducido (distribución no uniforme / agrupada).
     */
    static int[] generateClustered(int size, long seed) {
        Random r = new Random(seed);
        int[] a = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = r.nextInt(100); // valores concentrados entre 0 y 99
        }
        Arrays.sort(a);
        return a;
    }

    // ------------------------------------------------------------------
    // Utilidad para imprimir una fila de resultados
    // ------------------------------------------------------------------
    static void printRow(String algorithm, SearchResult res) {
        System.out.printf("%-14s %-8s %-16s %-14s %-10s%n",
                algorithm, res.index, res.comparisons, res.nanos,
                res.found ? "SÍ" : "NO");
    }

    // ------------------------------------------------------------------
    // Ejecuta los tres algoritmos sobre un arreglo y un conjunto de objetivos
    // ------------------------------------------------------------------
    static void runSuite(String title, int[] data, int[] targets) {
        System.out.println("\n=== " + title + " (size=" + data.length + ") ===");
        System.out.printf("%-14s %-8s %-16s %-14s %-10s%n",
                "Algoritmo", "Índice", "Comparaciones", "Tiempo (ns)", "Encontrado");
        System.out.println("---------------------------------------------------------------");

        for (int target : targets) {
            System.out.println("\n--> Buscando valor: " + target);
            SearchResult r1 = linearSearch(data, target);
            SearchResult r2 = binarySearchIter(data, target);
            SearchResult r3 = interpolationSearch(data, target);

            printRow("Lineal", r1);
            printRow("Binaria", r2);
            printRow("Interpolación", r3);
        }
    }

    // ------------------------------------------------------------------
    // MAIN
    // ------------------------------------------------------------------
    public static void main(String[] args) {
        int size = 2000;

        System.out.println("============================================================");
        System.out.println("  EJERCICIO 26 - BUSCADOR POR INTERPOLACIÓN");
        System.out.println("  Estructuras de datos - Algoritmos de búsqueda");
        System.out.println("============================================================");

        // ----------------------------------------------------------
        // PARTE A: Datos uniformemente distribuidos
        // ----------------------------------------------------------
        int[] uniform = generateUniform(size, 42L);
        int[] uniformTargets = {
                uniform[0],                    // valor al inicio
                uniform[size / 2],             // valor al centro
                uniform[size - 1],             // valor al final
                -1,                            // valor inexistente (menor que todos)
                uniform[size / 2] + 99999      // valor inexistente (mayor que todos)
        };

        runSuite("PRUEBA A - Datos uniformemente distribuidos", uniform, uniformTargets);

        // ----------------------------------------------------------
        // PARTE B: Datos con distribución agrupada (no uniforme)
        // ----------------------------------------------------------
        int[] clustered = generateClustered(size, 7L);
        int[] clusteredTargets = {
                clustered[0],
                clustered[size / 2],
                clustered[size - 1],
                -1,
                99999
        };

        runSuite("PRUEBA B - Datos agrupados / distribución no uniforme", clustered, clusteredTargets);

        // ----------------------------------------------------------
        // CONCLUSIÓN impresa
        // ----------------------------------------------------------
        System.out.println("\n============================================================");
        System.out.println("  CONCLUSIÓN SOBRE LA DISTRIBUCIÓN Y LA BÚSQUEDA POR");
        System.out.println("  INTERPOLACIÓN");
        System.out.println("============================================================");
        System.out.println("• Con datos uniformemente distribuidos, la búsqueda por");
        System.out.println("  interpolación estima bien la posición y suele requerir");
        System.out.println("  menos comparaciones que la binaria (cerca de O(log log n)).");
        System.out.println("• Con datos agrupados o no uniformes, la estimación de");
        System.out.println("  posición es menos precisa y el algoritmo puede degradarse");
        System.out.println("  hacia O(n) en el peor caso, realizando más comparaciones");
        System.out.println("  que la búsqueda binaria.");
        System.out.println("• La búsqueda lineal no depende de la distribución y siempre");
        System.out.println("  realiza como máximo n comparaciones (O(n)).");
        System.out.println("• La búsqueda binaria es independiente de la distribución");
        System.out.println("  y siempre es O(log n) sobre datos ordenados.");
        System.out.println("============================================================");
    }
}

import java.util.Arrays;
import java.util.Random;

/**
 * Ejercicio 26 - Buscador por interpolación
 * ---------------------------------------------------------------
 * Genera arreglos ordenados con dos distribuciones distintas e
 * implementa y compara tres algoritmos de búsqueda:
 *
 *  1. Búsqueda lineal (O(n)) - no requiere ordenamiento.
 *  2. Búsqueda binaria iterativa (O(log n)) - requiere datos ordenados.
 *  3. Búsqueda por interpolación (O(log log n) promedio en datos
 *     uniformes; O(n) en el peor caso) - requiere datos ordenados y
 *     funciona mejor cuando los valores están distribuidos de forma
 *     aproximadamente uniforme.
 *
 * Distribuciones probadas:
 *  a) Uniforme: valores separados por un paso casi constante.
 *  b) Agrupada (no uniforme): dos grupos densos separados por un
 *     salto enorme, para que la estimación lineal falle.
 *
 * Métrica de comparaciones: se cuenta únicamente cada comparación
 * entre el valor buscado y un elemento del arreglo. Así los conteos
 * de los tres algoritmos son comparables entre sí.
 *
 * Para cada búsqueda se reporta el índice, las comparaciones, el
 * tiempo promedio y el rango completo de ocurrencias (los datos
 * agrupados contienen valores repetidos).
 *
 * Autor: Estudiante - Estructuras de datos (Unidad 2)
 */
public class InterpolationSearchDemo {

    /** Repeticiones para promediar el tiempo de cada búsqueda. */
    private static final int REPETICIONES = 200;

    @FunctionalInterface
    interface AlgoritmoBusqueda {
        SearchResult buscar(int[] arreglo, int objetivo);
    }

    /**
     * Resultado de una búsqueda. Empaqueta el índice encontrado, la cantidad
     * de comparaciones realizadas, el tiempo transcurrido y si se encontró.
     */
    static class SearchResult {
        String algorithm;
        int index;           // índice encontrado o -1 si no existe
        long comparisons;    // comparaciones entre el objetivo y un elemento
        long nanos;          // tiempo promedio de ejecución en nanosegundos
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
        validarArregloNoNulo(a);
        long comps = 0;
        long start = System.nanoTime();
        for (int i = 0; i < a.length; i++) {
            comps++; // comparar a[i] == target
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
        validarArregloOrdenado(a);
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
        validarArregloOrdenado(a);
        long comps = 0;
        long start = System.nanoTime();

        if (a.length == 0) {
            return new SearchResult("Interpolación", -1, comps,
                    System.nanoTime() - start, false);
        }

        // Descarte rápido si el objetivo está fuera del rango global.
        comps++; // comparar target < a[0]
        if (target < a[0]) {
            return new SearchResult("Interpolación", -1, comps,
                    System.nanoTime() - start, false);
        }
        comps++; // comparar target > a[n-1]
        if (target > a[a.length - 1]) {
            return new SearchResult("Interpolación", -1, comps,
                    System.nanoTime() - start, false);
        }

        int lo = 0, hi = a.length - 1;

        while (lo <= hi) {
            // Si todo el rango tiene el mismo valor, solo puede ser target
            // o no existir. Esta comparación no es contra el objetivo.
            if (a[hi] == a[lo]) {
                comps++; // comparar a[lo] == target
                if (a[lo] == target) {
                    return new SearchResult("Interpolación", lo, comps,
                            System.nanoTime() - start, true);
                }
                return new SearchResult("Interpolación", -1, comps,
                        System.nanoTime() - start, false);
            }

            // Se usa aritmética long para evitar desbordamientos con valores
            // grandes. Si la posición estimada sale del rango actual, el
            // objetivo no está dentro de este segmento.
            long pos = lo + (((long) target - a[lo]) * (hi - lo))
                    / ((long) a[hi] - a[lo]);
            if (pos < lo || pos > hi) {
                return new SearchResult("Interpolación", -1, comps,
                        System.nanoTime() - start, false);
            }

            int indice = (int) pos;
            comps++; // comparar a[indice] == target
            if (a[indice] == target) {
                return new SearchResult("Interpolación", indice, comps,
                        System.nanoTime() - start, true);
            }
            comps++; // comparar a[indice] < target
            if (a[indice] < target) {
                lo = indice + 1;
            } else {
                hi = indice - 1;
            }
        }

        return new SearchResult("Interpolación", -1, comps,
                System.nanoTime() - start, false);
    }

    // ------------------------------------------------------------------
    // Precondiciones
    // ------------------------------------------------------------------
    static void validarArregloNoNulo(int[] a) {
        if (a == null) {
            throw new IllegalArgumentException("El arreglo no puede ser nulo.");
        }
    }

    static void validarArregloOrdenado(int[] a) {
        validarArregloNoNulo(a);
        for (int i = 1; i < a.length; i++) {
            if (a[i - 1] > a[i]) {
                throw new IllegalArgumentException(
                        "El arreglo debe estar ordenado de forma no decreciente "
                                + "(falla entre las posiciones " + (i - 1)
                                + " y " + i + ").");
            }
        }
    }

    // ------------------------------------------------------------------
    // Generadores de datos
    // ------------------------------------------------------------------

    /**
     * Genera un arreglo ordenado de tamaño {@code size} con valores
     * aproximadamente uniformes: cada valor avanza un paso cercano a 10.
     */
    static int[] generateUniform(int size, long seed) {
        Random r = new Random(seed);
        int[] a = new int[size];
        for (int i = 0; i < size; i++) {
            a[i] = i * 10 + r.nextInt(5);
        }
        Arrays.sort(a);
        return a;
    }

    /**
     * Genera un arreglo ordenado con una distribución realmente no uniforme:
     * la mitad de los valores se concentra entre 0 y 99, y la otra mitad
     * entre 100000 y 100099. El salto enorme entre ambos grupos hace que la
     * estimación lineal de la interpolación sea muy imprecisa.
     */
    static int[] generateClustered(int size, long seed) {
        Random r = new Random(seed);
        int[] a = new int[size];
        int mitad = size / 2;
        for (int i = 0; i < mitad; i++) {
            a[i] = r.nextInt(100);              // grupo bajo: 0..99
        }
        for (int i = mitad; i < size; i++) {
            a[i] = 100_000 + r.nextInt(100);    // grupo alto: 100000..100099
        }
        Arrays.sort(a);
        return a;
    }

    // ------------------------------------------------------------------
    // Búsqueda del rango completo de ocurrencias (para valores repetidos)
    // ------------------------------------------------------------------
    static int primeraOcurrencia(int[] a, int target) {
        validarArregloOrdenado(a);
        int lo = 0, hi = a.length - 1, resultado = -1;
        while (lo <= hi) {
            int mid = lo + ((hi - lo) >>> 1);
            if (a[mid] == target) {
                resultado = mid;
                hi = mid - 1;       // seguir buscando a la izquierda
            } else if (a[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return resultado;
    }

    static int ultimaOcurrencia(int[] a, int target) {
        validarArregloOrdenado(a);
        int lo = 0, hi = a.length - 1, resultado = -1;
        while (lo <= hi) {
            int mid = lo + ((hi - lo) >>> 1);
            if (a[mid] == target) {
                resultado = mid;
                lo = mid + 1;       // seguir buscando a la derecha
            } else if (a[mid] < target) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return resultado;
    }

    // ------------------------------------------------------------------
    // Medición y salida
    // ------------------------------------------------------------------

    /**
     * Ejecuta una búsqueda varias veces para promediar el tiempo. El conteo
     * de comparaciones es determinista, así que se conserva el del último
     * resultado.
     */
    static SearchResult medir(AlgoritmoBusqueda algoritmo, int[] datos, int objetivo) {
        algoritmo.buscar(datos, objetivo); // calentamiento de la JVM

        SearchResult ultimo = null;
        long totalNanos = 0;
        for (int i = 0; i < REPETICIONES; i++) {
            ultimo = algoritmo.buscar(datos, objetivo);
            totalNanos += ultimo.nanos;
        }
        return new SearchResult(ultimo.algorithm, ultimo.index, ultimo.comparisons,
                totalNanos / REPETICIONES, ultimo.found);
    }

    static void printRow(String algorithm, SearchResult res) {
        System.out.printf("%-14s %-8s %-16s %-14s %-10s%n",
                algorithm, res.index, res.comparisons, res.nanos,
                res.found ? "SÍ" : "NO");
    }

    static void imprimirOcurrencias(int[] data, int target) {
        int primera = primeraOcurrencia(data, target);
        if (primera == -1) {
            System.out.println("   Ocurrencias: ninguna");
            return;
        }
        int ultima = ultimaOcurrencia(data, target);
        System.out.println("   Ocurrencias: " + (ultima - primera + 1)
                + " (índices " + primera + " a " + ultima + ")");
    }

    // ------------------------------------------------------------------
    // Ejecuta los tres algoritmos sobre un arreglo y un conjunto de objetivos
    // ------------------------------------------------------------------
    static void runSuite(String title, String description, int[] data, int[] targets) {
        validarArregloOrdenado(data);

        System.out.println("\n=== " + title + " (size=" + data.length + ") ===");
        System.out.println(description);
        System.out.printf("%-14s %-8s %-16s %-14s %-10s%n",
                "Algoritmo", "Índice", "Comparaciones", "Tiempo (ns)", "Encontrado");
        System.out.println("---------------------------------------------------------------");

        for (int target : targets) {
            System.out.println("\n--> Buscando valor: " + target);
            SearchResult r1 = medir(InterpolationSearchDemo::linearSearch, data, target);
            SearchResult r2 = medir(InterpolationSearchDemo::binarySearchIter, data, target);
            SearchResult r3 = medir(InterpolationSearchDemo::interpolationSearch, data, target);

            printRow("Lineal", r1);
            printRow("Binaria", r2);
            printRow("Interpolación", r3);
            imprimirOcurrencias(data, target);
        }
    }

    // ------------------------------------------------------------------
    // Demostración de precondiciones
    // ------------------------------------------------------------------
    static void demostrarPrecondiciones() {
        System.out.println("\n============================================================");
        System.out.println("  VALIDACIÓN DE PRECONDICIONES");
        System.out.println("============================================================");

        int[] desordenado = {50, 10, 40, 20, 30};
        try {
            binarySearchIter(desordenado, 20);
            System.out.println("ERROR: la binaria aceptó un arreglo desordenado.");
        } catch (IllegalArgumentException e) {
            System.out.println("Binaria rechaza datos desordenados: " + e.getMessage());
        }

        try {
            interpolationSearch(desordenado, 20);
            System.out.println("ERROR: la interpolación aceptó un arreglo desordenado.");
        } catch (IllegalArgumentException e) {
            System.out.println("Interpolación rechaza datos desordenados: " + e.getMessage());
        }

        try {
            linearSearch(null, 20);
            System.out.println("ERROR: la lineal aceptó un arreglo nulo.");
        } catch (IllegalArgumentException e) {
            System.out.println("Los tres algoritmos rechazan un arreglo nulo: "
                    + e.getMessage());
        }

        int[] vacio = {};
        System.out.println("Arreglo vacío -> lineal: " + linearSearch(vacio, 5).index
                + ", binaria: " + binarySearchIter(vacio, 5).index
                + ", interpolación: " + interpolationSearch(vacio, 5).index);
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
        System.out.println("Comparaciones = comparaciones entre el objetivo y un elemento.");
        System.out.println("Tiempos = promedio de " + REPETICIONES + " ejecuciones.");

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

        runSuite("PRUEBA A - Datos uniformemente distribuidos",
                "Paso casi constante: la interpolación debería estimar bien.",
                uniform, uniformTargets);

        // ----------------------------------------------------------
        // PARTE B: Datos con distribución agrupada (no uniforme)
        // ----------------------------------------------------------
        int[] clustered = generateClustered(size, 7L);
        int[] clusteredTargets = {
                clustered[0],                  // inicio del grupo bajo
                clustered[size / 4],           // centro del grupo bajo
                clustered[size / 2],           // inicio del grupo alto
                clustered[3 * size / 4],       // centro del grupo alto
                clustered[size - 1],           // final del grupo alto
                -1,                            // valor inexistente (menor que todos)
                99_999                         // valor inexistente entre los grupos
        };

        runSuite("PRUEBA B - Datos agrupados / distribución no uniforme",
                "Dos grupos densos separados por un salto de 100000: "
                        + "la estimación lineal falla.",
                clustered, clusteredTargets);

        demostrarPrecondiciones();

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
        System.out.println("  posición es muy imprecisa y el algoritmo se degrada");
        System.out.println("  hacia O(n) en el peor caso, realizando muchas más");
        System.out.println("  comparaciones que la búsqueda binaria.");
        System.out.println("• La búsqueda lineal no depende de la distribución y siempre");
        System.out.println("  realiza como máximo n comparaciones (O(n)).");
        System.out.println("• La búsqueda binaria es independiente de la distribución");
        System.out.println("  y siempre es O(log n) sobre datos ordenados.");
        System.out.println("• Los datos agrupados contienen valores repetidos; por eso");
        System.out.println("  se informa el rango completo de ocurrencias.");
        System.out.println("============================================================");
    }
}

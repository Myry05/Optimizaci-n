import java.util.Random;

public class OptimizacionParametros {

    static class Individuo {
        double x;
        double fitness;

        Individuo(double x) {
            this.x = x;
            this.fitness = evaluarFitness();
        }

        double evaluarFitness() {
            return Math.pow(x, 2) + 5 * x + 6; // Función a minimizar
        }
    }

    static class AlgoritmoGenetico {
        static final int TAMANO_POBLACION = 50;
        static final double PROBABILIDAD_MUTACION = 0.1;
        static final int NUM_GENERACIONES = 1000;
        static final double RANGO_X_MIN = -10;
        static final double RANGO_X_MAX = 10;
        static final double RANGO_MUTACION_MIN = -1;
        static final double RANGO_MUTACION_MAX = 1;

        Random random = new Random();

        Individuo[] generarPoblacionInicial() {
            Individuo[] poblacion = new Individuo[TAMANO_POBLACION];
            for (int i = 0; i < TAMANO_POBLACION; i++) {
                double x = RANGO_X_MIN + (RANGO_X_MAX - RANGO_X_MIN) * random.nextDouble();
                poblacion[i] = new Individuo(x);
            }
            return poblacion;
        }

        Individuo seleccionTorneo(Individuo[] poblacion) {
            Individuo mejorIndividuo = poblacion[random.nextInt(poblacion.length)];
            for (int i = 0; i < 3; i++) {
                Individuo individuoAleatorio = poblacion[random.nextInt(poblacion.length)];
                if (individuoAleatorio.fitness < mejorIndividuo.fitness) {
                    mejorIndividuo = individuoAleatorio;
                }
            }
            return mejorIndividuo;
        }

        Individuo[] reproducir(Individuo padre1, Individuo padre2) {
            double puntoCorte = random.nextDouble();
            double xHijo1 = puntoCorte * padre1.x + (1 - puntoCorte) * padre2.x;
            double xHijo2 = (1 - puntoCorte) * padre1.x + puntoCorte * padre2.x;
            return new Individuo[]{new Individuo(xHijo1), new Individuo(xHijo2)};
        }

        void mutar(Individuo individuo) {
            if (random.nextDouble() < PROBABILIDAD_MUTACION) {
                // Mutación gaussiana
                // individuo.x += random.nextGaussian();
                
                // Mutación uniforme
                double mutacion = RANGO_MUTACION_MIN + (RANGO_MUTACION_MAX - RANGO_MUTACION_MIN) * random.nextDouble();
                individuo.x += mutacion;
            }
        }

        Individuo optimizar() {
            Individuo[] poblacion = generarPoblacionInicial();
            for (int generacion = 0; generacion < NUM_GENERACIONES; generacion++) {
                Individuo[] nuevaGeneracion = new Individuo[TAMANO_POBLACION];
                for (int i = 0; i < TAMANO_POBLACION; i++) {
                    Individuo padre1 = seleccionTorneo(poblacion);
                    Individuo padre2 = seleccionTorneo(poblacion);
                    Individuo[] hijos = reproducir(padre1, padre2);
                    nuevaGeneracion[i] = hijos[0];
                    mutar(nuevaGeneracion[i]);
                }
                poblacion = nuevaGeneracion;
            }

            Individuo mejorIndividuo = poblacion[0];
            for (Individuo individuo : poblacion) {
                if (individuo.fitness < mejorIndividuo.fitness) {
                    mejorIndividuo = individuo;
                }
            }
            return mejorIndividuo;
        }
    }

    public static void main(String[] args) {
        AlgoritmoGenetico algoritmo = new AlgoritmoGenetico();
        Individuo mejorSolucion = algoritmo.optimizar();
        System.out.println("La solución encontrada es: x = " + mejorSolucion.x + ", f(x) = " + mejorSolucion.fitness);
    }
}

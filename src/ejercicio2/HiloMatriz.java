package ejercicio2;

import java.util.Random;

/**
 * Clase que genera y suma las matrices, y en función del
 * tamaño, imprime en un panel u otro la solución obtenida
 * de la suma de matrices.
 * @author javib
 */
public class HiloMatriz implements Runnable{
	
	public static final int TAMMATRIX = 10;	//tamaño máximo de la matriz
	
	private int id;
	private Panel[] paneles;
	
	/**
	 * Constructor de la clase HiloMatriz, que inicializa
	 * el id del hilo con el parámetro pasado, y asigna el
	 * array de paneles al atributo paneles.
	 * 
	 * @param id entero que represetna el identificador del hilo.
	 * @param paneles array de Objetos panel que actuarán como
	 * salida del programa.
	 */
	public HiloMatriz(int id, Panel[] paneles) {
		this.id = id;
		this.paneles = paneles;
	}
	
	/**
	 * Método que genera las matrices A y B con un tamaño aleatorio entre 2 y
	 * el máximo indicado como constante en TAMMATRIX. Tras eso, crea una tercera
	 * matriz C en la que se guarda la suma A + B. Para estas operaciones se hace
	 * uso de los metodos de la clase Matriz. 
	 * 
	 * <p>A continuación, si el tamaño de la solución es 2 o 3, imprime la matriz C 
	 * por el último panel en exclusión mutua gracias al semáforo semP. En caso de que
	 * el tamaño sea mayor, el hilo escogerá en exclusión mutua el primer panel libre
	 * de los tres primeros, haciendo uso para ello del array huecos[] y de la función
	 * buscarHueco(),ambos en la clase principal del programa, y tras haber imprimido la 
	 * matriz solución C, libera el panel con la función liberarPanel() también de la clase
	 * Ej2 y hace el signal del semáforo semG para acabar con la exclusión mútua de
	 * la ejecución de este hilo.
	 */
	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {	//se itera 10 veces
			Random r = new Random();
			int tam = 2 + r.nextInt(TAMMATRIX-2);	//se genera un tamaño aleatorio entre 2 y el maximo
			Matriz a = Matriz.generarMatriz(tam);
			Matriz b = Matriz.generarMatriz(tam);
			Matriz c = Matriz.sumaMatrices(a, b, tam);
			
			
			if (tam < 4) {	//caso en el que se usa el cuarto panel
				try {
					Ej2.semP.acquire();	//sincronización
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//seccion crítica
				paneles[3].escribir_mensaje(" ---- Hilo ID:" + id + ", iter:" + i + " ----\n");
				paneles[3].escribir_mensaje(a.toString() + "\n +");
				paneles[3].escribir_mensaje(b.toString() + "\n =");
				paneles[3].escribir_mensaje(c.toString());
				paneles[3].escribir_mensaje("   ----- Fin hilo ID " + id + " -----\n");
				//fin seccion critica
				Ej2.semP.release();
			}
			else { 	//caso en el que se usa cualquiera de los 3 primeros paneles
				try {
					Ej2.semG.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int n = Ej2.buscarHueco();
				// secion critica
				paneles[n].escribir_mensaje(" ---- Hilo ID:" + id + ", iter:" + i + " ----\n");
				paneles[n].escribir_mensaje(a.toString() + "\n +");
				paneles[n].escribir_mensaje(b.toString() + "\n =");
				paneles[n].escribir_mensaje(c.toString());
				paneles[n].escribir_mensaje("   ----- Fin hilo ID " + id + " -----\n");
				// fin seccion critica
				Ej2.liberarPanel(n);
				Ej2.semG.release();
			}
		}
	}
}

package ejercicio2;

import java.util.concurrent.Semaphore;

/**
 * Clase que contiene el recurso no compartible paneles, que
 * a su vez contiene a los cuatro objetos Panel a utilizar
 * en el ejercicio 2. También maneja la exclusión mutua de los
 * de los hilos en el acceso a los paneles.
 * @author javib
 *
 */
public class Paneles {
	
	//estructura con los 4 paneles que se usaran en el ejercicio
	private Panel[] paneles;

	/**
	 * El semáforo pequeño de las matrices pequeñas es binario porque
	 * sólo hay un posible panel por donde se pueden imprimir.
	 */
	
	public static Semaphore semP = new Semaphore(1);
	/**
	 * 	El semáforo de las matrices grandes, se inicializa a 0 porque el
	 *	propio código del hilo maneja el uso de los paneles, y este semáforo
	 *	solamente sirve para bloquear hilos cuando no deban entrar a ningun 
	 *	panel y por tanto deban esperar a que se libere alguno.
	 */
	
	public static Semaphore semG = new Semaphore(0);
	/**
	 * Semáforo binario que mantiene la exclusión mutua de los hilos
	 * cuando están buscando y eligiendo un panel.
	 */
	
	public static Semaphore mutex = new Semaphore(1);
	/**
	 * estructura de booleanos que representa la disponibilidad de
	 * los tres primeros paneles
	 */
	private static Boolean[] huecos = {true, true, true};
	
	//nº de procesos en seccion critica y nº de procesos esperando, respectivamente
	public static int np, ne;
	private static int i = 0;
	
	/**
	 * Constructor del objeto Paneles. Inicializa el array de
	 * paneles y las variables ne y np.	
	 */
	public Paneles() {
		paneles = new Panel[4];
		for (int i = 0; i < 4; i++) 
			paneles[i] = new Panel("Panel " + i, 300*i, 100*i);
		ne = 0;
		np = 0;
	}
	
	/**
	 * Método get de un panel en concreto.
	 * @param i número del panel.
	 * @return el objeto panel.
	 */
	public Panel getPanel(int i) {
		return paneles[i];
	}
	
	/**
	 * {@code
	 * 		ne++
	 * }
	 */
	public void addNe() {
		ne++;
	}
	
	/**
	 * {@code
	 * 		ne--
	 * }
	 */
	public void subNe() {
		ne--;
	}
	
	/**
	 * getter del entero ne
	 * @return ne
	 */
	public static int getNe() {
		return ne;
	}
	
	/**
	 * {@code
	 * 		np++
	 * }
	 */
	public void addNp() {
		np++;
	}
	
	/**
	 * getter del entero np
	 * @return np
	 */
	public static int getNp() {
		return np;
	}
	/**
	 * Función que recorre el array de booleanos huecos, y
	 * devuelve la primera posición del array que contenga
	 * un valor true (indicando así la disponibilidad del
	 * panel asociado a dicho número), no sin antes poner
	 * esa posición a false para que no se pueda volver a
	 * usar ese panel hasta que el hilo que lo haya llamado
	 * lo libere. En vez de empezar cada ejecución siempre
	 * desde i=0, se guarda el último valor de i de cada
	 * ejecución en un atributo de la clase para así repartir
	 * mejor la impresión de matrices entre los tres paneles
	 * y evitar sobrecargar al panel 0.
	 * @return entero con el número del panel libre.
	 */
	public static int buscarHueco() {
		while (!huecos[i]) i = (i+1) % 3;
		huecos[i] = false;
		return i;
	}
	
	/**
	 * Función que pone la posición i del array huecos a true,
	 * liberando así el panel asociado y dejándolo disponible 
	 * para el próximo que lo requiera.
	 * @param i número del panel a liberar.
	 */
	public static void liberarPanel(int i) {
		np--;
		huecos[i] = true;
	}
	
//	public void debugMatrices() {	//debug
//		paneles[0].escribir_mensaje(" > Nº de matrices calculadas: [" + paneles[0].n + "]");
//		paneles[1].escribir_mensaje(" > Nº de matrices calculadas: [" + paneles[1].n + "]");
//		paneles[2].escribir_mensaje(" > Nº de matrices calculadas: [" + paneles[2].n + "]");
//		paneles[3].escribir_mensaje(" > Nº de matrices calculadas: [" + paneles[3].n + "]");
//		System.out.println(" > Nº de matrices calculadas: [" + paneles[0].n + "]");
//		System.out.println(" > Nº de matrices calculadas: [" + paneles[1].n + "]");
//		System.out.println(" > Nº de matrices calculadas: [" + paneles[2].n + "]");
//		System.out.println(" > Nº de matrices calculadas: [" + paneles[3].n + "]");
//	}
	
}

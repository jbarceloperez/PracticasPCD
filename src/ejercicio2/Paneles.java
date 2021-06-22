package ejercicio2;

import java.util.concurrent.Semaphore;

/**
 * Clase que contiene el recurso no compartible paneles, que
 * a su vez contiene a los cuatro objetos Panel a utilizar
 * en el ejercicio 2. Tambi�n maneja la exclusi�n mutua de los
 * de los hilos en el acceso a los paneles.
 * @author javib
 *
 */
public class Paneles {
	
	//estructura con los 4 paneles que se usaran en el ejercicio
	private Panel[] paneles;

	/**
	 * El sem�foro peque�o de las matrices peque�as es binario porque
	 * s�lo hay un posible panel por donde se pueden imprimir.
	 */
	
	public static Semaphore semP = new Semaphore(1);
	/**
	 * 	El sem�foro de las matrices grandes, se inicializa a 0 porque el
	 *	propio c�digo del hilo maneja el uso de los paneles, y este sem�foro
	 *	solamente sirve para bloquear hilos cuando no deban entrar a ningun 
	 *	panel y por tanto deban esperar a que se libere alguno.
	 */
	
	public static Semaphore semG = new Semaphore(0);
	/**
	 * Sem�foro binario que mantiene la exclusi�n mutua de los hilos
	 * cuando est�n buscando y eligiendo un panel.
	 */
	
	public static Semaphore mutex = new Semaphore(1);
	/**
	 * estructura de booleanos que representa la disponibilidad de
	 * los tres primeros paneles
	 */
	private static Boolean[] huecos = {true, true, true};
	
	//n� de procesos en seccion critica y n� de procesos esperando, respectivamente
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
	 * M�todo get de un panel en concreto.
	 * @param i n�mero del panel.
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
	 * Funci�n que recorre el array de booleanos huecos, y
	 * devuelve la primera posici�n del array que contenga
	 * un valor true (indicando as� la disponibilidad del
	 * panel asociado a dicho n�mero), no sin antes poner
	 * esa posici�n a false para que no se pueda volver a
	 * usar ese panel hasta que el hilo que lo haya llamado
	 * lo libere. En vez de empezar cada ejecuci�n siempre
	 * desde i=0, se guarda el �ltimo valor de i de cada
	 * ejecuci�n en un atributo de la clase para as� repartir
	 * mejor la impresi�n de matrices entre los tres paneles
	 * y evitar sobrecargar al panel 0.
	 * @return entero con el n�mero del panel libre.
	 */
	public static int buscarHueco() {
		while (!huecos[i]) i = (i+1) % 3;
		huecos[i] = false;
		return i;
	}
	
	/**
	 * Funci�n que pone la posici�n i del array huecos a true,
	 * liberando as� el panel asociado y dej�ndolo disponible 
	 * para el pr�ximo que lo requiera.
	 * @param i n�mero del panel a liberar.
	 */
	public static void liberarPanel(int i) {
		np--;
		huecos[i] = true;
	}
	
//	public void debugMatrices() {	//debug
//		paneles[0].escribir_mensaje(" > N� de matrices calculadas: [" + paneles[0].n + "]");
//		paneles[1].escribir_mensaje(" > N� de matrices calculadas: [" + paneles[1].n + "]");
//		paneles[2].escribir_mensaje(" > N� de matrices calculadas: [" + paneles[2].n + "]");
//		paneles[3].escribir_mensaje(" > N� de matrices calculadas: [" + paneles[3].n + "]");
//		System.out.println(" > N� de matrices calculadas: [" + paneles[0].n + "]");
//		System.out.println(" > N� de matrices calculadas: [" + paneles[1].n + "]");
//		System.out.println(" > N� de matrices calculadas: [" + paneles[2].n + "]");
//		System.out.println(" > N� de matrices calculadas: [" + paneles[3].n + "]");
//	}
	
}

package ejercicio2;

import java.util.concurrent.Semaphore;

/**
 * Clase con el hilo principal del ejercicio 2, que además
 * contiene funciones y atributos publicos para garantizar
 * la exclusión mutua de los hilos de matrices.
 * @author javib
 */
public class Ej2 {
	
	public static final int NUMTHREADS = 20;
	// El semáforo de las matrices grandes se inicializa a 3, 
	// para contemplar los 3 posibles paneles por donde se puede imprimir.
	public static Semaphore semG = new Semaphore(3);
	// El semáforo pequeño de las matrices pequeñas es binario porque
	// sólo hay un posible panel por donde se pueden imprimir.
	public static Semaphore semP = new Semaphore(1);
	//estructura de booleanos que representa la disponibilidad de
	//los tres primeros paneles
	private static Boolean[] huecos = {true, true, true};
	
	/**
	 * Función que recorre el array de booleanos huecos, y
	 * devuelve la primera posición del array que contenga
	 * un valor true (indicando así la disponibilidad del
	 * panel asociado a dicho número), no sin antes poner
	 * esa posición a false para que no se pueda volver a
	 * usar ese panel hasta que el hilo que lo haya llamado
	 * lo libere.
	 * @return entero con el número del panel libre.
	 */
	public static int buscarHueco() {
		int i=0;
		while (!huecos[i]) i++;
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
		huecos[i] = true;
	}
	
	/**
	 * Ejecución principal del programa.
	 * @param args args
	 */
	public static void main(String[] args) {
		//inicializar los paneles
		Panel[] paneles = new Panel[4];
		for (int i = 0; i < 4; i++) 
			paneles[i] = new Panel("Panel " + i, 300*i, 100*i);
		
		//inicializar los hilos
		HiloMatriz[] thrs = new HiloMatriz[NUMTHREADS];
		Thread[] hilos = new Thread[NUMTHREADS];
		for(int i = 0; i < NUMTHREADS; i++) {
			thrs[i] = new HiloMatriz(i, paneles);
			hilos[i] = new Thread(thrs[i]);
			hilos[i].start();	//método run
		}
		
		//Llamamos al método join para que este hilo principal no se
		//siga ejecutando hasta que el resto de los 20 hilos matriz
		//hayan finalizado su ejecución.
		try {
			for (Thread t : hilos)
				t.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("   -------- Resultados impresos en los paneles --------\n\n");
	}
}







package ejercicio2;

/**
 * Clase con el hilo principal del ejercicio 2.
 * @author javib
 */
public class Ej2 {
	
	public static final int NUMTHREADS = 20;
	
	/**
	 * Ejecución principal del programa.
	 * @param args args
	 */
	public static void main(String[] args) {
		//inicializar los paneles
		Paneles paneles = new Paneles();
		
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
		//hayan finalizado su ejecución para el debug.
		try {
			for (Thread t : hilos)
				t.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("   -------- Resultados impresos en los paneles --------\n\n");
		paneles.debugMatrices();
	}
}







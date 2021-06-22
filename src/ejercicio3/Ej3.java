package ejercicio3;

/**
 * Clase con el hilo principal del ejercicio 3.
 * @author javib
 */
public class Ej3 {
	
	public static final int MAXTHREADS = 50;
	
	/**
	 * Ejecución principal del programa.
	 * @param args args
	 */
	public static void main(String[] args) {
		Monitor monitor = new Monitor();	//inicializamos el monitor
		HiloCliente[] hilos = new HiloCliente[MAXTHREADS];
		for (int i = 0; i < MAXTHREADS; i++) {//se inicializan los hilos
			hilos[i] = new HiloCliente(i, monitor);
			hilos[i].start();
		}
		
		//se usa join para que no se siga ejecutando el hilo principal
		// hasta que no acaben los hilos cliente.
		for (int i = 0; i < hilos.length; i++) {
			try {
				hilos[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("____________________________________________________\n");
		System.out.println("Fin de la ejecución.");
	}
}

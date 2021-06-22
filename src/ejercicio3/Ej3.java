package ejercicio3;

public class Ej3 {
	
	public static final int MAXTHREADS = 50;
	
	public static void main(String[] args) {
		Monitor monitor = new Monitor();
		HiloCliente[] hilos = new HiloCliente[MAXTHREADS];
		for (int i = 0; i < MAXTHREADS; i++) {
			hilos[i] = new HiloCliente(i, monitor);
			hilos[i].start();
		}
		
		for (int i = 0; i < hilos.length; i++) {
			try {
				hilos[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

package ejercicio3;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que representa al monitor del ejercicio, implementado con
 * las clases ReentrantLock y Condition. Incluye una operación para
 * seleccionar y entrar a una cola, para salir de esta y para imprimir
 * el mensaje de cada hilo cliente.
 * @author javib
 */
public class Monitor {
	
	public static final int NUMCAJAS = 3;
	
	/**
	 * Array con los tiempos de espera de cada caja
	 */
	private int[] tiempos;
	/**
	 * Array de condiciones que controlan cada cola
	 */
	private Condition[] cajas;
	private ReentrantLock cerrojo = new ReentrantLock();

	/**
	 * Constructor del monitor, que inicializa los arrays
	 * de tiempos y condiciones.
	 */
	public Monitor() {
		tiempos = new int[NUMCAJAS];
		cajas = new Condition[NUMCAJAS];
		for (int i = 0; i < NUMCAJAS; i++) {
			tiempos[i] = 0;
			cajas[i] = cerrojo.newCondition();
		}
	}
	
	/**
	 * Método por el cual un hilo cliente consigue 
	 */
	public int entrar_cola(int id, int x, int y) throws InterruptedException {
		cerrojo.lock();
		try {
			int ncaja = 0;
			int tMin = tiempos[0];
			for (int i = 1; i < NUMCAJAS; i++) 
				if (tiempos[i] < tMin) {
					tMin = tiempos[i];
					ncaja = i;
				}
			this.imprimir(id, x, y, ncaja); //antes de ponerse en cola imprime
			tiempos[ncaja] += y;
			while (cerrojo.hasWaiters(cajas[ncaja]))	// si ya hay clientes en cola
				cajas[ncaja].await();
			return ncaja;
		} finally {cerrojo.unlock();}
	}
	
	
	public void salir_cola(int y, int ncaja) {
		cerrojo.lock();
		try {
			tiempos[ncaja] -= y;
			System.out.println("Avanza caja " + ncaja);//debug
			cajas[ncaja].signal();
		} finally {cerrojo.unlock();}
	}
	
	public void imprimir(int id, int x, int y, int ncaja) {
		int aux = ncaja + 1;
		System.out.println("____________________________________________________\n");
		System.out.println("Cliente " + id + " será atendido en caja " + aux);
		System.out.println("Tiempo de compra " + x);
		System.out.println("Tiempo estimado en caja " + y);
		System.out.println("Tiempo de espera cola1=" + tiempos[0] + ",cola2="
							+ tiempos[1] + ",cola3=" + tiempos[2]);
	}
	
	
	
	
}

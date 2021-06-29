package ejercicio3;

import java.util.LinkedList;
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

//debug
//	
//	private int[] debug_cajas;
//	private LinkedList<Integer> cola1 = new LinkedList<Integer>();
//	private LinkedList<Integer> cola2 = new LinkedList<Integer>();
//	private LinkedList<Integer> cola3 = new LinkedList<Integer>();
	
	/**
	 * Constructor del monitor, que inicializa los arrays
	 * de tiempos y condiciones.
	 */
	public Monitor() {
		tiempos = new int[NUMCAJAS];
		cajas = new Condition[NUMCAJAS];
//		debug_cajas = new int[NUMCAJAS];
		for (int i = 0; i < NUMCAJAS; i++) {
			tiempos[i] = 0;
			cajas[i] = cerrojo.newCondition();
//			debug_cajas[i] = 0;
		}
	}


	/**
	 * Método que comprueba el tiempo estimado de cada una de las cajas mediante el
	 * array tiempos, y obtiene así la cola a la que debe acceder el cliente.
	 * <p>Después, imprime un mensaje con la información del cliente y las cajas por 
	 * pantalla usando el método imprimir del monitor, y tras eso añade al tiempo 
	 * de espera de la cola el tiempo estimado en caja del cliente, almacenado previamente 
	 * en la variable y.
	 * @param id entero que representa el id del hilo.
	 * @param x entero con el tiempo estimado de la compradel cliente.
	 * @param y entero con el tamaño de la compra, que es el tiempo estimado en caja.
	 * @return entero con el numero de la caja a entrar.
	 */
	private int buscar_cola(int id, int x, int y) {
		int ncaja = 0;
		int tMin = tiempos[0];
		for (int i = 1; i < NUMCAJAS; i++) 
			if (tiempos[i] < tMin) {
				tMin = tiempos[i];
				ncaja = i;
			}
		this.imprimir(id, x, y, ncaja); //antes de ponerse en cola imprime
		tiempos[ncaja] +=y;
//		debug1(y, ncaja);
		return ncaja;
	}
	
	/**
	 * La función lama a buscar_cola para identificar la caja a la que debe entrar el
	 * cliente, y guarda el número en la variable ncaja.
	 * Después, hace un delay del hilo en caso de que haya más clientes en la cola,
	 * añadiendo así al hilo cliente a la cola de la caja, mediante el uso de la variable
	 * condición apropiada del array cajas.
	 * <p>El método devuelve el entero de la caja correspondiente, para que posteriormente
	 * el hilo cliente pueda liberar la caja que le toca.
	 * @param id entero con el id del HiloCliente que llama a la función.
	 * @param x entero con el tiempo de compra del cliente.
	 * @param y entero con el tiempo estimado en caja del cliente.
	 * @return el entero correspondiente a la cola a la que se une el cliente.
	 * @throws InterruptedException si el hilo actual es interrumpido y pusto en suspensión.
	 */
	public int entrar_cola(int id, int x, int y) throws InterruptedException {
		cerrojo.lock();
		try {
			int ncaja = buscar_cola(id, x, y);
			if (tiempos[ncaja] != y)	// si ya hay clientes en cola
				cajas[ncaja].await();
//			debug3(ncaja, y, id);
			return ncaja;
		} finally {cerrojo.unlock();}
	}

	
	/**
	 * Método por el cual un hilo cliente deja de "estar en caja", liberando así una
	 * de las tres cajas. El método primero resta el tiempo que ha estado el cliente
	 * en caja al tiempo de espera de la cola, y tras eso hace un signal de la variable
	 * condición asociada a la caja ncaja.
	 * @param y entero con el tiempo que ha pasado el cliente en caja.
	 * @param ncaja entero correspondiente a la caja de la que sale el cliente.
	 */
	public void salir_cola(int y, int ncaja) {
		cerrojo.lock();
		try {
			tiempos[ncaja] -= y;
//			debug2(y, ncaja);
			cajas[ncaja].signalAll();
		} finally {cerrojo.unlock();}
	}
	
	/**ªª
	 * Método para que cada hilo imprima en exclusión mutua la información de su cliente
	 * y del estado de las cajas en el momento de entrar a una cola.
	 * @param id entero con el id del hilo.
	 * @param x entero con el tiempo que ha pasado el cliente comprando.
	 * @param y entero con el tiempo que va a pasar el cliente en caja.
	 * @param ncaja entero correspondiente a la cola a la que se une el cliente.
	 */
	private void imprimir(int id, int x, int y, int ncaja) {
		int aux = ncaja + 1;
		System.out.println("____________________________________________________\n");
		System.out.println("Cliente " + id + " será atendido en caja " + aux);
		System.out.println("Tiempo de compra: " + x);
		System.out.println("Tiempo estimado en caja: " + y);
		System.out.println("Tiempo de espera cola1=" + tiempos[0] + ", cola2="
							+ tiempos[1] + ", cola3=" + tiempos[2]);
	}
//	
//	private void debug1(int y, int ncaja) {
//		//debug
//		if (ncaja==0) cola1.add(y);
//		if (ncaja==1) cola2.add(y);
//		if (ncaja==2) cola3.add(y);
//		System.out.println("[CAJA1]: " + cola1.toString());
//		System.out.println("[CAJA2]: " + cola2.toString());
//		System.out.println("[CAJA3]: " + cola3.toString());
//		System.out.println("Cajas: [" + debug_cajas[0] + "][" + debug_cajas[1] + "][" + debug_cajas[2] + "]");
//	}
//	
//	
//	private void debug2(int y, int ncaja) {
//		int aux = ncaja + 1;
//		System.out.printf("Avanza caja %d [%d]\n", aux, y);//debug
//		if (ncaja==0) cola1.remove();
//		if (ncaja==1) cola2.remove();
//		if (ncaja==2) cola3.remove();
//		System.out.println("[CAJA1]: " + cola1.toString());
//		System.out.println("[CAJA2]: " + cola2.toString());
//		System.out.println("[CAJA3]: " + cola3.toString());
//		debug_cajas[ncaja] = 0;
//		System.out.println("Cajas: [" + debug_cajas[0] + "][" + debug_cajas[1] + "][" + debug_cajas[2] + "]");
//	}
//	
//
//	private void debug3(int ncaja, int y, int id_debug) {
//		int aux = ncaja + 1;
//		System.out.println(id_debug + " ENTRO A CAJA " + aux + " CON " + y); //debug
//		debug_cajas[ncaja] = id_debug;
//		System.out.println("Cajas: [" + debug_cajas[0] + "][" + debug_cajas[1] + "][" + debug_cajas[2] + "]");
//	}
}

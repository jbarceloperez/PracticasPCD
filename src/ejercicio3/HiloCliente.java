package ejercicio3;

import java.util.Random;

/**
 * Clase con la implementación del hilo que represeta a un cliente
 * del supermercado. 
 * @author javib
 */
public class HiloCliente extends Thread{
	public static final int MAXCOMPRA = 200; //milisegundos
	public static final int MAXPAGO = 20; //milisegundos
	
	private int id, x, y;
	private Monitor m;
	
	/**
	 * Constructor que asigna el monitor pasado como parámetro
	 * al monitor del hilo, así como el id del hilo. También 
	 * inicializa las variables x e y, que representan el tiempo
	 * que pasa el cliente comprando y el tiempo que pasa en
	 * caja, respectivamente. Estos valores son inicializados
	 * con un valor aleatorio entre 1 y el rango indicado en las
	 * constantes MAXPAGO y MAXCOMPRA.
	 * @param id entero con el id del hilo.
	 * @param monitor objeto Monitor.
	 */
	public HiloCliente(int id, Monitor monitor) {
		m = monitor;
		this.id = id;
		Random r = new Random();
		x = r.nextInt(MAXCOMPRA)+1;
		y = r.nextInt(MAXPAGO)+1;
	}
	/**
	 * Método que se ejecuta al iniciar un hilo. Para este ejercicio,
	 * el tiempo pasado comprando y en caja se simula mediante el
	 * método sleep().
	 * <p>Primeramente, el hilo realiza la compra (duerme x milisegundos).
	 * Cuando ha terminado, llama al método entrar:cola del monitor m
	 * para buscar la cola con el menor tiempo de espera para meterse.
	 * Dicha cola es identificada con un entero que se guarda en la variabke
	 * caja.
	 * <p>Tras esto, el método duerme y milisegundos el hilo simulando el
	 * tiempo que pasa el cliente en caja. Por último, se llama al método
	 * salir_cola del monitor, para indicar que el cliente sale de la
	 * cola de la caja.
	 */
	@Override
	public void run() {
		try {
			//hacer la compra x milisegundos
			sleep(x);
			int caja = m.entrar_cola(id, x, y);
			//seccion critica: pagar en caja
			sleep(y);
			//fin sc
			m.salir_cola(y, caja);			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}

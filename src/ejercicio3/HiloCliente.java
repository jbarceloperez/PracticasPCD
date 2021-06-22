package ejercicio3;

import java.util.Random;

public class HiloCliente extends Thread{
	public static final int MAXCOMPRA = 500; //milisegundos
	public static final int MAXPAGO = 20; //milisegundos
	
	private int id, x, y;
	private Monitor m;
	
	public HiloCliente(int id, Monitor monitor) {
		m = monitor;
		this.id = id;
		Random r = new Random();
		x = r.nextInt(MAXCOMPRA)+1;
		y = r.nextInt(MAXPAGO)+1;
	}
	
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

package ejercicio3;

public class MonitorSy {

	public static final int NUMCAJAS = 3;

	private Caja[] cajas = new Caja[NUMCAJAS];
	
	public MonitorSy() {
		for (int i = 0; i < NUMCAJAS; i++) 
			cajas[i] = new Caja();
	}
	
	public synchronized int entrar_cola(int id, int x, int y) {
		int ncaja = 0;
		int tMin = cajas[0].getTiempo();
		for (int i = 1; i < NUMCAJAS; i++) 
			if (cajas[i].getTiempo() < tMin) {
				tMin = cajas[i].getTiempo();
				ncaja = i;
			}
		imprimir(id, x, y, ncaja);
		cajas[ncaja].addTiempo(y);
		cajas[ncaja].addCliente(id);
		while (!cajas[ncaja].isColaVacia()) {
			try {wait();} 
			catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
		return ncaja;		
	}
	
	
	public synchronized void salir_cola(int y) {
		
	}
	
	
	private synchronized void imprimir(int id, int x, int y, int ncaja) {
		int aux = ncaja + 1;
		System.out.println("____________________________________________________\n");
		System.out.println("Cliente " + id + " será atendido en caja " + aux);
		System.out.println("Tiempo de compra: " + x);
		System.out.println("Tiempo estimado en caja: " + y);
		System.out.println("Tiempo de espera cola1=" + cajas[0].getTiempo() + ", cola2="
							+ cajas[1].getTiempo() + ", cola3=" + cajas[2].getTiempo());
	}
	
}

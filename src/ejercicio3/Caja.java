package ejercicio3;

import java.util.LinkedList;

public class Caja {

	private int tiempo;
	private LinkedList<Integer> cola;
	
	public Caja() {
		tiempo = 0;
		cola  = new LinkedList<Integer>();
	}
	
	public int getTiempo() {
		return tiempo;
	}
	
	public void addTiempo(int tiempo) {
		this.tiempo += tiempo;
	}
	
	public void subTiempo(int tiempo) {
		this.tiempo -= tiempo;
	}
	
	public void addCliente(int id) {
		cola.add(id);
	}
	
	public void popCliente(int id) {
		cola.remove();
	}
	
	public boolean isColaVacia() {
		return cola.isEmpty();
	}
}

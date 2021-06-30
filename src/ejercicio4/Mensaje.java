package ejercicio4;

import java.io.Serializable;
import java.util.HashMap;

@SuppressWarnings("serial")
public class Mensaje implements Serializable{
	
	private int id, valor;
	private HashMap<Integer, Integer> lista;
	
	public Mensaje(HashMap<Integer, Integer> _lista) {
		lista = _lista;
	}
	
	public Mensaje(int _id, int _valor) {
		id = _id;
		valor = _valor;
	}
	
	
	
	public int getId() {
		return id;
	}
	
	public HashMap<Integer, Integer> getLista() {
		return new HashMap<Integer, Integer>(lista);
	}
	
	public int getValor() {
		return valor;
	}
}


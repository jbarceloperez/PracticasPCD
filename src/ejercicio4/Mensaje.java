package ejercicio4;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Clase con la funcionalidad de un mensaje que se envian los jugadores
 * entre sí para sincronizarse e intercambian los datos del juego. Tiene
 * dos constructores, para los dos posibles casos de uso de Mensaje.
 * @author javib
 */
@SuppressWarnings("serial")
public class Mensaje implements Serializable{
	
	private int id, valor;
	private HashMap<Integer, Integer> lista;
	
	/**
	 * Constructor de los mensajes utilizados cuando un hilo que ha calculado
	 * el ganador envía el diccionario de ids y valores a otro hilo ganador.
	 * @param _lista diccionario HashMap que asocia cada id de jugador con su valor.
	 */
	public Mensaje(HashMap<Integer, Integer> _lista) {
		lista = _lista;
	}
	
	/**
	 * Constructor de los mensajes utilizados cuando un hilo que no calcula el
	 * ganador envía su valor e id al que sí lo calcula.
	 * @param _id entero con el id del hilo que ha enviado el mensaje.
	 * @param _valor entero con el valor generado por el hilo que ha enviado el mensaje.
	 */
	public Mensaje(int _id, int _valor) {
		id = _id;
		valor = _valor;
	}
	
	/**
	 * Getter del campo id
	 * @return entero con el id del hilo que ha enviado el mensaje.
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Getter del campo lista
	 * @return diccionario HashMap que asocia cada id de jugador con su valor.
	 */
	public HashMap<Integer, Integer> getLista() {
		return new HashMap<Integer, Integer>(lista);
	}
	
	/**
	 * Getter del campo valor
	 * @return entero con el valor generado por el hilo que ha enviado el mensaje.
	 */
	public int getValor() {
		return valor;
	}
}


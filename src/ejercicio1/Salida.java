package ejercicio1;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que contiene el recurso no compartido de la salida por
 * consola del programa, para que los diferentes hilos puedan 
 * imprimir sus resultados en exclusi�n m�tua.
 * @author javib
 */
public class Salida {

	//Tal y como se indica en el enunciado, se har� uso del mecanismo
	//de cerrojos ReentrantLock para esta funci�n.
	private ReentrantLock l = new ReentrantLock();

	/**
	 * M�todo que permite a los hilos imprimir los resultados
	 * de la ordenaci�n de palabras en exclusi�n m�tua.
	 * @param id entero con el identificador del hilo que ha 
	 * llamado a la funci�n.
	 * @param frase cadena con la frase sin ordenar.
	 * @param ordenada cadena con la frase ordenada.
	 */
	public void imprimir(int id, String frase, String ordenada) {
		l.lock();	// comienzo de la secci�n cr�tica
		try {
			System.out.println(" ---- Hilo ID:" + id + " ---- ");
			System.out.println("Frase original: " + frase);
			System.out.println("Frase ordenada: " + ordenada);
			System.out.println(" ---- FIN HILO " + id + " ---- \n");
		} finally {
			l.unlock(); // fin de la secci�n cr�tica
		}
	}
}
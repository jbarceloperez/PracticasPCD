package ejercicio1;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Clase que contiene el recurso no compartido de la salida por
 * consola del programa, para que los diferentes hilos puedan 
 * imprimir sus resultados en exclusión mútua.
 * @author javib
 */
public class Salida {

	//Tal y como se indica en el enunciado, se hará uso del mecanismo
	//de cerrojos ReentrantLock para esta función.
	private ReentrantLock l = new ReentrantLock();

	/**
	 * Método que permite a los hilos imprimir los resultados
	 * de la ordenación de palabras en exclusión mútua.
	 * @param id entero con el identificador del hilo que ha 
	 * llamado a la función.
	 * @param frase cadena con la frase sin ordenar.
	 * @param ordenada cadena con la frase ordenada.
	 */
	public void imprimir(int id, String frase, String ordenada) {
		l.lock();	// comienzo de la sección crítica
		try {
			System.out.println(" ---- Hilo ID:" + id + " ---- ");
			System.out.println("Frase original: " + frase);
			System.out.println("Frase ordenada: " + ordenada);
			System.out.println(" ---- FIN HILO " + id + " ---- \n");
		} finally {
			l.unlock(); // fin de la sección crítica
		}
	}
}
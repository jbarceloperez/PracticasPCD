package ejercicio1;

import java.util.Random;

/**
 * Clase con el hilo principal del ejercicio 1.
 * @author javib
 */
public class Ej1 {
	
	// tamaños indicados para el número de hilos y de frases en el array
	public static final int NUMTHREADS = 50;
	public static final int NUMFRASES = 50;
	// cada frase tendra al menos una palabra y como máximo MAXPALABRAS palabras
	public static final int MAXPALABRAS = 10;
	
	/**
	 * Ejecución principal del programa.
	 * @param args args
	 */
	public static void main(String[] args) {
		Salida salida = new Salida();
		String frases[] = new String[NUMFRASES];
		HiloOrdenAlfabetico[] thrs = new HiloOrdenAlfabetico[NUMTHREADS];
		
		//inicializamos el array de frases
		Random r = new Random();
		for (int i = 0; i < NUMFRASES; i++) {
			//una palabra se asegura
			String frase = Palabra.getPalabra();
			//se le añaden entre 0 y 9 palabras más a la frase
			for (int j = 0; j < r.nextInt(MAXPALABRAS); j++) {
				frase = frase + " " + Palabra.getPalabra();
			}
			frases[i] = frase;
		}
		
		//Hay que crear los objetos Thread ya que la clase HiloOrdenAlfabetico 
		//es runnable e inicializarlos todos
		Thread[] hilos = new Thread[NUMTHREADS];
		for(int i = 0; i < NUMTHREADS; i++) {
			thrs[i] = new HiloOrdenAlfabetico(i, frases, salida);
			hilos[i] = new Thread(thrs[i]);
			hilos[i].start();	//método run
		}
		
		//Llamamos al método join para que este hilo principal no se siga
		//ejecutando hasta que el resto de los 50 hilos de orden alfabético
		//hayan finalizado su ejecución.
		try {
			for (Thread t : hilos)
				t.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//Así podemos imprimir por pantalla sin problemas ya que esta sección del
		//código tiene garantizada la exclusión mútua.
		System.out.println(" -------- ARRAY -------- ");
		//IMPRESIÓN DEL ARRAY FRASES
		for (String f : frases) 
			System.out.println("-" + f);
		System.out.println(" -------- Fin array. -------- ");

	}

}

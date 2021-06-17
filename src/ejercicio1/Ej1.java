package ejercicio1;

import java.util.Random;

/**
 * Clase con el hilo principal del ejercicio 1.
 * @author javib
 */
public class Ej1 {
	
	// tama�os indicados para el n�mero de hilos y de frases en el array
	public static final int NUMTHREADS = 50;
	public static final int NUMFRASES = 50;
	// cada frase tendra al menos una palabra y como m�ximo MAXPALABRAS palabras
	public static final int MAXPALABRAS = 10;
	
	/**
	 * Ejecuci�n principal del programa.
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
			//se le a�aden entre 0 y 9 palabras m�s a la frase
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
			hilos[i].start();	//m�todo run
		}
		
		//Llamamos al m�todo join para que este hilo principal no se siga
		//ejecutando hasta que el resto de los 50 hilos de orden alfab�tico
		//hayan finalizado su ejecuci�n.
		try {
			for (Thread t : hilos)
				t.join();
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//As� podemos imprimir por pantalla sin problemas ya que esta secci�n del
		//c�digo tiene garantizada la exclusi�n m�tua.
		System.out.println(" -------- ARRAY -------- ");
		//IMPRESI�N DEL ARRAY FRASES
		for (String f : frases) 
			System.out.println("-" + f);
		System.out.println(" -------- Fin array. -------- ");

	}

}

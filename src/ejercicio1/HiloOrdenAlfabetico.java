package ejercicio1;

import java.util.Arrays;

/**
 * Clase de los hilos que ordenan las palabras de las frases del array.
 * @author javib
 */
public class HiloOrdenAlfabetico implements Runnable{

	private int id;
	private String frase, ordenada;
	private String palabras[];
	
	/**
	 * Constructor que inicializa la frase del array a ordenar
	 * por este hilo, y el identificador del hilo.
	 * 
	 * @param id el identificador del hilo, representado con un entero.
	 * @param frases array de cadenas cuyas frases se quieren ordenar.
	 */
	public HiloOrdenAlfabetico(int id, String[] frases) {
		this.id = id;
		frase = frases[id];
	}
	
	/**
	 * Método que separa las diferentes palabras de la frase a ordenar en
	 * diferentes cadenas, las compara una a una, las ordena y las inserta
	 * en la posición correspondiente del array.
	 */
	public void run() {
		palabras = frase.split(" ", 0);
		//La biblioteca Arrays ofrece una manera sencilla de ordenar alfabéticamente
		//un array de cadenas haciendo uso del comparador case_sensitive_order
		Arrays.sort(palabras, String.CASE_INSENSITIVE_ORDER);
		ordenada = palabras[0];
		for (int i = 0; i < palabras.length; i++) 
			ordenada = " " + palabras[i];
		//frases[id] = ordenada;	¿hay que reescribir el array con las frases ordenadas?
		
		System.out.println("Hilo ID:" + id);
		System.out.println("Frase original: " + frase);
		System.out.println("Frase ordenada: " + ordenada);
		System.out.println(" ---- FIN HILO " + id + " ---- \n");
	}
	
		
//    for (int i = 0; i < count; i++) 
//    {
//        for (int j = i + 1; j < count; j++) { 
//            if (str[i].compareTo(str[j])>0) 
//            {
//                temp = str[i];
//                str[i] = str[j];
//                str[j] = temp;
//            }
//        }
//    }
	
}

package ejercicio2;

import java.util.concurrent.Semaphore;

public class Ej2 {
	public static Semaphore semG = new Semaphore(3);
	public static Semaphore semP = new Semaphore(1);
	
	//estructura de booleanos que representa la disponibilidad de
	//los tres primeros paneles
	private static Boolean[] huecos = {true, true, true};
	
	public static int buscarHueco() {
		int i;
		if (huecos[0]) i = 0;
		else if (huecos[1]) i = 1;
		else i = 2;
		huecos[i] = false;
		return i;
	}

	
	public static void liberarPanel(int i) {
		huecos[i] = true;
	}
	
	public static void main(String[] args) {
		
	}
}

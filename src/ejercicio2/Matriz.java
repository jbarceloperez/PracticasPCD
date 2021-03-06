package ejercicio2;

import java.util.Random;

/**
 * Clase que contiene la estructura de arrays de enteros
 * que representa una matriz cuadrada con tama?o indicado
 * en el constructor y con valores aleatorios. Adem?s, con
 * el m?todo sumaMatrices permite sumar dos matrices.
 * @author javib
 */
public class Matriz {
	private static final int MAXNUM = 100;
	private int[][] valores;
	
	/**
	 * M?todo que genera y devuelve una matriz de valores enteros
	 * aleatorios, con mismas filas que columnas, y con un tama?o
	 * indicado como par?metro.
	 * @param tam entero que representa el tama?o de la matriz.
	 * @return un objeto matriz con tama?o tam.
	 */
	public static Matriz generarMatriz(int tam) {
		Matriz m = new Matriz();
		m.valores = new int[tam][tam];
		Random r = new Random();
		for (int i = 0; i < tam; i++) 
			for (int j = 0; j < tam; j++)
				m.valores[i][j] = r.nextInt(MAXNUM);
		return m;
	}
	
	/**
	 * M?todo que crea una nueva matriz, y recorre todos los
	 * valores de las dos matrices pasadas como par?metros
	 * para as? sumar sus valores y asignarlos a los valores
	 * de la matriz nueva. Devuelve la matriz resultado.
	 * @param a matriz sumando.
	 * @param b matriz sumando
	 * @param tam entero que representa el tama?o de las matrices.
	 * @return matriz resultado.
	 */
	public static Matriz sumaMatrices(Matriz a, Matriz b, int tam) {
		Matriz c = new Matriz();
		c.valores = new int[tam][tam];
		for (int i = 0; i < tam; i++) 
			for (int j = 0; j < tam; j++)
				c.valores[i][j] = a.valores[i][j] + b.valores[i][j];
		return c;
	}
	/**
	 * M?todo que devuelve una cadena con los valores de la
	 * matriz por filas y columnas en un formato legible.
	 * @return str cadena con los valores de la matriz.
	 */
	@Override
	public String toString() {
		String str = "";
		int tam = valores[0].length;
		for (int i = 0; i < tam; i++) {
			str = str + "[" + valores[i][0];
			for (int j = 1; j < tam; j++) {
				String espacio;
				if (valores[i][j-1]<10) espacio = "      ";
				else if (valores[i][j-1]<100) espacio = "    ";
				else espacio = "  ";
				str = str + espacio + valores[i][j];
			}
			str = str + "]\n";
		}
		return str;
	}
}

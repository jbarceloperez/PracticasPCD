package ejercicio2;

import java.util.Random;

/**
 * Clase que contiene la estructura de arrays de enteros
 * que representa una matriz cuadrada con tamaño indicado
 * en el constructor y con valores aleatorios. Además, con
 * el método sumaMatrices permite sumar dos matrices.
 * @author javib
 */
public class Matriz {
	private static final int MAXNUM = 100;
	private int[][] valores;
	
	/**
	 * Método que genera y devuelve una matriz de valores enteros
	 * aleatorios, con mismas filas que columnas, y con un tamaño
	 * indicado como parámetro.
	 * @param tam entero que representa el tamaño de la matriz.
	 * @return un objeto matriz con tamaño tam.
	 */
	public static Matriz generarMatriz(int tam) {
		Matriz m = new Matriz();
		Random r = new Random();
		for (int i = 0; i < tam; i++) 
			for (int j = 0; j < tam; j++)
				m.valores[i][j] = r.nextInt(MAXNUM);
		return m;
	}
	
	/**
	 * Método que crea una nueva matriz, y recorre todos los
	 * valores de las dos matrices pasadas como parámetros
	 * para así sumar sus valores y asignarlos a los valores
	 * de la matriz nueva. Devuelve la matriz resultado.
	 * @param a matriz sumando.
	 * @param b matriz sumando
	 * @param tam entero que representa el tamaño de las matrices.
	 * @return matriz resultado.
	 */
	public static Matriz sumaMatrices(Matriz a, Matriz b, int tam) {
		Matriz c = new Matriz();
		for (int i = 0; i < tam; i++) 
			for (int j = 0; j < tam; j++)
				c.valores[i][j] = a.valores[i][j] + b.valores[i][j];
		return c;
	}
	
}

package ejercicio2;

import java.util.Random;

/**
 * Clase que genera y suma las matrices, y en función del
 * tamaño, imprime en un panel u otro la solución obtenida
 * de la suma de matrices.
 * @author javib
 */
public class HiloMatriz implements Runnable{
	
	private int id;
	private Panel[] paneles;
	

	public HiloMatriz(int id, Panel[] paneles) {
		this.id = id;
		this.paneles = paneles;
	}
	
	
	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {
			Random r = new Random();
			int tam = 2 + r.nextInt(8);
			Matriz a = Matriz.generarMatriz(tam);
			Matriz b = Matriz.generarMatriz(tam);
			Matriz c = Matriz.sumaMatrices(a, b, tam);
			
			
			if (tam < 4) {	//caso en el que se usa el cuarto panel
				try {
					Ej2.semP.acquire();	//sincronización
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//seccion crítica
				paneles[3].escribir_mensaje(" ---- Hilo ID:" + id + ", iter:" + i + " ----\n");
				paneles[3].escribir_mensaje(a.toString() + "\n +");
				paneles[3].escribir_mensaje(b.toString() + "\n =");
				paneles[3].escribir_mensaje(c.toString());
				paneles[3].escribir_mensaje("   ----- Fin hilo ID " + id + " -----\n");
				//fin seccion critica
				Ej2.semP.release();
			}
			else { 	//caso en el que se usa cualquiera de los 3 primeros paneles
				try {
					Ej2.semG.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				int n = Ej2.buscarHueco();
				// secion critica
				paneles[n].escribir_mensaje(" ---- Hilo ID:" + id + ", iter:" + i + " ----\n");
				paneles[n].escribir_mensaje(a.toString() + "\n +");
				paneles[n].escribir_mensaje(b.toString() + "\n =");
				paneles[n].escribir_mensaje(c.toString());
				paneles[n].escribir_mensaje("   ----- Fin hilo ID " + id + " -----\n");
				
				// fin seccion critica
			}
		}
	}	
	
	
	
	
	
	
	
}

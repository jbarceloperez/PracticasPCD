package ejercicio2;

import java.util.Random;

/**
 * Clase que genera y suma las matrices, y en funci�n del
 * tama�o, imprime en un panel u otro la soluci�n obtenida
 * de la suma de matrices.
 * @author javib
 */
public class HiloMatriz implements Runnable{
	
	public static final int TAMMATRIX = 10;	//tama�o m�ximo de la matriz
	
	private int id;
	private Paneles paneles;
	
	/**
	 * Constructor de la clase HiloMatriz, que inicializa
	 * el id del hilo con el par�metro pasado, y asigna el
	 * array de paneles al atributo paneles.
	 * 
	 * @param id entero que represetna el identificador del hilo.
	 * @param paneles array de Objetos panel que actuar�n como
	 * salida del programa.
	 */
	public HiloMatriz(int id, Paneles paneles) {
		this.id = id;
		this.paneles = paneles;
	}
	
	/**
	 * M�todo que genera las matrices A y B con un tama�o aleatorio entre 2 y
	 * el m�ximo indicado como constante en TAMMATRIX. Tras eso, crea una tercera
	 * matriz C en la que se guarda la suma A + B. Para estas operaciones se hace
	 * uso de los metodos de la clase Matriz. 
	 * 
	 * <p>A continuaci�n, si el tama�o de la soluci�n es 2 o 3, imprime la matriz C 
	 * por el �ltimo panel en exclusi�n mutua gracias al sem�foro semP. 
	 * 
	 * <p>En caso de que el tama�o sea mayor, el hilo escoger� en exclusi�n mutua el 
	 * primer panel libre de los tres primeros, haciendo uso para ello del array 
	 * huecos[] y de la funci�n buscarHueco(), ambos en la clase Paneles. Esto se
	 * realizar� en exclusi�n mutua, haciendo uso del semaforo mutex de la clase
	 * Paneles, para evitar que varios hilos obtengan el mismo panel a utilizar. En 
	 * caso de no haber paneles disponibles, se suma uno al numero de hilos esperando y
	 * se hace un signal del mutex para que otros hilos continuen. Se hace un wait del semaforo
	 * semG, poniendo a la espera hasta que otro hilo finalice y se libere un panel (que se
	 * disminuye en uno el numero de hilos esperando).
	 * 
	 * <p>Tras haber imprimido la  matriz soluci�n C, libera el panel con la funci�n 
	 * liberarPanel() tambi�n de la clase Paneles, y si hay alg�n hilo esperando hace 
	 * el signal del sem�foro semG, y si no del semaforo mutex.
	 */
	@Override
	public void run() {
		for (int i = 0; i < 10; i++) {	//se itera 10 veces
			Random r = new Random();
			int tam = 2 + r.nextInt(TAMMATRIX-1);	//se genera un tama�o aleatorio entre 2 y el maximo
			Matriz a = Matriz.generarMatriz(tam);
			Matriz b = Matriz.generarMatriz(tam);
			Matriz c = Matriz.sumaMatrices(a, b, tam);
			
			
			if (tam < 4) {	//caso en el que se usa el cuarto panel
				try {
					Paneles.semP.acquire();	//sincronizaci�n
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//seccion cr�tica
				paneles.getPanel(3).escribir_mensaje(" ---- Hilo ID:" + id + ", iter:" + i + " ----\n");
				paneles.getPanel(3).escribir_mensaje(a.toString() + "\n +");
				paneles.getPanel(3).escribir_mensaje(b.toString() + "\n =");
				paneles.getPanel(3).escribir_mensaje(c.toString());
				paneles.getPanel(3).escribir_mensaje("   ----- Fin hilo ID " + id + " -----\n");
				paneles.getPanel(3).n++;//debug
				//fin seccion critica
				Paneles.semP.release();
			}
			else { 	//caso en el que se usa cualquiera de los 3 primeros paneles
				
				try {
					Paneles.mutex.acquire();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				if (Paneles.getNp()==3) {
					paneles.addNe();	//se a�ade un hilo esperando
					Paneles.mutex.release(); //se deja seguir al resto
					try {
						Paneles.semG.acquire(); //se espera a q acabe un hilo de matriz grande
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					paneles.subNe();	//un hilo menos esperando
				}
				paneles.addNp(); //un hilo mas en la seccion critica
				//MUTEX
				int n = Paneles.buscarHueco();	// esta funcion es un follonazo, por culpa de esto hay que acceder a 
											// esta instrucci�n en concreto en axclusi�n mutua :(
				Paneles.mutex.release();
				// secion critica
				paneles.getPanel(n).escribir_mensaje(" ---- Hilo ID:" + id + ", iter:" + i + " ----\n");
				paneles.getPanel(n).escribir_mensaje(a.toString() + "\n +");
				paneles.getPanel(n).escribir_mensaje(b.toString() + "\n =");
				paneles.getPanel(n).escribir_mensaje(c.toString());
				paneles.getPanel(n).escribir_mensaje("   ----- Fin hilo ID " + id + " -----\n");
				paneles.getPanel(n).n++;//debug
				// fin seccion critica
				try {
					Paneles.mutex.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Paneles.liberarPanel(n);
				if (Paneles.getNe() > 0) Paneles.semG.release();
				else Paneles.mutex.release();
			}
		}
	}
}

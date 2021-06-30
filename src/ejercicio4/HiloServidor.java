package ejercicio4;

import messagepassing.MailBox;

/**
 * Clase que implementa la funcionalidad del servidor, el cual agrupa
 * los jugadores que solicitan jugar en partidas, recibe los ganadores 
 * de estas y sigue agrupando semifinales y finales, para al final
 * imprimir el ganador.
 * @author javib
 */
public class HiloServidor implements Runnable{
	
	public static final int NJUGADORES = 32;
	
	/**Entero con la cuenta de las partidas echadas*/
	private int npartida;
	/**Buzon por el que entran solicitudes de juego de los jugadores*/
	private MailBox server;
	/**Buzon por el que entran los resultados de los juegos*/
	private MailBox sigJuego;
	/**Buzones con los que el servidor envia los grupos a los jugadores*/
	private MailBox[] buzones;
	/**Buzon para la exclusion mutua de la pantalla*/
	private MailBox pantalla;
	
	/**
	 * Constructor que inicializa el atributo {@code npartida} a 1, e
	 * inicializa tambi�n el resto de buzones con los pasados como
	 * par�metro.
	 * @param _server buzon por el que entran solicitudes de juego de los jugadores
	 * @param _sigJuego buzon por el que entran los resultados de los juegos
	 * @param _pantalla buzones con los que el servidor envia los grupos a los jugadores
	 * @param _buzones buzon para la exclusion mutua de la pantalla
	 */
	public HiloServidor(MailBox _server, MailBox _sigJuego, MailBox _pantalla, MailBox[] _buzones) {
		npartida = 1;
		server = _server;
		sigJuego = _sigJuego;
		buzones = _buzones;
		pantalla = _pantalla;
	}

	/**
	 * Gracias al {@code while(true)} est� constantemente en ejecuci�n. El servidor
	 * espera en las sentencias recieve hasta que 32 jugadores han solicitado jugar
	 * por el buz�n {@code server}.
	 * <p>Entonces inicia la primera partida, agrupando en grupos de 4 a los jugadores
	 * y enviando un array con los ids de cada jugador de su grupo a cada jugador.
	 * Despu�s espera por el buz�n {@code sigJuego} los resultados de los 8 juegos, y cuando
	 * los tiene realiza el mismo proceso de separaci�n en dos grupos de 4, y posterior
	 * env�o de ids a los semifinalistas. Tras eso, espera los resultados de las dos
	 * semifinales, y agrupa a los dos finalistas en un solo grupo, para que compitan
	 * ellos dos solos.
	 * <p>Por �ltimo, despu�s de recibir un ganador de entre los dos finalistas, imprime
	 * por pantalla el resultado de la partida, aumenta en uno la cuenta de partidas y vuelve
	 * a empezar.
	 */
	@Override
	public void run() {
		while(true) {	//iteracion de una partida
			int[] jugadores = new int[NJUGADORES];
			int[] semis = new int[8];
			int[] finalJ = new int[2];
			
			for (int i = 0; i < NJUGADORES; i++) 	//obtener las ids de los 32 jugadores
				jugadores[i] = (int) server.receive();
			//hacer los grupos del primer juego
			for (int i = 0; i < 8; i++) {
				int[] ids = new int[4];
				for (int j = 0; j < ids.length; j++) 
					ids[j] = jugadores[j+(4*i)];
				for (int j = 0; j < ids.length; j++)
					buzones[ids[j]].send(ids); //enviar su grupo a cada jugador
			}
			//esperar a la segunda ronda
			for (int i = 0; i < 8; i++) 	//obtener las ids de los semifinalistas
				semis[i] = (int) sigJuego.receive();
			//hacer los 2 grupos del segundo juego
			for (int i = 0; i < 2; i++) {
				int[] ids = new int[4];
				for (int j = 0; j < ids.length; j++) 
					ids[j] = semis[j+(4*i)];
				for (int j = 0; j < ids.length; j++) {
					buzones[ids[j]].send(ids);} //enviar su grupo a cada jugador
			}
			//esperar a los dos finalistas
			finalJ[0] = (int) sigJuego.receive();
			finalJ[1] = (int) sigJuego.receive();
			//enviar su grupo a cada finalista
			buzones[finalJ[0]].send(finalJ); 
			buzones[finalJ[1]].send(finalJ);
			//esperar al ganador e imprimir
			int ganador = (int)sigJuego.receive();
			pantalla.receive();	//se accede a la pantalla en EXCLUSION MUTUA
			System.out.println("________________________________________");
			System.out.println("Hilo Servidor.");
			System.out.println("El ganador del juego n�mero " + npartida + " es el hilo con el id " + ganador + "\n\n");
			pantalla.send(0);	//se libera la pantalla
			npartida++;
			
			// try { // debug, pa frenar un poco el torrente de partidas 
			// 	Thread.sleep(500);
			// } catch (InterruptedException e) {
			// 	e.printStackTrace();
			// }
			
		}
	}

}

package ejercicio4;

import messagepassing.MailBox;

/**
 * Clase con el hilo principal del ejercicio 4.
 * @author javib
 */
public class Ej4 {

	public static final int NHILOS = 40;
	
	/**
	 * Ejecución principal del programa.
	 * @param args args
	 */
	public static void main(String[] args) {
		// Buzones para la comunicacion de servidor a jugadores
		MailBox[] buzones = new MailBox[NHILOS];
		// Buzones para la sincronización entre jugadores
		MailBox[] sincJuego = new MailBox[NHILOS];
		MailBox[] ganadores = new MailBox[NHILOS];
		
		// Buzones para la comunicacion de jugador a servidor
		MailBox server = new MailBox();
		MailBox sigJuego = new MailBox();
		
		// Buzon para la exclusion mutua de la pantalla
		MailBox pantalla = new MailBox();
		pantalla.send(0);	//para que el primer hilo que quiera imprimir pueda
							//obtener la pantalla en exclusion mutua
		
		
		//Conjunto de jugadores
		HiloJugador[] j = new HiloJugador[NHILOS];
		Thread[] jugadores = new Thread[NHILOS];
		
		for (int i = 0; i < NHILOS; i++) {	//inicializamos los elementos pertinentes
			buzones[i] = new MailBox();
			sincJuego[i] = new MailBox();
			ganadores[i] = new MailBox();
			j[i] = new HiloJugador(i, server, buzones[i], sigJuego, pantalla, sincJuego, ganadores);
			jugadores[i] = new Thread(j[i]);
			//empiezan los hilos jugador
			jugadores[i].start();
		}
		
		//Instancia de la clase hilo servidor y su hilo
		HiloServidor s = new HiloServidor(server, sigJuego, pantalla, buzones);
		Thread servidor = new Thread(s);
		//empieza el hilo servidor
		servidor.start();
		
		
		for (int i = 0; i < NHILOS; i++) {
			try {
				jugadores[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

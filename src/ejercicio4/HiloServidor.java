package ejercicio4;

import messagepassing.MailBox;

public class HiloServidor implements Runnable{
	public static final int NJUGADORES = 32;
	
	private int npartida;
	private MailBox server;
	private MailBox sigJuego;
	private MailBox[] buzones;
	private MailBox pantalla;
	
	
	public HiloServidor(MailBox _server, MailBox _sigJuego, MailBox _pantalla, MailBox[] _buzones) {
		npartida = 1;
		server = _server;
		sigJuego = _sigJuego;
		buzones = _buzones;
		pantalla = _pantalla;
	}


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
			//debug
			System.out.println("\nPRIMERA RONDA");
			//esperar a la segunda ronda
			for (int i = 0; i < 8; i++) 	//obtener las ids de los semifinalistas
				semis[i] = (int) sigJuego.receive();
			//hacer los 2 grupos del segundo juego
			for (int i = 0; i < 2; i++) {
				int[] ids = new int[4];
				for (int j = 0; j < ids.length; j++) 
					ids[j] = semis[j+(4*i)];
				for (int j = 0; j < ids.length; j++) {
					System.out.println(ids[j]);//debug
					buzones[ids[j]].send(ids);} //enviar su grupo a cada jugador
			}
			//debug
			System.out.println("\nSEGUNDA RONDA");
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
			System.out.println("El ganador del juego número " + npartida + " es el hilo con el id " + ganador);
			pantalla.send(0);	//se libera la pantalla
			npartida++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}

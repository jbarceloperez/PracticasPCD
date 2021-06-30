package ejercicio4;

import java.util.HashMap;
import java.util.Random;

import messagepassing.MailBox;
import messagepassing.Selector;

public class HiloJugador implements Runnable{
	
	public static final int RANGONUMEROS = 100;
	
	private int id;
	private MailBox server;
	private MailBox sigJuego;
	private MailBox miBuzon;
	private MailBox pantalla;
	
	private MailBox[] sincJuego, ganadores;
	
	private Selector s;
	
	public HiloJugador(int _id, MailBox _server, MailBox _miBuzon, MailBox _sigJuego, MailBox _pantalla, MailBox[] _sincJuego, MailBox[] _ganadores) {
		id = _id;
		server = _server;
		miBuzon = _miBuzon;
		sigJuego = _sigJuego;
		sincJuego = _sincJuego;
		ganadores = _ganadores;
		pantalla = _pantalla;
		s = new Selector();
		s.addSelectable(sincJuego[_id], false);
		s.addSelectable(ganadores[_id], false);
	}

	
	@Override
	public void run() {
		while (true) {	// repeat forever
			server.send(id);
			boolean pasa;
			do {	//iteracion de un juego
				pasa = false;
				int[] ids = (int[]) miBuzon.receive();
				Random r = new Random();
				int valor = r.nextInt(RANGONUMEROS) + 1;
				int min = getMinimo(ids);
				if (id == min) { //el id mas pequeño se encarga de elegir el ganador
					HashMap<Integer, Integer> juego = new HashMap<Integer, Integer>();
					juego.put(id, valor);
					for (int i = 1; i < ids.length; i++) {
						Mensaje m = (Mensaje) sincJuego[id].receive();
						juego.put(m.getId(), m.getValor());
					}
					int ganador = getGanador(juego);
					if (id == ganador) { //si ha ganado el hilo con id mas pequeño
						imprimir(pantalla, juego);
						for (int clave : juego.keySet())
							if (clave != id) //avisa a los perdedores de que han perdido
								sincJuego[clave].send(null);
						sigJuego.send(id); //avisar al servidor de q ha ganado
						pasa = true; // se tiene q ejecutar una iteracion mas para el siguiente juego
					} else { //si ha ganado un hilo diferente 
						for (int clave : juego.keySet())
							if (clave != id) //avisa a los otros hilos si han ganado o no
								if (clave == ganador)
									ganadores[clave].send(new Mensaje(juego));
								else
									sincJuego[clave].send(null);
					}
				} else { // como no es el hilo con el id mas pequeño del grupo, solo manda el valor y espera
					sincJuego[min].send(new Mensaje(id, valor));
					switch (s.selectOrBlock()) {
					case 1:
						sincJuego[id].receive();
						break;
					case 2:
						Mensaje m = (Mensaje) ganadores[id].receive();
						imprimir(pantalla, m.getLista());
						sigJuego.send(id); //avisar al servidor de q ha ganado
						pasa = true;	// se tiene q ejecutar una iteracion mas para el siguiente juego
						break;
					}
				}
			} while (pasa);
		}//repeat forever
	}


	
	private void imprimir(MailBox pantalla, HashMap<Integer, Integer> juego) {
		pantalla.receive();	//se accede a la pantalla en EXCLUSION MUTUA
		System.out.println("________________________________________");
		System.out.println("Jugador con ID " + id + " fue ganador.");
		System.out.printf("Jugó con los jugadores: ");
		for (int clave : juego.keySet())
			if (id != clave) 
				System.out.printf("%d(valor=%d) ", clave, juego.get(clave));
		System.out.printf("\nValores:\n\t");
		for (int clave : juego.keySet())
			System.out.printf("%d ", juego.get(clave));
		System.out.println("");
		pantalla.send(0);	//se libera la pantalla
		
	}


	private int getGanador(HashMap<Integer, Integer> juego) {
		int valor = 0;
		int ganador = 0;
		for (int clave : juego.keySet()) {
			if (juego.get(clave) > valor) {
				valor = juego.get(clave);
				ganador = clave;
			}
			else if (juego.get(clave) == valor && clave > ganador)
				ganador = clave;
		}
		return ganador;
	}


	private int getMinimo(int[] ids) {
		int min = 99;
		for (int i : ids) 
			if (i < min)
				min = i;
		return min;
	}
	
}

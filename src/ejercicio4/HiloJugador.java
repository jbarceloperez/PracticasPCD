package ejercicio4;

import java.util.HashMap;
import java.util.Random;

import messagepassing.MailBox;
import messagepassing.Selector;

/**
 * Clase con la implementaci�n de la funcionalidad de un jugador, que
 * se solicita jugar al servidor, recibe sus grupos para el juego, y
 * dependiendo de si es el id m�s peque�o de su grupo, calcula el ganador
 * o no.
 * @author javib
 */
public class HiloJugador implements Runnable{
	
	public static final int RANGONUMEROS = 100;
	
	/**Entero con el id del hilo*/
	private int id;
	/**Buzon por el que se mandan solicitudes de juego al servidor*/
	private MailBox server;
	/**c*/
	private MailBox sigJuego;
	/**Buzon por el que llegan los mensajes del servidor*/
	private MailBox miBuzon;
	/**Buzon para la exclusion mutua de la pantalla*/
	private MailBox pantalla;
	
	/**Buzones por los que se comunican los hilos de una partida*/
	private MailBox[] sincJuego, ganadores;
	/**Objeto selector para la implementaci�n del select*/
	private Selector s;
	
	/**
	 * Constructor del HiloJugador que inicializa los atributos de la clase
	 * con los par�metros entrantes, y adem�s inicializa el objeto Selector
	 * para la implementaci�n de la sentencia Select.
	 * @param _id Entero con el id del hilo
	 * @param _server Buzon por el que se mandan solicitudes de juego al servidor
	 * @param _miBuzon Buzon por el que llegan los mensajes del servidor
	 * @param _sigJuego Buzon por el que se mandan los resultados de los juegos al servidor
	 * @param _pantalla Buzon para la exclusion mutua de la pantalla
	 * @param _sincJuego buzon por el que se mandan valores al jugador con id m�s peque�o,
	 * y que tambi�n representa recibir un mensaje de que no se ha ganado la ronda. El jugador
	 * con el id m�s peque�o lo usa para enviar recibir los valores y enviar los resultados.
	 * @param _ganadores buzon que representa la victoria de un hilo en una partida.
	 */
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

	/**
	 * Gracias al {@code while(true)} est� constantemente en ejecuci�n. El hilo del
	 * jugador manda una solicitud al servidor para entrar en partida. Cuando el servidor
	 * le responde la partida en la que est�, el jugador genera su valor aleatorio. <p>La
	 * implementaci�n de la comunicaci�n entre jugadores en este caso est� implementada de
	 * la siguiente manera: de los jugadores de un grupo, el jugador con el id m�s peque�o
	 * ser� el encargado de recibir los valores de sus contrincantes, compararlos, calcular 
	 * el ganador y notificar a cada uno de sus contrincantes si ha ganado o perdido, adem�s
	 * de enviarle los datos de los valores al ganador para que pueda imprimir por pantalla.
	 * 
	 * <p>Cuando se es el hilo m�s peque�o, se llama a la funci�n {@code calcularGanador()},
	 * que calcula el ganador de la ronda recibiendo los valores de los contrincantes y
	 * compar�ndolos para despu�s calcular el ganador. Adem�s, esta funci�n tambi�n notifica
	 * a los perdedores y al ganador del resultado haciendo uso de los buzones {@code sincJuego}
	 * y {@code ganadores} corresponedientes a cada hilo.
	 * Por otro lado, cuando no se es el hilo m�s peque�o, simplemente se env�a el valor al
	 * hilo peque�o, y se espera en una sentencia select a que llegue un mensaje. Si el mensaje
	 * llega al buz�n sincJuego correspondiente al hilo, quiere decir que ha perdido la ronda,
	 * y por lo tanto acaba la ejecuci�n del hilo. Si por el contrario el mensaje llega al 
	 * buz�n ganadores, significa que el hilo es ganador.
	 * 
	 * <p>Cuando un hilo es perdedor, simplemente acaba su ejecuci�n, porque el llega a la 
	 * condici�n pasa, que se pone a false al empezar cada iteraci�n del do while. Sin embargo, 
	 * cuando un jugador es ganador, se imprime el resultado del juego por pantalla con la funci�n
	 * imprimir, se env�a el id ganador al servidor por el buz�n sigJuego y se comprueba que no
	 * sea la ronda final (esto es, que el juego haya tenido solo dos contrincantes).
	 */
	@Override
	public void run() {
		while (true) {	// repeat forever
			server.send(id);
			boolean pasa;
			do {	//iteracion de un juego
				pasa = false;
				int[] ids = (int[]) miBuzon.receive();
				Random r = new Random();
				int valor = r.nextInt(RANGONUMEROS) + 1;	//generar valor aleatorio del 1 al 100
				int min = getMinimo(ids);
				if (id == min) { //el id mas peque�o se encarga de elegir el ganador
					pasa = calcularGanador(pasa, ids, valor);
				} else { // como no es el hilo con el id mas peque�o del grupo, solo manda el valor y espera
					sincJuego[min].send(new Mensaje(id, valor));
					//SENTENCIA SELECT
					switch (s.selectOrBlock()) {	//se espera en el select a recibir el resultado
					case 1:
						sincJuego[id].receive();
						break;
					case 2:
						Mensaje m = (Mensaje) ganadores[id].receive();
						imprimir(pantalla, m.getLista());
						sigJuego.send(id); //avisar al servidor de q ha ganado
						if(ids.length > 2)
							pasa = true;	// se tiene q ejecutar una iteracion mas para el siguiente juego, solo si no es la final
						break;
					}
				}
			} while (pasa);
		}//repeat forever
	}

	/**
	 * Funci�n calcularGanador, funcionalidad explicada en la documentaci�n de run().
	 * @param pasa booleano que indica si hace falta seguir jugando en esta partida.
	 * @param ids enteros con las id de los contrincantes
	 * @param valor valor calculado por este hilo
	 * @return booleano indicando si se debe seguir iterando en esta partida
	 */
	private boolean calcularGanador(boolean pasa, int[] ids, int valor) {
		//se crea un mapa que asocia ids y valores
		HashMap<Integer, Integer> juego = new HashMap<Integer, Integer>();
		juego.put(id, valor);
		for (int i = 1; i < ids.length; i++) {	//se reciben los valores y se van guardando en el mapa juego
			Mensaje m = (Mensaje) sincJuego[id].receive();
			juego.put(m.getId(), m.getValor());
		}
		int ganador = getGanador(juego);	//se calcula con una funcion auxiliar
		if (id == ganador) { //si ha ganado el hilo con id mas peque�o
			imprimir(pantalla, juego);
			for (int clave : juego.keySet())
				if (clave != id) //avisa a los perdedores de que han perdido
					sincJuego[clave].send(null);
			sigJuego.send(id); //avisar al servidor de q ha ganado
			if(ids.length > 2)
				pasa = true; // se tiene q ejecutar una iteracion mas para el siguiente juego, solo si no es la final
		} else { //si ha ganado un hilo diferente 
			for (int clave : juego.keySet())
				if (clave != id) //avisa a los otros hilos si han ganado o no
					if (clave == ganador)
						ganadores[clave].send(new Mensaje(juego));
					else
						sincJuego[clave].send(null);
		}
		return pasa;
	}


	/**
	 * Imprime por pantalla el resultado de un juego, en exclusi�n mutua.
	 * @param pantalla buzon que garantiza la exclusion mutua de la pantalla.
	 * @param juego diccionario HashMap que asocia cada id de jugador con su valor.
	 */
	private void imprimir(MailBox pantalla, HashMap<Integer, Integer> juego) {
		pantalla.receive();	//se accede a la pantalla en EXCLUSION MUTUA
		System.out.println("________________________________________");
		System.out.println("Jugador con ID " + id + " fue ganador.");
		System.out.printf("Jug� con los jugadores: ");
		for (int clave : juego.keySet())
			if (id != clave) 
				System.out.printf("%d(valor=%d) ", clave, juego.get(clave));
		System.out.printf("\nValores:\n\t");
		for (int clave : juego.keySet())
			System.out.printf("%d ", juego.get(clave));
		System.out.println("");
		pantalla.send(0);	//se libera la pantalla
		
	}

	/**
	 * Calcula el ganador de un juego. En caso de empate, gana el jugador con id
	 * m�s alto.
	 * @param juego diccionario HashMap que asocia cada id de jugador con su valor.
	 * @return entero con el id del jugador ganador.
	 */
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

	/**
	 * Devuelve el id m�s peque�o
	 * @param ids array de enteros con las ids
	 * @return entero con el id m�s peque�o
	 */
	private int getMinimo(int[] ids) {
		int min = 99;
		for (int i : ids) 
			if (i < min)
				min = i;
		return min;
	}
	
}

package Modelo;
import java.util.List; // es el tipo que use para guardar la lista de rutas de sonidos
import java.util.Random; //es para elegir un sonido aleatorio de la lista


public class Gato {
    
//ATRIBUTOS

private Boolean encontrado;

private List<String> sonidosFlashback = List.of(
    "Recursos/Sonidos/sonidos_gato/flashback1.wav",
    "Recursos/Sonidos/sonidos_gato/flashback2.wav",
    "Recursos/Sonidos/sonidos_gato/flashback3.wav",
    "Recursos/Sonidos/sonidos_gato/flashback4.wav"
);

private List<String> sonidosEncuentro = List.of(
    "Recursos/Sonidos/sonidos_gato/encuentro1.wav",
    "Recursos/Sonidos/sonidos_gato/encuentro2.wav",
    "Recursos/Sonidos/sonidos_gato/encuentro3.wav",
    "Recursos/Sonidos/sonidos_gato/encuentro4.wav"
);

private Random random = new Random(); //Sirve para elegir cual sonido reproducir (De mi lista de sonidos)
private static final long DURACION_LENTITUD_MS = 2000;

//CONSTRUCTOR

public Gato(){
        this.encontrado = false; //el gato se inicializa como no encontrado
}

//GET Y SET

public boolean getEncontrado(){
    return encontrado;
}

public void setEncontrado(boolean encontrado){
    this.encontrado = encontrado;
}


public void reproducirSonidoFlashback(){
    int indice = random.nextInt(sonidosFlashback.size());
    ReproductorSonido.reproducir(sonidosFlashback.get(indice));
}

public void reproducirSonidoEncuentro(){
    int indice= random.nextInt(sonidosEncuentro.size());
    ReproductorSonido.reproducir(sonidosEncuentro.get(indice));
}

//acca movi lo de flashback para que respete mvc (Sonido + lentitud + cuando se revirete)

public void activarFlashback(Personaje personaje){
    reproducirSonidoFlashback();
    personaje.aplicarLentitud();

    java.util.Timer temporizador = new java.util.Timer(true); // el "true" es como el setDaemon
    temporizador.schedule(new java.util.TimerTask() {
        @Override
        public void run() {
            personaje.quitarLentitud();
             }
        }, DURACION_LENTITUD_MS);
    
    }
}
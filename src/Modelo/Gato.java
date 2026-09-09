package Modelo;
import java.util.List; // es el tipo que use para guardar la lista de rutas de sonidos
import java.util.Random; //es para elegir un sonido aleatorio de la lista


public class Gato {
    
//ATRIBUTOS

private Boolean encontrado;

private List<String> sonidosFlashback = List.of(
    "Recursos/sonidos_gato/flashback1.wav",
    "Recursos/sonidos_gato/flashback2.wav",
    "Recursos/sonidos_gato/flashback3.wav",
    "Recursos/sonidos_gato/flashback4.wav"
);

private List<String> sonidosEncuentro = List.of(
    "Recursos/sonidos_gato/encuentro1.wav",
    "Recursos/sonidos_gato/encuentro2.wav",
    "Recursos/sonidos_gato/encuentro3.wav",
    "Recursos/sonidos_gato/encuentro4.wav"
);

private Random random = new Random(); //Sirve para elegir cual sonido reproducir (De mi lista de sonidos)


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

//METODOS
/* creo que podemos sacar este metodo o podemos dejarlo como uno generico en caso de que elijamos agregar otro sonido
public void maullar(){
    System.out.println("El gato emite un maullido ecoico y distorsionado...");
}
*/
public void reproducirSonidoFlashback(){
    int indice = random.nextInt(sonidosFlashback.size());
    ReproductorSonido.reproducir(sonidosFlashback.get(indice));
}

public void reproducirSonidoEncuentro(){
    int indice= random.nextInt(sonidosEncuentro.size());
    ReproductorSonido.reproducir(sonidosEncuentro.get(indice));
}

}

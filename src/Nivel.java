
import java.util.ArrayList;
import java.util.List;

public class Nivel {
    
//--ATRIBUTOS--

private int numeroNivel;
private List<Enemigo> listaEnemigos; //esta seria mi lista dinamica para los enemigos.
//private Acertijo acertijo;  nos falta definir la clase acertijo.
private boolean nivelSuperado;
private boolean checkpoint; //va a ser una bandera, si es falso no se activa, verdadero activado


//--CONSTRUCTOR--

public Nivel (int numeroNivel, Acertijo acertijo){ //aca va a marcar error hasta que este definida la clase acertijo
    this.numeroNivel= numeroNivel;
    //this.acertijo = acertijo;
    this.nivelSuperado = false;
    this.checkpoint = false; //arranca desactivado por defecto
    this.listaEnemigos = new ArrayList<>();
}

//--METODOS--

public void guardarCheckpoint(){
    this.checkpoint = true;
    System.out.println("Checkpoint activado en el nivel "+ this.numeroNivel + "!!!");
}
//--GET--
//va a preguntar si es checkpoint o no
public boolean getCheckpoint(){
    return this.checkpoint;
}

}

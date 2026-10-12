package Modelo;

public interface ObservadorAcertijo {
    void acertijoSolicitado(AcertijoNumerico acertijo, Personaje personaje); // abre pantalla
    void acertijoSinIntentos(Acertijo acertijo);                     // se agotaron, hubo daño, cambio a acertijo
    void acertijoResuelto(Acertijo acertijo);                                // resuelto sin pantalla
    void acertijoBloqueado(Acertijo acertijo);                               // falta algo
}
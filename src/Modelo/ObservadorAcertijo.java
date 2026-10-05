package Modelo;

public interface ObservadorAcertijo {
    void acertijoSolicitado(AcertijoNumerico acertijo, Personaje personaje); // abre pantalla
    void acertijoSinIntentos(AcertijoNumerico acertijo);                     // se agotaron, hubo daño
    void acertijoResuelto(Acertijo acertijo);                                // resuelto sin pantalla
    void acertijoBloqueado(Acertijo acertijo);                               // falta algo
}
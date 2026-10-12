package Modelo;

public interface ObservadorPasos {

    // El personaje empezó a caminar (o cambió de piso mientras caminaba)
    void pasosIniciados(TipoSuelo suelo);

    // El personaje frenó
    void pasosDetenidos();

}
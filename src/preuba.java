import Modelo.*;
import Vista.*;

public class preuba {
    public static void main(String[] args) throws InterruptedException {
    ReproductorSonido.reproducirEfectoEnLoop(Sonidos.PASOS_MADERA);
    Thread.sleep(4000);
    ReproductorSonido.reproducirEfectoEnLoop(Sonidos.PASOS_METAL); // corta madera y suena metal
    Thread.sleep(4000);
    ReproductorSonido.detenerEfectoEnLoop(); // silencio
    Thread.sleep(1000);
}
}

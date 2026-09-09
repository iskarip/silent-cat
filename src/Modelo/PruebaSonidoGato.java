
    package Modelo;

public class PruebaSonidoGato {
    public static void main(String[] args) throws InterruptedException {
        Gato gato = new Gato();

        System.out.println("Probando sonido de flashback...");
        gato.reproducirSonidoFlashback();
        Thread.sleep(2000); // esperar a que termine de sonar

        System.out.println("Probando sonido de encuentro...");
        gato.reproducirSonidoEncuentro();
        Thread.sleep(2000);

        System.out.println("Listo.");
    }
}

//sirve para probar los sonidos que hace el gatinho owo
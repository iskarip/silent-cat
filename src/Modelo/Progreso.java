package Modelo;

public class Progreso {

    private final String genero;
    private final int numeroNivel;
    private final int checkpointX;
    private final int checkpointY;
    private final int vida;
    private final int bateria;

    public Progreso(String genero, int numeroNivel, int checkpointX, int checkpointY,
                    int vida, int bateria) {
        if (genero == null || genero.isBlank()) {
            throw new IllegalArgumentException("Falta el personaje elegido");
        }
        if (numeroNivel < 1) {
            throw new IllegalArgumentException("El nivel tiene que ser 1 o mayor");
        }
        if (vida < 1 || vida > 100) {
            throw new IllegalArgumentException("La vida tiene que estar entre 1 y 100");
        }
        if (bateria < 0 || bateria > 100) {
            throw new IllegalArgumentException("La bateria tiene que estar entre 0 y 100");
        }
        this.genero = genero;
        this.numeroNivel = numeroNivel;
        this.checkpointX = checkpointX;
        this.checkpointY = checkpointY;
        this.vida = vida;
        this.bateria = bateria;
    }

    public String getGenero() { return genero; }
    public int getNumeroNivel() { return numeroNivel; }
    public int getCheckpointX() { return checkpointX; }
    public int getCheckpointY() { return checkpointY; }
    public int getVida() { return vida; }
    public int getBateria() { return bateria; }
    
}

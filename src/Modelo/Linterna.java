package Modelo;

import java.util.ArrayList;
import java.util.List;

public class Linterna {

    private int bateria;
    private boolean encendido;
    private int alcance;

    // Lista de observadores (igual a Caja en la filmina)
    private final List<ObservadorLinterna> observadores = new ArrayList<>();

    public Linterna() {
        this.bateria = 100;
        this.encendido = true;
        this.alcance = 100;
    }

    public void agregarObservador(ObservadorLinterna obs) {
        if (obs != null && !observadores.contains(obs)) {
            observadores.add(obs);
        }
    }

    public void removerObservador(ObservadorLinterna obs) {
        observadores.remove(obs);
    }

    private void notificar() {
        for (ObservadorLinterna o : observadores) {
            o.linternaCambio(this.bateria, this.encendido);
        }
    }

    // Cada método que altera el estado termina con notificar():
    public void setBateria(int bateria) {
        if (bateria >= 100) {
            this.bateria = 100;
        } else if (bateria <= 0) {
            this.bateria = 0;
            this.encendido = false;
        } else {
            this.bateria = bateria;
        }
        notificar();
    }

    public void setEncendido(boolean encendido) {
        this.encendido = encendido;
        notificar();
    }

    public void encenderLinterna() {
        if (this.bateria > 0) {
            setEncendido(true);
        }
    }

    public void apagarLinterna() {
        setEncendido(false);
    }

    public void recargarLinterna(int recargaLinterna) {
        setBateria(this.bateria + recargaLinterna);
    }

    public void gastarBateria() {
        if (encendido && bateria > 0) {
            bateria--;
            if (bateria == 0) {
                encendido = false;
            }
            notificar();
        }
    }

    // Getters limpios
    public int getBateria() { return bateria; }
    public boolean getEncendido() { return encendido; }
    public int getAlcance() { return alcance; }
    public boolean bateriaBaja() { return bateria < 20; }
}
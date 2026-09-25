package Controlador;

import Vista.JuegoFrame;
import Vista.NivelPanel;
import Vista.PausaPanel;

public class ControladorPausa {

    private final NivelPanel nivelPanel;
    private final PausaPanel pausaPanel;
    private final JuegoFrame ventanaPrincipal;
    private final ControladorNivel controladorNivel;

    public ControladorPausa(NivelPanel nivelPanel, JuegoFrame ventanaPrincipal, ControladorNivel controladorNivel) {
        this.nivelPanel = nivelPanel;
        this.pausaPanel = nivelPanel.getPausaPanel();
        this.ventanaPrincipal = ventanaPrincipal;
        this.controladorNivel = controladorNivel;

        configurarEventos();
    }

    private void configurarEventos() {
        // Accionar pausa desde el botón flotante en pantalla
        nivelPanel.getBotonPausa().addActionListener(e -> pausar());

        // Acciones dentro de la ventana de pausa
        pausaPanel.getBotonReanudar().addActionListener(e -> reanudar());
        pausaPanel.getBotonMenuPrincipal().addActionListener(e -> volverAlMenu());
        
        // Botón de Game Over si el personaje muere
        nivelPanel.getBotonVolverMenu().addActionListener(e -> volverAlMenu());
    }

    public void pausar() {
        controladorNivel.setPausado(true);
        nivelPanel.mostrarPausa();
    }

    public void reanudar() {
        controladorNivel.setPausado(false);
        nivelPanel.ocultarPausa();
        nivelPanel.requestFocusInWindow(); // Devuelve el foco al teclado del juego
    }

    public void volverAlMenu() {
        controladorNivel.detener();
        nivelPanel.ocultarPausa();
        ventanaPrincipal.mostrarPantalla("menu");
    }

}

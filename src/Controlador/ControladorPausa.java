package Controlador;

import Modelo.ReproductorSonido;
import Vista.FinDelJuego;
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
        if (nivelPanel.getBotonPausa() != null) {
            nivelPanel.getBotonPausa().addActionListener(e -> pausar());
        }

        // Acciones dentro de la ventana de pausa
        if (pausaPanel != null) {
            pausaPanel.getBotonReanudar().addActionListener(e -> reanudar());
            pausaPanel.getBotonMenuPrincipal().addActionListener(e -> volverAlMenu());
        }

        // Botones y acciones de fin del juego o Game Over.
       /* FinDelJuego panelFin = nivelPanel.getFinDelJuego();
        if (panelFin != null) {
            panelFin.getBotonVolverMenu().addActionListener(e -> {
                panelFin.setVisible(false);
                volverAlMenu();
            });

        */
    }

    public void pausar() {
        controladorNivel.setPausado(true);
        if (pausaPanel != null) {
            pausaPanel.setBounds(0, 0, nivelPanel.getWidth(), nivelPanel.getHeight());
            pausaPanel.setVisible(true);
        }
        if (nivelPanel.getBotonPausa() != null) {
            nivelPanel.getBotonPausa().setVisible(false);
        }
    }

    public void reanudar() {
        controladorNivel.setPausado(false);
        if (pausaPanel != null) {
            pausaPanel.setVisible(false);
        }
        if (nivelPanel.getBotonPausa() != null) {
            nivelPanel.getBotonPausa().setVisible(true);
        }
        nivelPanel.requestFocusInWindow(); // Devuelve el foco al teclado del juego
    }

    public void volverAlMenu() {
        controladorNivel.detener();
        if (pausaPanel != null) {
            pausaPanel.setVisible(false);
        }
        if (nivelPanel.getBotonPausa() != null) {
            nivelPanel.getBotonPausa().setVisible(true);
        }
        ventanaPrincipal.mostrarPantallaConFundido("menu");
        ReproductorSonido.reproducirEnLoop(ControladorPrincipal.MUSICA_MENU);
    }

}

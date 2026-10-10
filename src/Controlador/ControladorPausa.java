package Controlador;

import Vista.ReproductorSonido;
import Vista.FinDelJuego;
import Vista.JuegoFrame;
import Vista.NivelPanel;
import Vista.PausaPanel;
import java.awt.event.ActionListener;

public class ControladorPausa {

    private final NivelPanel nivelPanel;
    private final PausaPanel pausaPanel;
    private final JuegoFrame ventanaPrincipal;
    private final ControladorNivel controladorNivel;
    private final ActionListener alPausar = e -> pausar();
    private final ActionListener alReanudar = e -> reanudar();
    private final ActionListener alVolverAlMenu = e -> volverAlMenu();

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
            nivelPanel.getBotonPausa().addActionListener(alPausar);
        }

        // Acciones dentro de la ventana de pausa
        if (pausaPanel != null) {
            pausaPanel.getBotonReanudar().addActionListener(alReanudar);
            pausaPanel.getBotonMenuPrincipal().addActionListener(alVolverAlMenu);
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

    public void desconectar() {
        if (nivelPanel.getBotonPausa() != null) {
            nivelPanel.getBotonPausa().removeActionListener(alPausar);
        }
        if (pausaPanel != null) {
            pausaPanel.getBotonReanudar().removeActionListener(alReanudar);
            pausaPanel.getBotonMenuPrincipal().removeActionListener(alVolverAlMenu);
        }
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

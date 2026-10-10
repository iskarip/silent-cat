package Controlador;

import Modelo.Configuracion;
import Persistencia.ConfiguracionDAO;
import Persistencia.ConfiguracionDAOSQLite;
import Vista.ReproductorSonido;
import Vista.AjustesPanel;
import Vista.JuegoFrame;

public class ControladorAjustes {
    
    private final AjustesPanel vista;
    private final JuegoFrame ventanaPrincipal;
    private final ConfiguracionDAO configuracionDAO;

    public ControladorAjustes(AjustesPanel vista, JuegoFrame ventanaPrincipal) {
        this.vista = vista;
        this.ventanaPrincipal = ventanaPrincipal;
        this.configuracionDAO = new ConfiguracionDAOSQLite();

        cargarConfiguracionGuardada();
        configurarListeners();
    }

        private void cargarConfiguracionGuardada() {
        Configuracion config = configuracionDAO.cargar();
        ReproductorSonido.setVolumenMusica(config.getVolumenMusica());
        ReproductorSonido.setVolumenEfectos(config.getVolumenEfectos());

        vista.setVolumenMusica((int) (config.getVolumenMusica() * 100));
        vista.setVolumenEfectos((int) (config.getVolumenEfectos() * 100));
    }

    private void configurarListeners() {
        // Cambiar el slider aplica el volumen al momento
        vista.getSliderMusica().addChangeListener(e ->
            ReproductorSonido.setVolumenMusica(vista.getSliderMusica().getValue() / 100f)
        );
        vista.getSliderEfectos().addChangeListener(e ->
            ReproductorSonido.setVolumenEfectos(vista.getSliderEfectos().getValue() / 100f)
        );

        // Guardar en la Base de datos recien al salir
        vista.getBotonVolver().addActionListener(e -> {
            guardarConfiguracion();
            ventanaPrincipal.mostrarPantalla("menu");
        });
    }

    private void guardarConfiguracion() {
        float musica = vista.getSliderMusica().getValue() / 100f;
        float efectos = vista.getSliderEfectos().getValue() / 100f;
        configuracionDAO.guardar(new Configuracion(musica, efectos));
    }
}


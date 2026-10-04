package Vista;

import javax.swing.*;
import java.awt.*;


public class AjustesPanel extends JPanel{
    
    private JSlider sliderMusica;
    private JSlider sliderEfectos;
    private BotonJuego botonVolver;

    public AjustesPanel() {
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0;

        JLabel titulo = new JLabel("AJUSTES");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 28));
        gbc.gridy = 0;
        add(titulo, gbc);

        JLabel labelMusica = new JLabel("Volumen de música");
        labelMusica.setForeground(Color.WHITE);
        gbc.gridy = 1;
        add(labelMusica, gbc);

        sliderMusica = new JSlider(0, 100, 50);
        gbc.gridy = 2;
        add(sliderMusica, gbc);

        JLabel labelEfectos = new JLabel("Volumen de efectos");
        labelEfectos.setForeground(Color.WHITE);
        gbc.gridy = 3;
        add(labelEfectos, gbc);

        sliderEfectos = new JSlider(0, 100, 100);
        gbc.gridy = 4;
        add(sliderEfectos, gbc);

//el botonVolver usa la imagen de "NuevaPartida" porque es la unica que tiene el mismo tamaño que los sliders
        botonVolver = new BotonJuego("/Recursos/UI/Menu/Botones/NuevaPartida.png", "/Recursos/UI/Menu/Botones/NuevaPartidaHover.png", "Recursos/Sonidos/UI/sonido3.wav");
        gbc.gridy = 5;
        add(botonVolver, gbc);
    }

    public JSlider getSliderMusica() { return sliderMusica; }
    public JSlider getSliderEfectos() { return sliderEfectos; }
    public JButton getBotonVolver() { return botonVolver; }

    public void setVolumenMusica(int valor) { sliderMusica.setValue(valor); }
    public void setVolumenEfectos(int valor) { sliderEfectos.setValue(valor); }
}

package logica.configuraciones;

import java.awt.Dimension;
import java.awt.Toolkit;

public class Resolucion {
    private static final double WIDTH = 1920.0;
    private static final double HEIGTH = 1080.0;

    private static double escalaX;
    private static double escalaY;

    public static void iniciar() {
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        escalaX = screen.getWidth() / WIDTH;
        escalaY = screen.getHeight() / HEIGTH;
    }

    public static int width(int valor) {
        return (int) (valor * escalaX);
    }

    public static int heigth(int valor) {
        return (int) (valor * escalaY);
    }
    
    public static int fuente(int valor) {
        return (int) (valor * ((escalaX + escalaY) / 2));
    }
}
package logica.controles;

import assets.Barco;
import assets.Tablero;
import assets.componentes.Casilla;
import codigos.CodigoCasilla;
import java.awt.Component;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javax.swing.Box;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import logica.gestores.GestorTablero;

public class ControlFlota extends ControlBase {

    private JLayeredPane areaJuego;
    private JPanel flota;

    public ControlFlota(Control control, GestorTablero gestor) {
        super(control, gestor);
    }

    void moverBarco(Barco barco, int i) {
        if (barco.getParent() instanceof JPanel panelFlota) {
            flota = panelFlota;

            areaJuego.add(barco, JLayeredPane.DRAG_LAYER);
            flota.add(Box.createRigidArea(barco.getSize()), i);
            barco.setLocation(SwingUtilities.convertPoint(flota, barco.getLocation(), areaJuego));

            flota.revalidate();
            flota.repaint();
            areaJuego.revalidate();
            areaJuego.repaint();
        }
    }

    void noMoverBarco(Barco barco, int i) {
        if (barco.getParent() instanceof JLayeredPane && !barco.estaColocado()) {
            flota.remove(i);
            flota.add(barco, i);

            areaJuego.revalidate();
            areaJuego.repaint();
            flota.revalidate();
            flota.repaint();
        }
    }

    void setAreaJuego(JLayeredPane areaJuego) {
        this.areaJuego = areaJuego;
    }

    boolean validarPosicion(Barco barco) {
        HashSet<Casilla> casillas = obtenerCasillas(barco);
        boolean libre = casillas != null;

        if (casillas != null) {
            Iterator it = casillas.iterator();

            while (it.hasNext() && libre) {
                libre = ((Casilla) (it.next())).getEstado() == CodigoCasilla.AGUA;
            }
        }

        return libre;
    }

    void limpiarPos(Barco barco) {
        for (Casilla casilla : obtenerCasillas(barco)) {
            casilla.setEstado(CodigoCasilla.AGUA);
        }
    }

    void colocar(Barco barco) {
        HashSet<Casilla> casillas = obtenerCasillas(barco);
        ArrayList<String> coords = new ArrayList<>();
        for (Casilla casilla : casillas) {
            casilla.setEstado(CodigoCasilla.BARCO);
            coords.add(casilla.toString());
        }
        
        gestorTablero.agregarBarco(barco, String.join(",", coords));

        barco.setColocado(true);
    }

    private HashSet<Casilla> obtenerCasillas(Barco barco) {
        int x, y, nCasillas = 0;
        boolean existe = true;
        HashSet<Casilla> casillas = new HashSet<>();
        Tablero tablero = control.getTablero();

        x = Math.round((barco.getX() - tablero.getX()) / 70.0f); // numero
        y = Math.round((barco.getY() - tablero.getY()) / 70.0f); // letra

        while (nCasillas < barco.getLongitud() && existe) {
            if (((x > 0 && x < 11) && (y > 0 && y < 11))) {
                Casilla actual = tablero.getCasilla(((char) (y + 64)) + "" + x);

                if (actual != null) {
                    casillas.add(actual);

                    x = barco.esHorizontal() ? ++x : x;
                    y = barco.esHorizontal() ? y : ++y;

                    nCasillas++;
                } else {
                    existe = false;
                }

            } else {
                existe = false;
            }
        }

        if (nCasillas != barco.getLongitud() || !existe) {
            casillas = null;
        }

        return casillas;
    }

    void cambiarListenersFlota() {
        for (Component c : areaJuego.getComponents()) {
            if (c instanceof Barco b) {
                b.cambiarListeners();
            }
        }
    }

    boolean barcosColocados() {
        int i = 0;
        boolean hayBarcos = false;
        Component componentes[] = flota.getComponents();

        while (!hayBarcos && componentes.length > i) {
            hayBarcos = componentes[i] instanceof Barco;

            i++;
        }

        return !hayBarcos;
    }

    public ArrayList<Barco> getFlota() {
        ArrayList<Barco> flota = new ArrayList<>();

        for (Component c : areaJuego.getComponents()) {
            if (c instanceof Barco b) {
                flota.add(b);
            }
        }

        return flota;
    }

    String getCoordBarco(Barco barco) {
        return gestorTablero.getCoordInicio(barco);
    }

}

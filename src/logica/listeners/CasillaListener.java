package logica.listeners;

import assets.componentes.Casilla;
import codigos.CodigoCasilla;
import codigos.TipoBarco;
import logica.controles.Control;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;

public class CasillaListener extends MouseAdapter {

    private Control control;
    private Casilla casilla;
    private static boolean seleccionado;

    public CasillaListener(Control control, Casilla casilla) {
        this.control = control;
        this.casilla = casilla;
        seleccionado = false;
    }

    public void limpiarSeleccionado() {
        seleccionado = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (casilla.getEstado() != CodigoCasilla.SELECCIONADA && casilla.getEstado() != CodigoCasilla.TOCADO && !seleccionado) {
            casilla.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (casilla.getEstado() != CodigoCasilla.SELECCIONADA && casilla.getEstado() != CodigoCasilla.TOCADO && !seleccionado) {
            casilla.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 0, 128)));
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e)) {
            if (casilla.getEstado() != CodigoCasilla.TOCADO) {
                seleccionado = control.casillaSeleccionada(casilla);
            }
        } else if (SwingUtilities.isRightMouseButton(e)) {
            if (control.isAtacante(TipoBarco.SUBMARINO) && casilla.getEstado() != CodigoCasilla.TOCADO) {
                seleccionado = control.areaSeleccionada(casilla);
            }
        }
    }

}

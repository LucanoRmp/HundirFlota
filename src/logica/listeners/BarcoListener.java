package logica.listeners;

import assets.Barco;
import logica.controles.Control;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BarcoListener extends MouseAdapter {

    private int xActual, yActual;
    private Point puntoRetorno;
    private final Control control;
    private final Barco barco;
    private int indice;

    public BarcoListener(Control control, Barco barco) {
        this.control = control;
        this.barco = barco;
        puntoRetorno = new Point();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!control.partidaEmpezada()) {
            if (barco.estaColocado()) {
                control.limpiarBarco(barco);
            }

            rotar(barco);

            if (control.validarPosicion(barco)) {
                control.colocarBarco(barco);
            } else {
                rotar(barco);
            }
        } else if (!barco.hundido()) {
            control.barcoSeleccionado(barco);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!control.partidaEmpezada()) {
            indice = barco.getParent().getComponentZOrder(barco);
            control.iniciarArrastre(barco, indice);

            if (barco.estaColocado()) {
                control.limpiarBarco(barco);
            }

            puntoRetorno = barco.getLocation();
            xActual = e.getX();
            yActual = e.getY();

            barco.getParent().setComponentZOrder(barco, 0);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!control.partidaEmpezada()) {
            int x = barco.getX() + e.getX() - this.xActual;
            int y = barco.getY() + e.getY() - this.yActual;

            barco.setLocation(x, y);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!control.partidaEmpezada()) {
            if (control.validarPosicion(barco)) {
                control.colocarBarco(barco);
                barco.setCoord(control.getCoordBarco(barco));
            } else {
                control.cancelarArrastre(barco, indice);
                barco.setLocation(puntoRetorno);

                if (barco.estaColocado()) {
                    control.colocarBarco(barco);
                }
            }

            centrar(barco);
            puntoRetorno = barco.getLocation();
        }
    }

    private void rotar(Barco barco) {
        int longitud = barco.getLongitud();
        if (barco.esHorizontal()) {
            //Vertical
            barco.setPreferredSize(new Dimension(barco.getTam(), barco.getTam() * longitud));
            barco.setSize(new Dimension(barco.getTam(), barco.getTam() * longitud));

            barco.rotarImg();
        } else {
            //Horizontal
            barco.setPreferredSize(new Dimension(barco.getTam() * longitud, barco.getTam()));
            barco.setSize(new Dimension(barco.getTam() * longitud, barco.getTam()));

            barco.rotarImg();
        }

        centrar(barco);
    }

    private void centrar(Barco barco) {
        int x = barco.getX() - barco.getTam();
        int y = barco.getY() - barco.getTam();

        int newX = (int) (Math.round((float) (x) / barco.getTam()) * barco.getTam());
        int newY = (int) (Math.round((float) (y) / barco.getTam()) * barco.getTam());

        int nuevaCasillaX = newX - x + barco.getX();
        int nuevaCasillaY = newY - y + barco.getY();

        barco.setLocation(nuevaCasillaX, nuevaCasillaY);

        barco.revalidate();
        barco.repaint();
    }
}

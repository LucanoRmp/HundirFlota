package assets.componentes;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import codigos.CodigoCasilla;
import codigos.CodigoDisparo;
import logica.configuraciones.Assets;
import logica.controles.Control;
import logica.listeners.CasillaListener;

public class Casilla extends JLabel {

    private CodigoCasilla estado;
    private CodigoDisparo disparo;
    private Boolean signal;
    private CasillaListener listener;
    private String codigo;

    public Casilla(String text, int horizontalAlignment, Control control) {
        super(text, horizontalAlignment);
        setFont(new Font("Arial", Font.BOLD, 20));
        setOpaque(false);

        estado = CodigoCasilla.NO_SELECCIONADA;
        listener = new CasillaListener(control, this);
        signal = null;
    }

    public Casilla(String text, int horizontalAlignment) {
        super(text, horizontalAlignment);
        setFont(new Font("Arial", Font.BOLD, 20));

        estado = CodigoCasilla.AGUA;
    }

    public void setEstado(CodigoCasilla estado) {
        this.estado = estado;
    }

    public void disparo(CodigoDisparo disparo) {
        if (disparo.equals(CodigoDisparo.HUNDIDO)) {
            this.disparo = CodigoDisparo.TOCADO;
        } else {
            this.disparo = disparo;
        }
        estado = CodigoCasilla.DISPARO;
        repaint();
    }

    public CodigoCasilla getEstado() {
        return estado;
    }

    public void anyadirListeners() {
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public void eliminarListeners() {
        removeMouseListener(listener);
        removeMouseMotionListener(listener);
    }

    public void limpiarCasillas() {
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        if (estado != CodigoCasilla.DISPARO && estado != CodigoCasilla.TOCADO) {
            estado = CodigoCasilla.NO_SELECCIONADA;
        }

        listener.limpiarSeleccionado();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (signal != null) {
            if (signal) {
                g.setColor(new Color(50, 255, 50, 40));
                g.fillRect(0, 0, getWidth(), getHeight());
            } else {
                g.setColor(new Color(255, 255, 255, 40));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }

        if (disparo != null) {
            switch (disparo) {
                case AGUA -> {
                    g.drawImage(Assets.AGUA, 0, 0, getWidth(), getHeight(), this);
                    estado = CodigoCasilla.TOCADO;
                }

                case RESISTIDO -> {
                    g.drawImage(Assets.RESISTIDO, 0, 0, getWidth(), getHeight(), this);
                }

                case TOCADO -> {
                    g.drawImage(Assets.TOCADO, 0, 0, getWidth(), getHeight(), this);
                    estado = CodigoCasilla.TOCADO;
                }

            }
        }
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setSignal(boolean signal) {
        this.signal = signal;
        repaint();
    }

    @Override
    public String toString() {
        return codigo;
    }

}

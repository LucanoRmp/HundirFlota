package assets;

import assets.componentes.SeccionBarco;
import codigos.TipoBarco;
import logica.controles.Control;
import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import codigos.CodigoDisparo;
import logica.configuraciones.Assets;
import logica.listeners.BarcoListener;

public class Barco extends JPanel {

    private boolean hundido;
    private boolean horizontal, colocado, listo;
    private int sizeCasilla = 70;
    private final Control control;
    private final BarcoListener listener;
    private float opacidad;
    private final ArrayList<SeccionBarco> estructura;
    private int letra;
    private int numero;
    private final TipoBarco tipo;
    private int vida;

    public Barco(TipoBarco tipo, Control control) {
        setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        setSize(sizeCasilla * tipo.getLongitud(), sizeCasilla);
        setPreferredSize(new Dimension(sizeCasilla * tipo.getLongitud(), sizeCasilla));
        setOpaque(false);

        opacidad = 1.0f;
        estructura = new ArrayList<>();

        this.control = control;
        this.tipo = tipo;

        vida = tipo.getLongitud();
        horizontal = true;
        colocado = false;
        listo = false;
        listener = new BarcoListener(this.control, this);

        for (int i = 0; i < tipo.getLongitud(); i++) {
            SeccionBarco seccion = new SeccionBarco();

            seccion.setIcon(new ImageIcon(Assets.obtenerBarco(tipo, i).getScaledInstance(sizeCasilla, sizeCasilla, Image.SCALE_SMOOTH)));

            seccion.setVida(tipo.getVida());

            estructura.add(seccion);

            add(seccion);
        }

        hundido = tipo.getVida() <= 0;

        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public void rotarImg() {
        horizontal = !horizontal;

        for (int i = 0; i < tipo.getLongitud(); i++) {
            SeccionBarco seccion = estructura.get(i);

            seccion.setIcon(new ImageIcon(Assets.rotar(tipo, i, horizontal ? 0 : 90).getScaledInstance(sizeCasilla, sizeCasilla, Image.SCALE_SMOOTH)));
        }
    }

    public void cambiarListeners() {
        listo = !listo;

        if (listo) {
            removeMouseListener(listener);
            removeMouseMotionListener(listener);
        } else {
            addMouseListener(listener);
            addMouseMotionListener(listener);
        }
    }

    public void redimension() {
        int columna = getX() / sizeCasilla;
        int fila = getY() / sizeCasilla;
        
        sizeCasilla = control.getSizeCasilla();

        if (horizontal) {
            setSize(tipo.getLongitud() * sizeCasilla, sizeCasilla);
            setPreferredSize(new Dimension(sizeCasilla * tipo.getLongitud(), sizeCasilla));
        } else {
            setSize(sizeCasilla, tipo.getLongitud() * sizeCasilla);
            setPreferredSize(new Dimension(sizeCasilla, sizeCasilla * tipo.getLongitud()));
        }
        
        for (SeccionBarco seccion : estructura) {
            seccion.setIcon(new ImageIcon(((ImageIcon) seccion.getIcon()).getImage().getScaledInstance(sizeCasilla, sizeCasilla, Image.SCALE_SMOOTH)));
            seccion.setPreferredSize(new Dimension(sizeCasilla, sizeCasilla));
        }

        setLocation(columna * sizeCasilla, fila * sizeCasilla + 15);
    }

    public CodigoDisparo recibirDanyo(String coord, int fuerza) {
        int l = coord.charAt(0);
        int n = Integer.parseInt(coord.substring(1));
        int pos;

        if (this.letra == l) {
            pos = Math.abs(n - this.numero);
        } else {
            pos = Math.abs(l - this.letra);
        }

        CodigoDisparo resultado = estructura.get(pos).recibirAtaque(fuerza);

        if (resultado == CodigoDisparo.TOCADO) {
            vida--;
            hundido(vida <= 0);
        }

        return resultado;
    }

    public int getLongitud() {
        return tipo.getLongitud();
    }

    public boolean esHorizontal() {
        return horizontal;
    }

    public boolean estaColocado() {
        return colocado;
    }

    public void setColocado(boolean colocado) {
        this.colocado = colocado;
    }

    public String getDescripcion() {
        return tipo.getDescripcion();
    }

    public int getTam() {
        return sizeCasilla;
    }

    public void setOpacidad(float opacidad) {
        this.opacidad = opacidad;
        repaint();
    }

    public int getDisparos() {
        return tipo.getDisparos();
    }

    public int getFuerza() {
        return tipo.getFuerza();
    }

    public void setCoord(String coord) {
        letra = coord.charAt(0);
        numero = Integer.parseInt(coord.substring(1));
    }

    public boolean hundido() {
        return hundido;
    }

    public void hundido(boolean hundido) {
        this.hundido = hundido;
    }

    public TipoBarco getTipo() {
        return tipo;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Barco)) {
            return false;
        }

        return tipo == ((Barco) obj).getTipo();
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(sizeCasilla * tipo.getLongitud(), sizeCasilla);
    }
    
    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, hundido() ? 1f : opacidad));

        super.paintChildren(g2d);
        g2d.dispose();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        for (SeccionBarco seccion : estructura) {
            seccion.setEnabled(enabled);
            seccion.repaint();
        }

        repaint();
    }
}

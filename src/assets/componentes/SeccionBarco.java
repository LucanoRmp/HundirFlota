package assets.componentes;

import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JLabel;
import codigos.CodigoDisparo;
import logica.configuraciones.Assets;

public class SeccionBarco extends JLabel {

    private int vida;
    private CodigoDisparo disparo;

    public SeccionBarco() {
        setHorizontalAlignment(JLabel.CENTER);
        setVerticalAlignment(JLabel.CENTER);
        setOpaque(false);
        disparo = null;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (disparo != null) {
            Graphics2D g2d = (Graphics2D) g.create();
            
            g2d.drawImage((disparo == CodigoDisparo.TOCADO) ? Assets.TOCADO : Assets.RESISTIDO, 0, 0, getWidth(), getHeight(), this);

            g2d.dispose();
        }
    }

    public CodigoDisparo recibirAtaque(int fuerza) {
        vida -= fuerza;

        disparo = vida <= 0 ? CodigoDisparo.TOCADO : CodigoDisparo.RESISTIDO;

        repaint();

        return disparo;
    }

}

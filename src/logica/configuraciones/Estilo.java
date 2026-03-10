package logica.configuraciones;

import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class Estilo {

    public static final SimpleAttributeSet ALIADO = new SimpleAttributeSet();
    public static final SimpleAttributeSet ENEMIGO = new SimpleAttributeSet();
    public static final SimpleAttributeSet SISTEMA = new SimpleAttributeSet();

    public static void iniciar() {
        StyleConstants.setBold(ALIADO, true);
        StyleConstants.setForeground(ALIADO, Color.blue);

        StyleConstants.setBold(ENEMIGO, true);
        StyleConstants.setForeground(ENEMIGO, Color.red);

        StyleConstants.setBold(SISTEMA, true);
        StyleConstants.setForeground(SISTEMA, Color.black);
    }
}

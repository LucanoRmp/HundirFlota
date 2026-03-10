package ventanas;

import codigos.CodigoBoton;
import logica.controles.Control;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import logica.configuraciones.Assets;

public class Create extends JFrame {

    public Create(Control control) {
        super("Crear una partida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setIconImage(Assets.LOGO);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0, 50)));

        JLabel textCodigo = new JLabel(control.generarCodigo(), SwingConstants.CENTER);
        textCodigo.setAlignmentX(CENTER_ALIGNMENT);
        textCodigo.setFont(new Font("Serif", Font.BOLD, 30));

        panel.add(textCodigo);
        panel.add(Box.createRigidArea(new Dimension(0, 75)));

        //----------------------------------------------------------------------
        JButton volver = new JButton("Volver");
        volver.addActionListener(control.getControlBoton());
        volver.setAlignmentX(CENTER_ALIGNMENT);
        volver.setActionCommand(CodigoBoton.CANCELAR.toString());
        volver.setFocusable(false);
        volver.setMaximumSize(new Dimension(100, 40));

        panel.add(volver);

        add(panel);

        setVisible(true);
    }
}

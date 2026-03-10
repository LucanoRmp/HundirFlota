package ventanas;

import codigos.CodigoBoton;
import logica.controles.Control;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import logica.configuraciones.Assets;
import logica.configuraciones.Input;

public class Join extends JFrame {
    
    private final Control control;
    private final Input input;
    
    public Join(Control control) {
        super("Unirse a una partida");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setIconImage(Assets.LOGO);
        
        this.control = control;
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(Box.createRigidArea(new Dimension(0, 40)));

        JLabel titulo = new JLabel("Introduce el codigo");
        titulo.setFont(new Font("Arial", Font.PLAIN, 20));
        titulo.setHorizontalAlignment(SwingConstants.LEFT);
        titulo.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        //----------------------------------------------------------------------
        input = new Input("Introduce el codigo de la sala...");
        input.setFont(new Font("Serif", Font.PLAIN, 15));
        input.setMaximumSize(new Dimension(175, input.getPreferredSize().height + 10));

        panel.add(input);
        panel.add(Box.createRigidArea(new Dimension(0, 50)));

        //----------------------------------------------------------------------
        JPanel panelBtn = new JPanel();
        panelBtn.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 5));
        
        JButton enviar = new JButton("Enviar");
        enviar.setAlignmentX(CENTER_ALIGNMENT);
        enviar.setMaximumSize(new Dimension(100, enviar.getPreferredSize().width));
        enviar.setFocusable(false);
        
        panelBtn.add(enviar);
        
        JButton volver = new JButton("Volver");
        volver.setAlignmentX(CENTER_ALIGNMENT);
        volver.setFocusable(false);
        volver.setMaximumSize(new Dimension(100, 40));
        
        panelBtn.add(volver);
        
        panel.add(panelBtn);
        panel.add(Box.createRigidArea(new Dimension(0, 40)));
        
        enviar.addActionListener(this.control.getControlBoton());
        enviar.setActionCommand(CodigoBoton.CONEXION.toString());
        volver.addActionListener(this.control.getControlBoton());
        volver.setActionCommand(CodigoBoton.CANCELAR.toString());
        input.addActionListener(this.control.getControlBoton());
        input.setActionCommand(CodigoBoton.CONEXION.toString());
        
        add(panel);
        
        setVisible(true);
    }

    public String getInputText() {
        return input.getText();
    }

    public void setInputText(String text) {
        input.setText(text);
    }
}

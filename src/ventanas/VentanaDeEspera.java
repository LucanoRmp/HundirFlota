package ventanas;

import codigos.CodigoBoton;
import logica.controles.Control;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

public class VentanaDeEspera extends JDialog {
    
    private final JProgressBar progressBar;

    public VentanaDeEspera(JFrame parent, Control control) {
        super(parent, "Por Favor Espere");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(300, 150);
        setLayout(new BorderLayout());
        setResizable(false);
        setLocationRelativeTo(parent);

        JLabel mensaje = new JLabel("Por favor espere al otro usuario...", SwingConstants.CENTER);
        
        add(mensaje, BorderLayout.NORTH);
        
        JPanel panelBarra = new JPanel();
        panelBarra.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(false);
        
        
        panelBarra.add(progressBar);
        
        add(panelBarra);
        
        JPanel panelBoton = new JPanel();
        
        JButton cancelar = new JButton("Cancelar");
        cancelar.addActionListener(control.getControlBoton());
        cancelar.setActionCommand(CodigoBoton.CANCELAR.toString());
        
        panelBoton.add(cancelar);
        
        add(panelBoton, BorderLayout.SOUTH);
        
        addWindowListener(new WindowAdapter(){
            @Override
            public void windowClosed(WindowEvent e) {
                control.cancelarListo();
            }
        });
        
        setVisible(true);
    }
}

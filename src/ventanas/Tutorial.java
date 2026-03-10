package ventanas;

import assets.Barco;
import codigos.TipoBarco;
import logica.controles.Control;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import logica.configuraciones.Assets;

public class Tutorial extends JFrame {

    private final JPanel cabecera, cuerpo, pie;
    private final JTextArea descripcion;
    private ArrayList<Barco> flota;
    private int posBarco;

    public Tutorial(Control control) {
        super("Guia");
        setSize(750, 600);
        setLocationRelativeTo(null);
        setIconImage(Assets.LOGO);
        posBarco = 0;

        cabecera = new JPanel();
        add(cabecera, BorderLayout.NORTH);

        //----------------------------------------------------------------------
        cuerpo = new JPanel(new GridBagLayout());

        flota = new ArrayList<>();
        flota.add(new Barco(TipoBarco.PORTAAVIONES, control));
        flota.add(new Barco(TipoBarco.ACORAZADO, control));
        flota.add(new Barco(TipoBarco.CRUCERO, control));
        flota.add(new Barco(TipoBarco.SUBMARINO, control));
        flota.add(new Barco(TipoBarco.DESTRUCTOR, control));

        descripcion = new JTextArea();
        descripcion.setPreferredSize(new Dimension(500, 300));
        mostrarBarco();

        add(cuerpo);

        //----------------------------------------------------------------------
        pie = new JPanel();
        pie.setLayout(new BorderLayout());

        JButton back = new JButton("<<< Back");
        JButton next = new JButton("Next >>>");

        back.addActionListener((ActionEvent e) -> {
            posBarco--;
            mostrarBarco();

            if (posBarco == 0) {
                pie.remove(back);
                pie.revalidate();
                pie.repaint();

            } else if (posBarco < flota.size()) {
                pie.add(next, BorderLayout.EAST);
                pie.revalidate();
                pie.repaint();
            }
        });

        next.addActionListener((ActionEvent e) -> {
            posBarco++;
            mostrarBarco();

            if (posBarco == flota.size() - 1) {
                pie.remove(next);
                pie.revalidate();
                pie.repaint();

            } else if (posBarco > 0) {
                pie.add(back, BorderLayout.WEST);
                pie.revalidate();
                pie.repaint();
            }
        });

        pie.add(next, BorderLayout.EAST);
        pie.setBorder(BorderFactory.createEmptyBorder(25, 75, 25, 75));
        add(pie, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void mostrarBarco() {
        cuerpo.removeAll();
        cabecera.removeAll();

        Barco barco = flota.get(posBarco);
        barco.setFocusable(false);

        
        JLabel titulo = new JLabel();
        String nombre = barco.getTipo().toString();
        nombre = nombre.substring(0, 1) + nombre.substring(1).toLowerCase();
        titulo.setText(nombre);
        titulo.setFont(new Font("Arial", Font.BOLD, 40));
        cabecera.add(titulo);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weighty = 0.1;
        cuerpo.add(barco, gbc);

        descripcion.setText(barco.getDescripcion());
        descripcion.setLineWrap(true);
        descripcion.setWrapStyleWord(true);
        descripcion.setEditable(false);
        descripcion.setBackground(getBackground());
        descripcion.setFont(new Font("Arial", Font.BOLD, 20));
        descripcion.setFocusable(false);
        gbc.gridy = 2;
        gbc.weighty = 0.8;

        cuerpo.add(descripcion, gbc);

        cuerpo.revalidate();
        cuerpo.repaint();
        cabecera.revalidate();
        cabecera.repaint();
    }
}

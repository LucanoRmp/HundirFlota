package assets;

import codigos.CodigoBoton;
import logica.controles.Control;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import logica.configuraciones.Input;

public class Chat extends JPanel {

    private final JTextPane chat;
    private final Input input;
    private final StyledDocument doc;
    private final Control control;

    public Chat(Control control) {
        setPreferredSize(new Dimension(500, 750));
        setLayout(new BorderLayout());

        this.control = control;

        chat = new JTextPane() {
            @Override
            public boolean getScrollableTracksViewportWidth() {
                return true;
            }
        };
        chat.setBackground(Color.LIGHT_GRAY);
        chat.setFont(new Font("Arial", Font.PLAIN, 15));
        chat.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        chat.setOpaque(true);
        chat.setEditable(false);
        chat.setText("Chat conectado\n");

        doc = chat.getStyledDocument();

        JScrollPane scroll = new JScrollPane(chat);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(scroll);

        input = new Input("Escribe tu mensaje...");
        input.setPreferredSize(new Dimension(500, 50));
        input.setFont(new Font("Arial", Font.PLAIN, 15));
        input.setFocusable(true);

        add(input, BorderLayout.SOUTH);

        input.addActionListener(this.control.getControlBoton());
        input.setActionCommand(CodigoBoton.MENSAJE.toString());
    }
    
    public void redimension() {
        setPreferredSize(new Dimension(500, 900));
    }

    public void escribirChat(String mensaje, SimpleAttributeSet style) throws BadLocationException {
        int posNombre = mensaje.indexOf("]")+1;
        SimpleAttributeSet blackStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(blackStyle, Color.BLACK);
        
        doc.insertString(doc.getLength(), mensaje.substring(0, posNombre)+": ", style);

        doc.insertString(doc.getLength(), mensaje.substring(posNombre).strip() + "\n", blackStyle);

        chat.setCaretPosition(doc.getLength());
    }

    public String getInputText() {
        return input.getText();
    }

    public void setInputText(String text) {
        input.setText(text);
    }
}

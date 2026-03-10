package logica.configuraciones;

import codigos.Ruta;
import codigos.TipoBarco;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;

public class Assets {

    public static final Image LOGO = cargarImg(Ruta.LOGO);
    public static final Image MUTE = cargarImg(Ruta.MUTE);
    public static final Image SONIDO = cargarImg(Ruta.SONIDO);
    public static final Image AGUA = cargarImg(Ruta.AGUA);
    public static final Image TOCADO = cargarImg(Ruta.DISPARO);
    public static final Image RESISTIDO = cargarImg(Ruta.RESISTIDO);
    public static final ImageIcon VICTORIA = cargarIcon(Ruta.VICTORIA);
    public static final ImageIcon DERROTA = cargarIcon(Ruta.DERROTA);
    private static final ArrayList<Image> PORTAAVIONES = cargarImgs(TipoBarco.PORTAAVIONES);
    private static final ArrayList<Image> ACORAZADO = cargarImgs(TipoBarco.ACORAZADO);
    private static final ArrayList<Image> SUBMARINO = cargarImgs(TipoBarco.SUBMARINO);
    private static final ArrayList<Image> CRUCERO = cargarImgs(TipoBarco.CRUCERO);
    private static final ArrayList<Image> DESTRUCTOR = cargarImgs(TipoBarco.DESTRUCTOR);
    private static final ArrayList<ImageIcon> MAR = cargarImgs();

    private static Image cargarImg(Ruta ruta) {
        return new ImageIcon(Assets.class.getResource(ruta.toString())).getImage();
    }

    private static ImageIcon cargarIcon(Ruta ruta) {
        return new ImageIcon(Assets.class.getResource(ruta.toString()));
    }

    private static ArrayList<Image> cargarImgs(TipoBarco tipo) {
        ArrayList<Image> barco = new ArrayList<>();

        for (int i = 0; i < tipo.getLongitud(); i++) {
            if (i == 0) {
                barco.add(new ImageIcon(Assets.class.getResource(tipo.getRuta() + "/proa.png")).getImage());
            } else if (i == (tipo.getLongitud() - 1)) {
                barco.add(new ImageIcon(Assets.class.getResource(tipo.getRuta() + "/popa.png")).getImage());
            } else {
                barco.add(new ImageIcon(Assets.class.getResource(tipo.getRuta() + "/cuerpo" + i + ".png")).getImage());
            }
        }

        return barco.isEmpty() ? null : barco;
    }
    
    private static ArrayList<ImageIcon> cargarImgs() {
        ArrayList<ImageIcon> mar = new ArrayList<>();

        for (int i = 0; i < Datos.FILAS; i++) {
            for (int j = 1; j <= Datos.COLUMNAS; j++) {
                mar.add(new ImageIcon(Assets.class.getResource(Ruta.MAR.toString() + ((i * 10) + j) + ").png")));
            }
        }
        
        return mar.isEmpty() ? null : mar;
    }

    public static Image rotar(TipoBarco barco, int pos, int grados) {
        return rotar(obtenerBarco(barco, pos), grados);
    }

    private static Image rotar(Image img, int grados) {
        int lado = img.getWidth(null);

        BufferedImage copia = new BufferedImage(lado, lado, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = copia.createGraphics();

        AffineTransform at = new AffineTransform();
        at.rotate(Math.toRadians(grados), lado / 2.0, lado / 2.0);
        g2d.transform(at);

        g2d.drawImage(img, 0, 0, lado, lado, null);

        g2d.dispose();

        return copia;
    }

    public static Image obtenerBarco(TipoBarco barco, int pos) {
        return switch (barco) {
            case PORTAAVIONES -> {
                yield PORTAAVIONES.get(pos);
            }

            case ACORAZADO -> {
                yield ACORAZADO.get(pos);
            }

            case SUBMARINO -> {
                yield SUBMARINO.get(pos);
            }

            case CRUCERO -> {
                yield CRUCERO.get(pos);
            }

            case DESTRUCTOR -> {
                yield DESTRUCTOR.get(pos);
            }

            default -> {
                yield null;
            }
        };
    }
    
    public static ImageIcon obtenerMar(int pos){
        return MAR.get(pos);
    }
}

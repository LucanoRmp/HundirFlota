package logica.gestores;

import assets.Barco;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;
import javax.swing.Timer;
import codigos.CodigoDisparo;
import codigos.TipoBarco;
import logica.controles.Control;

public class GestorTablero {

    private HashMap<String, Barco> tablero;
    private int barcosVivos;
    private Control control;

    public GestorTablero(Control control) {
        tablero = new HashMap<>();
        barcosVivos = 5;
        this.control = control;
    }

    public void agregarBarco(Barco barco, String coords) {
        if (tablero.values().contains(barco)) {
            for (int i = 0; i < barco.getLongitud(); i++) {
                tablero.values().remove(barco);
            }
        }

        for (String coord : coords.split(",")) {
            tablero.put(coord, barco);
        }
    }

    public String disparo(String ataque) {
        HashSet<String> resultado = new HashSet<>();
        int fuerza = Integer.parseInt(ataque.charAt(ataque.indexOf(":") + 1) + "");
        ataque = ataque.substring(0, ataque.indexOf(":"));
        String[] casillas = ataque.split(",");

        for (int i = 0; i < casillas.length; i++) {
            String casilla = casillas[i];
            Barco barco = tablero.get(casilla);

            if (barco == null) {
                resultado.add(casilla + ":" + CodigoDisparo.AGUA);
            } else {
                if (barco.getTipo() == TipoBarco.CRUCERO && i != 0) {
                    resultado.add(casilla + ":" + CodigoDisparo.DESTRUIDO);
                } else {
                    String disparo = barco.recibirDanyo(casilla, fuerza) + "";

                    if (barco.hundido()) {
                        resultado.add(casilla + ":" + CodigoDisparo.HUNDIDO);
                        barcosVivos--;
                    } else {
                        resultado.add(casilla + ":" + disparo);
                    }

                    Timer t = new Timer(16, arg -> {
                        barco.repaint();
                    });
                    t.setRepeats(false);
                    t.start();
                }
            }
        }

        return String.join(",", resultado);
    }

    public String sonar(String ataque) {
        boolean deteccion = false;
        int i = 0;
        String[] casillas = ataque.split(",");
        
        while (!deteccion && i < casillas.length) {
            Barco barco = tablero.get(casillas[i]);
            
            if (barco != null && barco.getTipo() != TipoBarco.SUBMARINO) {
                deteccion = true;
            }

            i++;
        }

        return String.valueOf(deteccion);
    }

    public boolean estaDerrotado() {
        return barcosVivos == 0;
    }

    public String getCoordInicio(Barco barco) {
        return buscarBarco(barco).getFirst();
    }

    private ArrayList<String> buscarBarco(Barco barco) {
        ArrayList<String> casillas = new ArrayList<>();

        for (Map.Entry<String, Barco> entrada : tablero.entrySet()) {
            if (entrada.getValue() == barco) {
                casillas.add(entrada.getKey());
            }
        }

        casillas.sort(Comparator.naturalOrder());

        return casillas;
    }

    public ArrayList<Barco> getHundidos() {
        if (barcosVivos == 5) {
            return null;
        }

        return (ArrayList<Barco>) tablero.values()
                .stream()
                .distinct()
                .filter(b -> b.hundido())
                .collect(Collectors.toList());
    }
}

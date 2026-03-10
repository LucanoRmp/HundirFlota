package logica.configuraciones;

import codigos.Efecto;
import codigos.Ruta;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sonido {

    private final Map<Efecto, Clip> SONIDOS;

    public Sonido() {
        SONIDOS = new HashMap<>();
        cargarSonidos();
    }

    private Clip cargarSonido(Efecto efecto) {
        Clip sonido = null;
        Ruta ruta = switch(efecto){
            case FONDO ->{
                yield Ruta.SONIDO_FONDO;
            }
            
            case DISPARO ->{
                yield Ruta.SONIDO_DISPARO;
            }
            
            case RESISTIDO ->{
                yield Ruta.SONIDO_RESISTIDO;
            }
            
            case AGUA ->{
                yield Ruta.SONIDO_AGUA;
            }
            
            case DESTRUIDO ->{
                yield Ruta.SONIDO_DESTRUIDO;
            }
            
            case ENCONTRADO ->{
                yield Ruta.SONIDO_ENCONTRADO;
            }
            
            case NO_ENCONTRADO ->{
                yield Ruta.SONIDO_NO_ENCONTRADO;
            }
            
            case VICTORIA ->{
                yield Ruta.SONIDO_VICTORIA;
            }
            
            case DERROTA ->{
                yield Ruta.SONIDO_DERROTA;
            }
            
            case HUNDIDO ->{
                yield Ruta.SONIDO_HUNDIDO;
            }
            
            default ->{
                yield null;
            }  
        };
        
        if (ruta == null) {
            return sonido;
        }
        
        try (AudioInputStream sonidoStream = AudioSystem.getAudioInputStream(new BufferedInputStream(Sonido.class.getResourceAsStream(ruta.toString())))) {

            sonido = AudioSystem.getClip();
            sonido.open(sonidoStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            System.out.println(ex.getMessage());
        }

        return sonido;
    }

    private void cargarSonidos() {
        for (Efecto efecto : Efecto.values()) {
            SONIDOS.put(efecto, cargarSonido(efecto));
        }
    }

    public void reproducir(Efecto efecto) {
        Clip sonido = SONIDOS.get(efecto);

        if (sonido != null) {
            if (sonido.isRunning()) {
                sonido.stop();
            }
            sonido.setFramePosition(0);

            if (efecto == Efecto.FONDO) {
                sonido.loop(Clip.LOOP_CONTINUOUSLY);
                sonido.start();
            } else {
                sonido.loop(0);
                sonido.start();
            }
        }
    }

    public void detener(Efecto efecto) {
        Clip sonido = SONIDOS.get(efecto);

        if (sonido != null && sonido.isRunning()) {
            sonido.stop();
        }
    }
}

package logica.configuraciones;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Seguridad {

    public static String encriptar(String password) {
        String passwordSecure = password + "RafuTofu 14/05/25";
        String hash = "";
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytesPw = md.digest(passwordSecure.getBytes());
            StringBuilder sb = new StringBuilder();
            
            for (byte b : bytesPw) {
                sb.append(String.format("%02x", b));
            }
            
            hash = sb.toString();
            
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Error encriptando - " + ex.getMessage());
        }
        return hash;
    }
}

package Actividad2;

import javax.crypto.*;
import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Cliente {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int puerto = 5555;

        try (Socket socketServidor = new Socket(host, puerto)) {
            // Recibimos la clave del servidor
            ObjectInputStream in = new ObjectInputStream(socketServidor.getInputStream());
            SecretKey secretKey = (SecretKey) in.readObject();

            // Leemos el fichero que queremos cifrar
            byte[] ficheroBytes = Files.readAllBytes(Paths.get("fichero_original.txt"));

            // Ciframos el fichero
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] ficheroCifrado = cipher.doFinal(ficheroBytes);

            // Enviamos el fichero cifrado al servidor
            ObjectOutputStream out = new ObjectOutputStream(socketServidor.getOutputStream());
            out.writeObject(ficheroCifrado);
            out.flush();

            System.out.println("Fichero cifrado enviado al servidor.");

            socketServidor.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

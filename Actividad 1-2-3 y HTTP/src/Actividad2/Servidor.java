package Actividad2;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import java.net.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Servidor {
    public static void main(String[] args) throws Exception {
        int puerto = 5555;

        // Generamos la clave simétrica DES
        KeyGenerator keyGenerator = KeyGenerator.getInstance("DES");
        SecretKey secretKey = keyGenerator.generateKey();

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor esperando conexiones...");
            Socket socketCliente = serverSocket.accept(); // Aceptamos la conexión del cliente

            // Enviamos la clave al cliente
            ObjectOutputStream out = new ObjectOutputStream(socketCliente.getOutputStream());
            out.writeObject(secretKey);
            out.flush();

            // Esperamos a recibir el fichero cifrado
            ObjectInputStream in = new ObjectInputStream(socketCliente.getInputStream());
            byte[] ficheroCifrado = (byte[]) in.readObject();

            // Desciframos el fichero
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] ficheroDescifrado = cipher.doFinal(ficheroCifrado);

            // Guardamos el fichero descifrado (como ejemplo, directamente en el sistema de archivos)
            try (FileOutputStream fos = new FileOutputStream("fichero_descifrado.txt")) {
                fos.write(ficheroDescifrado);
            }

            System.out.println("Fichero descifrado y guardado como 'fichero_descifrado.txt'.");

            socketCliente.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

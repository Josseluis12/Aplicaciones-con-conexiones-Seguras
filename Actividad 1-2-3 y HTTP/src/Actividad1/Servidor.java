package Actividad1;

import javax.crypto.Cipher;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyPair;
import java.security.KeyPairGenerator;

public class Servidor {
    public static void main(String[] args) throws Exception {
        int puerto = 5555;

        // Generar par de claves RSA
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.genKeyPair();

        // Crear socket de servidor y esperar a un cliente
        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Servidor esperando conexiones...");
            Socket socketCliente = serverSocket.accept();

            // Enviar clave pública al cliente
            ObjectOutputStream oos = new ObjectOutputStream(socketCliente.getOutputStream());
            oos.writeObject(keyPair.getPublic());
            oos.flush();

            // Recibir mensaje cifrado del cliente
            DataInputStream dis = new DataInputStream(socketCliente.getInputStream());
            int length = dis.readInt();
            byte[] mensajeCifrado = null;
            if(length>0) {
                mensajeCifrado = new byte[length];
                dis.readFully(mensajeCifrado, 0, mensajeCifrado.length);
            }

            // Descifrar mensaje
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
            byte[] mensajeDescifrado = cipher.doFinal(mensajeCifrado);

            // Mostrar mensaje descifrado
            System.out.println("Mensaje recibido y descifrado: " + new String(mensajeDescifrado));

            socketCliente.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

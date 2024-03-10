package Actividad1;

import javax.crypto.Cipher;
import java.io.*;
import java.net.Socket;
import java.security.PublicKey;

public class Cliente {
    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int puerto = 5555;

        // Conectar al servidor y recibir la clave pública
        try (Socket socketServidor = new Socket(host, puerto)) {
            ObjectInputStream ois = new ObjectInputStream(socketServidor.getInputStream());
            PublicKey publicKey = (PublicKey) ois.readObject();

            // Mensaje a cifrar
            String mensaje = "Este es un mensaje secreto";
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] mensajeCifrado = cipher.doFinal(mensaje.getBytes());

            // Enviar mensaje cifrado al servidor
            DataOutputStream dos = new DataOutputStream(socketServidor.getOutputStream());
            dos.writeInt(mensajeCifrado.length);
            dos.write(mensajeCifrado);

            System.out.println("Mensaje cifrado enviado al servidor.");

            socketServidor.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

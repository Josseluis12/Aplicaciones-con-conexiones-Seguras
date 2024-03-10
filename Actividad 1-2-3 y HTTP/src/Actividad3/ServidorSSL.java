package Actividad3;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class ServidorSSL {

    public static void main(String[] args) {
        int puerto = 5555;

        // Configura el keystore y el truststore
        System.setProperty("javax.net.ssl.keyStore", "serverKeystore.jks");
        char[] keyStorePassword = "changeit".toCharArray(); // Contraseña del keystore
        KeyStore keyStore;

        // Crea y configura el SSLContext para usarlo en el SSLSocket
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyStore = KeyStore.getInstance("JKS");
            String keystorePath = "serverKeystore.jks"; // Ruta relativa al archivo serverKeystore.jks
            try (FileInputStream fis = new FileInputStream(keystorePath)) {
                keyStore.load(fis, keyStorePassword);
            } catch (IOException | CertificateException e) {
                e.printStackTrace();
                return;
            }
            keyManagerFactory.init(keyStore, keyStorePassword);
            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyStoreException | UnrecoverableKeyException | KeyManagementException e) {
            e.printStackTrace();
            return;
        }

        // Configura el SSLServerSocket
        SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
        try (SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(puerto)) {
            System.out.println("Servidor SSL esperando conexiones...");
            try (SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept()) {
                // Aquí pondrías tu lógica para manejar la conexión, como en la actividad 1
                // Por ejemplo, leer un mensaje cifrado enviado por el cliente y descifrarlo
                InputStream input = sslSocket.getInputStream();
                ObjectInputStream objectInput = new ObjectInputStream(input);
                // Suponiendo que el cliente envía un objeto Message
                byte[] encryptedMessage = (byte[]) objectInput.readObject();
                // Suponiendo que tienes la clave privada del servidor cargada en alguna variable
                PrivateKey privateKey;
                try {
                    privateKey = (PrivateKey) keyStore.getKey("serverKey", keyStorePassword);
                } catch (NoSuchAlgorithmException | UnrecoverableKeyException | KeyStoreException e) {
                    e.printStackTrace();
                    return;
                }

                byte[] decryptedData;
                try {
                    decryptedData = CryptoUtils.decrypt(encryptedMessage, privateKey);
                } catch (NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                    e.printStackTrace();
                    return;
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
                String decryptedMessage = new String(decryptedData);
                System.out.println("Mensaje recibido del cliente: " + decryptedMessage);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

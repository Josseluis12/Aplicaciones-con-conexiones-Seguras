package Actividad3;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.net.ssl.*;
import java.io.*;
import java.security.*;
import java.security.cert.CertificateException;

public class ClienteSSL {
    public static void main(String[] args) {
        String host = "localhost";
        int puerto = 5555;

        try {
            // Carga el keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            char[] keyStorePassword = "changeit".toCharArray();
            String keystorePath = "serverKeystore.jks"; // Ruta relativa al archivo serverKeystore.jks
            try (FileInputStream fis = new FileInputStream(keystorePath)) {
                keyStore.load(fis, keyStorePassword);
            } catch (CertificateException | IOException e) {
                e.printStackTrace();
                return;
            }

            // Configura el SSLSocket
            System.setProperty("javax.net.ssl.trustStore", "truststore.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
            SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            try (SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, puerto)) {
                // Aquí pondrías tu lógica para comunicarte con el servidor, como en la actividad 1
                // Por ejemplo, cifrar un mensaje y enviarlo al servidor
                OutputStream output = sslSocket.getOutputStream();
                ObjectOutputStream objectOutput = new ObjectOutputStream(output);
                // Suponiendo que tienes una clase Message y un método para cifrarla
                String message = "Este es un mensaje secreto";
                // Suponiendo que tienes la clave pública del servidor cargada en alguna variable
                PublicKey publicKey = (PublicKey) keyStore.getCertificate("serverKey").getPublicKey();
                byte[] encryptedMessage = CryptoUtils.encrypt(message, publicKey);
                objectOutput.writeObject(encryptedMessage);
                objectOutput.flush();
            } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException | KeyStoreException e) {
            e.printStackTrace();
        }
    }
}

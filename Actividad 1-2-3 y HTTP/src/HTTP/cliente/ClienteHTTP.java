package HTTP.cliente;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Cliente HTTP para realizar solicitudes a un servidor.
 *
 * Autor: Pelu
 */
public class ClienteHTTP {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Por favor, proporciona una URL como parámetro.");
            return;
        }

        String urlString = args[0];

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Realizar la petición HTTP
            int responseCode = connection.getResponseCode();
            System.out.println("Código de respuesta: " + responseCode);

            // Mostrar las cabeceras recibidas desde el servidor
            System.out.println("Cabeceras:");
            connection.getHeaderFields().forEach((key, value) -> System.out.println(key + ": " + value));

            // Leer el contenido de la respuesta
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                    response.append("\n");
                }

                // Mostrar el contenido de la respuesta
                System.out.println("Contenido de la respuesta:\n" + response.toString());

                // Almacenar el contenido en un archivo si es de tipo "text/HTML"
                String contentType = connection.getHeaderField("Content-Type");
                if (contentType != null && contentType.startsWith("text/html")) {
                    try (BufferedWriter writer = new BufferedWriter(new FileWriter("salida.html"))) {
                        writer.write(response.toString());
                        System.out.println("Contenido almacenado en salida.html");
                    }
                }
            }

            // Cerrar la conexión
            connection.disconnect();
        } catch (MalformedURLException e) {
            System.out.println("La URL proporcionada no es válida.");
        } catch (IOException e) {
            System.out.println("Error al realizar la conexión: " + e.getMessage());
        }
    }
}

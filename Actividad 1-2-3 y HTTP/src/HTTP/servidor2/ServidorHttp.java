package HTTP.servidor2;

import com.sun.net.httpserver.HttpServer;
import HTTP.servidor1.ManejadorSaludos;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Servidor HTTP para manejar solicitudes en un puerto especificado.
 * Crea contextos para manejar saludos y operaciones de calculadora.
 *
 * Autor: Pelu
 */
public class ServidorHttp {
    public static void main(String[] args) {
        // Verificar si se proporcionó un puerto como argumento
        int puerto = 80;  // Puerto predeterminado
        if (args.length > 0) {
            try {
                puerto = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("El puerto proporcionado no es válido. Usando el puerto predeterminado (80).");
            }
        }

        try {
            // Crear e iniciar el servidor HTTP
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(puerto), 0);

            // Crear y asignar contexto "/saludar"
            httpServer.createContext("/saludar", new ManejadorSaludos());
            httpServer.createContext("/calculadora", new HttpHandlerCalculadora());
            // Iniciar el servidor
            httpServer.start();

            System.out.println("Servidor HTTP iniciado en el puerto " + puerto);
        } catch (IOException e) {
            System.out.println("Error al iniciar el servidor HTTP: " + e.getMessage());
        }
    }
}

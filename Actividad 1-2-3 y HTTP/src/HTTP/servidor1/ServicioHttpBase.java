package HTTP.servidor1;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Servidor HTTP básico para manejar peticiones en un puerto definido.
 * Configura un manejador para responder a las rutas especificadas.
 *
 * Autor: Pelu
 */
public class ServicioHttpBase {

    public static void main(String[] args) {
        // Si se da un puerto por línea de comandos, úsalo; si no, puerto 80.
        int puerto = args.length > 0 ? Integer.parseInt(args[0]) : 80;

        try {
            // Configuración y arranque del servidor HTTP
            HttpServer servidor = HttpServer.create(new InetSocketAddress(puerto), 0);

            // Define un contexto para la ruta "/saludar"
            servidor.createContext("/saludar", new ManejadorSaludos());

            // Pone en marcha el servidor HTTP
            servidor.start();

            System.out.println("Servidor HTTP corriendo en el puerto " + puerto);
        } catch (NumberFormatException | IOException ex) {
            System.out.println("Hubo un error al iniciar el servidor HTTP: " + ex.getMessage());
        }
    }
}

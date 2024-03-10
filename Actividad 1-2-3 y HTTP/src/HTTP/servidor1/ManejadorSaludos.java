package HTTP.servidor1;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import HTTP.utilidades.Herramientas;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

/**
 * Esta clase maneja las peticiones HTTP para el contexto "/saludar" del servidor.
 * Genera un saludo personalizado basado en los parámetros de la solicitud.
 *
 * Autor: Pelu
 */
public class ManejadorSaludos implements HttpHandler {

    @Override
    public void handle(HttpExchange intercambio) throws IOException {
        // Obtener la URI solicitada
        URI uri = intercambio.getRequestURI();

        // Formatear la fecha y hora actuales
        String momentoActual = Herramientas.obtenerFechaHora(); // Utiliza el método obtenerFechaHora() de la clase Herramientas

        // Obtener y registrar la petición
        String rutaSolicitud = uri.toString();
        System.out.println("[" + momentoActual + "] Petición recibida: " + rutaSolicitud);

        // Analizar los parámetros de la URI
        String consulta = uri.getQuery();
        String respuesta;

        // Chequear si los parámetros son los esperados
        if (consulta != null && consulta.matches("nombre=.*&apellido=.*")) {
            // Extraer nombre y apellido de la consulta
            String nombre = extraerParametro(consulta, "nombre");
            String apellido = extraerParametro(consulta, "apellido");

            // Formular la respuesta
            respuesta = "Qué onda, " + nombre + " " + apellido + "!";
        } else {
            // Respuesta por defecto
            respuesta = "Qué tal! No reconozco tu nombre.";
        }

        // Establecer las cabeceras y enviar la respuesta
        intercambio.getResponseHeaders().set("Content-Type", "text/plain");
        intercambio.sendResponseHeaders(200, respuesta.getBytes().length);

        // Emitir la respuesta
        try (OutputStream salida = intercambio.getResponseBody()) {
            salida.write(respuesta.getBytes());
        }

        // Confirmar la respuesta enviada
        System.out.println("[" + momentoActual + "] Respuesta enviada: " + rutaSolicitud + " -> " + respuesta);
    }

    private String extraerParametro(String consulta, String parametro) {
        // Dividir la consulta para obtener el valor del parámetro especificado
        String[] pares = consulta.split("&");
        for (String par : pares) {
            String[] entrada = par.split("=");
            if (entrada.length == 2 && entrada[0].equals(parametro)) {
                return entrada[1];
            }
        }
        return "Desconocido";
    }
}

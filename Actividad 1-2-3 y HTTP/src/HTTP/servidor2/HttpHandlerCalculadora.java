package HTTP.servidor2;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import HTTP.utilidades.Herramientas;
import HTTP.utilidades.MiniCalculadora;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador de solicitudes HTTP para el contexto "/calculadora" del servidor.
 * Realiza operaciones aritméticas básicas utilizando los parámetros de la solicitud.
 *
 * Autor: Pelu
 */
public class HttpHandlerCalculadora implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // Obtener la URI de la petición
        URI uri = exchange.getRequestURI();

        // Obtener la fecha y hora actual formateada
        String fechaHoraActual = Herramientas.obtenerFechaHora();

        // Obtener la petición y mostrarla por consola
        String peticion = uri.toString();
        System.out.println("[" + fechaHoraActual + "] Atendiendo a petición: " + peticion);

        // Obtener los parámetros de la URI
        String query = uri.getQuery();
        String respuesta;

        // Verificar si la query tiene el formato esperado
        if (query != null) {
            // Parsear los parámetros de la query
            Map<String, String> queryParams = obtenerParametros(query);

            // Verificar si los parámetros son válidos
            if (isValidRequest(queryParams)) {
                // Realizar la operación y generar la respuesta
                respuesta = performCalculation(queryParams);
            } else {
                // Responder con un mensaje de error
                respuesta = "Petición no válida";
            }
        } else {
            // Responder con un mensaje de error para casos donde no hay parámetros
            respuesta = "Petición no válida";
        }

        // Configurar las cabeceras de la respuesta
        exchange.getResponseHeaders().set("Content-Type", "text/plain");
        exchange.sendResponseHeaders(200, respuesta.getBytes(StandardCharsets.UTF_8).length);

        // Escribir la respuesta al cuerpo de la respuesta
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(respuesta.getBytes());
        }

        // Mostrar la respuesta por consola
        System.out.println("[" + fechaHoraActual + "] Respuesta a la petición: " + peticion + " -> " + respuesta);
    }

    private Map<String, String> obtenerParametros(String query) {
        // Método para obtener los parámetros de la query
        Map<String, String> queryParams = new HashMap<>();
        String[] partes = query.split("&");
        for (String parte : partes) {
            String[] keyValue = parte.split("=");
            if (keyValue.length == 2) {
                queryParams.put(keyValue[0], keyValue[1]);
            }
        }
        return queryParams;
    }

    private boolean isValidRequest(Map<String, String> queryParams) {
        // Verificar si los parámetros necesarios están presentes
        return queryParams.containsKey("op1") && queryParams.containsKey("op2") && queryParams.containsKey("ope");
    }

    private String performCalculation(Map<String, String> queryParams) {
        // Realizar la operación utilizando la clase MiniCalculadora
        try {
            double op1 = Double.parseDouble(queryParams.get("op1"));
            double op2 = Double.parseDouble(queryParams.get("op2"));
            char operation = queryParams.get("ope").charAt(0);

            MiniCalculadora calculadora = new MiniCalculadora(op1, op2, operation);
            return calculadora.getResultado();
        } catch (NumberFormatException e) {
            return "Error en los operandos";
        }
    }
}

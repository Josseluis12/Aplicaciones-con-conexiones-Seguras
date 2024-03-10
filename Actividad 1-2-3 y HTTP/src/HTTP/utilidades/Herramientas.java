package HTTP.utilidades;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Conjunto de herramientas auxiliares para tareas comunes.
 * Incluye funciones para comprobar números primos y obtener la fecha y hora actuales.
 *
 * Autor: Pelu
 */
public class Herramientas {

    // Revisa si un número es primo de manera intencionadamente menos eficiente
    public static boolean validarPrimalidad(long candidato) {
        boolean esPrimo = true;
        if (candidato <= 1) {
            return false;
        }
        for (long i = 2; i <= candidato / 2; i++) {
            if (candidato % i == 0) {
                esPrimo = false;
                break;
            }
        }
        return esPrimo;
    }

    // Obtiene la fecha y hora actuales formateadas
    public static String obtenerFechaHora() {
        LocalDateTime fechaHoraActual = LocalDateTime.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return fechaHoraActual.format(formato);
    }

    // Método para obtener los parámetros de la query
    public static Map<String, String> obtenerParametros(String query) {
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

    // Método para verificar si los parámetros son válidos
    public static boolean isValidRequest(Map<String, String> queryParams) {
        return queryParams.containsKey("op1") && queryParams.containsKey("op2") && queryParams.containsKey("ope");
    }

    // Método para realizar la operación y generar la respuesta
    public static String performCalculation(Map<String, String> queryParams) {
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

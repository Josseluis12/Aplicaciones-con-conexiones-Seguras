package HTTP.utilidades;

/**
 * Una calculadora simple que puede sumar, restar, multiplicar y dividir.
 *
 * Autor: Pelu
 */
public class MiniCalculadora {
    private double op1;
    private double op2;
    private char operacion;
    private String resultado;

    public MiniCalculadora(double op1, double op2, char operacion) {
        this.op1 = op1;
        this.op2 = op2;
        this.operacion = operacion;
        calcular();
    }

    private void calcular() {
        switch (operacion) {
            case '+':
                resultado = String.valueOf(op1 + op2);
                break;
            case '-':
                resultado = String.valueOf(op1 - op2);
                break;
            case '*':
                resultado = String.valueOf(op1 * op2);
                break;
            case '/':
                if (op2 != 0) {
                    resultado = String.valueOf(op1 / op2);
                } else {
                    resultado = "Error: División por cero";
                }
                break;
            default:
                resultado = "Error: Operación no válida";
                break;
        }
    }

    public String getResultado() {
        return resultado;
    }
}

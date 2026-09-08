import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Desvio {
    public static void main(String[] args) {
        Scanner scn = new Scanner(System.in);

        List<List<Double>> conjuntos = List.of(
                List.of(2.0, 4.0, 6.0, 8.0, 10.0),
                List.of(1.0, 3.0, 5.0, 7.0, 9.0),
                List.of(12.0, 15.0, 18.0, 20.0, 22.0),
                List.of(7.5, 8.2, 9.1, 7.8, 8.5),
                List.of(23.4, 23.1, 23.6, 23.3, 23.2),
                List.of(25.0, 30.0, 28.0, 32.0, 29.0)
            );

        for (List<Double> conjunto : conjuntos) {
            double desvio = calcularDesvioPadrao(conjunto);
            
            System.out.println("conjunto: "+conjunto);
            System.out.println("desvio padrão: " + desvio);
            
        }


        scn.close();
    }

    public static double calcularDesvioPadrao(List<Double> conjunto) {
        double media = calcularMedia(conjunto);
        double desvio = 0;
        int somaDosQuadrados = 0;

        for (double i : conjunto) {
            somaDosQuadrados += (i - media) * (i - media);
        }

        desvio = Math.sqrt(somaDosQuadrados / conjunto.size());

        return desvio;
    }

    public static double calcularMedia(List<Double> conjunto) {
        return conjunto.stream().reduce(0.00, Double::sum) / conjunto.size();
    }
}
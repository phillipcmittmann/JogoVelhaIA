package jogovelhaia;

import java.util.ArrayList;

class Neuronio {
    private ArrayList<Double> entradas;

    public Neuronio(ArrayList<Double> entradas) {
        this.entradas = entradas;
    }

    public double calcularSaida(ArrayList<Double> pesos) {
        if (entradas.size() != pesos.size()) {
            throw new IllegalArgumentException("O número de entradas deve ser igual ao número de pesos.");
        }

        double soma = 0.0;
        
        for (int i = 0; i < entradas.size(); i++) {
            soma += entradas.get(i) * pesos.get(i);
        }
        
        return 1 / (1 + Math.exp(-soma)); // Função sigmoide
    }
}

package jogovelhaia;

public class Neuronio {
    public double[] pesos;
    public double bias;

    public Neuronio(double[] pesos) {
        this.pesos = new double[9];
        this.bias = (Math.random() * 3) - 1;
        
        for (int i = 0; i < pesos.length; i++) {
            this.pesos[i] = pesos[i];
        }
    }

 // Inicialização dos pesos com distribuição normal de He
    public Neuronio() {
        this.pesos = new double[9];
        this.bias = (Math.random() * 3) - 1;  // Inicializa o viés com valores entre -1 e 2
        
        // Usando distribuição normal de He (para ReLU)
        for (int i = 0; i < 9; i++) {
            this.pesos[i] = Math.random() * Math.sqrt(2.0 / 9);  // Normalização para ReLU
        }
    }

    public double relu(double x) {
        return Math.max(0, x);  // Aplica a função ReLU (retorna 0 para valores negativos e x para positivos)
    }

    // Método para calcular a saída com base nas entradas
    public double[] calcularSaida(double[] entradas) {
        if (entradas.length != 9) {
            throw new IllegalArgumentException("O número de entradas deve ser igual ao número de pesos.");
        }

        double[] saidas = new double[9];  // Vetor de saídas, uma para cada posição do tabuleiro

        for (int i = 0; i < 9; i++) {
            // Calcula a saída para cada posição
            double soma = entradas[i] * pesos[i] + bias;  // Soma ponderada de cada entrada
            saidas[i] = relu(soma);  // Aplica a função sigmoide para normalizar a saída
        }

        return saidas;  // Retorna o vetor de saídas para as 9 posições do tabuleiro
    }

    public double[] getPesos() {
        return pesos;
    }

    public void setPesos(double[] pesos) {
        this.pesos = pesos;
    }

    public double getBias() {
        return bias;
    }

    public void setBias(double bias) {
        this.bias = bias;
    }

    // Método de mutação: altera aleatoriamente um dos pesos
    public void mutarPeso(int indice) {
        double novoPeso = (Math.random() * 2) - 1;
        pesos[indice] = novoPeso; 
    }

    // Método de crossover entre dois neurônios
    public static Neuronio crossover(Neuronio pai1, Neuronio pai2) {
        double[] novosPesos = new double[9];
        for (int i = 0; i < 9; i++) {
            novosPesos[i] = Math.random() < 0.5 ? pai1.pesos[i] : pai2.pesos[i];
        }
        return new Neuronio(novosPesos);
    }
}

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

    public Neuronio() {
    	this.pesos = new double[9];
        this.bias = (Math.random() * 2) - 1;

        for (int i = 0; i < 9; i++) {
            this.pesos[i] = (Math.random() * 2) - 1;
        }
    }

    public double sigmoide(double x) {
        return 1 / (1 + Math.exp(-x));
    }

    public static int escolherMelhorJogada(Individuo individuo, int[][] tabuleiro) {
        double[] jogada = individuo.getNeuronio().calcularSaida(tabuleiroParaVetor(tabuleiro));

        int melhorPosicao = -1;
        double melhorValor = -Double.MAX_VALUE;

        for (int i = 0; i < jogada.length; i++) {
            if (tabuleiro[i / 3][i % 3] == 0 && jogada[i] > melhorValor) {
                melhorValor = jogada[i];
                melhorPosicao = i;
            }
        }

        return melhorPosicao;
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
    
    public static double[] tabuleiroParaVetor(int[][] tabuleiro) {
        double[] vetor = new double[9];
        int index = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                vetor[index++] = tabuleiro[i][j];
            }
        }
        return vetor;
    }

    public double[] calcularSaida(double[] entradas) {
        double[] saida = new double[9];
        double soma;

        for (int i = 0; i < 9; i++) {
            soma = 0.0;
            soma += entradas[i] * pesos[i];
            soma += bias;

            saida[i] = sigmoide(soma);
        }

        return saida;
    }
}

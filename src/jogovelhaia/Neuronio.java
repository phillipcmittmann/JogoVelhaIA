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
        this.bias = (Math.random() * 2) - 1;  // Inicializa o viés com valores entre -1 e 1 (não entre -1 e 2)

        // Usando distribuição normal de Xavier (para Sigmoid)
        for (int i = 0; i < 9; i++) {
            this.pesos[i] = (Math.random() * 2) - 1;  // Inicializa os pesos com valores entre -1 e 1
        }
    }

    public double sigmoide(double x) {
        return 1 / (1 + Math.exp(-x));  // Função sigmoide
    }

    public static int escolherMelhorJogada(Individuo individuo, int[][] tabuleiro) {
        double[] jogada = individuo.getNeuronio().calcularSaida(tabuleiroParaVetor(tabuleiro));

        int melhorPosicao = -1;
        double melhorValor = -Double.MAX_VALUE;

        // Itera sobre as saídas da rede neural e escolhe a posição com o maior valor de saída
        for (int i = 0; i < jogada.length; i++) {
            // Verifica se a posição está vazia e se a jogada é melhor
            if (tabuleiro[i / 3][i % 3] == 0 && jogada[i] > melhorValor) {
                melhorValor = jogada[i];
                melhorPosicao = i;
            }
        }

        return melhorPosicao;  // Retorna a melhor posição
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
        double[] saida = new double[9];  // A saída tem 9 valores, correspondendo às 9 células do tabuleiro
        double soma;

        // Para cada posição do tabuleiro, calcula a soma ponderada (peso * entrada) + viés
        for (int i = 0; i < 9; i++) {
            soma = 0.0;
            soma += entradas[i] * pesos[i];  // Soma ponderada das entradas
            soma += bias;  // Adiciona o viés

            // Aplica a função sigmoide para gerar o valor de saída
            saida[i] = sigmoide(soma);  // Função de ativação para cada saída
        }

        return saida;  // Retorna o vetor de saídas
    }
}

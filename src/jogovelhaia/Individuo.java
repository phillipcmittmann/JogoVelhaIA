package jogovelhaia;

import java.util.Random;

public class Individuo {
    private Neuronio neuronio;
    private double avaliacao; // 1 para vitória, 0 para empate, -1 para derrota

    public Individuo(double[] pesos) {
        neuronio = new Neuronio(pesos);
        this.avaliacao = 0;
    }

    public Individuo() {
        Random rand = new Random();
        double[] pesos = new double[9];
        for (int i = 0; i < 9; i++) {
            pesos[i] = (rand.nextDouble() * 2) - 1;
        }
        neuronio = new Neuronio(pesos);
        this.avaliacao = 0;
    }

    // revisar isso
    // Avalia o desempenho do indivíduo com base no jogo da velha
    public void avaliar(double[] entradas, int[][] tabuleiro) {
        // Obtém o vetor de saídas da rede neural para as 9 posições
        double[] saídas = neuronio.calcularSaida(entradas);

        // Encontrar a melhor jogada (a maior saída)
        double melhorValor = -Double.MAX_VALUE;
        int melhorPosicao = -1;
        for (int i = 0; i < saídas.length; i++) {
            // Considera a jogada apenas se a posição estiver vazia (tabuleiro[i] == 0)
            if (tabuleiro[i / 3][i % 3] == 0 && saídas[i] > melhorValor) {
                melhorValor = saídas[i];
                melhorPosicao = i;
            }
        }

        // Avaliação baseada na melhor jogada
        if (melhorPosicao != -1) {
            // Exemplo de um sistema de pontuação de vitórias, derrotas e empates
            int linha = melhorPosicao / 3;
            int coluna = melhorPosicao % 3;
            
            // Simule a jogada no tabuleiro (apenas a jogada do indivíduo)
            tabuleiro[linha][coluna] = 1; // Supondo que o indivíduo jogue como X

            // Verifique o resultado do jogo (vitória, derrota, empate)
            if (verificarVitoria(tabuleiro, 1)) {
                this.avaliacao = 1;  // Vitória
            } else if (verificarEmpate(tabuleiro)) {
                this.avaliacao = 0;  // Empate
            } else {
                this.avaliacao = -1; // Derrota
            }
            
            // Restaurar o tabuleiro (retirar a jogada do indivíduo)
            tabuleiro[linha][coluna] = 0;
        } else {
            this.avaliacao = -1;  // Caso não haja jogada válida
        }
    }
    
    public boolean verificarVitoria(int[][] tabuleiro, int jogador) {
        // Lógica para verificar se o jogador venceu (por exemplo, linha, coluna ou diagonal)
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] == jogador && tabuleiro[i][1] == jogador && tabuleiro[i][2] == jogador)
                return true; // Linha
            if (tabuleiro[0][i] == jogador && tabuleiro[1][i] == jogador && tabuleiro[2][i] == jogador)
                return true; // Coluna
        }
        if (tabuleiro[0][0] == jogador && tabuleiro[1][1] == jogador && tabuleiro[2][2] == jogador)
            return true; // Diagonal principal
        if (tabuleiro[0][2] == jogador && tabuleiro[1][1] == jogador && tabuleiro[2][0] == jogador)
            return true; // Diagonal secundária

        return false;
    }

    // Método para verificar se houve empate
    public boolean verificarEmpate(int[][] tabuleiro) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == 0) { // Se houver alguma posição vazia
                    return false; // Não é empate
                }
            }
        }
        return true; // Empate
    }


    // Crossover
    public Individuo crossover(Individuo outro) {
        Random rand = new Random();
        double[] novosPesos = new double[9];

        for (int i = 0; i < 9; i++) {
            double pesoPai1 = this.neuronio.getPesos()[i];
            double pesoPai2 = outro.neuronio.getPesos()[i];

            // Média ponderada entre os pesos dos dois pais
            novosPesos[i] = (pesoPai1 + pesoPai2) / 2;
        }

        Individuo filho = new Individuo(novosPesos);
        return filho;
    }

    // Mutação
    public void mutar() {
        Random rand = new Random();
        int indice = rand.nextInt(9);
        double novoPeso = (rand.nextDouble() * 2) - 1;
        neuronio.getPesos()[indice] = novoPeso;
    }

    public Neuronio getNeuronio() {
        return neuronio;
    }

    public double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(double avaliacao) {
        this.avaliacao = avaliacao;
    }
}

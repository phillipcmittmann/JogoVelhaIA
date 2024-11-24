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

    public void avaliar(double[] entradas, int[][] tabuleiro, int resultado) {
        // A avaliação é determinada pelo resultado do jogo
        if (resultado == 1) {
            this.avaliacao = 1;  // Vitória da rede neural
        } else if (resultado == -1) {
            this.avaliacao = -1; // Derrota
        } else if (resultado == 2) {
            this.avaliacao = 0.5; // Empate
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
        int indice = rand.nextInt(9);  // Seleciona aleatoriamente um índice de peso
        double novoPeso = (rand.nextDouble() * 2) - 1;  // Gera um novo peso entre -1 e 1
        neuronio.getPesos()[indice] = novoPeso;  // Altera o peso no índice escolhido
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

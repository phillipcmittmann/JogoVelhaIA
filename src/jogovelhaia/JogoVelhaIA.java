package jogovelhaia;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class JogoVelhaIA {
	private static int[][] tabuleiro = new int[3][3];

    public static void inicializarTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = 0; // 0 representa uma célula vazia
            }
        }
    }

    public static void imprimirTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == 1) {
                    System.out.print("X "); // Jogador
                } else if (tabuleiro[i][j] == -1) {
                    System.out.print("O "); // Máquina
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
        
        System.out.println();
    }

    public static int verificarVencedor(int[][] tabuleiro) {
        // Verifica linhas, colunas e diagonais para vitória
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2] && tabuleiro[i][0] != 0) {
                return tabuleiro[i][0];  // Retorna 1 para vitória do jogador 1, -1 para jogador -1
            }
            if (tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i] && tabuleiro[0][i] != 0) {
                return tabuleiro[0][i];  // Retorna 1 para vitória do jogador 1, -1 para jogador -1
            }
        }

        // Verifica diagonais
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2] && tabuleiro[0][0] != 0) {
            return tabuleiro[0][0];
        }
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0] && tabuleiro[0][2] != 0) {
            return tabuleiro[0][2];
        }

        // Verifica empate (se o tabuleiro estiver cheio e não houve vencedor)
        boolean cheio = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == 0) {
                    cheio = false;  // Se houver uma posição vazia, não é empate
                }
            }
        }

        return (cheio) ? 2 : 0;  // Retorna 2 para empate, 0 se o jogo ainda continuar
    }


    
    public static void avaliarJogada(Individuo individuo, int resultado) {
        if (resultado == 1) {
            individuo.setAvaliacao(1);  // Vitória
        } else if (resultado == -1) {
            individuo.setAvaliacao(-1);  // Derrota
        } else if (resultado == 2) {
            individuo.setAvaliacao(0.5);  // Empate
        }
    }

    public static boolean jogadaRedeNeural(Populacao populacao, int[][] tabuleiro) {
        int resultado = verificarVencedor(tabuleiro);

        // Seleciona o melhor indivíduo da população para fazer a jogada
        Individuo melhorIndividuo = populacao.selecionarIndividuo();

        // Calcula as saídas da rede neural para as 9 posições do tabuleiro
        int melhorPosicao = escolherMelhorJogada(melhorIndividuo, tabuleiro);

        // Se a rede neural não conseguiu encontrar uma posição válida
        if (melhorPosicao == -1) {
            System.out.println("Erro! A rede neural não encontrou uma posição válida.");
            melhorIndividuo.setAvaliacao(-1);  // A avaliação é negativa porque a rede não jogou corretamente
            return false;
        } else {
            int linha = melhorPosicao / 3;
            int coluna = melhorPosicao % 3;

            // Faz a jogada no tabuleiro
            tabuleiro[linha][coluna] = 1;  // Jogador da rede neural é representado por 1

            // Exibe a jogada
            System.out.println(">>> Rede Neural escolheu - Linha: " + linha + " Coluna: " + coluna);

            // Verifica o resultado após a jogada
            resultado = verificarVencedor(tabuleiro);

            // Atualiza a avaliação do melhor indivíduo (aquele que fez a jogada)
            avaliarJogada(melhorIndividuo, resultado);  // Avalia o indivíduo específico que fez a jogada

            return true;  // A jogada foi bem-sucedida
        }
    }

    public static int escolherMelhorJogada(Individuo individuo, int[][] tabuleiro) {
        double[] jogada = individuo.getNeuronio().calcularSaida(tabuleiroParaVetor(tabuleiro));

        int melhorPosicao = -1;
        double melhorValor = -Double.MAX_VALUE;

        // Itera sobre as saídas da rede neural e escolhe a posição com o maior valor de saída
        for (int i = 0; i < jogada.length; i++) {
            // Verifica se a posição está vazia (tabuleiro[i / 3][i % 3] == 0) e se a jogada é melhor
            if (tabuleiro[i / 3][i % 3] == 0 && jogada[i] > melhorValor) {
                melhorValor = jogada[i];
                melhorPosicao = i;
            }
        }

        return melhorPosicao;  // Retorna a melhor posição
    }
    
//    public static int escolherMelhorJogada(Individuo individuo, int[][] tabuleiro) {
//        double[] jogada = individuo.getNeuronio().calcularSaida(tabuleiroParaVetor(tabuleiro));
//
//        // Debug: imprimir as saídas para cada posição
//        System.out.println("Saídas da Rede Neural para as posições do tabuleiro:");
//        for (int i = 0; i < jogada.length; i++) {
//            System.out.println("Posição " + i + ": " + jogada[i]);
//        }
//
//        int melhorPosicao = -1;
//        double melhorValor = -Double.MAX_VALUE;
//
//        // Itera sobre as saídas da rede neural e escolhe a posição com o maior valor de saída
//        for (int i = 0; i < jogada.length; i++) {
//            if (tabuleiro[i / 3][i % 3] == 0 && jogada[i] > melhorValor) {
//                melhorValor = jogada[i];
//                melhorPosicao = i;
//            }
//        }
//
//        return melhorPosicao;  // Retorna a melhor posição
//    }

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

    public static void jogadaComputador(Populacao populacao, int[][] tabuleiro) {
        Random rand = new Random();
        int linha, coluna;

        // Faz a jogada aleatória do computador
        while (true) {
            linha = rand.nextInt(3);  // Escolhe uma linha aleatória entre 0 e 2
            coluna = rand.nextInt(3); // Escolhe uma coluna aleatória entre 0 e 2

            if (tabuleiro[linha][coluna] == 0) {
                tabuleiro[linha][coluna] = -1; // Marca a jogada com O (-1)
                break;
            }
        }

        System.out.println("O computador jogou em: " + linha + " " + coluna + "\n");

        // Verifica o resultado após a jogada do computador
        int resultado = verificarVencedor(tabuleiro);

        // Atualiza a avaliação do melhor indivíduo da população
        if (resultado != 0) {
            // Aqui estamos assumindo que o melhor indivíduo foi quem jogou a rede neural
            Individuo individuo = populacao.selecionarIndividuo();
            avaliarJogada(individuo, resultado);  // Avalia o indivíduo que fez a jogada
        }

        // Exibe o resultado do jogo
        if (resultado == 1) {
            System.out.println("Rede Neural venceu!\n");
        } else if (resultado == -1) {
            System.out.println("O computador venceu!\n");
        } else if (resultado == 2) {
            System.out.println("Empate!\n");
        }
    }

    
    public static void jogadaMinimax() {
        int[][] velha = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == 0) 
                    velha[i][j] = -1;  // Posição vazia
                else if (tabuleiro[i][j] == 1) 
                    velha[i][j] = 1;   // Posição do jogador
                else 
                    velha[i][j] = 0;   // Posição ocupada por "O" (computador)
            }
        }

        TestaMinimax mini = new TestaMinimax(velha);

        while (true) {
            Sucessor melhor = mini.joga();

            // Aqui, o Minimax deve verificar se a célula já foi ocupada antes de jogar
            if (tabuleiro[melhor.getLinha()][melhor.getColuna()] == 0) {
                System.out.println(">>> MINIMAX escolheu - Linha: " + melhor.getLinha() + " Coluna: " + melhor.getColuna());
                tabuleiro[melhor.getLinha()][melhor.getColuna()] = -1; // O computador (O) joga
                break;
            } else {
                // Caso a posição já esteja ocupada, tenta novamente
                System.out.println("Posição já ocupada. Tentando outra posição...");
            }
        }
    }
    
    public static void main(String[] args) {
        while(true) {
            Scanner scanner = new Scanner(System.in);

            Populacao populacao = new Populacao(180);

            int geracao = 0;

            System.out.println("\n==============================================\n");
            System.out.println("Digite a dificuldade que deseja jogar: 1- Fácil; 2- Médio 3- Difícil:");
            int dificuldade = scanner.nextInt();

            while (geracao < 1000) {
                geracao++;
                
                if (dificuldade == 1) {
                    inicializarTabuleiro();
                    int resultado = 0;

                    while (resultado == 0) {
                        boolean jogadaValida = jogadaRedeNeural(populacao, tabuleiro);
                        imprimirTabuleiro();

                        if (!jogadaValida) {
                            System.out.println("A rede neural fez uma jogada inválida. Iniciando um novo jogo...\n\n");
                            Individuo individuo = populacao.selecionarIndividuo();
                            individuo.setAvaliacao(-1);
                            break;
                        }

                        resultado = verificarVencedor(tabuleiro);
                        if (resultado != 0) break;

                        jogadaComputador(populacao, tabuleiro);
                        imprimirTabuleiro();

                        resultado = verificarVencedor(tabuleiro);
                    }

                    imprimirTabuleiro();
                    Individuo individuo = populacao.selecionarIndividuo();
                    if (resultado == 1) {
                        System.out.println("Rede Neural venceu!\n");
                        individuo.setAvaliacao(1);  // Atualiza a avaliação do vencedor
                    } else if (resultado == -1) {
                        System.out.println("O computador venceu!\n");
                        individuo.setAvaliacao(-0.5);  // Avaliação para o perdedor
                    } else if (resultado == 2) {
                        System.out.println("Empate!\n");
                        individuo.setAvaliacao(0.5);  // Avaliação para empate
                    }
                } else if (dificuldade == 2) {
                	inicializarTabuleiro();
                    int resultado = 0;

                    while (resultado == 0) {
                        boolean jogadaValida = jogadaRedeNeural(populacao, tabuleiro);
                        imprimirTabuleiro();

                        if (!jogadaValida) {
                            System.out.println("A rede neural fez uma jogada inválida. Iniciando um novo jogo...\n\n");
                            Individuo individuo = populacao.selecionarIndividuo();
                            individuo.setAvaliacao(-1);
                            break;
                        }

                        resultado = verificarVencedor(tabuleiro);
                        if (resultado != 0) break;

                        // 50% chance de jogar aleatório ou usar Minimax
                        Random rand = new Random();
                        int randomInt = rand.nextInt(2); // 0 ou 1

                        if (randomInt == 0) {
                            jogadaComputador(populacao, tabuleiro);
                        } else {
                            jogadaMinimax();  // Jogo usando Minimax
                        }
                        imprimirTabuleiro();

                        resultado = verificarVencedor(tabuleiro);
                    }

                    imprimirTabuleiro();
                    Individuo individuo = populacao.selecionarIndividuo();
                    if (resultado == 1) {
                        System.out.println("Rede Neural venceu!\n");
                        individuo.setAvaliacao(1);  // Atualiza a avaliação do vencedor
                    } else if (resultado == -1) {
                        System.out.println("O computador venceu!\n");
                        individuo.setAvaliacao(-0.5);  // Avaliação para o perdedor
                    } else if (resultado == 2) {
                        System.out.println("Empate!\n");
                        individuo.setAvaliacao(0.5);  // Avaliação para empate
                    }
                } else if (dificuldade == 3) {
                	inicializarTabuleiro();
                    int resultado = 0;

                    while (resultado == 0) {
                        // Jogada da Rede Neural
                        boolean jogadaValida = jogadaRedeNeural(populacao, tabuleiro);
                        imprimirTabuleiro();

                        if (!jogadaValida) {
                            System.out.println("A rede neural fez uma jogada inválida. Iniciando um novo jogo...\n\n");
                            break;
                        }

                        // Verifica se alguém venceu ou houve empate
                        resultado = verificarVencedor(tabuleiro);
                        if (resultado != 0) break;

                        // Jogada do Minimax
                        jogadaMinimax();
                        imprimirTabuleiro();

                        // Verifica novamente após a jogada do Minimax
                        resultado = verificarVencedor(tabuleiro);
                    }

                    imprimirTabuleiro();
                    Individuo individuo = populacao.selecionarIndividuo();
                    if (resultado == 1) {
                        System.out.println("Rede Neural venceu!\n");
                        individuo.setAvaliacao(1);  // Atualiza a avaliação do vencedor
                    } else if (resultado == -1) {
                        System.out.println("O computador venceu!\n");
                        individuo.setAvaliacao(-0.5);  // Avaliação para o perdedor
                    } else if (resultado == 2) {
                        System.out.println("Empate!\n");
                        individuo.setAvaliacao(0.5);  // Avaliação para empate
                    }
                } else {
                    System.out.println("Opção inválida.");
                }

                populacao.gerarNovaGeracao();
            }
        }
    }
}

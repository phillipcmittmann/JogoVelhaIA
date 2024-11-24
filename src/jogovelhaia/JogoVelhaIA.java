package jogovelhaia;

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

    public static int verificarVencedor() {
        // Verificar linhas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2] && tabuleiro[i][0] != 0) {
                return tabuleiro[i][0]; // Retorna 1 (rede neural) ou -1 (computador)
            }
        }

        // Verificar colunas
        for (int j = 0; j < 3; j++) {
            if (tabuleiro[0][j] == tabuleiro[1][j] && tabuleiro[1][j] == tabuleiro[2][j] && tabuleiro[0][j] != 0) {
                return tabuleiro[0][j];
            }
        }

        // Verificar diagonais
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2] && tabuleiro[0][0] != 0) {
            return tabuleiro[0][0];
        }
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0] && tabuleiro[0][2] != 0) {
            return tabuleiro[0][2];
        }

        // Verificar se há empate
        boolean temEspacosVazios = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == 0) {
                    temEspacosVazios = true;
                    break;
                }
            }
        }

        if (!temEspacosVazios) {
            return 2; // Empate
        }

        return 0; // O jogo continua (sem vencedor)
    }
    
    public static void avaliarJogada(Individuo individuo) {
        // Verifica o estado do tabuleiro após a jogada da rede neural
        int resultado = verificarVencedor();

        // Se a rede neural venceu com a jogada, atribui uma alta aptidão
        if (resultado == 1) {
            individuo.setAvaliacao(1.0);  // Aptidão positiva, vitória
        } else if (resultado == 2) {
            individuo.setAvaliacao(0.5);  // Empate, aptidão média
        } else {
            // Se a rede neural não venceu, verificamos se ela bloqueou o computador
            if (bloquearVitoriaComputador()) {
                individuo.setAvaliacao(0.8);  // Aptidão alta por bloquear vitória do oponente
            } else {
                individuo.setAvaliacao(0.0);  // Jogada neutra (não ajudou nem prejudicou)
            }
        }
    }

    private static boolean bloquearVitoriaComputador() {
        // Verificar se o computador (O) estava prestes a ganhar e a rede neural bloqueou
        // Simula uma jogada de "O" e verifica se a jogada da rede neural evitou a vitória
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == 0) {
                    tabuleiro[i][j] = -1;  // Tenta colocar o O (computador)
                    if (verificarVencedor() == -1) {
                        tabuleiro[i][j] = 0;  // Reverte a jogada
                        return true;  // A jogada foi um bloqueio
                    }
                    tabuleiro[i][j] = 0;  // Reverte a jogada
                }
            }
        }
        return false;
    }

    public static boolean jogadaRedeNeural(Populacao populacao) {
        // Avaliar a população passando tanto o vetor de entradas quanto o tabuleiro
        populacao.avaliarPopulacao(tabuleiroParaVetor(tabuleiro), tabuleiro);

        // Seleciona o melhor indivíduo da população
        Individuo melhorIndividuo = populacao.selecionarIndividuo();

        // Chama o método para escolher a melhor jogada baseada nas saídas da rede neural
        int melhorPosicao = escolherMelhorJogada(melhorIndividuo, tabuleiro);

        // Verifica se há uma posição válida para jogar
        if (melhorPosicao == -1) {
            System.out.println("Erro! A rede neural não encontrou uma posição válida.");
            melhorIndividuo.setAvaliacao(-1);  // A avaliação é negativa porque a rede não jogou corretamente
            return false; // A rede neural não conseguiu jogar
        } else {
            // Converte o índice da posição para linha e coluna
            int linha = melhorPosicao / 3;
            int coluna = melhorPosicao % 3;

            // Faz a jogada no tabuleiro
            tabuleiro[linha][coluna] = 1;  // O jogador da rede neural é representado por 1
            
            // Exibe a jogada
            System.out.println(">>> Rede Neural escolheu - Linha: " + linha + " Coluna: " + coluna);
            return true;  // A jogada foi bem-sucedida
        }
    }

    // Método que escolhe a melhor jogada com base nas saídas da rede neural
    public static int escolherMelhorJogada(Individuo individuo, int[][] tabuleiro) {
        double[] jogada = individuo.getNeuronio().calcularSaida(tabuleiroParaVetor(tabuleiro));

        int melhorPosicao = -1;
        double melhorValor = -Double.MAX_VALUE;

        // Itera sobre as saídas da rede neural e escolhe a posição com o maior valor de saída
        for (int i = 0; i < jogada.length; i++) {
            // Verifica se a posição está vazia e se a jogada é melhor
            if (jogada[i] > melhorValor && tabuleiro[i / 3][i % 3] == 0) {
                melhorValor = jogada[i];
                melhorPosicao = i;
            }
        }

        return melhorPosicao;  // Retorna a melhor posição
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

    public static void jogadaComputador() {
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

            while (geracao < 100) {
                geracao++;

                if (dificuldade == 1) {
                	inicializarTabuleiro();
                    int resultado = 0;
                    
                    while (resultado == 0) {
                        boolean jogadaValida = jogadaRedeNeural(populacao);
                        imprimirTabuleiro();

                        if (!jogadaValida) {
                            System.out.println("A rede neural fez uma jogada inválida. Iniciando um novo jogo...\n\n");
                            
                            Individuo individuo = populacao.selecionarIndividuo();
                            individuo.setAvaliacao(-1);
                            
                            break;
                        }

                        resultado = verificarVencedor();
                        if (resultado != 0) break;

                        jogadaComputador();
                        imprimirTabuleiro();

                        resultado = verificarVencedor();
                    }
                    
                    imprimirTabuleiro();
                    if (resultado == 1) {
                        System.out.println("Rede Neural venceu!\n");
                        Individuo individuo = populacao.selecionarIndividuo();
                        individuo.setAvaliacao(1);
                    } else if (resultado == -1) {
                        System.out.println("O computador venceu!\n");
                        Individuo individuo = populacao.selecionarIndividuo();
                        individuo.setAvaliacao(-0.5);
                    } else if (resultado == 2) {
                        System.out.println("Empate!\n");
                        Individuo individuo = populacao.selecionarIndividuo();
                        individuo.setAvaliacao(0.5);
                    }
                }else if (dificuldade == 2) {
                    inicializarTabuleiro();
                    int resultado = 0;

                    while (resultado == 0) {
                        boolean jogadaValida = jogadaRedeNeural(populacao);
                        imprimirTabuleiro();

                        if (!jogadaValida) {
                            System.out.println("A rede neural fez uma jogada inválida. Iniciando um novo jogo...\n\n");
                            Individuo individuo = populacao.selecionarIndividuo();
                            individuo.setAvaliacao(-1);
                            break;
                        }

                        resultado = verificarVencedor();
                        if (resultado != 0) break;

                        // 50% chance de jogar aleatório ou usar Minimax
                        Random rand = new Random();
                        int randomInt = rand.nextInt(2); // 0 ou 1

                        if (randomInt == 0) {
                            jogadaComputador();  // Jogo aleatório
                        } else {
                            jogadaMinimax();  // Jogo usando Minimax
                        }
                        imprimirTabuleiro();

                        resultado = verificarVencedor();
                    }

                    imprimirTabuleiro();
                    if (resultado == 1) {
                        System.out.println("Rede Neural venceu!\n");
                        Individuo individuo = populacao.selecionarIndividuo();
                        individuo.setAvaliacao(1);
                    } else if (resultado == -1) {
                        System.out.println("O computador venceu!\n");
                        Individuo individuo = populacao.selecionarIndividuo();
                        individuo.setAvaliacao(-0.5);
                    } else if (resultado == 2) {
                        System.out.println("Empate!\n");
                        Individuo individuo = populacao.selecionarIndividuo();
                        individuo.setAvaliacao(0.5);
                    }
                } else if (dificuldade == 3) {
                	inicializarTabuleiro();
                    int resultado = 0;

                    while (resultado == 0) {
                        // Jogada da Rede Neural
                        boolean jogadaValida = jogadaRedeNeural(populacao);
                        imprimirTabuleiro();

                        if (!jogadaValida) {
                            System.out.println("A rede neural fez uma jogada inválida. Iniciando um novo jogo...\n\n");
                            break;
                        }

                        // Verifica se alguém venceu ou houve empate
                        resultado = verificarVencedor();
                        if (resultado != 0) break;

                        // Jogada do Minimax
                        jogadaMinimax();
                        imprimirTabuleiro();

                        // Verifica novamente após a jogada do Minimax
                        resultado = verificarVencedor();
                    }

                    imprimirTabuleiro();
                    if (resultado == 1) {
                        System.out.println("Rede Neural venceu!\n");
                        Individuo individuo = populacao.selecionarIndividuo();
                        individuo.setAvaliacao(1);
                    } else if (resultado == -1) {
                        System.out.println("O computador venceu!\n");
                        Individuo individuo = populacao.selecionarIndividuo();
                        individuo.setAvaliacao(-0.5);
                    } else if (resultado == 2) {
                        System.out.println("Empate!\n");
                        Individuo individuo = populacao.selecionarIndividuo();
                        individuo.setAvaliacao(0.5);
                    }
                }
                else {
                    System.out.println("Opção inválida.");
                }
            }
        }
    }
}

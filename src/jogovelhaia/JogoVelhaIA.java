package jogovelhaia;

import java.util.Random;
import java.util.Scanner;

public class JogoVelhaIA {
	private static int[][] tabuleiro = new int[3][3];
	private static int vitorias = 0;
	private static int geracaoTotal = 0;

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

    public static int verificarVencedor(int[][] tabuleiro) {
        // Verifica linhas e colunas
        for (int i = 0; i < 3; i++) {
            // Verifica linhas
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2] && tabuleiro[i][0] != 0) {
                return tabuleiro[i][0];  // Retorna JOGADOR_1 ou JOGADOR_2
            }
            // Verifica colunas
            if (tabuleiro[0][i] == tabuleiro[1][i] && tabuleiro[1][i] == tabuleiro[2][i] && tabuleiro[0][i] != 0) {
                return tabuleiro[0][i];  // Retorna JOGADOR_1 ou JOGADOR_2
            }
        }

        // Verifica diagonais
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2] && tabuleiro[0][0] != 0) {
            return tabuleiro[0][0];
        }
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0] && tabuleiro[0][2] != 0) {
            return tabuleiro[0][2];
        }

        // Verifica se o tabuleiro está cheio (sem posições vazias)
        boolean cheio = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == 0) {
                    cheio = false;  // Se houver uma posição vazia, o tabuleiro não está cheio
                    break;
                }
            }
        }

        return (cheio) ? 2 : 0;  // Retorna 2 para empate, 0 se o jogo ainda continuar
    }
    
    public static boolean jogadaRedeNeural(int[][] tabuleiro, Populacao populacao, RedeNeural redeNeural) {
    	Cromossomo melhorCromossomo = populacao.selecionarMelhores(1)[0];
    	
    	int[] melhorPosicao = redeNeural.redeNeuralEscolheMelhorJogada(tabuleiroParaVetor(tabuleiro), melhorCromossomo);
    	int linha = melhorPosicao[0];
    	int coluna = melhorPosicao[1];
    	
        System.out.println(">>> Rede Neural escolheu - Linha: " + linha + " Coluna: " + coluna);
    	if (linha == -1 || coluna == -1) {
            System.out.println("Erro! A rede neural não encontrou uma posição válida.");
            populacao.selecionarMelhores(1)[0].setAptidao(-1);
            return false;
    	} else if (tabuleiro[linha][coluna] == -1 || tabuleiro[linha][coluna] == 1) {
            System.out.println("Erro! A rede neural jogou em uma posição já ocupada.");
            populacao.selecionarMelhores(1)[0].setAptidao(-0.25);
            return false;
        } else {
        	tabuleiro[linha][coluna] = 1;
            populacao.selecionarMelhores(1)[0].setAptidao(0.25);
        }
    	
    	return true;
    }
    
    public static void jogadaComputador() {
    	Random rand = new Random();
    	int linha, coluna;
    	
    	while (true) {
    		linha = rand.nextInt(3);
    		coluna = rand.nextInt(3);
    		
    		if (tabuleiro[linha][coluna] == 0) {
    			tabuleiro[linha][coluna] = -1;
                break;
    		}
    	}
    	
        System.out.println("O computador jogou em: " + linha + " " + coluna + "\n");
    }

    public static void main(String args[]) {
    	int numEntradas = 9;
        int numOcultos = 9;
        int numSaidas = 9;

        int tamanhoCromossomo = numEntradas * numOcultos + numOcultos * numSaidas + numOcultos + numSaidas;

    	while (true) {
    		Scanner scanner = new Scanner(System.in);

            RedeNeural rede = new RedeNeural(numEntradas, numOcultos, numSaidas);

            int geracao = 0;

        	System.out.println("\n==============================================\n");
            System.out.println("Digite a dificuldade que deseja jogar: 1- Fácil; 2- Médio 3- Difícil:");
            
            int dificuldade = scanner.nextInt();

            while (geracao < 1000) {
            	geracao++;
            	System.out.println("Geração: " + geracao);
            	
                Populacao populacao = new Populacao(100, tamanhoCromossomo);
                
            	int resultado = 0;

                if (dificuldade == 1) {
                	inicializarTabuleiro();
                	
                	while (resultado == 0) {
                		boolean jogadaValida = jogadaRedeNeural(tabuleiro, populacao, rede);
                		imprimirTabuleiro();
                		
                		if (!jogadaValida) {
                            System.out.println("A rede neural fez uma jogada inválida. Iniciando um novo jogo...\n\n");
                            break;
                		}
                		
                		resultado = verificarVencedor(tabuleiro);
                		if (resultado != 0) break;
                		
                		jogadaComputador();
                		imprimirTabuleiro();
                		
                		resultado = verificarVencedor(tabuleiro);
                	}
                	
                	// Avaliar aptidão após o jogo
                	if (resultado == 1) {
                	    System.out.println("Rede Neural venceu!\n");
                	    populacao.selecionarMelhores(1)[0].setAptidao(1);
                	    vitorias++;
                	} else if (resultado == -1) {
                	    System.out.println("O computador venceu!\n");
                	    populacao.selecionarMelhores(1)[0].setAptidao(-1);
                	} else if (resultado == 2) {
                	    System.out.println("Empate!\n");
                	}

                }
                else if (dificuldade == 2) {
                	inicializarTabuleiro();
                	
                	while (resultado == 0) {
                		boolean jogadaValida = jogadaRedeNeural(tabuleiro, populacao, rede);
                		imprimirTabuleiro();
                		
                		if (!jogadaValida) {
                            System.out.println("A rede neural fez uma jogada inválida. Iniciando um novo jogo...\n\n");
                            break;
                		}
                		
                		resultado = verificarVencedor(tabuleiro);
                		if (resultado != 0) break;
                		
                		// 50% chance de jogar aleatório ou usar Minimax
                        Random rand = new Random();
                        int randomInt = rand.nextInt(2); // 0 ou 1
                		
                        if (randomInt == 0) {
                            jogadaComputador();
                        } else {
                            jogadaMinimax();  // Joga usando Minimax
                        }
                        
                        imprimirTabuleiro();
                        
                		resultado = verificarVencedor(tabuleiro);
                	}
                	
                	// Avaliar aptidão após o jogo
                	if (resultado == 1) {
                	    System.out.println("Rede Neural venceu!\n");
                	    populacao.selecionarMelhores(1)[0].setAptidao(1);
                	    vitorias++;
                	} else if (resultado == -1) {
                	    System.out.println("O computador venceu!\n");
                	    populacao.selecionarMelhores(1)[0].setAptidao(-1);
                	} else if (resultado == 2) {
                	    System.out.println("Empate!\n");
                	}

                }
                else if (dificuldade == 3) {
                	inicializarTabuleiro();
                	
                	while (resultado == 0) {
                		boolean jogadaValida = jogadaRedeNeural(tabuleiro, populacao, rede);
                		imprimirTabuleiro();
                		
                		if (!jogadaValida) {
                            System.out.println("A rede neural fez uma jogada inválida. Iniciando um novo jogo...\n\n");
                            break;
                		}
                		
                		resultado = verificarVencedor(tabuleiro);
                		if (resultado != 0) break;
                		
                		jogadaMinimax();
                		imprimirTabuleiro();
                		
                		resultado = verificarVencedor(tabuleiro);
                	}
                	
                	// Avaliar aptidão após o jogo
                	if (resultado == 1) {
                	    System.out.println("Rede Neural venceu!\n");
                	    populacao.selecionarMelhores(1)[0].setAptidao(1);
                	    vitorias++;
                	} else if (resultado == -1) {
                	    System.out.println("O computador venceu!\n");
                	    populacao.selecionarMelhores(1)[0].setAptidao(-1);
                	} else if (resultado == 2) {
                	    System.out.println("Empate!\n");
                	}

                }
                else {
                    System.out.println("Opção inválida.");
                    geracao--;
                }
                
                populacao.avaliarAptidao(resultado);
                populacao.selecionarElite(5);
                populacao.gerarNovaPopulacao(0.1);
                
                geracaoTotal = geracaoTotal + geracao;
                
                System.out.println("Vitórias: " + vitorias);
            }
    	}
    }
}

































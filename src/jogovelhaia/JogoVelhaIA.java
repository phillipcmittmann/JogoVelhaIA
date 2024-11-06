package jogovelhaia;

import java.util.Random;
import java.util.Scanner;

public class JogoVelhaIA {
    private static int[][] tabuleiro = new int[3][3]; // Tabuleiro 3x3

    // Função para inicializar o tabuleiro
    public static void inicializarTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                tabuleiro[i][j] = -1; // -1 representa uma célula vazia
            }
        }
    }

    // Função para imprimir o tabuleiro
    public static void imprimirTabuleiro() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == 1) {
                    System.out.print("X ");
                } else if (tabuleiro[i][j] == 0) {
                    System.out.print("O ");
                } else {
                    System.out.print("- "); // Representa células vazias (-1)
                }
            }
            System.out.println();
        }
    }

    // Função para verificar se há um vencedor
    public static int verificarVencedor() {
        // Verificar linhas
        for (int i = 0; i < 3; i++) {
            if (tabuleiro[i][0] == tabuleiro[i][1] && tabuleiro[i][1] == tabuleiro[i][2] && tabuleiro[i][0] != -1) {
                return tabuleiro[i][0]; // Retorna o vencedor (1 = X, 0 = O)
            }
        }

        // Verificar colunas
        for (int j = 0; j < 3; j++) {
            if (tabuleiro[0][j] == tabuleiro[1][j] && tabuleiro[1][j] == tabuleiro[2][j] && tabuleiro[0][j] != -1) {
                return tabuleiro[0][j];
            }
        }

        // Verificar diagonais
        if (tabuleiro[0][0] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][2] && tabuleiro[0][0] != -1) {
            return tabuleiro[0][0];
        }
        if (tabuleiro[0][2] == tabuleiro[1][1] && tabuleiro[1][1] == tabuleiro[2][0] && tabuleiro[0][2] != -1) {
            return tabuleiro[0][2];
        }

        // Verificar se há empate (sem mais espaços vazios)
        boolean temEspacosVazios = false;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (tabuleiro[i][j] == -1) {
                    temEspacosVazios = true;
                    break;
                }
            }
        }
        if (!temEspacosVazios) {
            return 2; // Empate
        }

        return -1; // O jogo continua
    }

    // Função para a jogada do jogador (X)
    public static void jogadaJogador() {
        Scanner scanner = new Scanner(System.in);
        int linha, coluna;
        
        System.out.println("\n=========================================\n");

        while (true) {
            System.out.print("Digite a linha (0, 1, 2) para jogar X: ");
            linha = scanner.nextInt();
            System.out.print("Digite a coluna (0, 1, 2) para jogar X: ");
            coluna = scanner.nextInt();

            if (linha >= 0 && linha < 3 && coluna >= 0 && coluna < 3 && tabuleiro[linha][coluna] == -1) {
                tabuleiro[linha][coluna] = 1; // Marca a jogada com X (1)
                System.out.println("\nO jogador jogou em: " + linha + " " + coluna + "\n");
                break;
            } else {
                System.out.println("Jogada inválida! Tente novamente.");
            }
        }
    }

    // Função para a jogada do computador (O)
    public static void jogadaComputador() {
        Random rand = new Random();
        int linha, coluna;

        // Faz a jogada aleatória do computador
        while (true) {
            linha = rand.nextInt(3);  // Escolhe uma linha aleatória entre 0 e 2
            coluna = rand.nextInt(3); // Escolhe uma coluna aleatória entre 0 e 2

            // Se a célula estiver vazia (-1), o computador faz a jogada
            if (tabuleiro[linha][coluna] == -1) {
                tabuleiro[linha][coluna] = 0; // Marca a jogada com O (0)
                break;
            }
        }

        // Exibe a jogada do computador (linha e coluna escolhidas)
        System.out.println("O computador jogou em: " + linha + " " + coluna + "\n");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
        	System.out.println("Digite a dificuldade que deseja jogar: 1- Fácil; 2- Médio 3- Difícil:");
        	int dificuldade = scanner.nextInt();
        	
        	if (dificuldade == 1) {
        		inicializarTabuleiro();
    	        int resultado = -1;
    	
    	        // Loop do jogo: continua enquanto não houver vencedor ou empate
    	        while (resultado == -1) {
    	            imprimirTabuleiro();
    	            jogadaJogador();  // Jogada do jogador
    	
    	            // Verifica se há vencedor ou empate após a jogada do jogador
    	            resultado = verificarVencedor();
    	            if (resultado != -1) break;
    	
    	            jogadaComputador();  // Jogada do computador
    	
    	            // Verifica novamente após a jogada do computador
    	            resultado = verificarVencedor();
    	        }
    	
    	        // Exibe o estado final do tabuleiro
    	        imprimirTabuleiro();
    	
    	        // Exibe o resultado final do jogo
    	        if (resultado == 1) {
    	            System.out.println("Você venceu!");
    	        } else if (resultado == 0) {
    	            System.out.println("O computador venceu!");
    	        } else {
    	            System.out.println("Empate!");
    	        }
        	}
        	
        	else if (dificuldade == 2) {
        		inicializarTabuleiro();
    	        int resultado = -1;
    	        
    	        while (resultado == -1) {
    	        	imprimirTabuleiro();
    	            jogadaJogador();  // Jogada do jogador
    	
    	            // Verifica se há vencedor ou empate após a jogada do jogador
    	            resultado = verificarVencedor();
    	            if (resultado != -1) break;
    	            
    	            int randomInt = 1 + (int)(Math.random() * 2);
    	            
    	            if (randomInt == 1) {
        	            jogadaComputador();  // Jogada do computador
    	            } else {
    	            	TestaMinimax mini = new TestaMinimax(tabuleiro);
	    	            Sucessor melhor = mini.joga();
	    	            
	    	            System.out.println(">>> MINIMAX escolheu - Linha: " + melhor.getLinha() + " Coluna: " + melhor.getColuna());

	    	            if(tabuleiro[melhor.getLinha()][melhor.getColuna()]!=-1) {
	    	            	System.out.println("Posicao ocupada");
	    	            }
	    	            else {
	    	                tabuleiro[melhor.getLinha()][melhor.getColuna()] = 0;
	    	            }
    	            }
    	            
    	            resultado = verificarVencedor();    	            
    	        }
    	        
    	        imprimirTabuleiro();
    	        
    	     // Exibe o resultado final do jogo
    	        if (resultado == 1) {
    	            System.out.println("Você venceu!");
    	        } else if (resultado == 0) {
    	            System.out.println("O computador venceu!");
    	        } else {
    	            System.out.println("Empate!");
    	        }
        	}
        	
        	else if (dificuldade == 3) {
        		inicializarTabuleiro();
    	        int resultado = -1;
    	        
    	        while (resultado == -1) {
    	        	imprimirTabuleiro();
    	            jogadaJogador();  // Jogada do jogador
    	
    	            // Verifica se há vencedor ou empate após a jogada do jogador
    	            resultado = verificarVencedor();
    	            if (resultado != -1) break;
    	                	            
	            	TestaMinimax mini = new TestaMinimax(tabuleiro);
    	            Sucessor melhor = mini.joga();
    	            
    	            System.out.println(">>> MINIMAX escolheu - Linha: " + melhor.getLinha() + " Coluna: " + melhor.getColuna());

    	            if(tabuleiro[melhor.getLinha()][melhor.getColuna()]!=-1) {
    	            	System.out.println("Posicao ocupada");
    	            }
    	            else {
    	                tabuleiro[melhor.getLinha()][melhor.getColuna()] = 0;
    	            }
    	            
    	            resultado = verificarVencedor();    	            
    	        }
    	        
    	        imprimirTabuleiro();
    	        
    	     // Exibe o resultado final do jogo
    	        if (resultado == 1) {
    	            System.out.println("Você venceu!");
    	        } else if (resultado == 0) {
    	            System.out.println("O computador venceu!");
    	        } else {
    	            System.out.println("Empate!");
    	        }
        	}
        	
        	else {
        		System.out.println("Opção inválida.");
        	}
        }
    }
}

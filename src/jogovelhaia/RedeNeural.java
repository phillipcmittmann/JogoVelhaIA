package jogovelhaia;

class RedeNeural {
    private int numEntradas;
    private int numOcultos;
    private int numSaidas;

    public RedeNeural(int numEntradas, int numOcultos, int numSaidas) {
        this.numEntradas = numEntradas;
        this.numOcultos = numOcultos;
        this.numSaidas = numSaidas;
    }

    // Calcula a saída da rede neural para um conjunto de entradas e pesos
    public double[] calcularSaida(double[] entradas, double[] pesos) {
        if (pesos.length != numEntradas * numOcultos + numOcultos * numSaidas + numOcultos + numSaidas) {
            throw new IllegalArgumentException("Número de pesos incompatível com a arquitetura da rede.");
        }

        double[] saidasOcultas = new double[numOcultos];
        int index = 0;

        // Cálculo das saídas da camada oculta
        for (int i = 0; i < numOcultos; i++) {
            double soma = 0.0;
            for (int j = 0; j < numEntradas; j++) {
                soma += entradas[j] * pesos[index++];
            }
            soma += pesos[index++]; // Bias
            saidasOcultas[i] = ativacao(soma); // Aplicação da função de ativação
        }

        // Cálculo das saídas da camada de saída
        double[] saidas = new double[numSaidas];
        for (int i = 0; i < numSaidas; i++) {
            double soma = 0.0;
            for (int j = 0; j < numOcultos; j++) {
                soma += saidasOcultas[j] * pesos[index++];
            }
            soma += pesos[index++]; // Bias
            saidas[i] = ativacao(soma);
        }

        return saidas;
    }
    
    

    // Função de ativação (sigmoide)
    private double ativacao(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
    
    public int[] redeNeuralEscolheMelhorJogada(double[] tabuleiro, Cromossomo cromossomo) {
    	double[] saidas = calcularSaida(tabuleiro, cromossomo.getGenes());
    	
    	int melhorJogadaIndex = 0;
    	for (int i = 1; i < saidas.length; i++) {
    		if (saidas[i] > saidas[melhorJogadaIndex]) { 
    			melhorJogadaIndex = i;
    		}
    	}
    	
    	int linha = melhorJogadaIndex / 3;
    	int coluna = melhorJogadaIndex % 3;
    	
    	return new int[] {linha, coluna};
    }
}
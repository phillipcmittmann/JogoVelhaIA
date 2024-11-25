package jogovelhaia;

import java.util.Random;

class Cromossomo {
    private double[] genes; // Genes: pesos e biases da rede neural
    private double aptidao; // Aptidão do cromossomo

    public Cromossomo(int tamanho) {
        this.genes = new double[tamanho];
        Random random = new Random();
        
        for (int i = 0; i < tamanho; i++) {
            this.genes[i] = -1 + 2 * random.nextDouble(); // Valores entre -1 e 1
        }
        
        this.aptidao = 0;
    }
    
    public Cromossomo(double[] pesos) {
    	this.genes = new double[pesos.length];
    	
    	for (int i = 0; i < pesos.length; i++) {
    		this.genes[i] = pesos[i];
    	}
    }

    public double[] getGenes() {
        return genes;
    }

    public double getAptidao() {
        return aptidao;
    }

    public void setAptidao(double aptidao) {
        this.aptidao = aptidao;
    }

    @Override
    public String toString() {
        return "Aptidão: " + aptidao;
    }
    
    public Cromossomo crossover(Cromossomo outro) {
        Random rand = new Random();
        double[] novosPesos = new double[this.genes.length];

        for (int i = 0; i < this.genes.length; i++) {
            if (rand.nextBoolean()) {
                novosPesos[i] = this.genes[i];  // Gene do pai 1
            } else {
                novosPesos[i] = outro.getGenes()[i];  // Gene do pai 2
            }
        }

        return new Cromossomo(novosPesos);
    }
    
    public void mutar(double taxaMutacao) {
    	Random rand = new Random();
    	
    	for (int i = 0; i < genes.length; i++) {
    		genes[i] += (rand.nextDouble() * 2 - 1) * 0.1;
    		genes[i] = Math.max(-1, Math.min(1, genes[i]));
    	}
    }
}
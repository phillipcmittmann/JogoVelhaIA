package jogovelhaia;

import java.util.Arrays;
import java.util.Random;

class Populacao {
    private Cromossomo[] cromossomos;
    @SuppressWarnings("unused")
	private int tamanhoPopulacao;

    public Populacao(int tamanhoPopulacao, int tamanhoCromossomo) {
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.cromossomos = new Cromossomo[tamanhoPopulacao];

        for (int i = 0; i < tamanhoPopulacao; i++) {
            cromossomos[i] = new Cromossomo(tamanhoCromossomo);
        }
    }

    public void avaliarAptidao(int resultado) {
    	for (Cromossomo cromossomo : cromossomos) {
    		if (resultado == 1) {
    			cromossomo.setAptidao(1);
    			break;
    		} else if (resultado == 0) {
    			cromossomo.setAptidao(0.5);
    			break;
    		} else if (resultado == -1) {
    			cromossomo.setAptidao(-1);
    			break;
    		}
    	}
    }

    public Cromossomo[] selecionarMelhores(int quantidade) {
        Arrays.sort(cromossomos, (a, b) -> Double.compare(b.getAptidao(), a.getAptidao()));
        return Arrays.copyOfRange(cromossomos, 0, Math.min(quantidade, cromossomos.length));
    }

    public void mostrarPopulacao() {
        for (Cromossomo cromossomo : cromossomos) {
            System.out.println(cromossomo);
        }
    }
    
    public void gerarNovaPopulacao(double taxaMutacao) {
        Random rand = new Random();
        Cromossomo[] novaPopulacao = new Cromossomo[tamanhoPopulacao];
        
        // Seleciona os melhores cromossomos
        Cromossomo[] melhores = selecionarMelhores(this.tamanhoPopulacao / 2);
        
        for (int i = 0; i < this.tamanhoPopulacao; i++) {
            // Seleciona dois pais aleatórios da elite
            Cromossomo pai1 = melhores[rand.nextInt(melhores.length)];
            Cromossomo pai2 = melhores[rand.nextInt(melhores.length)];
            
            // Realiza o crossover usando o método da classe Cromossomo
            novaPopulacao[i] = pai1.crossover(pai2);
            
            // Aplica mutação ao filho gerado
            novaPopulacao[i].mutar(taxaMutacao);
        }
        
        cromossomos = novaPopulacao;
    }
    
    public void selecionarElite(int quantidadeElite) {
        Arrays.sort(cromossomos, (a, b) -> Double.compare(b.getAptidao(), a.getAptidao()));

        Cromossomo[] novaPopulacao = new Cromossomo[tamanhoPopulacao];
        System.arraycopy(cromossomos, 0, novaPopulacao, 0, quantidadeElite); // Preserva a elite

        Random rand = new Random();
        for (int i = quantidadeElite; i < tamanhoPopulacao; i++) {
            // Crossover entre dois cromossomos da elite
            int pai1 = rand.nextInt(quantidadeElite);
            int pai2 = rand.nextInt(quantidadeElite);
            novaPopulacao[i] = cromossomos[pai1].crossover(cromossomos[pai2]); // Gera o filho

            // Aplica mutação ao filho
            novaPopulacao[i].mutar(0.05); // Pode ajustar a taxa de mutação
        }

        cromossomos = novaPopulacao;
    }
}
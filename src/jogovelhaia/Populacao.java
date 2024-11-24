package jogovelhaia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Populacao {
    private ArrayList<Individuo> individuos;
    private int tamanhoPopulacao;
    private Random rand;

    public Populacao(int tamanhoPopulacao) {
        this.tamanhoPopulacao = tamanhoPopulacao;
        this.individuos = new ArrayList<>();
        this.rand = new Random();

        for (int i = 0; i < tamanhoPopulacao; i++) {
            individuos.add(new Individuo());
        }
    }
    
    public ArrayList<Individuo> getIndividuos() {
    	return this.individuos;
    }

    public Individuo selecionarIndividuo() {
        Random rand = new Random();
        int tamanhoTorneio = 3;  // Número de indivíduos a serem selecionados para o torneio
        Individuo melhorIndividuo = null;

        // Realiza o torneio com 'tamanhoTorneio' indivíduos aleatórios
        for (int i = 0; i < tamanhoTorneio; i++) {
            // Escolhe um índice aleatório
            int indiceAleatorio = rand.nextInt(individuos.size());
            Individuo individuoAleatorio = individuos.get(indiceAleatorio);

            // Se for o primeiro torneio ou o indivíduo aleatório for melhor, seleciona ele
            if (melhorIndividuo == null || individuoAleatorio.getAvaliacao() > melhorIndividuo.getAvaliacao()) {
                melhorIndividuo = individuoAleatorio;
            }
        }

        // Retorna o melhor indivíduo do torneio
        return melhorIndividuo;
    }


    public void gerarNovaGeracao() {
        ArrayList<Individuo> novaPopulacao = new ArrayList<>();

        // Adiciona o melhor indivíduo da geração anterior (elitismo)
        novaPopulacao.add(selecionarIndividuo());

        // Realiza o cruzamento para gerar filhos até a população estar completa
        while (novaPopulacao.size() < tamanhoPopulacao) {
            // Seleciona os pais por torneio
            Individuo pai1 = selecionarIndividuo();
            Individuo pai2 = selecionarIndividuo();

            // Realiza o cruzamento para gerar um filho
            Individuo filho = pai1.crossover(pai2);

            // Com uma certa probabilidade, realiza a mutação no filho
            if (rand.nextDouble() < 0.1) {  // A taxa de mutação é de 10%
                filho.mutar();  // O método de mutação é chamado
            }

            // Adiciona o filho à nova população
            novaPopulacao.add(filho);
        }

        // Atualiza a população com a nova geração
        individuos = novaPopulacao;
    }

    public void avaliarPopulacao(int individuoAtivoIndex, double[] entradas, int[][] tabuleiro, int resultado) {
        // Avalia apenas o indivíduo ativo
        Individuo individuoAtivo = individuos.get(individuoAtivoIndex);
        individuoAtivo.avaliar(entradas, tabuleiro, resultado);
    }

    public void imprimirPopulacao() {
        for (Individuo individuo : individuos) {
            System.out.println("Avaliação: " + individuo.getAvaliacao());
        }
    }
}

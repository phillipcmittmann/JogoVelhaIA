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

    public Individuo selecionarIndividuo() {
        Collections.sort(individuos, (a, b) -> Double.compare(b.getAvaliacao(), a.getAvaliacao()));
        return individuos.get(0);
    }

    public void gerarNovaGeracao() {
        ArrayList<Individuo> novaPopulacao = new ArrayList<>();

        novaPopulacao.add(selecionarIndividuo());

        while (novaPopulacao.size() < tamanhoPopulacao) {
            Individuo pai1 = selecionarIndividuo();
            Individuo pai2 = selecionarIndividuo();

            Individuo filho = pai1.crossover(pai2);

            // Reduzir a taxa de mutação para 1% (exemplo)
            if (rand.nextDouble() < 0.01) {
                filho.mutar();
            }

            novaPopulacao.add(filho);
        }

        individuos = novaPopulacao;
    }

    public void avaliarPopulacao(double[] entradas, int[][] tabuleiro) {
        for (Individuo individuo : individuos) {
            individuo.avaliar(entradas, tabuleiro);  // Passa o vetor de entradas e o tabuleiro
        }
    }

    public void imprimirPopulacao() {
        for (Individuo individuo : individuos) {
            System.out.println("Avaliação: " + individuo.getAvaliacao());
        }
    }
}

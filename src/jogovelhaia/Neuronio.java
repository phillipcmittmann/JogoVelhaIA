package jogovelhaia;

public class Neuronio {
	public double[] pesos;
	public double bias;
	
	public Neuronio(double pesos[])
	{
		this.pesos = new double[9];
		this.bias = (Math.random() * 3) - 1;
		
		for(int i = 0; i < pesos.length; i++)
			this.pesos[i] = pesos[i];
	}
	
	public Neuronio() {
		this.pesos = new double[9];
		this.bias = (Math.random() * 3) - 1;
	}
	
	public double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }
	
	public double calcularSaida(double[] entradas) {
        if (entradas.length != 9) {
            throw new IllegalArgumentException("O número de entradas deve ser igual ao número de pesos.");
        }

        // Calculando a soma ponderada das entradas + viés
        double soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += entradas[i] * pesos[i];
        }
        soma += bias;

        // Aplica a função de ativação (sigmoide)
        return sigmoid(soma);
    }

	public double[] getPesos() {
		return pesos;
	}

	public void setPesos(double[] pesos) {
		this.pesos = pesos;
	}

	public double getBias() {
		return bias;
	}

	public void setBias(double bias) {
		this.bias = bias;
	}
}

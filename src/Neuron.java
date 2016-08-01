import java.util.List;

/**
 * Created by FuBaR on 7/29/2016.
 */
public class Neuron {
    double[] inputs;
    double output;
    double[] weights;
    double targetSalary, predictedSalary;
    double bias = -1;
    double learningRate = 0.9;

    public Neuron() {
        weights = new double[8];
        for (int i = 0; i < 8; i++) {
            weights[i] = 1;
        }
    }

    public void train(List<double[]> trainingSet) {
        for (double[] vector : trainingSet) {
            inputs = new double[8];
            for (int i = 0; i < 7; i++) {
                inputs[i] = vector[i + 1];
            }
            targetSalary = vector[0];
            inputs[7] = bias;
            updateWeights();
        }
        //displayWeights();
    }

    public double predictSalary(double[] vector) {
        inputs = new double[8];
        for (int i = 0; i < 7; i++) {
            inputs[i] = vector[i + 1];
        }
        inputs[7] = bias;
        return net();
    }

    private void displayWeights() {
        for (int i = 0; i < 8; i++) {
            System.out.print(weights[i]);
        }
    }

    private void updateWeights() {
        for (int i = 0; i < 8; i++) {
            weights[i] = weights[i] + 2 * learningRate * (targetSalary - net()) * inputs[i];
        }
    }

    private double net() {
        double net = 0;
        for (int i = 0; i < 8; i++) {
            net += inputs[i] * weights[i];
        }
        return net;
    }
}

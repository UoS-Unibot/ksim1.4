package org.evors.rs.sim.ctrnn;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author miles
 */
public class CTRNN {

    private List neuronNames;

    public CTRNN(CTRNNNeuron[] neurons, int[] sensorIndices, double stepSize) {
        this.neurons = neurons;
        inputarray = new double[neurons.length];
        this.sensorIndices = sensorIndices;
        Arrays.fill(inputarray, 0);
        activation = SIGMOID_ACTIVATION;
        this.stepSize = stepSize;
    }

    public void setNeuronNames(List neuronNames) {
        this.neuronNames = neuronNames;
    }

    public int getNumberOfNeurons() {
        return neurons.length;
    }

    public CTRNNNeuron[] getNeurons() {
        return neurons;
    }

    private final CTRNNNeuron[] neurons;
    private final double[] inputarray;
    private final ActivationFunction activation;
    private final int[] sensorIndices;
    private final double stepSize;

    public void integrate(double[] inputs) {
        for (int i = 0; i < inputs.length; i++) {
            inputarray[sensorIndices[i]] = inputs[i];
        }
        for (int i = 0; i < neurons.length; i++) {
            double preSynInput = 0;
            for (int j = 0; j < neurons.length; j++) {
                preSynInput += neurons[j].weights[i] * neurons[j].activation;
            }
            neurons[i].state += stepSize * neurons[i].tau * (inputarray[i]
                    + preSynInput - neurons[i].state);
            neurons[i].activation = activation.getActivation((neurons[i].state
                    + neurons[i].bias) * neurons[i].gain);
        }
    }

    public static class CTRNNNeuron {

        final double gain;
        final double bias;
        final double tau;
        final double[] weights;
        double state;
        double activation;

        public CTRNNNeuron(double gain, double bias, double tau,
                double[] weights) {
            this.gain = gain;
            this.bias = bias;
            this.tau = tau;
            this.weights = weights;
            this.state = 0.5f;
        }

        public String toString() {
            NumberFormat fm = NumberFormat.getNumberInstance();
            fm.setMaximumFractionDigits(3);
            return fm.format(activation);
        }

    }

    public interface ActivationFunction {

        public double getActivation(double x);
    }
    public static final ActivationFunction SIGMOID_ACTIVATION
            = new ActivationFunction() {
                public double getActivation(double x) {
                    return (1.0f / (1 + Math.exp(-x)));
                }
            };
}

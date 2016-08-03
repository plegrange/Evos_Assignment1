import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import jxl.write.Number;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by FuBaR on 7/29/2016.
 */
public class Main {
    public static void main(String[] args) {
        try {
            new Main();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public Main() throws IOException, WriteException {
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        populateSets();
        // displayDoubles(normalizeSet(trainingSet));
        //displayBounds();
        double[] learningRates = new double[1000];
        double[] errors = new double[1000];
        double bestRate = 0, error, prevError;
        double bestError = 999999999;
        double learningRate = 0.01;
        for (int i = 0; i < 1000; i++) {
            train(learningRate);
            error = validate();
            if (error < bestError) {
                bestError = error;
                bestRate = learningRate;
            }
            if (error > bestError)
                break;
            //learningRates[i] = learningRate;
            //errors[i] = error;
            learningRate += 0.001;
        }
        displayTrainingSSE();
        //write(learningRates, errors);
        neuron.displayWeights();
        System.out.println("Best learning rate: " + bestRate + " -> Best SSE: " + bestError);
        System.out.println("SSE on validationSet: " + Math.sqrt(validate()/100));
        evaluateData();
    }

    private double validate() {
        double SSE = 0;
        for (int[] pattern : validationSet) {
            double[] vector = normalizeVector(pattern);
            SSE += validatePattern(vector);
        }
        // System.out.println(SSE);
        return SSE;

    }

    private double validatePattern(double[] vector) {
        double predictedSal = neuron.predictSalary(vector);
        predictedSal = denormalize(predictedSal);
        String s = String.format("%.5f  -> %.5f", denormalize(vector[0]), predictedSal);
       // System.out.println(s);
        return Math.pow(denormalize(vector[0]) - predictedSal, 2);
    }

    private void displayTrainingSSE() {
        double SSE = 0;
        for (int[] pattern : trainingSet) {
            double[] vector = normalizeVector(pattern);
            SSE += validatePattern(vector);
        }
        // System.out.println(SSE);
        System.out.println("Training Set SSE: " + SSE);
    }

    private double[] normalizeVector(int[] vector) {
        double[] normalizedVector = new double[8];
        for (int i = 0; i < 8; i++) {
            normalizedVector[i] = normalize(vector[i], i);
        }
        return normalizedVector;
    }

    private double denormalize(double sal) {
        return sal * (upperBound[0] - lowerBound[0]) + lowerBound[0];
    }

    List<int[]> completeSet, trainingSet, validationSet, evaluationSet;
    int[] lowerBound, upperBound;
    private String inputFile = "C:\\Users\\FuBaR\\IdeaProjects\\Evos_Assignment1\\SalData.xls";
    private Neuron neuron;

    private void train(double learningRate) {
        neuron = new Neuron(learningRate);
        neuron.train(normalizeSet(trainingSet));
    }

    private void populateSets() {
        trainingSet = new ArrayList<>();
        validationSet = new ArrayList<>();
        for (int x = 0; x < 1900; x++) {
            trainingSet.add(completeSet.get(x));
        }
        for (int x = 1900; x < 2000; x++) {
            validationSet.add(completeSet.get(x));
        }
    }

    private List<double[]> normalizeSet(List<int[]> set) {
        double[] normalizedVector;
        List<double[]> normalizedSet = new ArrayList<>();
        for (int[] vec : set) {
            normalizedVector = new double[8];
            for (int x = 0; x < 8; x++) {
                normalizedVector[x] = normalize(vec[x], x);
            }
            normalizedSet.add(normalizedVector);
        }
        return normalizedSet;
    }

    private void initializeBounds() {
        upperBound = new int[8];
        lowerBound = new int[8];
        for (int i = 0; i < 8; i++) {
            upperBound[i] = -9999;
            lowerBound[i] = 9999999;
        }
    }

    private void updateBounds(int[] vector) {
        if (vector[3] == 0) {
            System.out.print("");
        }
        for (int i = 0; i < 8; i++) {
            if (vector[i] * 2 > upperBound[i]) {
                upperBound[i] = vector[i] * 2;
            }
            if (vector[i] < lowerBound[i]) {
                lowerBound[i] = vector[i];
            }
        }
    }

    private void read() throws IOException {
        initializeBounds();
        completeSet = new ArrayList<>();
        File inputWorkbook = new File(inputFile);
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = workbook.getSheet(0);
            int[] newVector;
            for (int y = 1; y < 2001; y++) {
                newVector = new int[8];

                for (int x = 0; x < 8; x++) {
                    Cell cell = sheet.getCell(x, y);
                    newVector[x] = Integer.parseInt(cell.getContents());

                }
                updateBounds(newVector);
                completeSet.add(newVector);

            }
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    private String evaluationFile = "C://Users//FuBaR//IdeaProjects//Evos_Assignment1/Evaluation.xls";

    private void evaluateData() throws IOException {
        evaluationSet = new ArrayList<>();
        File inputWorkbook = new File(evaluationFile);
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = workbook.getSheet(0);
            int[] newVector;
            for (int y = 1; y < 11; y++) {
                newVector = new int[8];
                newVector[0] = 0;
                for (int x = 1; x < 8; x++) {
                    Cell cell = sheet.getCell(x - 1, y);
                    newVector[x] = Integer.parseInt(cell.getContents());

                }

                evaluationSet.add(newVector);

            }
        } catch (BiffException e) {
            e.printStackTrace();
        }
        System.out.println("Evaluation: ");
        for (int[] vector : evaluationSet) {
            double[] normalizedVector = normalizeVector(vector);
            double predictedSalary = neuron.predictSalary(normalizedVector);
            double denormalizedSalary = denormalize(predictedSalary);
            System.out.println(Math.round(denormalizedSalary));
        }
    }

    private String outputFile = "C://Users//FuBaR//IdeaProjects//Evos_Assignment1/output.xls";

    private void write(double[] learningRates, double[] SSE) throws IOException, WriteException {
        File file = new File(outputFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
        workbook.createSheet("Report", 0);
        WritableSheet excelSheet = workbook.getSheet(0);

        Number learningRate, sse;

        for (int i = 0; i < learningRates.length; i++) {
            learningRate = new Number(0, i, learningRates[i]);
            sse = new Number(1, i, SSE[i]);
            excelSheet.addCell(learningRate);
            excelSheet.addCell(sse);
        }

        workbook.write();
        workbook.close();
    }

    private void displayInts(List<int[]> set) {
        for (int[] vec : set) {
            for (int i = 0; i < 8; i++) {
                System.out.print(" " + vec[i]);
            }
            System.out.println();
        }
        System.out.println(set.size());
    }

    private void displayDoubles(List<double[]> set) {
        for (double[] vec : set) {
            for (int i = 0; i < 8; i++) {
                System.out.print(" " + vec[i]);
            }
            System.out.println();
        }
        System.out.println(set.size());
    }

    private void displayBounds() {
        for (int i = 0; i < 8; i++) {
            System.out.print(" " + upperBound[i]);
        }
        System.out.println();
        for (int i = 0; i < 8; i++) {
            System.out.print(" " + lowerBound[i]);
        }
        System.out.println();
    }

    private double normalize(int value, int index) {
        return (Double.valueOf(value) - Double.valueOf(lowerBound[index])) / (Double.valueOf(upperBound[index]) - Double.valueOf(lowerBound[index]));
    }
}

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FuBaR on 7/29/2016.
 */
public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        populateSets();
        displaySet(validationSet);
    }


    List<int[]> completeSet, trainingSet, validationSet;
    int[] lowerBound, upperBound;
    private String inputFile = "C:\\Users\\FuBaR\\IdeaProjects\\Evos_Assignment1\\SalData.xls";

    private void train() {

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

    private void initializeBounds() {
        for (int i = 0; i < 8; i++) {
            upperBound[i] = -9999;
            lowerBound[i] = 9999999;
        }
    }

    private void updateBounds(int[] vector) {
        for (int i = 0; i < 8; i++) {
            if (vector[i] > upperBound[i]) {
                upperBound[i] = vector[i];
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
                    updateBounds(newVector);
                }
                completeSet.add(newVector);

            }
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    private void displaySet(List<int[]> set) {
        for (int[] vec : set) {
            for (int i = 0; i < 8; i++) {
                System.out.print(" " + vec[i]);
            }
            System.out.println();
        }
        System.out.println(set.size());
    }

    private double normalize(int value, int index) {
        return (value - lowerBound[index]) / (upperBound[index] - lowerBound[index]);
    }
}

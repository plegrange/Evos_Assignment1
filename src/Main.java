import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by FuBaR on 7/29/2016.
 */
public class Main {
    public static void main(String[] args) {
        new Main();
    }

    public Main() {
        read();
    }


    List<int[]> completeSet, trainingSet, testingSet;
    int[] lowerBound, upperBound;

    private void train() {

    }

    private void populateSets() {

    }

    private void read() throws IOException {
        File inputWorkbook = new File(inputFile);
        Workbook workbook;
        try {
            workbook = Workbook.getWorkbook(inputWorkbook);
            Sheet sheet = workbook.getSheet(2);
            double[] newVector;
            for (int y = 1; y < 81; y++) {
                newVector = new double[8];

                for (int x = 0; x < 8; x++) {
                    Cell cell = sheet.getCell(x, y);
                    newVector[x] = cell
                }
                masterList.add(newLearner);

            }
        } catch (BiffException e) {
            e.printStackTrace();
        }
    }

    private double normalize(int value, int index) {
        return (value - lowerBound[index]) / (upperBound[index] - lowerBound[index]);
    }
}

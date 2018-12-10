package it.unisa.prioritization.criterion;

import cern.colt.matrix.impl.SparseDoubleMatrix2D;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class encapsulates a coverage matrix. The rows are the test, while the
 * columns are the coverage elements (e.g., statements)
 *
 * @author Annibale Panichella
 */
public class CoverageMatrix {

    private int maxCoverage_;
    private int[][] matrix;
    private int originalSize;

    public CoverageMatrix(String coverageFilename, boolean compacted) {
        try {
            matrix = readMatrixData(coverageFilename, compacted);
            maxCoverage_ = computeMaxCoverage();
        } catch (IOException e) {
            Logger.getLogger(CoverageMatrix.class.getName()).log(Level.SEVERE,
                    "Problem when creating the coverage matrix for the file "
                            + coverageFilename, e);
        }
    }

    public CoverageMatrix(ArrayList<ArrayList<Integer>> ma, boolean compacted) {
      
        matrix=readMatrice(ma);
      maxCoverage_= computeMaxCoverage();
        
        
    }

    /**
     * This method reads the coverage matrix from ASCII file
     *
     * @param coverageFilename name (+ full path) of the file containing the
     *                         coverage matrix
     * @return return a {@link SparseDoubleMatrix2D} encapsulating the coverage
     * matrix
     * @throws IOException
     */
    private int[][] readMatrixData(String coverageFilename, boolean compacted) throws IOException {
        if (coverageFilename == null) {
            throw new IllegalArgumentException();
        }
        BufferedReader coverageBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(coverageFilename)));
        int row = 0;
        int column = 0;
        String line;

        if (compacted) {
            originalSize = Integer.parseInt(coverageBufferedReader.readLine());
            Integer.parseInt(coverageBufferedReader.readLine());
        }

        while ((line = coverageBufferedReader.readLine()) != null) {
            row++;
            column = line.split(" ").length;
        }
        coverageBufferedReader.close();
        int[][] coverageMatrix = new int[row][column];

        coverageBufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(coverageFilename)));

        if (compacted) {
            coverageBufferedReader.readLine();
            coverageBufferedReader.readLine();
        }

        row = 0;
        while ((line = coverageBufferedReader.readLine()) != null) {
            column = 0;
            StringTokenizer st = new StringTokenizer(line, " ");

            while (st.hasMoreTokens()) {
                coverageMatrix[row][column] = Integer.parseInt(st.nextToken());
                column++;
            }
            row++;
        }

        return coverageMatrix;
    }

    /**
     * It computes the maximum coverage value that can be achieved when
     * executing all available tests
     *
     * @return
     */
    private int computeMaxCoverage() {
        int numRows = matrix.length;
        int numColumns = matrix[0].length;
        int maxCoverage = 0;

        int[] covered_statement = new int[numColumns];

        for (int[] row : matrix) {
            for (int j = 0; j < numColumns; j++) {
                covered_statement[j] = Math.max(row[j], covered_statement[j]);
            }
        }

        for (int i = 0; i < numColumns; i++) {
            maxCoverage += covered_statement[i];
        }

        return maxCoverage;
    }

    public int getSize() {
        return this.numberOfTests() * this.numberOfTargets();
    }

    public int getMaxCoverage() {
        return this.maxCoverage_;
    }

    public int numberOfTargets() {
        return this.matrix[0].length;
    }

    public int numberOfOriginalTargets() {
        return this.originalSize;
    }

    public int numberOfTests() {
        return this.matrix.length;
    }

    public int getElement(int testIndex, int targetIndex) {
        return this.matrix[testIndex][targetIndex];
    }


    private int[][] readMatrice(ArrayList<ArrayList<Integer>> ma) {
       int[][] a =new int[ma.get(1).size()][ma.get(1).size()] ;
      
       ArrayList<Integer> l=new ArrayList<>();
        for(int i=0;i<ma.size();i++){
           for(int j=0;j<ma.get(i).size();j++) {
               l=ma.get(i);
              a[i][j]=l.get(j); 
               
           }
            
        }
        
        
        return a;
  
    }

}

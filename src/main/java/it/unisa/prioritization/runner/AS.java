/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.prioritization.runner;

import it.unisa.plug.dado.WriteCvs;
import it.unisa.prioritization.algorithm.AdditionalGreedyPrioritization;
import it.unisa.prioritization.criterion.CoverageMatrix;
import it.unisa.prioritization.criterion.CumulativeCoverage;
import it.unisa.prioritization.criterion.ExecutionCostVector;
import it.unisa.prioritization.problems.GenericPrioritizationProblem;
import it.unisa.prioritization.problems.SingleObjectivePrioritizationProblem;
import it.unisa.prioritization.qualityIndicators.AFDPc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.fileoutput.SolutionListOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 *Utilizza l'algoritmo genetico per repriorizzare la test suite
 * 
 * @author Rembor
 */
public class AS {
/**
 * Prepara l'occorrente per l'algoritmo di riprioritizzazione
 * 
 * @param msg contiene la path assoluta del progetto
 * @throws InterruptedException
 * @throws IOException 
 */
    public static void matrixce(String msg) throws InterruptedException, IOException {
        String outputFolder = msg;
        String file = msg + "/matrice.csv";
        ArrayList<ArrayList<Integer>> ma= WriteCvs.getM();
        String file1 = msg + "/matricecosti.csv";
        CoverageMatrix a = new CoverageMatrix(file, false);
        CumulativeCoverage b = new CumulativeCoverage(a);
        //System.out.println(b.getMaxCoverage());
        ExecutionCostVector c = new ExecutionCostVector(file1);
        //System.out.println(c.getMaxCost());
        Algorithm algorithm = null;
        List<String> coverageFilenames = new ArrayList<>();
        coverageFilenames.add(msg + "/matrice.csv");
//        GenericPrioritizationProblem problem = new SingleObjectivePrioritizationProblem(
//                ocverageFilenames,
//                file1,
//                file2,
//                false);
        
        GenericPrioritizationProblem problem = new SingleObjectivePrioritizationProblem(
                ma,
                file1,
                false);

        algorithm = new AdditionalGreedyPrioritization(problem);
        algorithm.run();
        PermutationSolution<Integer> asd = (PermutationSolution<Integer>) algorithm.getResult();
        System.out.print(asd);

        Thread algorithmThread = new Thread(algorithm);
        algorithmThread.start();

        algorithmThread.join();

        List<PermutationSolution<Integer>> population = new ArrayList<>();

        if (algorithm.getResult() instanceof PermutationSolution) {
            population.add((PermutationSolution<Integer>) algorithm.getResult());
        } else if (algorithm.getResult() instanceof List) {
            population.addAll((List<PermutationSolution<Integer>>) algorithm.getResult());
        }


        new SolutionListOutput(population)
                .setSeparator(",")
                .setVarFileOutputContext(new DefaultFileOutputContext(outputFolder + "/VAR.txt"))
                .print();

        printAFDPc(problem, population, outputFolder);
      
    }
/**
 *  Stampa il risultato dell'algoritmo  nel file AFDP
 * @param problem contiene il risultato del problema
 * @param population contiene la popoazione
 * @param outputFolder contiene la path per il file output
 * @throws IOException 
 */
    private static void printAFDPc(GenericPrioritizationProblem problem, List<PermutationSolution<Integer>> population,
            String outputFolder) throws IOException {
        BufferedWriter afdpcBW = new DefaultFileOutputContext(outputFolder + "/AFDP.txt").getFileWriter();
        List<Double> afdpcList = new AFDPc(problem).evaluate(population);

        for (double afdpc : afdpcList) {
            afdpcBW.append(String.valueOf(afdpc)).append("\n");
        }
        
        afdpcBW.close();
    }

   

}

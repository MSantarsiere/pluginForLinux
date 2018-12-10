package it.unisa.prioritization.problems;

import it.unisa.prioritization.criterion.CoverageMatrix;
import it.unisa.prioritization.criterion.ExecutionCostVector;
import org.uma.jmetal.problem.impl.AbstractIntegerPermutationProblem;

import java.util.ArrayList;
import java.util.List;

/**
 * Super class representing a Testing Prioritization problem with any number of
 * testing criteria.
 *
 * @author Dario Di Nucci
 */
public abstract class GenericPrioritizationProblem extends AbstractIntegerPermutationProblem {

    public final List<CoverageMatrix> coverageCriteria = new ArrayList<>();
    public final ExecutionCostVector costCriterion;
   
 public  ArrayList<ArrayList<Integer>> ma=new ArrayList<>();
    /**
     * Public constructor
     *
     * @param coverageFilenames List of files (absolute path) storing the
     *                          coverage matrices
     * @param costFilename      file containing the execution cost info
     * @param faultFilename     file containing the fault coverage matrix
     */
    GenericPrioritizationProblem(List<String> coverageFilenames, String costFilename,  boolean compacted) {

        // load all coverage matrices
        for (String filename : coverageFilenames) {
            CoverageMatrix cov;
            cov = new CoverageMatrix(filename, compacted);
            this.coverageCriteria.add(cov);
            System.out.println("Read " + cov.getSize() + " elements from the coverage matrix '" + filename + "'");
        }

        // load cost info
        costCriterion = new ExecutionCostVector(costFilename);
        System.out.println("Read " + costCriterion.size() + " elements from the cost array.");

        // past fault matrix must not be compacted beacuse it is used for computing APFDc
        
        setNumberOfVariables(costCriterion.size());
        setNumberOfConstraints(0);
    }
    
    GenericPrioritizationProblem(ArrayList<ArrayList<Integer>> ma, String costFilename, boolean compacted) {

        // load all coverage matrices
       this.ma =ma;
         CoverageMatrix cov;
            cov = new CoverageMatrix(ma, compacted);
            this.coverageCriteria.add(cov);
        // load cost info
        costCriterion = new ExecutionCostVector(costFilename);
        System.out.println("Read " + costCriterion.size() + " elements from the cost array.");

     
        setNumberOfVariables(costCriterion.size());
        setNumberOfConstraints(0);
    }

}

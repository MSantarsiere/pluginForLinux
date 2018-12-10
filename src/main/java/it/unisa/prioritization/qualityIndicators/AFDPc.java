package it.unisa.prioritization.qualityIndicators;

import it.unisa.prioritization.criterion.GenericAveragePercentage;
import it.unisa.prioritization.problems.GenericPrioritizationProblem;
import org.uma.jmetal.qualityindicator.QualityIndicator;
import org.uma.jmetal.solution.PermutationSolution;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.naming.impl.SimpleDescribedEntity;

import java.util.ArrayList;
import java.util.List;

public class AFDPc extends SimpleDescribedEntity implements QualityIndicator<List<PermutationSolution<Integer>>, List<Double>> {

    private GenericPrioritizationProblem problem;

    public AFDPc(GenericPrioritizationProblem problem) {
        super("AFDPc", "Average Fault Detection Percentage");
        if (problem == null){
            throw new JMetalException("The problem is null");
        }
        this.problem = problem;
    }

    @Override
    public List<Double> evaluate(List<PermutationSolution<Integer>> solutions) {
        if (solutions == null) {
            throw new JMetalException("The solution list is null");
        }

        List<Double> afdpcList = new ArrayList<>();

       

        return  afdpcList;
    }

    @Override
    public String getName() {
        return "Average Fault Detection Percentage";
    }

    @Override
    public String getDescription() {
        return "Calculate the Average Fault Detection Percentage for each solution.";
    }
}

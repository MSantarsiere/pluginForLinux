package it.unisa.prioritization.problems;

import it.unisa.prioritization.criterion.CoverageMatrix;
import it.unisa.prioritization.criterion.CumulativeCoverage;
import org.uma.jmetal.solution.PermutationSolution;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a Testing Prioritization problem with any number of
 * testing criteria and a single objective.
 *
 * @author Annibale Panichella
 */
public class SingleObjectivePrioritizationProblem extends GenericPrioritizationProblem {

    public SingleObjectivePrioritizationProblem(List<String> coverageFilenames, String costFilename, boolean compacted) {
        super(coverageFilenames, costFilename, compacted);
        setNumberOfObjectives(1);
    }

    public SingleObjectivePrioritizationProblem(ArrayList<ArrayList<Integer>> ma, String file1, boolean b) {
        super(ma, file1,  b);
        setNumberOfObjectives(1);
    }

   
    /**
     * Evaluates a solution
     *
     * @param solution The solution to evaluate
     */
    @Override
    public void evaluate(PermutationSolution<Integer> solution) {
        // let's create cumulative coverage analyzers (one analyzer for each coverage matrix)
        List<CumulativeCoverage> objectives = new ArrayList<>();
        for (CoverageMatrix coverageCriterion : this.coverageCriteria) {
            objectives.add(new CumulativeCoverage(coverageCriterion));
        }

        double cost = 0;
        // initialize the cumulative coverage scores to 0
        List<Double> coverages = new ArrayList<>();
        for (int i = 0; i < this.coverageCriteria.size(); i++) {
            coverages.add(i, 0.0);
        }
        double hyperVolume = 0;
        for (int i = 0; i < getPermutationLength(); i++) {
            double previousCost = cost;

            cost = cost + costCriterion.getCostOfTest(solution.getVariableValue(i));

            // let's update the cumulative coverage scores
            for (int index = 0; index < this.coverageCriteria.size(); index++) {
                double cumulativeCoverage = objectives.get(index).updateCoverage(solution.getVariableValue(i));
                coverages.set(index, cumulativeCoverage);
            }

            // let's update the hyperVolume
            double delta = 1;
            for (int index = 0; index < this.coverageCriteria.size(); index++) {
                delta = delta * coverages.get(index);
            }
            hyperVolume = hyperVolume + (cost - previousCost) * delta;

            // if all coverage scores achieved the maximum values let's stop the cycle
            boolean isMaximum = true;
            for (int index = 0; index < this.coverageCriteria.size(); index++) {
                isMaximum = isMaximum & objectives.get(index).hasReachedMaxCoverage();
            }
            if (isMaximum) {
                break;
            }
        }
        // we add the remaining part of the hyperVolume (due to early stop when all coverage 
        // criteria reach the maximum coverage
        double delta = 1;
        for (CoverageMatrix coverageCriterion : this.coverageCriteria) {
            delta = delta * coverageCriterion.getMaxCoverage();
        }
        hyperVolume = hyperVolume + delta * (costCriterion.getMaxCost() - cost);

        // let's normalize the hypervolume
        for (int index = 0; index < this.coverageCriteria.size(); index++) {
            hyperVolume = hyperVolume / objectives.get(index).getMaxCoverage();
        }
        hyperVolume = hyperVolume / costCriterion.getMaxCost();
        solution.setObjective(0, -hyperVolume);
    }

    @Override
    public String getName() {
        return "SingleObjectivePrioritizationProblem";
    }

    @Override
    public int getPermutationLength() {
        return this.costCriterion.size();
    }

}

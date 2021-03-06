package metaheuristic.evolutionary;

import java.rmi.UnexpectedException;

import org.eclipse.ocl.ParserException;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

@SuppressWarnings("serial")
public class RMutation implements MutationOperator<RSolution> {
	private static final double DEFAULT_PROBABILITY = 0.01;
	private static final double DEFAULT_DISTRIBUTION_INDEX = 20.0;
	private double distributionIndex;
	private double mutationProbability;
	private RepairRSolution solutionRepair;

	private JMetalRandom randomGenerator;

	/** Constructor */
	public RMutation() {
		this(DEFAULT_PROBABILITY, DEFAULT_DISTRIBUTION_INDEX);
	}

	/** Constructor */
	public RMutation(RProblem problem, double distributionIndex) {
		this(1.0 / problem.getNumberOfVariables(), distributionIndex);
	}

	/** Constructor */
	public RMutation(double mutationProbability, double distributionIndex) {
		this(mutationProbability, distributionIndex, new RepairRSolutionAtBounds());
	}

	/** Constructor */
	public RMutation(double mutationProbability, double distributionIndex,
			RepairRSolution solutionRepair) {
		if (mutationProbability < 0) {
			throw new JMetalException("Mutation probability is negative: " + mutationProbability);
		} else if (distributionIndex < 0) {
			throw new JMetalException("Distribution index is negative: " + distributionIndex);
		}
		this.mutationProbability = mutationProbability;
		this.distributionIndex = distributionIndex;
		this.setSolutionRepair(solutionRepair);

		randomGenerator = JMetalRandom.getInstance();
	}

	/* Getters */
	public double getMutationProbability() {
		return mutationProbability;
	}

	public double getDistributionIndex() {
		return distributionIndex;
	}

	/* Setters */
	public void setMutationProbability(double probability) {
		this.mutationProbability = probability;
	}

	public void setDistributionIndex(double distributionIndex) {
		this.distributionIndex = distributionIndex;
	}

	/** Execute() method */
	@Override
	public RSolution execute(RSolution solution) throws JMetalException {
		if (null == solution) {
			throw new JMetalException("Null parameter");
		}
		//FIXME hardcoded
		int allowed = 1000;
		doMutation(mutationProbability, solution, allowed);
		return solution;
	}

	/** Perform the mutation operation */
	private void doMutation(double probability, RSolution solution, int allowed_failures) {

		for (int i = 0; i < solution.getNumberOfVariables(); i++) {
			if (randomGenerator.nextDouble() <= probability) {
				try {
					assert(i==0);
					boolean altered = false;
					int num_failures = 0;
					while(!altered){
						if(solution.alter(randomGenerator.nextInt(0, solution.getVariableValue(0).getRefactoring().getActions().size()-1))){
							altered = true;
							break;
						}
						else{
							num_failures++;
							if (num_failures >= allowed_failures)
								break;
						}
					}

					
					if(!altered)
						System.out.println("Mutation left solution unchanged");
						
				} catch (UnexpectedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParserException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	public RepairRSolution getSolutionRepair() {
		return solutionRepair;
	}

	public void setSolutionRepair(RepairRSolution solutionRepair) {
		this.solutionRepair = solutionRepair;
	}
}

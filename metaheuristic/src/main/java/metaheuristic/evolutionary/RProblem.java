package metaheuristic.evolutionary;

import java.rmi.UnexpectedException;

import org.eclipse.ocl.ParserException;
import org.eclipse.uml2.uml.Model;
import org.uma.jmetal.problem.impl.AbstractGenericProblem;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import metaheuristic.managers.Manager;

@SuppressWarnings("serial")
public class RProblem extends AbstractGenericProblem<RSolution>{	
	
	protected Model model;
	protected int length_of_refactorings;
	protected int number_of_actions;
	protected int allowed_failures;
	protected Manager manager;
	
	protected final int NUM_VAR = 1;
	protected final int NUM_OBJ = 3;
	protected final int VARIABLE_INDEX = 0;
	protected final int NUM_CON = 0;
	protected final int FIRST_OBJ = 0;
	protected final int SECOND_OBJ = 1;
	protected final int THIRD_OBJ = 2;

	  
	public RProblem(String modelUri, int desired_length, int n, int a,int population){
	
		//boolean isDebug = java.lang.management.ManagementFactory.getRuntimeMXBean().getInputArguments().toString().indexOf("jdwp") >= 0;

		this.setName("OptimalRefactoring");
		this.setNumberOfObjectives(NUM_OBJ);
		this.setNumberOfConstraints(NUM_CON);
		this.setNumberOfVariables(NUM_VAR);
		
		this.manager = Manager.getInstance();
		this.manager.init(modelUri);
		this.model = Manager.getInstance().getModel();
		this.length_of_refactorings = desired_length;
		this.allowed_failures = a;

		this.number_of_actions = n;



		
	}
	
	public Model getModel() {
		return model;
	}

	public void setModel(Model model) {
		this.model = model;
	}

	public int getLength_of_refactorings() {
		return length_of_refactorings;
	}

	public void setLength_of_refactorings(int length_of_refactorings) {
		this.length_of_refactorings = length_of_refactorings;
	}

	@Override
	public void evaluate(RSolution solution)  {
		
		for (int i = 0; i < this.getNumberOfObjectives(); i++) {
			if(i==FIRST_OBJ){
				solution.getVariableValue(VARIABLE_INDEX).getRefactoring().setCost(
						Manager.calculateCost(solution.getVariableValue(VARIABLE_INDEX).getRefactoring()));
				solution.setObjective(i, solution.getVariableValue(VARIABLE_INDEX).getRefactoring().getCost());
			}
			else if(i==SECOND_OBJ){
				solution.getVariableValue(VARIABLE_INDEX).getRefactoring().setNumOfChanges(
						Manager.calculateNumOfChanges(solution.getVariableValue(VARIABLE_INDEX).getRefactoring()));				
				solution.setObjective(i, solution.getVariableValue(VARIABLE_INDEX).getRefactoring().getNumOfChanges());
			}
			else if(i==THIRD_OBJ){
				//Manager.completeAntipatternDetection(antipatterns, umlModelUri);
				//FIXME
				solution.setObjective(i, JMetalRandom.getInstance().getRandomGenerator().nextInt(0, this.length_of_refactorings));
			}
			else{
				System.out.println("\n"+i);
				throw new RuntimeException("unexpected behav");
			}
				
	    }
	}

	@Override
	public RSolution createSolution()  {

		try {
			return new RSolution(this);
		} 
		catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnexpectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	

	

}

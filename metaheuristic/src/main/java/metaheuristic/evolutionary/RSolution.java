package metaheuristic.evolutionary;

import java.rmi.UnexpectedException;
import java.text.DecimalFormat;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ocl.ParserException;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.solution.impl.AbstractGenericSolution;
import org.uma.jmetal.util.solutionattribute.impl.CrowdingDistance;
import org.uma.jmetal.util.solutionattribute.impl.DominanceRanking;

import logicalSpecification.Action;
import logicalSpecification.LogicalSpecificationFactory;


@SuppressWarnings("serial")
public class RSolution extends AbstractGenericSolution<RSequence, RProblem> implements Solution<RSequence> {
	
	private final int VARIABLE_INDEX = 0;

	protected RSolution(RProblem p) throws ParserException, UnexpectedException {
		super(p);
	    
		this.createRandomRefactoring(p.length_of_refactorings,p.number_of_actions, p.allowed_failures);
	}

	private void createRandomRefactoring(int l, int n, int a) throws UnexpectedException, ParserException {
		this.setVariableValue(VARIABLE_INDEX,new RSequence(l,n,a));		
		this.setAttribute(CrowdingDistance.class, 0.0);
	}

	@Override
	public String getVariableValueString(int index) {
	    return getVariableValue(index).toString();
	}

	public RSolution(RSolution s) {
		super(s.problem);
		this.copyRefactoringVariable(s.getVariableValue(VARIABLE_INDEX));
		
		for (int i = 0; i < s.problem.getNumberOfObjectives(); i++) {
	        this.setObjective(i, s.getObjective(i)) ;
	    }
		this.attributes = s.attributes;
		this.setAttribute(CrowdingDistance.class, s.getAttribute(CrowdingDistance.class));
	}


	private void copyRefactoringVariable(RSequence variableValue) {
		this.setVariableValue(VARIABLE_INDEX, new RSequence(variableValue));	
		
	}

	@Override
	public Solution<RSequence> copy() {
		return new RSolution(this);
	}

	
	public RSolution(RSolution s1,RSolution s2,int point,boolean left) {
		
		super(s1.problem);
		
		assert(s1.problem.equals(s2.problem));
		assert(s1.getNumberOfObjectives()==s2.getNumberOfObjectives());
		assert(s1.getNumberOfVariables()==s2.getNumberOfVariables());
	    assert(s1.getLength()==s2.getLength());	    
	    assert(point>0 && point<s1.getLength());

		for (int i = 0; i < s1.problem.getNumberOfVariables(); i++) {
			if(i==VARIABLE_INDEX){
				if(left){
					this.setVariableValue(VARIABLE_INDEX,this.createChild(s1,s2,point));
				}
				else{
					this.setVariableValue(VARIABLE_INDEX,this.createChild(s2,s1,point));

				}
				assert(this.getLength()==s1.getLength() && s1.getLength()==s2.getLength());
			}
			else{
				try {
					throw new UnexpectedException("Should not happen");
				} 
				catch (UnexpectedException e) {
					e.printStackTrace();
				}
			}   
			
		}

		for (int i = 0; i < s1.problem.getNumberOfObjectives(); i++) {
	        this.setObjective(i, s1.getObjective(i));
		}

		this.setAttribute(CrowdingDistance.class,0.0);
		//FIXME
		//this.attributes = s1.attributes;

	    assert(this.getVariableValue(VARIABLE_INDEX).getLength()==s1.getVariableValue(VARIABLE_INDEX).getLength());
	    assert(this.getVariableValue(0).getRefactoring().getActions().size()==this.getLength());
	}
	


	private RSequence createChild(RSolution s1, RSolution s2, int point) {
		RSequence seq = new RSequence();
		for (int j = 0; j < point; j++) {
			Action _new = LogicalSpecificationFactory.eINSTANCE.createAction();
			_new = (Action) EcoreUtil.copy(s1.getActionAt(j));
			assert(_new.equals(s1.getActionAt(j)));
			seq.insert(j,_new);
	    }
	    for (int j = point; j < s1.getLength(); j++) {
	    	Action _new = LogicalSpecificationFactory.eINSTANCE.createAction();
			_new = (Action) EcoreUtil.copy(s2.getActionAt(j));
			assert(_new.equals(s2.getActionAt(j)));
			seq.insert(j,_new);		
		}	
	    return seq;
	}

	public Action getActionAt(int index) {
		return getVariableValue(VARIABLE_INDEX).get(index);
	}
	public int getLength() {
		return getVariableValue(VARIABLE_INDEX).getLength();
	}
	public boolean alter(int i) throws UnexpectedException, ParserException {
		
		return this.getVariableValue(VARIABLE_INDEX).alter(i,this.problem.number_of_actions);	
	}


	 @Override
	  public String toString() {
		
		String result = "Variables: " ;
		for (int i = 0; i < this.problem.getNumberOfVariables(); i++) {
	      result += "" + this.getVariableValueString(i) + " " ;
	    }
	    result += "Objectives: " ;
	    for (int i = 0; i < this.problem.getNumberOfObjectives(); i++) {
	      result += "" + new DecimalFormat("#.##").format(this.getObjective(i)).replaceAll(",",".") + " " ;
	    }
	    result += "DominanceRanking: " +  new DecimalFormat("#.##").format(this.getAttribute(DominanceRanking.class)).replaceAll(",",".");
	    result += " CrowdingDistance: " +  new DecimalFormat("#.##").format(this.getAttribute(CrowdingDistance.class)).replaceAll(",",".");
	    result += "\n";
	    return result ;
	  }


}

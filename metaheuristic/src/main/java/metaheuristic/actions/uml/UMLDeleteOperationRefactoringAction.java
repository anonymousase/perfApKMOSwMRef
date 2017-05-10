package metaheuristic.actions.uml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Operation;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import logicalSpecification.AndOperator;
import logicalSpecification.ExistsOperator;
import logicalSpecification.FOLSpecification;
import logicalSpecification.MultipleValuedParameter;
import logicalSpecification.NotOperator;
import logicalSpecification.Parameter;
import logicalSpecification.PostCondition;
import logicalSpecification.PreCondition;
import logicalSpecification.SingleValuedParameter;
import logicalSpecification.actions.UML.impl.UMLDeleteOperationActionImpl;
import logicalSpecification.impl.ActionImpl;
import metaheuristic.evolutionary.Controller;
import metaheuristic.managers.Manager;
import metaheuristic.managers.OclStringManager;

public class UMLDeleteOperationRefactoringAction extends UMLDeleteOperationActionImpl {

//	private Operation umlOpToDel;
//	
//	private SingleValuedParameter opToDelSVP;
//	private MultipleValuedParameter allOpsMVP;
	
	private static Double MAX_VALUE = 100.0;

	public UMLDeleteOperationRefactoringAction(Operation operation){
		setUmlOpToDel(operation);
		setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		setParameters();
		createPreCondition();
		createPostCondition();
	}
	
//	private UMLDeleteOperationRefactoringAction(UMLDeleteOperationRefactoringAction actionToCopy) {
//		setUmlOpToDel(actionToCopy.getUmlOpToDel());
//		setCost(actionToCopy.getCost());
//		setNumOfChanges(actionToCopy.getNumOfChanges());
//		setParameters(actionToCopy.getParameters());
//		createPreCondition(actionToCopy.getPre());
//		createPostCondition(actionToCopy.getPost());
//	}
//
//	private void createPostCondition(PostCondition post) {
//		setPost(post);
//	}
//
//	private void createPreCondition(PreCondition pre) {
//		setPre(pre);
//	}
//
//	private void setParameters(EList<Parameter> parameters) {
//		getParameters().addAll(parameters);
//	}
//	
//	public UMLDeleteOperationRefactoringAction cloneAction(){
//		return new UMLDeleteOperationRefactoringAction(this);
//	}
	
	@Override
	public void execute() {
		getUmlOpToDel().destroy();
	}

	@Override
	public void log(){
		Controller.logger_.info("UMLDeleteOperationRefactoringAction");
//		Controller.logger_.info(getUmlOpToDel().getName());
	}
	
	@Override
	public void createPostCondition() {
		PostCondition postCondition = Manager.createPostCondition();
		FOLSpecification specification = Manager.createFOLSpectification("DeleteOperationPostCondition");

		AndOperator preAnd = Manager.createAndOperator();
		
		NotOperator notOperator = Manager.createNotOperator();
		ExistsOperator existsOperator = Manager.createExistsOperator(getOpToDelSVP(), getAllOpsMVP());
		notOperator.setArgument(existsOperator);
		
		preAnd.getArguments().add(notOperator);

		specification.setRootOperator(preAnd);
		
		postCondition.setConditionFormula(specification);
		
		setPost(postCondition);

	}

	@Override
	public void createPreCondition() {
		PreCondition preCondition = Manager.createPreCondition();
		FOLSpecification specification = Manager.createFOLSpectification("DeleteOperationPreCondition");
		
		AndOperator preAnd = Manager.createAndOperator();
		
		ExistsOperator existsOperator = Manager.createExistsOperator(getOpToDelSVP(), getAllOpsMVP());
		
		preAnd.getArguments().add(existsOperator);
		
		specification.setRootOperator(preAnd);
		preCondition.setConditionFormula(specification);
		
		setPre(preCondition);
	}

	@Override
	public void setParameters() {
		// TODO Auto-generated method stub
		List<Parameter> delOpParams = new ArrayList<>();
		
		setOpToDelSVP(Manager.createSingleValueParameter(OclStringManager.getOperationQuery(getUmlOpToDel())));
		delOpParams.add(getOpToDelSVP());
		
		
		setAllOpsMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllOperationsQuery()));
		delOpParams.add(getAllOpsMVP());
		
		getParameters().addAll(delOpParams);
	}
	
//	public Operation getUmlOpToDel() {
//		return umlOpToDel;
//	}
//
//	public void setUmlOpToDel(Operation operation) {
//		this.umlOpToDel = operation;
//	}
//
//	SingleValuedParameter getOpToDelSVP() {
//		return opToDelSVP;
//	}
//
//	void setOpToDelSVP(SingleValuedParameter opToDelSVP) {
//		this.opToDelSVP = opToDelSVP;
//	}
//
//	MultipleValuedParameter getAllOpsMVP() {
//		return allOpsMVP;
//	}
//
//	void setAllOpsMVP(MultipleValuedParameter allOpsMVP) {
//		this.allOpsMVP = allOpsMVP;
//	}

}

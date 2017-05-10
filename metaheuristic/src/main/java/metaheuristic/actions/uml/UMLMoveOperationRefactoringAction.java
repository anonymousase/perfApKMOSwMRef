package metaheuristic.actions.uml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Component;
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
import logicalSpecification.actions.UML.impl.UMLMoveOperationActionImpl;
import logicalSpecification.impl.ActionImpl;
import metaheuristic.evolutionary.Controller;
import metaheuristic.managers.Manager;
import metaheuristic.managers.OclStringManager;

public class UMLMoveOperationRefactoringAction extends UMLMoveOperationActionImpl {

//	private Operation umlOpToMove;
//	private Component umlCompTarget;
//	
//	private SingleValuedParameter opToMoveSVP;
//	private SingleValuedParameter targetCompSVP;
//	private MultipleValuedParameter allOpsMVP;
//	private MultipleValuedParameter allCompsMVP;
//	private MultipleValuedParameter allTargetCompOpsMVP;
	
	private static Double MAX_VALUE = 100.0;
	
	
	public UMLMoveOperationRefactoringAction(Operation context, Component target) {
		setUmlOpToMove(context);
		setUmlTargetComp(target);
		setParameters();
		createPreCondition();
		createPostCondition();
		setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
	}
	
//	private UMLMoveOperationRefactoringAction(UMLMoveOperationRefactoringAction actionToCopy) {
//		setUmlOpToMove(actionToCopy.getUmlOpToMove());
//		setUmlCompTarget(actionToCopy.getUmlCompTarget());
//		
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
//	public UMLMoveOperationRefactoringAction cloneAction(){
//		return new UMLMoveOperationRefactoringAction(this);
//	}
	
	@Override
	public void execute() {
		getUmlOpToMove().setClass_(getUmlTargetComp());
	}

	@Override
	public void log() {
		Controller.logger_.info("UMLMoveOperationRefactoringAction");
//		Controller.logger_.info(getUmlCompTarget().toString());
//		for(Operation op : getUmlCompTarget().getOperations()){
//			Controller.logger_.info(op.toString());
//		}
	}

	@Override
	public void createPostCondition() {
		// TODO Auto-generated method stub
		PostCondition postCondition = Manager.createPostCondition();
		FOLSpecification specification = Manager.createFOLSpectification("MoveOperationPostCondition");
		
		ExistsOperator existsOpInOperations = Manager.createExistsOperator(getOpToMoveSVP(), getAllOpsMVP());
		ExistsOperator existsTargetInComponents = Manager.createExistsOperator(getTargetCompSVP(), getAllCompsMVP());
		ExistsOperator existsOpInOpsOfTarget = Manager.createExistsOperator(getOpToMoveSVP(), getAllTargetCompOpsMVP());
		
		AndOperator andRoot = Manager.createAndOperator();
		andRoot.getArguments().add(existsOpInOperations);
		andRoot.getArguments().add(existsTargetInComponents);
		andRoot.getArguments().add(existsOpInOpsOfTarget);
		
		specification.setRootOperator(andRoot);
		postCondition.setConditionFormula(specification);
		setPost(postCondition);
	}

	@Override
	public void createPreCondition() {
		// TODO Auto-generated method stub
		PreCondition preCondition = Manager.createPreCondition();
		FOLSpecification specification = Manager.createFOLSpectification("MoveOperationPreCondition");
		
		ExistsOperator existsOpInOperations = Manager.createExistsOperator(getOpToMoveSVP(), getAllOpsMVP());
		ExistsOperator existsTargetInComponents = Manager.createExistsOperator(getTargetCompSVP(), getAllCompsMVP());
		ExistsOperator existsOpInOpsOfTarget = Manager.createExistsOperator(getOpToMoveSVP(), getAllTargetCompOpsMVP());
		NotOperator notExistsOpInOpsOfTarget = Manager.createNotOperator(existsOpInOpsOfTarget);
		
		AndOperator andRoot = Manager.createAndOperator();
		andRoot.getArguments().add(existsOpInOperations);
		andRoot.getArguments().add(existsTargetInComponents);
		andRoot.getArguments().add(notExistsOpInOpsOfTarget);
		
		specification.setRootOperator(andRoot);
		preCondition.setConditionFormula(specification);
		setPre(preCondition);
	}

	@Override
	public void setParameters() {
		// TODO Auto-generated method stub
		List<Parameter> moveOpParams = new ArrayList<>();

		setOpToMoveSVP(Manager.createSingleValueParameter(OclStringManager.getOperationQuery(getUmlOpToMove())));
		moveOpParams.add(getOpToMoveSVP());
		
		setTargetCompSVP(Manager.createSingleValueParameter(OclStringManager.getComponentQuery(getUmlTargetComp())));
		moveOpParams.add(getTargetCompSVP());
		
		
		setAllOpsMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllOperationsQuery()));
		moveOpParams.add(getAllOpsMVP());
		
		setAllCompsMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllComponentsQuery()));
		moveOpParams.add(getAllCompsMVP());
		
		setAllTargetCompOpsMVP(Manager.createMultipleValuedParameter(OclStringManager.getOperationsOfQuery(getUmlTargetComp())));
		moveOpParams.add(getAllTargetCompOpsMVP());
		
		getParameters().addAll(moveOpParams);
	}

//	SingleValuedParameter getOpToMoveSVP() {
//		return opToMoveSVP;
//	}
//
//	void setOpToMoveSVP(SingleValuedParameter opToMoveSVP) {
//		this.opToMoveSVP = opToMoveSVP;
//	}
//
//	SingleValuedParameter getTargetCompSVP() {
//		return targetCompSVP;
//	}
//
//	void setTargetCompSVP(SingleValuedParameter targetCompSVP) {
//		this.targetCompSVP = targetCompSVP;
//	}
//
//	MultipleValuedParameter getAllOpsMVP() {
//		return allOpsMVP;
//	}
//
//	void setAllOpsMVP(MultipleValuedParameter allOps) {
//		this.allOpsMVP = allOps;
//	}
//
//	MultipleValuedParameter getAllCompsMVP() {
//		return allCompsMVP;
//	}
//
//	void setAllCompsMVP(MultipleValuedParameter allComps) {
//		this.allCompsMVP = allComps;
//	}
//
//	MultipleValuedParameter getAllTargetCompOpsMVP() {
//		return allTargetCompOpsMVP;
//	}
//
//	void setAllTargetCompOpsMVP(MultipleValuedParameter allTargetCompOps) {
//		this.allTargetCompOpsMVP = allTargetCompOps;
//	}
//
//	private Operation getUmlOpToMove() {
//		return umlOpToMove;
//	}
//
//	private void setUmlOpToMove(Operation umlOpToMove) {
//		this.umlOpToMove = umlOpToMove;
//	}
//
//	private Component getUmlCompTarget() {
//		return umlCompTarget;
//	}
//
//	private void setUmlCompTarget(Component umlCompTarget) {
//		this.umlCompTarget = umlCompTarget;
//	}

}

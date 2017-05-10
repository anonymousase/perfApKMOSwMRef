package metaheuristic.actions.uml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Node;
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
import logicalSpecification.actions.UML.impl.UMLDeleteNodeActionImpl;
import logicalSpecification.impl.ActionImpl;
import metaheuristic.evolutionary.Controller;
import metaheuristic.managers.Manager;
import metaheuristic.managers.OclStringManager;

public class UMLDeleteNodeRefactoringAction extends UMLDeleteNodeActionImpl {

//	private Node umlNodeToDel;
//	
//	private SingleValuedParameter nodeToDelSVP;
//	private MultipleValuedParameter allNodesMVP;
	
	private static Double MAX_VALUE = 100.0;

	public UMLDeleteNodeRefactoringAction(Node node) {
		setUmlNodeToDel(node);
		setParameters();
		createPreCondition();
		createPostCondition();
		setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
	}
	
//	private UMLDeleteNodeRefactoringAction(UMLDeleteNodeRefactoringAction actionToCopy) {
//		setUmlNodeToDel(actionToCopy.getUmlNodeToDel());
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
//	public UMLDeleteNodeRefactoringAction cloneAction(){
//		return new UMLDeleteNodeRefactoringAction(this);
//	}
	
	@Override
	public void execute() {
		
		umlNodeToDel.destroy();
		
	}

	@Override
	public void log(){
		Controller.logger_.info("UMLDeleteNodeRefactoringAction");
//		Controller.logger_.info(getUmlNodeToDel().getName());
	}

	@Override
	public void createPostCondition() {
		PostCondition postCondition = Manager.createPostCondition();
		FOLSpecification specification = Manager.createFOLSpectification("DeleteNodePostCondition");
		
		AndOperator preAnd = Manager.createAndOperator();
		
		NotOperator notOperator = Manager.createNotOperator();
		ExistsOperator existsOperator = Manager.createExistsOperator(getNodeToDelSVP(), getAllNodesMVP());

		notOperator.setArgument(existsOperator);
		preAnd.getArguments().add(notOperator);

		specification.setRootOperator(preAnd);
		postCondition.setConditionFormula(specification);
		setPost(postCondition);
	}

	@Override
	public void createPreCondition() {
		PreCondition preCondition = Manager.createPreCondition();

		FOLSpecification specification = Manager.createFOLSpectification("DeleteNodePreCondition");

		AndOperator preAnd = Manager.createAndOperator();
		
		ExistsOperator existsOperator = Manager.createExistsOperator(getNodeToDelSVP(), getAllNodesMVP());

		preAnd.getArguments().add(existsOperator);
		
		specification.setRootOperator(preAnd);
		preCondition.setConditionFormula(specification);
		setPre(preCondition);
	}

	@Override
	public void setParameters() {
		// TODO Auto-generated method stub
		List<Parameter> delNodeParams = new ArrayList<>();
		
		setNodeToDelSVP(Manager.createSingleValueParameter(OclStringManager.getNodeQuery(getUmlNodeToDel())));
		delNodeParams.add(getNodeToDelSVP());
		
		setAllNodesMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllNodesQuery()));
		delNodeParams.add(getAllNodesMVP());
		
		getParameters().addAll(delNodeParams);
	}

//	public Node getUmlNodeToDel() {
//		return umlNodeToDel;
//	}
//
//	public void setUmlNodeToDel(Node node) {
//		this.umlNodeToDel = node;
//	}
//
//	SingleValuedParameter getNodeToDelSVP() {
//		return nodeToDelSVP;
//	}
//
//	void setNodeToDelSVP(SingleValuedParameter nodeToDelSVP) {
//		this.nodeToDelSVP = nodeToDelSVP;
//	}
//
//	MultipleValuedParameter getAllNodesMVP() {
//		return allNodesMVP;
//	}
//
//	void setAllNodesMVP(MultipleValuedParameter allNodesMVP) {
//		this.allNodesMVP = allNodesMVP;
//	}

}

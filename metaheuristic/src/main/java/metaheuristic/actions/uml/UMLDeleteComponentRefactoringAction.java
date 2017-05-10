package metaheuristic.actions.uml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Manifestation;
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
import logicalSpecification.actions.UML.impl.UMLDeleteComponentActionImpl;
import logicalSpecification.impl.ActionImpl;
import metaheuristic.evolutionary.Controller;
import metaheuristic.managers.Manager;
import metaheuristic.managers.OclStringManager;
import metaheuristic.managers.uml.UMLManager;

public class UMLDeleteComponentRefactoringAction extends UMLDeleteComponentActionImpl {

//	private Component umlCompToDel;
//	
//	private SingleValuedParameter compToDelSVP;
//	private MultipleValuedParameter allCompsMVP;
	
	private static Double MAX_VALUE = 100.0;

	public UMLDeleteComponentRefactoringAction(Component component) {
		setUmlCompToDel(component);
		setParameters();
		createPreCondition();
		createPostCondition();
		setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
	}
	
//	private UMLDeleteComponentRefactoringAction(UMLDeleteComponentRefactoringAction actionToCopy) {
//		setUmlCompToDel(actionToCopy.getUmlCompToDel());
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
//	public UMLDeleteComponentRefactoringAction cloneAction(){
//		return new UMLDeleteComponentRefactoringAction(this);
//	}

	@Override
	public void execute() {
		List<Manifestation> list_of_manifestations = UMLManager.getAllManifestationsOf(getUmlCompToDel());
		for (Manifestation man : list_of_manifestations) {
			if (man.getUtilizedElement().getNamespace() == getUmlCompToDel().getNamespace()) {
				man.destroy();
			}
		}
		umlCompToDel.destroy();
	}

	@Override
	public void log() {
		Controller.logger_.info("UMLDeleteComponentRefactoringAction");
//		Controller.logger_.info(getUmlCompToDel().getName());
	}

//	public Component getUmlCompToDel() {
//		return umlCompToDel;
//	}
//
//	public void setUmlCompToDel(Component component) {
//		this.umlCompToDel = component;
//	}

	@Override
	public void createPostCondition() {
		PostCondition postCondition = Manager.createPostCondition();

		FOLSpecification specification = Manager.createFOLSpectification("DeleteComponentPostCondition");
		AndOperator preAnd = Manager.createAndOperator();
		NotOperator notOperator = Manager.createNotOperator();
		ExistsOperator existsOperator = Manager.createExistsOperator(getCompToDelSVP(), getAllCompsMVP());
		notOperator.setArgument(existsOperator);
		preAnd.getArguments().add(notOperator);
		specification.setRootOperator(preAnd);
		postCondition.setConditionFormula(specification);
		setPost(postCondition);
	}

	@Override
	public void createPreCondition() {
		PreCondition preCondition = Manager.createPreCondition();
		FOLSpecification specification = Manager.createFOLSpectification("DeleteComponentPreCondition");
		AndOperator preAnd = Manager.createAndOperator();
		ExistsOperator exists = Manager.createExistsOperator(getCompToDelSVP(), getAllCompsMVP());
		preAnd.getArguments().add(exists);
		specification.setRootOperator(preAnd);
		preCondition.setConditionFormula(specification);
		setPre(preCondition);
	}

	@Override
	public void setParameters() {
		List<Parameter> delCompParams = new ArrayList<>();
		
		setCompToDelSVP(Manager.createSingleValueParameter(OclStringManager.getComponentQuery(getUmlCompToDel())));
		delCompParams.add(getCompToDelSVP());
		
		setAllCompsMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllComponentsQuery()));
		delCompParams.add(getAllCompsMVP());
		
		getParameters().addAll(delCompParams);
	}

//	SingleValuedParameter getCompToDelSVP() {
//		return compToDelSVP;
//	}
//
//	void setCompToDelSVP(SingleValuedParameter compToDelSVP) {
//		this.compToDelSVP = compToDelSVP;
//	}
//
//	MultipleValuedParameter getAllCompsMVP() {
//		return allCompsMVP;
//	}
//
//	void setAllCompsMVP(MultipleValuedParameter allComps) {
//		this.allCompsMVP = allComps;
//	}

}

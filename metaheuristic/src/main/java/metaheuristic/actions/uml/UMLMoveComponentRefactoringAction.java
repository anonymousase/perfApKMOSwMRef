package metaheuristic.actions.uml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.Manifestation;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.UMLFactory;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import logicalSpecification.AndOperator;
import logicalSpecification.ExistsOperator;
import logicalSpecification.FOLSpecification;
import logicalSpecification.ForAllOperator;
import logicalSpecification.MultipleValuedParameter;
import logicalSpecification.NotOperator;
import logicalSpecification.Parameter;
import logicalSpecification.PostCondition;
import logicalSpecification.PreCondition;
import logicalSpecification.SingleValuedParameter;
import logicalSpecification.actions.UML.impl.UMLMoveComponentActionImpl;
import logicalSpecification.impl.ActionImpl;
import metaheuristic.evolutionary.Controller;
import metaheuristic.managers.Manager;
import metaheuristic.managers.OclStringManager;
import metaheuristic.managers.uml.UMLManager;

public class UMLMoveComponentRefactoringAction extends UMLMoveComponentActionImpl {

//	private Component umlCompToMove;
//	private List<Node> umlTargetNodes;
//	private List<Artifact> umlArtifacts = new ArrayList<Artifact>();
//	
//	private SingleValuedParameter compToMoveSVP;
//	private MultipleValuedParameter targetNodesMVP;
//	private MultipleValuedParameter artifactsMVP;
//	private MultipleValuedParameter allCompsMVP;
//	private MultipleValuedParameter allTargetsMVP;
//	private MultipleValuedParameter allNodesMVP;
//	private MultipleValuedParameter allDeployedElemsMVP;
	
	private static Double MAX_VALUE = 100.0;

	public UMLMoveComponentRefactoringAction(Component component, List<Node> targets) {
		setUmlCompToMove(component);
		getUmlTargetNodes().addAll(targets);
		setParameters();
		createPreCondition();
		createPostCondition();
		setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
	}
	
//	private UMLMoveComponentRefactoringAction(UMLMoveComponentRefactoringAction actionToCopy) {
//		setUmlCompToMove(actionToCopy.getUmlCompToMove());
//		setUmlTargetNodes(actionToCopy.getUmlTargetNodes());
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
//	public UMLMoveComponentRefactoringAction cloneAction(){
//		return new UMLMoveComponentRefactoringAction(this);
//	}

	@Override
	public void execute() {
		moveDeployments();
	}

	@SuppressWarnings({ })
	public void moveDeployments() {

		List<Manifestation> manifestations = new ArrayList<Manifestation>();

		manifestations = UMLManager.getAllManifestations();

		for (Manifestation man : manifestations) {
			if (man.getUtilizedElement().getQualifiedName().equals(umlCompToMove.getQualifiedName())) {
				man.destroy();
			}
		}

		umlArtifacts = UMLManager.getAllArtifacts();

		for (Artifact art : umlArtifacts) {
			if (art.getManifestations().isEmpty()) {
				art.destroy();
			}
		}

		deployOn();
	}

	public void deployOn() {
		Artifact art;
		for (Node target : umlTargetNodes) {
			art = UMLFactory.eINSTANCE.createArtifact();
			art.setName(umlCompToMove.getName() + "_Artifact");
			art.createManifestation(umlCompToMove.getName() + "_Manifestation", umlCompToMove);
			Deployment deploy = target.createDeployment(umlCompToMove.getName() + "_Deployment");
			deploy.getDeployedArtifacts().add(art);
			target.getDeployments().add(deploy);
		}
	}

	@Override
	public void log() {
		Controller.logger_.info("UMLMoveComponentRefactoringAction");
//		Controller.logger_.info(umlCompToMove.toString());
//
//		for (Node target : umlTargetNodes) {
//			for (Deployment dep : target.getDeployments()) {
//				Controller.logger_.info(dep.toString());
//			}
//		}
//
//		for (Artifact art : umlArtifacts) {
//			Controller.logger_.info(art.toString());
//			Controller.logger_.info(Integer.toString(art.getManifestations().size()));
//		}
	}

//	public Component getUmlCompToMove() {
//		return umlCompToMove;
//	}
//
//	public void setUmlCompToMove(Component component) {
//		this.umlCompToMove = component;
//	}
//
//	public List<Node> getUmlTargetNodes() {
//		return umlTargetNodes;
//	}
//
//	public void setUmlTargetNodes(List<Node> targets) {
//		this.umlTargetNodes = targets;
//	}
	
	@Override
	public void createPostCondition() {
		PostCondition postCondition = Manager.createPostCondition();

		FOLSpecification specification = Manager.createFOLSpectification("MoveComponentPostCondition");

		ExistsOperator existsComponentInComponents = Manager.createExistsOperator(getCompToMoveSVP(), getAllCompsMVP());

		ForAllOperator forAllNode = Manager.createForAllOperator(getTargetNodesMVP());
		ExistsOperator existsTargetInNodes = Manager.createExistsOperator(getAllNodesMVP());
		ExistsOperator componentExists = Manager.createExistsOperator(getCompToMoveSVP(), getAllDeployedElemsMVP());
		AndOperator andOperator = Manager.createAndOperator();
		andOperator.getArguments().add(existsTargetInNodes);
		andOperator.getArguments().add(componentExists);
		forAllNode.setArgument(andOperator);
		
		AndOperator andRoot = Manager.createAndOperator();
		andRoot.getArguments().add(existsComponentInComponents);
		andRoot.getArguments().add(forAllNode);
		
		specification.setRootOperator(andRoot);
		postCondition.setConditionFormula(specification);
		setPost(postCondition);
	}

	@Override
	public void createPreCondition() {
		PreCondition preCondition = Manager.createPreCondition();

		FOLSpecification specification = Manager.createFOLSpectification("MoveComponentPreCondition");

		ExistsOperator existsComponentInComponents = Manager.createExistsOperator(getCompToMoveSVP(), getAllCompsMVP());

		ForAllOperator forAllNode = Manager.createForAllOperator(getTargetNodesMVP());
		ExistsOperator existsTargetInNodes = Manager.createExistsOperator(getAllNodesMVP());
		NotOperator componentNotOperator = Manager.createNotOperator();
		
		//TODO getAllDeployedElems è l'insieme delle Component deployate sui target Nodes. Non va bene: la condition dice che 
				// su ogni target Node ci deve stare la Component spostata. Con l'unione basta che stia su uno dei target...
		ExistsOperator componentExists = Manager.createExistsOperator(getCompToMoveSVP(), getAllDeployedElemsMVP());
		
		componentNotOperator.setArgument(componentExists);
		AndOperator andOperator = Manager.createAndOperator();
		andOperator.getArguments().add(existsTargetInNodes);
		andOperator.getArguments().add(componentNotOperator);
		forAllNode.setArgument(andOperator);
		
		AndOperator andRoot = Manager.createAndOperator();
		andRoot.getArguments().add(existsComponentInComponents);
		andRoot.getArguments().add(forAllNode);
		
		specification.setRootOperator(andRoot);
		preCondition.setConditionFormula(specification);
		setPre(preCondition);
	}

	@Override
	public void setParameters() {
		// TODO Auto-generated method stub
		List<Parameter> moveCompParams = new ArrayList<>();
		
		setCompToMoveSVP(Manager.createSingleValueParameter(OclStringManager.getComponentQuery(getUmlCompToMove())));
		moveCompParams.add(getCompToMoveSVP());
		
		setTargetNodesMVP(Manager.createMultipleValuedParameter(OclStringManager.getNodesQuery(getUmlTargetNodes())));
		moveCompParams.add(getTargetNodesMVP());
		
		setAllArtifactsMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllArtifactsQuery()));
		moveCompParams.add(getAllArtifactsMVP());
		
		setAllCompsMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllComponentsQuery()));
		moveCompParams.add(getAllCompsMVP());
		
		setAllTargetsMVP(Manager.createMultipleValuedParameter(OclStringManager.getNodesQuery(getUmlTargetNodes())));
		moveCompParams.add(getAllTargetsMVP());
		
		setAllNodesMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllNodesQuery()));
		moveCompParams.add(getAllNodesMVP());
		
		setAllDeployedElemsMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllDeployedElementsQuery(getUmlTargetNodes())));
		moveCompParams.add(getAllDeployedElemsMVP());
		
		getParameters().addAll(moveCompParams);
	}

//	SingleValuedParameter getCompToMoveSVP() {
//		return compToMoveSVP;
//	}
//
//	void setCompToMoveSVP(SingleValuedParameter compToMoveSVP) {
//		this.compToMoveSVP = compToMoveSVP;
//	}
//
//	MultipleValuedParameter getTargetNodesMVP() {
//		return targetNodesMVP;
//	}
//
//	void setTargetNodesMVP(MultipleValuedParameter targetNodesMVP) {
//		this.targetNodesMVP = targetNodesMVP;
//	}
//
//	MultipleValuedParameter getArtifactsMVP() {
//		return artifactsMVP;
//	}
//
//	void setArtifactsMVP(MultipleValuedParameter artifactsMVP) {
//		this.artifactsMVP = artifactsMVP;
//	}
//
//	MultipleValuedParameter getAllCompsMVP() {
//		return allCompsMVP;
//	}
//
//	void setAllCompsMVP(MultipleValuedParameter allCompsMVP) {
//		this.allCompsMVP = allCompsMVP;
//	}

//	MultipleValuedParameter getAllTargetsMVP() {
//		return allTargetsMVP;
//	}
//
//	void setAllTargetsMVP(MultipleValuedParameter allTargetsMVP) {
//		this.allTargetsMVP = allTargetsMVP;
//	}
//
//	MultipleValuedParameter getAllNodesMVP() {
//		return allNodesMVP;
//	}
//
//	void setAllNodesMVP(MultipleValuedParameter allNodesMVP) {
//		this.allNodesMVP = allNodesMVP;
//	}
//
//	MultipleValuedParameter getAllDeployedElemsMVP() {
//		return allDeployedElemsMVP;
//	}
//
//	void setAllDeployedElemsMVP(MultipleValuedParameter allDeployedElemsMVP) {
//		this.allDeployedElemsMVP = allDeployedElemsMVP;
//	}

}

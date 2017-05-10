package metaheuristic.actions.uml;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.AggregationKind;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Deployment;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLFactory;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import logicalSpecification.AndOperator;
import logicalSpecification.ExistsOperator;
import logicalSpecification.FOLSpecification;
import logicalSpecification.ForAllOperator;
import logicalSpecification.MultipleValuedParameter;
import logicalSpecification.Parameter;
import logicalSpecification.PostCondition;
import logicalSpecification.PreCondition;
import logicalSpecification.SingleValuedParameter;
import logicalSpecification.actions.UML.impl.UMLAddNodeActionImpl;
import logicalSpecification.impl.ActionImpl;
import metaheuristic.evolutionary.Controller;
import metaheuristic.managers.Manager;
import metaheuristic.managers.OclStringManager;
import metaheuristic.managers.uml.UMLManager;

public class UMLAddNodeRefactoringAction extends UMLAddNodeActionImpl {

//	private Node umlNodeToAdd;
//	private List<Node> umlNeighbors;
//	private List<Component> umlCompsToDeploy;
//	private Package sourcePackage;
//	private PreCondition preCondition;
//	private PostCondition postCondition;
//
//	private SingleValuedParameter nodeToAddSVP;
//	private MultipleValuedParameter neighborsMVP;
//	private MultipleValuedParameter compsToDeployMVP;
//	private MultipleValuedParameter allNodesMVP;
//	private MultipleValuedParameter allCompsMVP;
//	private MultipleValuedParameter allDeployedElemsMVP;

	private static Double MAX_VALUE = 100.0;

	public UMLAddNodeRefactoringAction(List<Node> neighs, List<Component> deployedComps) {
		getUmlNeighbors().addAll(neighs);
		getUmlCompsToDeploy().addAll(deployedComps);
		setUmlSourcePackage(UMLManager.getNodePackage());
		setParameters();
		createPreCondition();
		createPostCondition();
		
		setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
	}
	
//	private UMLAddNodeRefactoringAction(UMLAddNodeRefactoringAction actionToCopy) {
//		setUmlNeighbors(actionToCopy.getUmlNeighbors());
//		setUmlCompsToDeploy(actionToCopy.getUmlCompsToDeploy());
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
	
//	public UMLAddNodeRefactoringAction cloneAction(){
//		return new UMLAddNodeRefactoringAction(this);
//	}

	@Override
	public void execute() {

		umlNodeToAdd = UMLFactory.eINSTANCE.createNode();
		umlNodeToAdd.setName("newNode" + Math.random());
		umlNodeToAdd.setPackage(umlSourcePackage);

		addCommunicationPaths();

		addDeployedComps();

	}

	public void addDeployedComps() {
		Artifact art;
		for (Component comp : getUmlCompsToDeploy()) {
			art = UMLFactory.eINSTANCE.createArtifact();
			art.setName(comp.getName() + "_Artifact");
			art.createManifestation(comp.getName() + "_Manifestation", comp);
			Deployment deploy = umlNodeToAdd.createDeployment(comp.getName() + "_Deployment");
			deploy.getDeployedArtifacts().add(art);
			umlNodeToAdd.getDeployments().add(deploy);
		}
	}

	public void addCommunicationPaths() {
		for (Node node : umlNeighbors) {
			/**
			 * createCommunicationPath(boolean end1IsNavigable, AggregationKind
			 * end1Aggregation, String end1Name, int end1Lower,int end1Upper,
			 * Node end1Node, boolean end2IsNavigable,AggregationKind
			 * end2Aggregation, String end2Name, int end2Lower,int end2Upper);
			 */
			umlNodeToAdd
					.createCommunicationPath(true, AggregationKind.COMPOSITE_LITERAL, node.getName(), 1, 1, node, true,
							AggregationKind.COMPOSITE_LITERAL, umlNodeToAdd.getName(), 1, 1)
					.setName(umlNodeToAdd.getName() + "_cp_" + node.getName());
		}
	}

//	public EList<Node> getUmlNeighbors() {
//		return umlNeighbors;
//	}
//
//	public void setUmlNeighbors(List<Node> umlNeighbors) {
//		this.umlNeighbors = umlNeighbors;
//	}

//	public Node getUmlNodeToAdd() {
//		return umlNodeToAdd;
//	}
//
//	public void setUmlNodeToAdd(Node umlNodeToAdd) {
//		this.umlNodeToAdd = umlNodeToAdd;
//	}
//
//	public List<Component> getUmlCompsToDeploy() {
//		return umlCompsToDeploy;
//	}
//
//	public void setUmlCompsToDeploy(List<Component> umlCompsToDeploy) {
//		this.umlCompsToDeploy = umlCompsToDeploy;
//	}

	@Override
	public void log() {
		Controller.logger_.info("UMLAddNodeRefactoringAction");
//		if (getUmlNodeToAdd() != null) {
//			Controller.logger_.info(getUmlNodeToAdd().getName());
//			Controller.logger_.info("--------Deployment----------");
//			for (Deployment depl : this.umlNodeToAdd.getDeployments())
//				Controller.logger_.info(depl.toString());
//			Controller.logger_.info("--------CommunicationPath----------");
//			for (CommunicationPath cp : umlNodeToAdd.getCommunicationPaths())
//				Controller.logger_.info(cp.toString());
//		}
	}

	@Override
	public void setParameters() {
		List<Parameter> addParams = new ArrayList<>();

		if (umlNodeToAdd != null) {
			// FIXME le add non dovrebbero avere come attributo l'oggetto da
			// creare
			setNodeToAddSVP(Manager.createSingleValueParameter(OclStringManager.getNodeQuery(umlNodeToAdd)));
			addParams.add(getNodeToAddSVP());
		}

		setNeighborsMVP(Manager.createMultipleValuedParameter(OclStringManager.getNodesQuery(this.umlNeighbors)));
		addParams.add(getNeighborsMVP());

		setCompsToDeployMVP(
				Manager.createMultipleValuedParameter(OclStringManager.getComponentsQuery(this.umlCompsToDeploy)));
		addParams.add(getCompsToDeployMVP());

		setAllNodesMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllNodesQuery()));
		addParams.add(getAllNodesMVP());

		setAllCompsMVP(Manager.createMultipleValuedParameter(OclStringManager.getAllComponentsQuery()));
		addParams.add(getAllCompsMVP());

		if (getUmlNodeToAdd() != null) {
			ArrayList<Node> nList = new ArrayList<Node>();
			// FIXME qui facciamo la query dei componenti deploiati sul nodo
			// appena creato ma e' vuota
			nList.add(getUmlNodeToAdd());
			setAllDeployedElemsMVP(
					Manager.createMultipleValuedParameter(OclStringManager.getAllDeployedElementsQuery(nList)));
			addParams.add(getAllDeployedElemsMVP());
		}

		getParameters().addAll(addParams);
	}

	@Override
	public void createPreCondition() {
		PreCondition preCondition = Manager.createPreCondition();

		FOLSpecification addPreSpec = Manager.createFOLSpectification("AddNodePrecondition");

		AndOperator addPreAnd = Manager.createAndOperator();

		// ExistsOperator addPreAndNotExists =
		// Manager.createExistsOperator(getNodeToAddSVP(), getAllNodesMVP());

		// NotOperator addPreAndNot =
		// Manager.createNotOperator(addPreAndNotExists);
		// addPreAnd.getArguments().add(addPreAndNot);

		ForAllOperator addPreAndForall = Manager.createForAllOperator(getNeighborsMVP());

		ExistsOperator addPreAndForallExists = Manager.createExistsOperator(getAllNodesMVP());
		addPreAndForall.setArgument(addPreAndForallExists);
		addPreAnd.getArguments().add(addPreAndForall);
		addPreSpec.setRootOperator(addPreAnd);

		preCondition.setConditionFormula(addPreSpec);
		setPre(preCondition);

	}

	@Override
	public void createPostCondition() {
		PostCondition postCondition = Manager.createPostCondition();

		FOLSpecification addPostSpec = Manager.createFOLSpectification("AddNodePostcondition");

		AndOperator addPostAnd = Manager.createAndOperator();

		ExistsOperator addPostAndExists = Manager.createExistsOperator(getNodeToAddSVP(), getAllNodesMVP());
		addPostAnd.getArguments().add(addPostAndExists);

		ForAllOperator addPostAndForallNeighs = Manager.createForAllOperator(getNeighborsMVP());

		AndOperator addPostAndForallNeighsAnd = Manager.createAndOperator();

		ExistsOperator addPostAndForallNeighsAndExistsAllNodes = Manager.createExistsOperator(getAllNodesMVP());
		addPostAndForallNeighsAnd.getArguments().add(addPostAndForallNeighsAndExistsAllNodes);

		ExistsOperator addPostAndForallNeighsAndExistsNeighs = Manager.createExistsOperator(getNeighborsMVP());
		addPostAndForallNeighsAnd.getArguments().add(addPostAndForallNeighsAndExistsNeighs);

		addPostAndForallNeighs.setArgument(addPostAndForallNeighsAnd);

		addPostAnd.getArguments().add(addPostAndForallNeighs);

		ForAllOperator addPostAndForallDeplComps = Manager.createForAllOperator(getCompsToDeployMVP());

		AndOperator addPostAndForallDeplCompsAnd = Manager.createAndOperator();

		ExistsOperator addPostAndForallDeplCompsAndExistsAllNodes = Manager.createExistsOperator(getAllCompsMVP());
		addPostAndForallDeplCompsAnd.getArguments().add(addPostAndForallDeplCompsAndExistsAllNodes);

		ExistsOperator addPostAndForallDeplCompsAndExistsNeighs = Manager
				.createExistsOperator(getAllDeployedElemsMVP());
		addPostAndForallDeplCompsAnd.getArguments().add(addPostAndForallDeplCompsAndExistsNeighs);

		addPostAndForallDeplComps.setArgument(addPostAndForallDeplCompsAnd);

		addPostAnd.getArguments().add(addPostAndForallDeplComps);

		addPostSpec.setRootOperator(addPostAnd);
		postCondition.setConditionFormula(addPostSpec);
		setPost(postCondition);
	}

	// @Override
	// public void setPreCondition() {
	// preCondition = Manager.createPreCondition();
	//
	// FOLSpecification addPreSpec =
	// Manager.createFOLSpectification("AddNodePrecondition");
	//
	// AndOperator addPreAnd = Manager.createAndOperator();
	//
	// ExistsOperator addPreAndNotExists = Manager.createExistsOperator(
	// Manager.createSingleValueParameter(OclStringManager.getNodeQuery(getNewNode())),
	// Manager.createMultipleValuedParameter(OclStringManager.getAllNodesQuery()));
	//
	// NotOperator addPreAndNot = Manager.createNotOperator(addPreAndNotExists);
	// addPreAnd.getArguments().add(addPreAndNot);
	//
	// ForAllOperator addPreAndForall = Manager.createForAllOperator(
	// Manager.createMultipleValuedParameter(OclStringManager.getNodesQuery(getNeighs())));
	//
	// ExistsOperator addPreAndForallExists =
	// Manager.createExistsOperator(Manager.createMultipleValuedParameter(OclStringManager.getAllNodesQuery()));
	// addPreAndForall.setArgument(addPreAndForallExists);
	// addPreAnd.getArguments().add(addPreAndForall);
	// addPreSpec.setRootOperator(addPreAnd);
	//
	// preCondition.setConditionFormula(addPreSpec);
	// setPre(preCondition);
	//
	// }
	//
	// @Override
	// public void setPostCondition() {
	// postCondition = Manager.createPostCondition();
	//
	// FOLSpecification addPostSpec =
	// Manager.createFOLSpectification("AddNodePostcondition");
	//
	// AndOperator addPostAnd = Manager.createAndOperator();
	//
	// ExistsOperator addPostAndExists = Manager.createExistsOperator(
	// Manager.createSingleValueParameter(OclStringManager.getNodeQuery(getNewNode())),
	// Manager.createMultipleValuedParameter(OclStringManager.getAllNodesQuery()));
	// addPostAnd.getArguments().add(addPostAndExists);
	//
	// ForAllOperator addPostAndForallNeighs = Manager.createForAllOperator(
	// Manager.createMultipleValuedParameter(OclStringManager.getNodesQuery(getNeighs())));
	//
	// AndOperator addPostAndForallNeighsAnd = Manager.createAndOperator();
	//
	// ExistsOperator addPostAndForallNeighsAndExistsAllNodes = Manager
	// .createExistsOperator(Manager.createMultipleValuedParameter(OclStringManager.getAllNodesQuery()));
	// addPostAndForallNeighsAnd.getArguments().add(addPostAndForallNeighsAndExistsAllNodes);
	//
	// ExistsOperator addPostAndForallNeighsAndExistsNeighs =
	// Manager.createExistsOperator(
	// Manager.createMultipleValuedParameter(OclStringManager.getNodesQuery(getNeighs())));
	// addPostAndForallNeighsAnd.getArguments().add(addPostAndForallNeighsAndExistsNeighs);
	//
	// addPostAndForallNeighs.setArgument(addPostAndForallNeighsAnd);
	//
	// addPostAnd.getArguments().add(addPostAndForallNeighs);
	//
	// ForAllOperator addPostAndForallDeplComps = Manager.createForAllOperator(
	// Manager.createMultipleValuedParameter(OclStringManager.getComponentsQuery(getDeployedComps())));
	//
	// AndOperator addPostAndForallDeplCompsAnd = Manager.createAndOperator();
	//
	// ExistsOperator addPostAndForallDeplCompsAndExistsAllNodes = Manager
	// .createExistsOperator(Manager.createMultipleValuedParameter(OclStringManager.getAllComponentsQuery()));
	// addPostAndForallDeplCompsAnd.getArguments().add(addPostAndForallDeplCompsAndExistsAllNodes);
	//
	// // TODO ANSWERED AND FIXED: non capisco a cosa si riferisce n_deplComps
	// ->
	// // si riferisce alle componenti deployate sul nuovo nodo (dopo averlo
	// aggiunto,
	// // le deployedComps devono starci sopra)
	//// ExistsOperator addPostAndForallDeplCompsAndExistsNeighs =
	// Manager.createExistsOperator(
	//// Manager.createMultipleValuedParameter(OclStringManager.getComponentsQuery(deployedComps)));
	//
	// ArrayList<Node> nList = new ArrayList<Node>();
	// nList.add(getNewNode());
	// ExistsOperator addPostAndForallDeplCompsAndExistsNeighs =
	// Manager.createExistsOperator(
	// Manager.createMultipleValuedParameter(OclStringManager.getAllDeployedElementsQuery(nList)));
	// addPostAndForallDeplCompsAnd.getArguments().add(addPostAndForallDeplCompsAndExistsNeighs);
	//
	// addPostAndForallDeplComps.setArgument(addPostAndForallDeplCompsAnd);
	//
	// addPostAnd.getArguments().add(addPostAndForallDeplComps);
	//
	// addPostSpec.setRootOperator(addPostAnd);
	// postCondition.setConditionFormula(addPostSpec);
	// setPost(postCondition);
	// }

//	public PostCondition getPostCondition() {
//		return postCondition;
//	}
//
//	public PreCondition getPreCondition() {
//		return preCondition;
//	}
//
//	SingleValuedParameter getNodeToAddSVP() {
//		return nodeToAddSVP;
//	}
//
//	void setNodeToAddSVP(SingleValuedParameter nodeToAddSVP) {
//		this.nodeToAddSVP = nodeToAddSVP;
//	}
//
//	MultipleValuedParameter getNeighborsMVP() {
//		return neighborsMVP;
//	}
//
//	void setNeighborsMVP(MultipleValuedParameter neighborsMVP) {
//		this.neighborsMVP = neighborsMVP;
//	}
//
//	MultipleValuedParameter getCompsToDeployMVP() {
//		return compsToDeployMVP;
//	}
//
//	void setCompsToDeployMVP(MultipleValuedParameter compsToDeployMVP) {
//		this.compsToDeployMVP = compsToDeployMVP;
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
//	MultipleValuedParameter getAllCompsMVP() {
//		return allCompsMVP;
//	}
//
//	void setAllCompsMVP(MultipleValuedParameter allCompsMVP) {
//		this.allCompsMVP = allCompsMVP;
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

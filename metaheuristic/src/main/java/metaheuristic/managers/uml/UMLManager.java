package metaheuristic.managers.uml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ocl.ecore.OCL;
import org.eclipse.papyrus.MARTE.MARTEFactory;
import org.eclipse.papyrus.MARTE.MARTEPackage;
import org.eclipse.papyrus.MARTE.MARTE_AnalysisModel.GQAM.GQAMFactory;
import org.eclipse.papyrus.MARTE.MARTE_AnalysisModel.GQAM.GQAMPackage;
import org.eclipse.uml2.uml.Artifact;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Manifestation;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.profile.standard.StandardPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import logicalSpecification.Action;
import logicalSpecification.actions.UML.UMLAddComponentAction;
import logicalSpecification.actions.UML.UMLAddNodeAction;
import logicalSpecification.actions.UML.UMLAddOperationAction;
import logicalSpecification.actions.UML.UMLDeleteComponentAction;
import logicalSpecification.actions.UML.UMLDeleteNodeAction;
import logicalSpecification.actions.UML.UMLDeleteOperationAction;
import logicalSpecification.actions.UML.UMLFactory;
import logicalSpecification.actions.UML.UMLMoveComponentAction;
import logicalSpecification.actions.UML.UMLMoveOperationAction;
import metaheuristic.managers.Manager;
import metaheuristic.managers.OclManager;
import metaheuristic.managers.OclStringManager;

public class UMLManager {

	private static final double MAX_VALUE = 100;
	private Model model;
	private ResourceSet set;
	private Resource resource;
	private UMLResource umlResource;

	private static class ManagerHolder {
		private static final UMLManager INSTANCE = new UMLManager();
	}

	public static UMLManager getInstance() {
		return ManagerHolder.INSTANCE;
	}

	public static Action getUMLRandomAction(int length) throws UnexpectedException {

		int index = JMetalRandom.getInstance().getRandomGenerator().nextInt(0, length - 1);

		switch (index) {

		case 0:
			return getRandomAddNodeAction();
		case 1:
			return getRandomAddComponentAction();
		case 2:
			return getRandomAddOperationAction();
		case 3:
			return getRandomMoveOpertionAction();
		case 4:
			return getRandomMoveComponentAction();
		case 5:
			return getRandomDeleteOperationAction();
		case 6:
			return getRandomDeleteComponentAction();
		case 7:
			return getRandomDeleteNodeAction();
		default:
			throw new UnexpectedException("");
		}
	}

	private static Action getRandomDeleteNodeAction() {
		Node node = UMLManager.getRandomNode();
		// Action action = new UMLDeleteNodeRefactoringAction(node);

		UMLDeleteNodeAction action = UMLFactory.eINSTANCE.createUMLDeleteNodeAction();
		action.setUmlNodeToDel(node);
		action.setParameters();
		action.createPreCondition();
		action.createPostCondition();
		action.setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		action.setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));

		return action;
	}

	private static Action getRandomAddNodeAction() {
		List<Node> neighs = UMLManager.getRandomNodes();
		List<Component> deployedComps = UMLManager.getRandomComponents();
		UMLAddNodeAction action = (UMLAddNodeAction) UMLFactory.eINSTANCE.createUMLAddNodeAction();
		action.getUmlNeighbors().addAll(neighs);
		action.getUmlCompsToDeploy().addAll(deployedComps);
		action.setUmlSourcePackage(UMLManager.getNodePackage());
		action.setParameters();
		action.createPreCondition();
		action.createPostCondition();

		action.setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		action.setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));

		// Action action = new UMLAddNodeRefactoringAction(targets,
		// list_of_components);
		return action;
	}

	private static Action getRandomAddOperationAction() {
		Component targetUMLComp = UMLManager.getRandomComponent();
		// Action action = new UMLAddOperationRefactoringAction(component);
		UMLAddOperationAction action = UMLFactory.eINSTANCE.createUMLAddOperationAction();
		action.setUmlTargetComp(targetUMLComp);
		action.setParameters();
		action.createPreCondition();
		action.createPostCondition();
		action.setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		action.setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));

		return action;
	}

	private static Action getRandomDeleteComponentAction() {
		Component component = UMLManager.getRandomComponent();
		// Action action = new UMLDeleteComponentRefactoringAction(component);

		UMLDeleteComponentAction action = UMLFactory.eINSTANCE.createUMLDeleteComponentAction();
		action.setUmlCompToDel(component);
		action.setParameters();
		action.createPreCondition();
		action.createPostCondition();
		action.setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		action.setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));

		return action;
	}

	private static Action getRandomDeleteOperationAction() {
		Operation operation = UMLManager.getRandomOperation();
		// Action action = new UMLDeleteOperationRefactoringAction(operation);

		UMLDeleteOperationAction action = UMLFactory.eINSTANCE.createUMLDeleteOperationAction();

		action.setUmlOpToDel(operation);
		action.setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		action.setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		action.setParameters();
		action.createPreCondition();
		action.createPostCondition();

		return action;
	}

	private static Action getRandomMoveComponentAction() {
		Component component = UMLManager.getRandomComponent();
		List<Node> targets = UMLManager.getRandomNodes();
		// Action action = new UMLMoveComponentRefactoringAction(component,
		// list_of_nodes);

		UMLMoveComponentAction action = UMLFactory.eINSTANCE.createUMLMoveComponentAction();
		action.setUmlCompToMove(component);
		action.getUmlTargetNodes().addAll(targets);
		action.setParameters();
		action.createPreCondition();
		action.createPostCondition();
		action.setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		action.setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		return action;
	}

	private static Action getRandomMoveOpertionAction() {
		Operation context = UMLManager.getRandomOperation();
		Component target = UMLManager.getRandomComponent();
		// Action action = new UMLMoveOperationRefactoringAction(operation,
		// target);
		UMLMoveOperationAction action = UMLFactory.eINSTANCE.createUMLMoveOperationAction();
		action.setUmlOpToMove(context);
		action.setUmlTargetComp(target);
		action.setParameters();
		action.createPreCondition();
		action.createPostCondition();
		action.setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		action.setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));

		return action;
	}

	private static Action getRandomAddComponentAction() {
		EList<Node> targets = UMLManager.getRandomNodes();
		UMLAddComponentAction action = UMLFactory.eINSTANCE.createUMLAddComponentAction();
		// Action action = new UMLAddComponentRefactoringAction(list_of_nodes);
		action.getUmlTargetNodes().addAll(targets);
		action.setUmlSourcePackage(UMLManager.getComponentPackage());
		action.setCost(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		action.setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextDouble(1, MAX_VALUE));
		action.setParameters();
		action.createPreCondition();
		action.createPostCondition();
		return action;
	}

	public Model getModel() {
		return model;
	}

	public Resource createModelResource() {
		ResourceSet set = new ResourceSetImpl();
		set.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		Resource resource = set.getResource(Manager.string2FileUri(Manager.getModelUri()), true);
		return resource;
	}

	public void unloadModelResource() {
		if (getUmlResource() != null) { // unload previous resources if existing
			// unload every resource in the resourceSet including profiles
			for (Iterator<Resource> i = getResourceSet().getResources().iterator(); i.hasNext();) {
				Resource current = (Resource) i.next();
				current.unload();
				i.remove();
			}
		}

	}

	public void save(Package package_, String stringUri) {
		unloadModelResource();
		
		UMLResource res = (UMLResource) UMLManager.getInstance().getResourceSet().createResource(Manager.string2FileUri(stringUri));
		
		res.getContents().add(package_);

		try {
			res.save(null);
			System.out.println("Done.");
		} catch (IOException ioe) {
			System.err.println(ioe.getMessage());
		}
	}

	public boolean saveModel() {
		try {
			UMLManager.getInstance().getResource().save(null);
			return true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public Resource createModelResource(String umlModelUri) {
		ResourceSet set = new ResourceSetImpl();
		set.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		Resource resource = set.getResource(Manager.string2FileUri(umlModelUri), true);
		return resource;
	}

	public Resource createModelResource(String umlModelUri, boolean loadOnDemand) {
		ResourceSet set = new ResourceSetImpl();
		set.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		Resource resource = set.getResource(Manager.string2FileUri(umlModelUri), loadOnDemand);
		return resource;
	}

	public void init(String modelUri) {
		Manager.getInstance().setModelUri(modelUri);
		setResourceSet(new ResourceSetImpl());
		getResourceSet().getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
		getResourceSet().getPackageRegistry().put(StandardPackage.eNS_URI, StandardPackage.eINSTANCE);
		getResourceSet().getPackageRegistry().put(org.eclipse.papyrus.MARTE.MARTEPackage.eNS_URI,
				MARTEPackage.eINSTANCE);
		getResourceSet().getPackageRegistry()
				.put(org.eclipse.papyrus.MARTE.MARTE_AnalysisModel.GQAM.GQAMPackage.eNS_URI, GQAMPackage.eINSTANCE);
		getResourceSet().getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				MARTEFactory.eINSTANCE);
		getResourceSet().getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				GQAMFactory.eINSTANCE);
		getResourceSet().getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
				UMLResource.Factory.INSTANCE);
		OCL.initialize(getResourceSet());
//		setResource(getResourceSet().getResource(Manager.string2FileUri(Manager.getModelUri()), true));
		setUmlResource((UMLResource) getResourceSet().getResource(Manager.string2FileUri(Manager.getModelUri()), true));
//		model = (Model) EcoreUtil.getObjectByType(getResource().getContents(), UMLPackage.Literals.MODEL);
		model = (Model) EcoreUtil.getObjectByType(getUmlResource().getContents(), UMLPackage.Literals.MODEL);
	}

	@SuppressWarnings("null")
	public static EList<Artifact> getAllArtifacts() {
		EList<Artifact> list_of_artifacts = new BasicEList<Artifact>();
		String query = OclStringManager.getAllArtifactsQuery();
		HashSet<Object> hashSetQuery = OclManager.evaluateQuery(query);
		for (Object object : hashSetQuery) {
			if (object instanceof Artifact)
				list_of_artifacts.add((Artifact) object);
		}
		return list_of_artifacts;
	}

	@SuppressWarnings("null")
	public static EList<Manifestation> getAllManifestations() {
		EList<Manifestation> list_of_manifestations = new BasicEList<Manifestation>();
		String query = OclStringManager.getAllManifestationsQuery();
		HashSet<Object> hashSetQuery = OclManager.evaluateQuery(query);
		for (Object object : hashSetQuery) {
			if (object instanceof Manifestation)
				list_of_manifestations.add((Manifestation) object);
		}
		return list_of_manifestations;
	}

	public static List<Manifestation> getAllManifestationsOf(Component component) {
		List<Manifestation> list_of_manifestations = new ArrayList<Manifestation>();
		String query = OclStringManager.getAllManifestationsOfQuery(component);
		HashSet<Object> hashSetQuery = OclManager.evaluateQuery(query);
		for (Object object : hashSetQuery) {
			if (object instanceof Manifestation)
				list_of_manifestations.add((Manifestation) object);
		}
		return list_of_manifestations;
	}

	public static List<Node> getAllNodes() {
		List<Node> list_of_nodes = new ArrayList<Node>();
		String query = OclStringManager.getAllNodesQuery();
		HashSet<Object> hashSetQuery = OclManager.evaluateQuery(query);
		for (Object object : hashSetQuery) {
			if (object instanceof Node)
				list_of_nodes.add((Node) object);
		}
		return list_of_nodes;
	}

	public static List<Component> getRandomComponents() {
		int upperBound = (int) Double
				.parseDouble(((HashSet<Object>) OclManager.evaluateQuery(OclStringManager.countComponentsQuery()))
						.iterator().next().toString());
		int[] bounds = generateRandomInterval(upperBound);
		List<Component> list_of_components = new ArrayList<Component>();
		HashSet<Object> hashSet = OclManager
				.evaluateQuery(OclStringManager.generateRandomComponentsQuery(bounds[0], bounds[1]));
		for (Object object : hashSet) {
			if (object instanceof Component)
				list_of_components.add((Component) object);
		}
		return list_of_components;
	}

	public static List<Component> getAllComponents() {
		List<Component> list_of_components = new ArrayList<Component>();
		HashSet<Object> hashSet = OclManager.evaluateQuery(OclStringManager.getAllComponentsQuery());
		for (Object object : hashSet) {
			if (object instanceof Component)
				list_of_components.add((Component) object);
		}
		return list_of_components;
	}

	@SuppressWarnings("null")
	public static EList<Node> getRandomNodes() {
		int upperBound = (int) Double
				.parseDouble(((HashSet<Object>) OclManager.evaluateQuery(OclStringManager.countNodesQuery())).iterator()
						.next().toString());
		int[] bounds = generateRandomInterval(upperBound);
		// List<Node> list_of_nodes = new ArrayList<Node>();
		EList<Node> list_of_nodes = new BasicEList<Node>();
		HashSet<Object> hashSet = OclManager
				.evaluateQuery(OclStringManager.generateRandomNodesQuery(bounds[0], bounds[1]));
		for (Object object : hashSet) {
			if (object instanceof Node)
				list_of_nodes.add((Node) object);
		}
		return list_of_nodes;
	}

	// public static List<Node> getNodes() {
	// int upperBound = (int) Double.parseDouble(((HashSet<Object>)
	// OclManager.evaluateQuery(OclStringManager.countNodesQuery())).iterator().next().toString());
	// int[] bounds = generateRandomInterval(upperBound);
	// List<Node> list_of_nodes = new ArrayList<Node>();
	// HashSet<Object> hashSet = OclManager
	// .evaluateQuery(OclStringManager.generateRandomNodesQuery(bounds[0],
	// bounds[1]));
	// for (Object object : hashSet) {
	// if (object instanceof Node)
	// list_of_nodes.add((Node) object);
	// }
	// return list_of_nodes;
	// }

	public static Package getComponentPackage() {
		Package pack = UMLManager.getAllComponents().get(0).getNearestPackage();
		return pack;
	}

	private static int[] generateRandomInterval(int upperBound) {
		assert (upperBound > 0);
		int bounds[] = new int[2];
		bounds[0] = JMetalRandom.getInstance().getRandomGenerator().nextInt(1, upperBound);
		bounds[1] = JMetalRandom.getInstance().getRandomGenerator().nextInt(bounds[0], upperBound);
		assert (bounds[1] - bounds[0] < 0);
		return bounds;
	}

	public static Component getRandomComponent() {
		int upperBound = (int) Double
				.parseDouble(((HashSet<Object>) OclManager.evaluateQuery(OclStringManager.countComponentsQuery()))
						.iterator().next().toString());
		int[] bounds = generateRandomInterval(upperBound);
		List<Component> list_of_components = new ArrayList<Component>();
		HashSet<Object> hashSet = OclManager
				.evaluateQuery(OclStringManager.generateRandomComponentsQuery(bounds[0], bounds[1]));
		for (Object object : hashSet) {
			if (object instanceof Component)
				list_of_components.add((Component) object);
		}
		assert (list_of_components.size() > 0);
		int[] randomInterval = generateRandomInterval(list_of_components.size());
		return list_of_components.get(randomInterval[0] - 1);
	}

	public static Operation getRandomOperation() {
		int upperBound = (int) Double
				.parseDouble(((HashSet<Object>) OclManager.evaluateQuery(OclStringManager.countOperationsQuery()))
						.iterator().next().toString());
		int[] bounds = generateRandomInterval(upperBound);
		List<Operation> list_of_operations = new ArrayList<Operation>();
		HashSet<Object> hashSet = OclManager
				.evaluateQuery(OclStringManager.generateRandomOperationsQuery(bounds[0], bounds[1]));
		for (Object object : hashSet) {
			if (object instanceof Operation)
				list_of_operations.add((Operation) object);
		}
		return list_of_operations.get(generateRandomInterval(list_of_operations.size())[0] - 1);
	}

	public static Node getRandomNode() {
		int upperBound = (int) Double
				.parseDouble(((HashSet<Object>) OclManager.evaluateQuery(OclStringManager.countNodesQuery())).iterator()
						.next().toString());
		int[] bounds = generateRandomInterval(upperBound);
		List<Node> list_of_nodes = new ArrayList<Node>();
		HashSet<Object> hashSet = OclManager
				.evaluateQuery(OclStringManager.generateRandomNodesQuery(bounds[0], bounds[1]));
		for (Object object : hashSet) {
			if (object instanceof Node)
				list_of_nodes.add((Node) object);
		}
		return list_of_nodes.get(generateRandomInterval(list_of_nodes.size())[0] - 1);
	}

	public static List<Component> getDeploymentsOf(List<Node> targets) {
		List<Component> list_of_components = new ArrayList<Component>();
		HashSet<Object> hashSet = OclManager.evaluateQuery(OclStringManager.getAllDeployedElementsQuery(targets));
		for (Object object : hashSet) {
			if (object instanceof Component)
				list_of_components.add((Component) object);
		}
		return null;
	}

	public static Package getNodePackage() {
		Package pack = UMLManager.getAllNodes().get(0).getNearestPackage();
		return pack;
	}

	public ResourceSet getResourceSet() {
		return set;
	}

	public void setResourceSet(ResourceSet set) {
		this.set = set;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public UMLResource getUmlResource() {
		return umlResource;
	}

	public void setUmlResource(UMLResource umlResource) {
		this.umlResource = umlResource;
	}

}

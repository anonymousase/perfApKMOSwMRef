package metaheuristic.managers;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Operation;

public class OclStringManager {

	public static String getAllArtifactsQuery() {
		return "Artifact.allInstances()";
	}

	public static String getAllManifestationsQuery() {
		// TODO Auto-generated method stub
		return "Manifestation.allInstances()";
	}

	public static String getAllManifestationsOfQuery(Component component) {
		// TODO Auto-generated method stub
		return "Manifestation.allInstances()->select(man | man.supplier->exists(s|s.qualifiedName='"
				+ component.getQualifiedName() + "'))";
	}

	public static String getAllNodesQuery() {
		return "Node.allInstances()";
	}

	public static String getAllComponentsQuery() {
		// TODO Auto-generated method stub
		return "Component.allInstances()";
	}

	public static String generateRandomComponentsQuery(int start, int end) {
		String query = "";
		query = "Component.allInstances()->select(comp | comp.getAppliedStereotypes()->exists(s | s.name = 'PaRunTInstance'))->asSequence()->subSequence ("
				+ start + ", " + end + ")";
		return query;
	}

	public static String countComponentsQuery() {
		return "Component.allInstances()->select(comp | comp.getAppliedStereotypes()->exists(s | s.name = 'PaRunTInstance'))->size()";
	}

	public static String getComponentsQuery(EList<Component> list_of_components) {
		// TODO Auto-generated method stub
		String query;
		query = "Component.allInstances()->select(comp | comp.getAppliedStereotypes()->exists(s | s.name = 'PaRunTInstance'))->"
				+ "select(c | ";

		Iterator<Component> iterator = list_of_components.iterator();
		while (iterator.hasNext()) {
			query += "c.name = '" + iterator.next().getName() + "'";
			if (iterator.hasNext())
				query += " or ";
		}
		query += " )";

		return query;
	}

	// TODO cambiare lo stereotipo
	public static String getRandomNodes(int start, int end) {
		String query = "";
		query = "Node.allInstances()->select(node | node.getAppliedStereotypes()->exists(s | s.name = 'PaRunTInstance'))->asSequence()->subSequence ("
				+ start + ", " + end + ")";
		return query;

	}

	public static String countNodesQuery() {
		return "Node.allInstances()->select(node : Node | node.getAppliedStereotypes()->exists(s | s.name = 'GaExecHost'))->size()";
	}

	public static String getNodesQuery(List<Node> list_of_nodes) {
		assert (list_of_nodes == null);
		String query;
		query = "Node.allInstances()->select(node | node.getAppliedStereotypes()->exists(s | s.name = 'GaExecHost'))->"
				+ "select(n | ";
		Iterator<Node> iterator = list_of_nodes.iterator();
		while (iterator.hasNext()) {
			assert (iterator.next() == null);
			query += "n.name = '" + iterator.next().getName() + "'";
			if (iterator.hasNext())
				query += " or ";
		}
		query += " )";
		return query;
	}

	// TODO capire come far creare un component tramite ocl
	// sono riuscito a creare una classe ma non un component
	public String createRandomComponentQuery() {
		String query = "";
		return query;
	}

	public static String getComponentQuery(Component newComp) {
		String query;
		query = "Component.allInstances()->select(comp | comp.getAppliedStereotypes()->exists(s | s.name = 'PaRunTInstance'))->"
				+ "select(c | c.name = '" + newComp.getName() + "' )->asSequence()->first()";
		return query;
	}

	public static String getDeploymentsOf(Node node) {
		String query;
		query = "Node.allInstances()->select(n | n.name = '" + node.getName() + "').getDeployedElements()";
		return query;
	}

	public static String getAllDeployedElementsQuery(List<Node> targets) {
		String query = "";
		query = "Deployment.allInstances()->select(d | ";
		query += "(" + getNodesQuery(targets) + ").deployment->includes(d))";
		return query;
	}

	public static String getNodeQuery(Node newNode) {
		String query;
		query = "Node.allInstances()->select(node | node.getAppliedStereotypes()->exists(s | s.name = 'GaExecHost'))->"
				+ "select(c | c.name = '" + newNode.getName() + "' )->asSequence()->first()";
		return query;
	}

	public static String getNeighboursOf(Node node) {
		String query = "";
		return query;
	}

	public static String getAllOperationsQuery() {
		String query;
		query = "Operation.allInstances()->select(operation | operation.getAppliedStereotypes()->exists(s | s.name = 'GaStep'))";
		return query;
	}

	public static String getOperationQuery(Operation operation) {
		String query;
		query = "Operation.allInstances()->select(operation | operation.getAppliedStereotypes()->exists(s | s.name = 'GaStep'))->"
				+ "select(op | op.name = '" + operation.getName() + "' )->asSequence()->first()";
		return query;
	}

	public static String getOperationsOfQuery(Component comp) {
		String query;
		query = "Component.allInstances()->select(n | n.name = '" + comp.getName()
				+ "').getOperations()->select(o | o.getAppliedStereotypes()->exists(s | s.name = 'GaStep'))";
		return query;
	}

	public static String countOperationsQuery() {
		return "Operation.allInstances()->select(op | op.getAppliedStereotypes()->exists(s | s.name = 'GaStep'))->size()";
	}

	public static String operationsGetStereotypesQuery() {
		return "Operation.allInstances()->select(o | o.name = '').getAppliedStereotypes()";
	}

	public static String generateRandomOperationsQuery(int start, int end) {
		String query = "";
		query = "Operation.allInstances()->select(op | op.getAppliedStereotypes()->exists(s | s.name = 'GaStep'))->asSequence()->subSequence ("
				+ start + ", " + end + ")";
		return query;
	}

	public static String generateRandomNodesQuery(int start, int end) {
		String query = "";
		query = "Node.allInstances()->select(nd | nd.getAppliedStereotypes()->exists(s | s.name = 'GaExecHost'))->asSequence()->subSequence ("
				+ start + ", " + end + ")";
		return query;
	}

}

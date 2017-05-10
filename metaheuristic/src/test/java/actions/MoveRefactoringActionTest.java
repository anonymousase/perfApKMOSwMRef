package actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.eclipse.ocl.ParserException;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Operation;
import org.junit.Before;
import org.junit.Test;

import logicalSpecification.Action;
import metaheuristic.actions.uml.UMLMoveComponentRefactoringAction;
import metaheuristic.actions.uml.UMLMoveOperationRefactoringAction;
import metaheuristic.managers.Manager;
import metaheuristic.managers.OclManager;

public class MoveRefactoringActionTest {

	private Model testModel;

	@Test
	public void moveOperationTest() {

		String query = "Component.allInstances()->select(c | c.getOperations()->size() > 1)";
		HashSet<Object> hashSetQuery;
		Component resQuery = null;

		hashSetQuery = (HashSet<Object>) getHashSetObject(query);

		Iterator iterator = hashSetQuery.iterator();
		resQuery = (Component) iterator.next();

		Operation resQuery2 = resQuery.getOperations().get(0);

		query = "Component.allInstances()->asSequence()->at(1)";

		Component hashSetQuery2 = (Component) getHashSetObject(query);

		Action action = new UMLMoveOperationRefactoringAction(resQuery2, hashSetQuery2);
		action.execute();
		action.log();

	}

	@Test
	@SuppressWarnings({"unchecked"})
	public void MoveComponentRefactoringActionTest() {
		String query = "Component.allInstances()->select(c | c.name='Greenhouse management')->asSequence()->at(1)";
		HashSet<Object> hashSetQuery;
		Component resQuery = null;

		resQuery = (Component) getHashSetObject(query);

		String query2 = "Node.allInstances()";
		List<Node> resQuery2 = new ArrayList<Node>();

		hashSetQuery = (HashSet<Object>) getHashSetObject(query2);

		for (Object object : hashSetQuery) {
			if (object instanceof Node)
				resQuery2.add((Node) object);
		}
		
		UMLMoveComponentRefactoringAction action = new UMLMoveComponentRefactoringAction(resQuery, resQuery2);
		System.out.println("---------Pre execute----------");
		action.log();
		System.out.println("---------Pre execute----------");
		action.execute();
		System.out.println("---------After execute----------");
		action.log();
		System.out.println("---------After execute----------");
	}

	private Object getHashSetObject(String query) {
		Object hashSet;
		try {
			hashSet = OclManager.evaluateOCL(query, testModel);
			return hashSet;
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Before
	public void initialize() {
		Manager.getInstance().init("src/main/resources/models/BGCS/BGCS.uml");
		testModel = Manager.getInstance().getModel();
	}
}

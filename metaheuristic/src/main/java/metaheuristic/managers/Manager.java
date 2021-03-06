package metaheuristic.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.URIConverter;
import org.eclipse.ocl.ParserException;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.uma.jmetal.util.pseudorandom.JMetalRandom;

import logicalSpecification.Action;
import logicalSpecification.AndOperator;
import logicalSpecification.Antipattern;
import logicalSpecification.EqualOperator;
import logicalSpecification.ExistsOperator;
import logicalSpecification.FOLSpecification;
import logicalSpecification.ForAllOperator;
import logicalSpecification.GreaterEqualOperator;
import logicalSpecification.GreaterOperator;
import logicalSpecification.LessEqualOperator;
import logicalSpecification.LessOperator;
import logicalSpecification.LogicalSpecificationFactory;
import logicalSpecification.MultipleValuedParameter;
import logicalSpecification.NotOperator;
import logicalSpecification.Operator;
import logicalSpecification.OrOperator;
import logicalSpecification.Parameter;
import logicalSpecification.PostCondition;
import logicalSpecification.PreCondition;
import logicalSpecification.Refactoring;
import logicalSpecification.RelationalOperator;
import logicalSpecification.SingleValuedParameter;
import metaheuristic.managers.uml.UMLManager;

// TODO Singleton
public class Manager {

//	private static Model model;
	private static String modelUri = null;

	private Manager() {
	}

	public void init(String modelUri) {	
		UMLManager.getInstance().init(modelUri);
	}

    private static class ManagerHolder {
        private static final Manager INSTANCE = new Manager();
    }

	// TODO inserire qui il il modelUri non è proprio comodo
    public static Manager getInstance() {
        return ManagerHolder.INSTANCE;
    }

	public static String getModelUri() {
		return modelUri;
	}

	public void setModelUri(String modelUri) {
		Manager.modelUri = modelUri;
	}

	public static org.eclipse.emf.common.util.URI string2Uri(String stringToConvert) {
		URI uri = URI.createURI(stringToConvert);
		return uri;
	}

	public static org.eclipse.emf.common.util.URI string2FileUri(String stringToConvert) {
		URI fileUri = URI.createFileURI(stringToConvert);
		return fileUri;
	}

	public Model getModel() {
		return UMLManager.getInstance().getModel();
	}

	public static double calculateCost(Refactoring r) {
		double sum = 0.0;
		for (Action action : r.getActions()) {
			sum += action.getCost();
		}
		return sum;
	}

	public static double calculateNumOfChanges(Refactoring r) {
		double sum = 0.0;
		for (Action action : r.getActions()) {
			sum += action.getNumOfChanges();
		}
		return sum;
	}
	//
	// public static Model getUMLModel(String umlModelUri) {
	// ResourceSet set = new ResourceSetImpl();
	// set.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
	// set.getResourceFactoryRegistry().getExtensionToFactoryMap().put(UMLResource.FILE_EXTENSION,
	// UMLResource.Factory.INSTANCE);
	// Resource resource = set.getResource(string2FileUri(umlModelUri), true);
	// Model model = (Model) EcoreUtil.getObjectByType(resource.getContents(),
	// UMLPackage.Literals.MODEL);
	// return model;
	// }
	
	public static Action getTautologyRandomAction(int n) {
		
//use n to choose among n possible actions
		int u_bound = 10;
		int l_bound = 1;

		Action a = LogicalSpecificationFactory.eINSTANCE.createAction();
		a.setName("rand_action_"+JMetalRandom.getInstance().getRandomGenerator().nextInt(0,Short.MAX_VALUE));

		a.setCost(JMetalRandom.getInstance().getRandomGenerator().nextInt(l_bound, u_bound));
		a.setNumOfChanges(JMetalRandom.getInstance().getRandomGenerator().nextInt(l_bound,u_bound));

		
		SingleValuedParameter lhs = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String queryLhs = "4";
		lhs.setResolvingExpr(queryLhs);
		
		SingleValuedParameter rhs = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String queryRhs = "3";
		rhs.setResolvingExpr(queryRhs);
		
		PreCondition aPre = LogicalSpecificationFactory.eINSTANCE.createPreCondition();
		FOLSpecification aPreSpec = LogicalSpecificationFactory.eINSTANCE.createFOLSpecification();
		aPreSpec.setName("APreCondition");


		AndOperator aPreAnd = LogicalSpecificationFactory.eINSTANCE.createAndOperator();

		GreaterOperator aPreAndGreater = LogicalSpecificationFactory.eINSTANCE.createGreaterOperator();
		aPreAndGreater.setLhs(lhs);
		aPreAndGreater.setRhs(rhs);
		
		aPreAnd.getArguments().add(aPreAndGreater);		
		aPreSpec.setRootOperator(aPreAnd);
		aPre.setConditionFormula(aPreSpec);
		a.setPre(aPre);
		
		
		SingleValuedParameter lhs_ = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String queryLhs_ = "2";
		lhs_.setResolvingExpr(queryLhs_);
		
		SingleValuedParameter rhs_ = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String queryRhs_ = "1";
		rhs_.setResolvingExpr(queryRhs_);
		
		PostCondition aPost = LogicalSpecificationFactory.eINSTANCE.createPostCondition();
		FOLSpecification aPostSpec = LogicalSpecificationFactory.eINSTANCE.createFOLSpecification();
		aPostSpec.setName("APostCondition");
		AndOperator aPostAnd = LogicalSpecificationFactory.eINSTANCE.createAndOperator();
		GreaterOperator aPostAndGreater = LogicalSpecificationFactory.eINSTANCE.createGreaterOperator();
		aPostAndGreater.setLhs(lhs_);
		aPostAndGreater.setRhs(rhs_);
		aPostAnd.getArguments().add(aPreAndGreater);	
		aPostSpec.setRootOperator(aPostAnd);
		aPost.setConditionFormula(aPostSpec);
		a.setPost(aPost);


		return a;
	}

	/////// OPERATORS EVALUATION METHODS

	public static boolean evaluateFOL(FOLSpecification folSpec) throws ParserException {
		if (folSpec.getRootOperator() instanceof NotOperator)
			return evaluateOperator((NotOperator) folSpec.getRootOperator());
		else if (folSpec.getRootOperator() instanceof AndOperator)
			return evaluateOperator((AndOperator) folSpec.getRootOperator());
		else if (folSpec.getRootOperator() instanceof OrOperator)
			return evaluateOperator((OrOperator) folSpec.getRootOperator());
		else if (folSpec.getRootOperator() instanceof ForAllOperator)
			return evaluateOperator((ForAllOperator) folSpec.getRootOperator());
		else if (folSpec.getRootOperator() instanceof ExistsOperator)
			return evaluateOperator((ExistsOperator) folSpec.getRootOperator());
		else if (folSpec.getRootOperator() instanceof GreaterOperator)
			return evaluateOperator((GreaterOperator) folSpec.getRootOperator());
		else if (folSpec.getRootOperator() instanceof GreaterOperator)
			return evaluateOperator((GreaterEqualOperator) folSpec.getRootOperator());
		else if (folSpec.getRootOperator() instanceof GreaterEqualOperator)
			return evaluateOperator((LessOperator) folSpec.getRootOperator());
		else if (folSpec.getRootOperator() instanceof LessOperator)
			return evaluateOperator((LessEqualOperator) folSpec.getRootOperator());
		return evaluateOperator((EqualOperator) folSpec.getRootOperator());
	}

	// public static boolean evaluateOperator(Operator operator) {
	// return evaluateOperator(operator);
	// }

	public static boolean evaluateOperator(NotOperator operator) throws ParserException {
//		System.out.print("NOT(");
		boolean app = true;
		if (operator.getArgument() instanceof NotOperator)
			app = !evaluateOperator((NotOperator) operator.getArgument());
		else if (operator.getArgument() instanceof AndOperator)
			app = !evaluateOperator((AndOperator) operator.getArgument());
		else if (operator.getArgument() instanceof OrOperator)
			app = !evaluateOperator((OrOperator) operator.getArgument());
		else if (operator.getArgument() instanceof ForAllOperator)
			app = !evaluateOperator((ForAllOperator) operator.getArgument());
		else if (operator.getArgument() instanceof ExistsOperator)
			app = !evaluateOperator((ExistsOperator) operator.getArgument());
		else if (operator.getArgument() instanceof GreaterOperator)
			app = !evaluateOperator((GreaterOperator) operator.getArgument());
		else if (operator.getArgument() instanceof GreaterEqualOperator)
			app = !evaluateOperator((GreaterEqualOperator) operator.getArgument());
		else if (operator.getArgument() instanceof LessOperator)
			app = !evaluateOperator((LessOperator) operator.getArgument());
		else if (operator.getArgument() instanceof LessEqualOperator)
			app = !evaluateOperator((LessEqualOperator) operator.getArgument());
		else
			app = !evaluateOperator((EqualOperator) operator.getArgument());
//		System.out.print(")");
		return app;
	}

	public static boolean evaluateOperator(NotOperator operator, Object contextualElement) throws ParserException {
//		System.out.print("NOT(");
		boolean app = true;
		if (operator.getArgument() instanceof NotOperator)
			app = !evaluateOperator((NotOperator) operator.getArgument(), contextualElement);
		else if (operator.getArgument() instanceof AndOperator)
			app = !evaluateOperator((AndOperator) operator.getArgument(), contextualElement);
		else if (operator.getArgument() instanceof OrOperator)
			app = !evaluateOperator((OrOperator) operator.getArgument(), contextualElement);
		else if (operator.getArgument() instanceof ForAllOperator)
			app = !evaluateOperator((ForAllOperator) operator.getArgument(), contextualElement);
		else if (operator.getArgument() instanceof ExistsOperator)
			app = !evaluateOperator((ExistsOperator) operator.getArgument(), contextualElement);
		else if (operator.getArgument() instanceof GreaterOperator)
			app = !evaluateOperator((GreaterOperator) operator.getArgument(), contextualElement);
		else if (operator.getArgument() instanceof GreaterEqualOperator)
			app = !evaluateOperator((GreaterEqualOperator) operator.getArgument(), contextualElement);
		else if (operator.getArgument() instanceof LessOperator)
			app = !evaluateOperator((LessOperator) operator.getArgument(), contextualElement);
		else if (operator.getArgument() instanceof LessEqualOperator)
			app = !evaluateOperator((LessEqualOperator) operator.getArgument(), contextualElement);
		else
			app = !evaluateOperator((EqualOperator) operator.getArgument(), contextualElement);
//		System.out.print(")");
		return app;
	}

	public static boolean evaluateOperator(AndOperator operator) throws ParserException {
//		System.out.print("AND(");
		boolean app = true;
		Iterator<Operator> opsIterator = operator.getArguments().iterator();
		while (opsIterator.hasNext()) {
			// boolean app = true;
			Operator appOp = opsIterator.next();
			if (appOp instanceof NotOperator)
				app = app && evaluateOperator((NotOperator) appOp);
			else if (appOp instanceof AndOperator)
				app = app && evaluateOperator((AndOperator) appOp);
			else if (appOp instanceof OrOperator)
				app = app && evaluateOperator((OrOperator) appOp);
			else if (appOp instanceof ForAllOperator)
				app = app && evaluateOperator((ForAllOperator) appOp);
			else if (appOp instanceof ExistsOperator)
				app = app && evaluateOperator((ExistsOperator) appOp);
			else if (appOp instanceof GreaterOperator)
				app = app && evaluateOperator((GreaterOperator) appOp);
			else if (appOp instanceof GreaterEqualOperator)
				app = app && evaluateOperator((GreaterEqualOperator) appOp);
			else if (appOp instanceof LessOperator)
				app = app && evaluateOperator((LessOperator) appOp);
			else if (appOp instanceof LessEqualOperator)
				app = app && evaluateOperator((LessEqualOperator) appOp);
			else
				app = evaluateOperator((EqualOperator) appOp);
//			if (opsIterator.hasNext())
//				System.out.print(", ");
			// if(!app)
			// return false;
		}
//		System.out.print(")");
		return app;
	}

	public static boolean evaluateOperator(AndOperator operator, Object contextualElement) throws ParserException {
//		System.out.print("AND(");
		boolean app = true;
		Iterator<Operator> opsIterator = operator.getArguments().iterator();
		while (opsIterator.hasNext()) {
			Object appOp = opsIterator.next();
			// boolean app = true;
			if (appOp instanceof NotOperator)
				app = app && evaluateOperator((NotOperator) appOp, contextualElement);
			else if (appOp instanceof AndOperator)
				app = app && evaluateOperator((AndOperator) appOp, contextualElement);
			else if (appOp instanceof OrOperator)
				app = app && evaluateOperator((OrOperator) appOp, contextualElement);
			else if (appOp instanceof ForAllOperator)
				app = app && evaluateOperator((ForAllOperator) appOp, contextualElement);
			else if (appOp instanceof ExistsOperator)
				app = app && evaluateOperator((ExistsOperator) appOp, contextualElement);
			else if (appOp instanceof GreaterOperator)
				app = app && evaluateOperator((GreaterOperator) appOp, contextualElement);
			else if (appOp instanceof GreaterEqualOperator)
				app = app && evaluateOperator((GreaterEqualOperator) appOp, contextualElement);
			else if (appOp instanceof LessOperator)
				app = app && evaluateOperator((LessOperator) appOp, contextualElement);
			else if (appOp instanceof LessEqualOperator)
				app = app && evaluateOperator((LessEqualOperator) appOp, contextualElement);
			else
				app = app && evaluateOperator((EqualOperator) appOp, contextualElement);
//			if (opsIterator.hasNext())
//				System.out.print(", ");
			// if(!app)
			// return false;
		}
//		System.out.print(")");
		return app;
	}

	public static boolean evaluateOperator(OrOperator operator) throws ParserException {
//		System.out.print("OR(");
		boolean app = false;
		Iterator<Operator> opsIterator = operator.getArguments().iterator();
		while (opsIterator.hasNext()) {
			Object appOp = opsIterator.next();
			// boolean app = false;
			if (appOp instanceof NotOperator)
				app = app || evaluateOperator((NotOperator) appOp);
			else if (appOp instanceof AndOperator)
				app = app || evaluateOperator((AndOperator) appOp);
			else if (appOp instanceof OrOperator)
				app = app || evaluateOperator((OrOperator) appOp);
			else if (appOp instanceof ForAllOperator)
				app = app || evaluateOperator((ForAllOperator) appOp);
			else if (appOp instanceof ExistsOperator)
				app = app || evaluateOperator((ExistsOperator) appOp);
			else if (appOp instanceof GreaterOperator)
				app = app || evaluateOperator((GreaterOperator) appOp);
			else if (appOp instanceof GreaterEqualOperator)
				app = app || evaluateOperator((GreaterEqualOperator) appOp);
			else if (appOp instanceof LessOperator)
				app = app || evaluateOperator((LessOperator) appOp);
			else if (appOp instanceof LessEqualOperator)
				app = app || evaluateOperator((LessEqualOperator) appOp);
			else
				app = app || evaluateOperator((EqualOperator) appOp);
//			if (opsIterator.hasNext())
//				System.out.print(", ");
			// if(app)
			// return true;
		}
//		System.out.print(")");
		return app;
	}

	public static boolean evaluateOperator(OrOperator operator, Object contextualElement) throws ParserException {
//		System.out.print("OR(");
		boolean app = false;
		Iterator<Operator> opsIterator = operator.getArguments().iterator();
		while (opsIterator.hasNext()) {
			Object appOp = opsIterator.next();
			// boolean app = false;
			if (appOp instanceof NotOperator)
				app = app || evaluateOperator((NotOperator) appOp, contextualElement);
			else if (appOp instanceof AndOperator)
				app = app || evaluateOperator((AndOperator) appOp, contextualElement);
			else if (appOp instanceof OrOperator)
				app = app || evaluateOperator((OrOperator) appOp, contextualElement);
			else if (appOp instanceof ForAllOperator)
				app = app || evaluateOperator((ForAllOperator) appOp, contextualElement);
			else if (appOp instanceof ExistsOperator)
				app = app || evaluateOperator((ExistsOperator) appOp, contextualElement);
			else if (appOp instanceof GreaterOperator)
				app = app || evaluateOperator((GreaterOperator) appOp, contextualElement);
			else if (appOp instanceof GreaterEqualOperator)
				app = app || evaluateOperator((GreaterEqualOperator) appOp, contextualElement);
			else if (appOp instanceof LessOperator)
				app = app || evaluateOperator((LessOperator) appOp, contextualElement);
			else if (appOp instanceof LessEqualOperator)
				app = app || evaluateOperator((LessEqualOperator) appOp, contextualElement);
			else
				app = app || evaluateOperator((EqualOperator) appOp, contextualElement);
//			if (opsIterator.hasNext())
//				System.out.print(", ");
			// if(app)
			// return true;
		}
//		System.out.print(")");
		return app;
	}

	public static boolean evaluateOperator(ForAllOperator operator) throws ParserException {
		HashSet<Object> hashSetRes = (HashSet<Object>) OclManager.evaluateOCL(operator.getCollection().getResolvingExpr(),
				Manager.getInstance().getModel());
		List<Object> res = new ArrayList<Object>();
		for (Object object : hashSetRes) {
			if (object instanceof Model)
				res.add((Model) object);
			else if (object instanceof Package)
				res.add((Package) object);
			else if (object instanceof Component)
				res.add((Component) object);
			else if (object instanceof Operation)
				res.add((Operation) object);
			else if (object instanceof Node)
				res.add((Node) object);
		}

//		System.out.print("FORALL(element in \"" + operator.getCollection().getResolvingExpr() + "\": ");

//		if (res.size() > 1)
//			System.out.print("AND(");

		boolean app = true;
		for (Object object : res) {
			// boolean app = true;
			if (operator.getArgument() instanceof NotOperator)
				app = evaluateOperator((NotOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof AndOperator)
				app = evaluateOperator((AndOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof OrOperator)
				app = evaluateOperator((OrOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof ForAllOperator)
				app = evaluateOperator((ForAllOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof ExistsOperator)
				app = evaluateOperator((ExistsOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof GreaterOperator)
				app = evaluateOperator((GreaterOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof GreaterEqualOperator)
				app = evaluateOperator((GreaterEqualOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof LessOperator)
				app = evaluateOperator((LessOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof LessEqualOperator)
				app = evaluateOperator((LessEqualOperator) operator.getArgument(), object);
			else
				app = evaluateOperator((EqualOperator) operator.getArgument(), object);
//			if (res.get(res.size() - 1) != object)
//				System.out.print(", ");
//			else
//				System.out.print(")");
			// if(!app)
			// return false;
		}
//		System.out.print(")");
		return app;
	}

	public static boolean evaluateOperator(ForAllOperator operator, String umlModelUri) throws ParserException {
		HashSet<Object> hashSetRes = (HashSet<Object>) OclManager.evaluateOCL(operator.getCollection().getResolvingExpr(),
				Manager.getInstance().getModel());
		List<Object> res = new ArrayList<Object>();
		for (Object object : hashSetRes) {
			if (object instanceof Model)
				res.add((Model) object);
			else if (object instanceof Package)
				res.add((Package) object);
			else if (object instanceof Component)
				res.add((Component) object);
			else if (object instanceof Operation)
				res.add((Operation) object);
			else if (object instanceof Node)
				res.add((Node) object);
		}

//		System.out.print("FORALL(element in \"" + operator.getCollection().getResolvingExpr() + "\": ");

//		if (res.size() > 1)
//			System.out.print("AND(");

		boolean app = true;
		for (Object object : res) {
			// boolean app = true;
			if (operator.getArgument() instanceof NotOperator)
				app = evaluateOperator((NotOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof AndOperator)
				app = evaluateOperator((AndOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof OrOperator)
				app = evaluateOperator((OrOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof ForAllOperator)
				app = evaluateOperator((ForAllOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof ExistsOperator)
				app = evaluateOperator((ExistsOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof GreaterOperator)
				app = evaluateOperator((GreaterOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof GreaterEqualOperator)
				app = evaluateOperator((GreaterEqualOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof LessOperator)
				app = evaluateOperator((LessOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof LessEqualOperator)
				app = evaluateOperator((LessEqualOperator) operator.getArgument(), object);
			else
				app = evaluateOperator((EqualOperator) operator.getArgument(), object);
//			if (res.get(res.size() - 1) != object)
//				System.out.print(", ");
//			else
//				System.out.print(")");
			// if(!app)
			// return false;
		}
//		System.out.print(")");
		return app;
	}

	public static boolean evaluateOperator(ForAllOperator operator, Object contextualElement) throws ParserException {
		HashSet<Object> hashSetRes = (HashSet<Object>) OclManager.evaluateOCL(operator.getCollection().getResolvingExpr(),
				Manager.getInstance().getModel());
		List<Object> res = new ArrayList<Object>();
		for (Object object : hashSetRes) {
			if (object instanceof Model)
				res.add((Model) object);
			else if (object instanceof Package)
				res.add((Package) object);
			else if (object instanceof Component)
				res.add((Component) object);
			else if (object instanceof Operation)
				res.add((Operation) object);
			else if (object instanceof Node)
				res.add((Node) object);
		}

//		System.out.print("FORALL(element in \"" + operator.getCollection().getResolvingExpr() + "\": ");

//		if (res.size() > 1)
//			System.out.print("AND(");

		boolean app = true;
		for (Object object : res) {
			// boolean app = true;
			if (operator.getArgument() instanceof NotOperator)
				app = evaluateOperator((NotOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof AndOperator)
				app = evaluateOperator((AndOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof OrOperator)
				app = evaluateOperator((OrOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof ForAllOperator)
				app = evaluateOperator((ForAllOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof ExistsOperator)
				app = evaluateOperator((ExistsOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof GreaterOperator)
				app = evaluateOperator((GreaterOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof GreaterEqualOperator)
				app = evaluateOperator((GreaterEqualOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof LessOperator)
				app = evaluateOperator((LessOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof LessEqualOperator)
				app = evaluateOperator((LessEqualOperator) operator.getArgument(), object);
			else
				app = evaluateOperator((EqualOperator) operator.getArgument(), object);
//			if (res.get(res.size() - 1) != object)
//				System.out.print(", ");
//			else
//				System.out.print(")");
			// if(!app)
			// return false;
		}
//		System.out.print(")");
		return app;
	}

	public static boolean evaluateOperator(ForAllOperator operator, Object contextualElement, String umlModelUri)
			throws ParserException {
		HashSet<Object> hashSetRes = (HashSet<Object>) OclManager.evaluateOCL(operator.getCollection().getResolvingExpr(),
				Manager.getInstance().getModel());
		List<Object> res = new ArrayList<Object>();
		for (Object object : hashSetRes) {
			if (object instanceof Model)
				res.add((Model) object);
			else if (object instanceof Package)
				res.add((Package) object);
			else if (object instanceof Component)
				res.add((Component) object);
			else if (object instanceof Operation)
				res.add((Operation) object);
			else if (object instanceof Node)
				res.add((Node) object);
		}

//		System.out.print("FORALL(element in \"" + operator.getCollection().getResolvingExpr() + "\": ");

//		if (res.size() > 1)
//			System.out.print("AND(");

		boolean app = true;
		for (Object object : res) {
			// boolean app = true;
			if (operator.getArgument() instanceof NotOperator)
				app = evaluateOperator((NotOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof AndOperator)
				app = evaluateOperator((AndOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof OrOperator)
				app = evaluateOperator((OrOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof ForAllOperator)
				app = evaluateOperator((ForAllOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof ExistsOperator)
				app = evaluateOperator((ExistsOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof GreaterOperator)
				app = evaluateOperator((GreaterOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof GreaterEqualOperator)
				app = evaluateOperator((GreaterEqualOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof LessOperator)
				app = evaluateOperator((LessOperator) operator.getArgument(), object);
			else if (operator.getArgument() instanceof LessEqualOperator)
				app = evaluateOperator((LessEqualOperator) operator.getArgument(), object);
			else
				app = evaluateOperator((EqualOperator) operator.getArgument(), object);
//			if (res.get(res.size() - 1) != object)
//				System.out.print(", ");
//			else
//				System.out.print(")");
			// if(!app)
			// return false;
		}
//		System.out.print(")");
		return app;
	}

	public static boolean evaluateOperator(ExistsOperator operator) throws ParserException {
		 HashSet<Object> contextualElHS = OclManager.evaluateQuery(operator.getElement().getResolvingExpr());
//		 SingleValuedParameter contextualElement = null;
//		 if(contextualElHS.size() == 1) {
//			 contextualElement = (SingleValuedParameter) contextualElHS.iterator().next();
//		 }
		 Object contextualElement = contextualElHS.iterator().next();
		 if(contextualElement != null) {
			 //HashSet<Object> hashSetRes = (HashSet<Object>) OclManager.evaluateOCL(operator.getCollection().getResolvingExpr(), contextualElement);
			 HashSet<Object> hashSetRes = OclManager.evaluateQuery(operator.getElement().getResolvingExpr());
			 List<Object> res = new ArrayList<Object>();
			 for(Object object : hashSetRes) {
				 if(object instanceof Model)
					 res.add((Model) object);
				 else if(object instanceof Package)
					 res.add((Package) object);
				 else if(object instanceof Component)
					 res.add((Component) object);
				 else if(object instanceof Operation)
					 res.add((Operation) object);
				 else if(object instanceof Node)
					 res.add((Node) object);
			 }
		
//			 System.out.print("EXISTS(" + ((org.eclipse.uml2.uml.NamedElement) contextualElement).getName() + ", \""
//						+ operator.getCollection().getResolvingExpr() + "\"");
			 
			if(res != null && contextualElement != null) {
				 boolean found = false;
				 Iterator<Object> resIterator = res.iterator();
				 while (resIterator.hasNext() && !found) {
					 Object app = resIterator.next();
					 if(app.equals(contextualElement))
						 return true;
				 }
				 return false;
			}
			return false;
		}
//		if (operator.getElement() == null) {
//			System.out.print("EXISTS(element, \"" + operator.getCollection().getResolvingExpr() + "\"");
//		} else
//			System.out.print("EXISTS(" + operator.getElement().getResolvingExpr() + ", \""
//					+ operator.getCollection().getResolvingExpr() + "\"");
		return false;
	}

	public static boolean evaluateOperator(ExistsOperator operator, Object contextualElement) throws ParserException {
		@SuppressWarnings("unchecked")
		HashSet<Object> hashSetRes = ((HashSet<Object>) OclManager.evaluateOCL(operator.getCollection().getResolvingExpr(),
				contextualElement));
		List<Object> res = new ArrayList<Object>();
		for (Object object : hashSetRes) {
			if (object instanceof Model)
				res.add((Model) object);
			else if (object instanceof Package)
				res.add((Package) object);
			else if (object instanceof Component)
				res.add((Component) object);
			else if (object instanceof Operation)
				res.add((Operation) object);
			else if (object instanceof Node)
				res.add((Node) object);
		}

//		System.out.print("EXISTS(" + ((org.eclipse.uml2.uml.NamedElement) contextualElement).getName() + ", \""
//				+ operator.getCollection().getResolvingExpr() + "\"");

		boolean found = false;
		if (res != null && contextualElement != null) {
			// boolean found = false;
			Iterator<Object> resIterator = res.iterator();
			while (resIterator.hasNext() && !found) {
				Object app = resIterator.next();
				if (app.equals(contextualElement))
					found = true;
				// return true;
			}
//			System.out.print(")");
			return found;
		}
//		System.out.print(")");
		return found;
	}

	public static boolean evaluateOperator(GreaterOperator operator) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), Manager.getInstance().getModel());
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), Manager.getInstance().getModel());
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?>? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = lhsIterator.next();
					if (((Double) appLhs).doubleValue() <= operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(GreaterOperator operator, String umlModelUri) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), Manager.getInstance().getModel());
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), Manager.getInstance().getModel());
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?>? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = lhsIterator.next();
					if (((Double) appLhs).doubleValue() <= operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(GreaterOperator operator, Object contextualElement) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), contextualElement);
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), contextualElement);
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?>? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					contextualElement);
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					contextualElement);
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() <= operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(GreaterEqualOperator operator) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), Manager.getInstance().getModel());
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), Manager.getInstance().getModel());
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?>=? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() < operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(GreaterEqualOperator operator, String umlModelUri) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), Manager.getInstance().getModel());
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), Manager.getInstance().getModel());
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?>=? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() < operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(GreaterEqualOperator operator, Object contextualElement)
			throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), contextualElement);
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), contextualElement);
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?>=? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					contextualElement);
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					contextualElement);
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() < operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(LessOperator operator) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), Manager.getInstance().getModel());
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), Manager.getInstance().getModel());
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?<? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() >= operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(LessOperator operator, String umlModelUri) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), Manager.getInstance().getModel());
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), Manager.getInstance().getModel());
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?<? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() >= operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(LessOperator operator, Object contextualElement) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), contextualElement);
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), contextualElement);
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?<? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					contextualElement);
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					contextualElement);
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() >= operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(LessEqualOperator operator) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), Manager.getInstance().getModel());
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), Manager.getInstance().getModel());
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?<=? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() > operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(LessEqualOperator operator, String umlModelUri) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), Manager.getInstance().getModel());
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), Manager.getInstance().getModel());
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?<=? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() > operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(LessEqualOperator operator, Object contextualElement)
			throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), contextualElement);
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), contextualElement);
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?<=? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					contextualElement);
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					contextualElement);
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() > operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(EqualOperator operator) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), Manager.getInstance().getModel());
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), Manager.getInstance().getModel());
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?=? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() != operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(EqualOperator operator, String umlModelUri) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), Manager.getInstance().getModel());
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), Manager.getInstance().getModel());
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?=? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					Manager.getInstance().getModel());
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() != operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	public static boolean evaluateOperator(EqualOperator operator, Object contextualElement) throws ParserException {
		Object lhsRes = OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(), contextualElement);
		Object rhsRes = OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(), contextualElement);
		if ((lhsRes instanceof Integer && rhsRes instanceof Integer)
				|| (lhsRes instanceof Double && rhsRes instanceof Double)) {
			double lhs = Double.parseDouble(lhsRes.toString());
			double rhs = Double.parseDouble(rhsRes.toString());
//			System.out.print(lhs + " ?=? " + rhs);
			if (lhs < operator.getFactor() * rhs)
				return false;
		} else {
			HashSet<Object> lhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getLhs().getResolvingExpr(),
					contextualElement);
			HashSet<Object> rhsHS = (HashSet<Object>) OclManager.evaluateOCL(operator.getRhs().getResolvingExpr(),
					contextualElement);
			if (lhsHS.size() == rhsHS.size()) {
				List<Object> lhsObjs = new ArrayList<Object>();
				for (Object object : lhsHS) {
					if (object instanceof Double)
						lhsObjs.add((double) object);
					else
						return false;
				}
				List<Object> rhsObjs = new ArrayList<Object>();
				for (Object object : rhsHS) {
					if (object instanceof Double)
						rhsObjs.add((double) object);
					else
						return false;
				}
				Iterator<Object> lhsIterator = lhsObjs.iterator();
				Iterator<Object> rhsIterator = rhsObjs.iterator();
				while (lhsIterator.hasNext()) {
					Object appLhs = lhsIterator.next();
					Object appRhs = rhsIterator.next();
					if (((Double) appLhs).doubleValue() != operator.getFactor() * ((Double) appRhs).doubleValue())
						return false;
				}
				return true;
			}
		}
		return true;
	}

	////// PRE CONDITIONS METHODS

	public static PreCondition calculatePreCondition(Refactoring r) {
		PreCondition res = null;
		if (r != null) {
			if (r.getActions() != null) {
				if (r.getActions().size() == 1)
					res = r.getActions().get(0).getPre();
				if (r.getActions().size() > 1) {
					res = r.getActions().get(0).getPre();
					for (int i = 1; i < r.getActions().size() - 1; i++) {
						res = calculatePreCondition(res, r.getActions().get(i).getPre(),
								r.getActions().get(i - 1).getPost());
					}
				}
			}
		}
		return res;
	}

	public static PreCondition calculatePreCondition(PreCondition pre1, PreCondition pre2, PostCondition post1) {
		PreCondition res = null;
		//TODO TO MODIFY
		if (pre1 == null || pre2 == null || post1 == null) {
			if (pre1 == null)
				res = pre2;
			if (pre2 == null)
				res = pre1;
		} else {
			res = LogicalSpecificationFactory.eINSTANCE.createPreCondition();
			FOLSpecification resFol = LogicalSpecificationFactory.eINSTANCE.createFOLSpecification();
			resFol.setRootOperator(Manager.append((AndOperator) pre1.getConditionFormula().getRootOperator(),
					(AndOperator) Manager.reducePreCondition(pre2, post1).getConditionFormula().getRootOperator()));
			res.setConditionFormula(resFol);
		}
		return res;
	}

	public static PreCondition reducePreCondition(PreCondition pre, PostCondition post) {
		PreCondition res = pre;
		if (res != null && post != null) {
			AndOperator resRootOp = (AndOperator) res.getConditionFormula().getRootOperator();
			AndOperator postRootOp = (AndOperator) post.getConditionFormula().getRootOperator();
			if (resRootOp != null && postRootOp != null)
				if (resRootOp.getArguments() != null && postRootOp != null) {
					ArrayList<Boolean> resRootOpDelArrayList = new ArrayList<Boolean>(resRootOp.getArguments().size());
					for (int i = 0; i < resRootOp.getArguments().size(); i++) {
						resRootOpDelArrayList.add(i, false);
					}
					for (int i = 0; i < resRootOp.getArguments().size() - 1; i++) {
						if (Manager.guarantees(postRootOp, resRootOp.getArguments().get(i))) {
							resRootOpDelArrayList.set(i, true);
						}
					}
					int i = 0;
					while (i < resRootOp.getArguments().size()) {
						if (resRootOpDelArrayList.get(i)) {
							resRootOp.getArguments().remove(i);
							resRootOpDelArrayList.remove(i);
						} else
							i++;
					}
				}
		}
		return res;
	}

	/////// POST CONDITIONS METHODS

	public static PostCondition calculatePostCondition(Refactoring r) {
		PostCondition res = null;

		if (r != null) {
			if (r.getActions() != null) {
				if (r.getActions().size() == 1)
					res = r.getActions().get(0).getPost();
				if (r.getActions().size() > 1) {
					res = r.getActions().get(0).getPost();
					for (int i = 1; i < r.getActions().size() - 1; i++) {
						res = reducePostCondition(calculatePostCondition(res, r.getActions().get(i).getPost()));
					}
				}
			}
		}

		return res;
	}

	public static PostCondition reducePostCondition(PostCondition post) {
		PostCondition res = post;
		if (res != null) {
			AndOperator resRootOp = (AndOperator) res.getConditionFormula().getRootOperator();
			if (resRootOp != null)
				if (resRootOp.getArguments() != null)
					if (resRootOp.getArguments().size() > 1) {
						ArrayList<Boolean> resRootOpDelArrayList = new ArrayList<Boolean>(
								resRootOp.getArguments().size());
						for (int i = 0; i < resRootOp.getArguments().size(); i++) {
							resRootOpDelArrayList.add(i, false);
						}
						for (int i = 0; i < resRootOp.getArguments().size() - 1; i++) {
							boolean found = false;
							int j = i + 1;
							while (j < resRootOp.getArguments().size() && !found) {
								///// CHIAMARE isInverse, non equal
								if (Manager.equal(resRootOp.getArguments().get(i), resRootOp.getArguments().get(j)))
									found = true;
								j++;
							}
							if (found) {
								resRootOpDelArrayList.set(i, true);
							}
						}
						int i = 0;
						while (i < resRootOp.getArguments().size()) {
							if (resRootOpDelArrayList.get(i)) {
								resRootOp.getArguments().remove(i);
								resRootOpDelArrayList.remove(i);
							} else
								i++;
						}
					}
		}
		return res;
	}

	public static PostCondition calculatePostCondition(PostCondition post1, PostCondition post2) {
		PostCondition res = null;
		if (post1 == null || post2 == null) {
			if (post1 == null)
				res = post2;
			if (post2 == null)
				res = post1;
		} else {
			res = LogicalSpecificationFactory.eINSTANCE.createPostCondition();
			FOLSpecification resFol = LogicalSpecificationFactory.eINSTANCE.createFOLSpecification();
			resFol.setRootOperator(Manager.append((AndOperator) post1.getConditionFormula().getRootOperator(),
					(AndOperator) post2.getConditionFormula().getRootOperator()));
			res.setConditionFormula(resFol);
		}
		return res;
	}

	// public static PostCondition calculatePostCondition(Action a1, Action a2)
	// {
	// PostCondition res = null;
	// if(a1.getPost() == null || a2.getPost() == null) {
	// if(a1.getPost() == null)
	// res = a2.getPost();
	// if(a2.getPost() == null)
	// res = a1.getPost();
	// } else {
	// res = LogicalSpecificationFactory.eINSTANCE.createPostCondition();
	// FOLSpecification resFol =
	// LogicalSpecificationFactory.eINSTANCE.createFOLSpecification();
	// resFol.setRootOperator(Manager.append((AndOperator)
	// a1.getPost().getConditionFormula().getRootOperator(), (AndOperator)
	// a2.getPost().getConditionFormula().getRootOperator()));
	// res.setConditionFormula(resFol);
	// }
	// return res;
	// }

	////// UTILITY OPERATORS METHODS

	public static AndOperator append(AndOperator and1, AndOperator and2) {
		AndOperator res = null;
		if (and1 == null || and2 == null) {
			if (and1 == null)
				res = and2;
			if (and2 == null)
				res = and1;
		} else {
			if (and1.getArguments() != null && and2.getArguments() != null) {
				res = LogicalSpecificationFactory.eINSTANCE.createAndOperator();
				res.getArguments().addAll(and1.getArguments());
				res.getArguments().addAll(and2.getArguments());
			}
		}
		return res;
	}

	/////// OPERATORS COMPARISON METHODS

	public static boolean guarantees(Operator op1, Operator op2) {
		if (op1 != null && op2 != null) {
			if (op1 != op2) {
				if (op1 instanceof NotOperator)
					return guarantees((NotOperator) op1, op2);
				if (op1 instanceof AndOperator)
					return guarantees((AndOperator) op1, op2);
				if (op1 instanceof OrOperator)
					return guarantees((OrOperator) op1, op2);
				if (op1 instanceof ForAllOperator)
					return guarantees((ForAllOperator) op1, op2);
				if (op1 instanceof ExistsOperator)
					return guarantees((ExistsOperator) op1, op2);
			}
			return true;
		}
		return false;
	}

	public static boolean guarantees(NotOperator op1, Operator op2) {
		if (op1 != null && op2 != null) {
			if (op1 != op2)
				return guarantees(op1.getArgument(), op2);
			return true;
		}
		return false;
	}

	public static boolean guarantees(AndOperator op1, Operator op2) {
		if (op1 != null && op2 != null) {
			if (op1 != op2) {
				if (op1.getArguments() != null) {
					for (int i = 0; i < op1.getArguments().size(); i++) {
						if (Manager.guarantees(op1.getArguments().get(i), op2))
							return true;
					}
					return false;
				}
				return false;
			}
			return false;
		}
		return false;
	}

	public static boolean guarantees(OrOperator op1, Operator op2) {
		if (op1 != null && op2 != null) {
			if (op1 != op2) {
				if (op1.getArguments() != null) {
					for (int i = 0; i < op1.getArguments().size(); i++) {
						if (Manager.guarantees(op1.getArguments().get(i), op2))
							return true;
					}
					return false;
				}
				return false;
			}
			return false;
		}
		return false;
	}

	public static boolean guarantees(ForAllOperator op1, Operator op2) {
		if (op1 != null && op2 != null) {
			if (op1 != op2)
				return Manager.guarantees(op1.getArgument(), op2);
			return false;
		}
		return false;
	}

	public static boolean guarantees(ExistsOperator op1, Operator op2) {
		if (op1 != null && op2 != null) {
			if (op1 != op2) {
				if (op1.getArgument() != null)
					return Manager.guarantees(op1.getArgument(), op2);
				return false;
			}
			return false;
		}
		return false;
	}

	public static boolean equal(Operator op1, Operator op2) {
		if (op1 != null && op2 != null) {
			if (op1 instanceof NotOperator && op2 instanceof NotOperator)
				return equal((NotOperator) op1, (NotOperator) op2);
			if (op1 instanceof AndOperator && op2 instanceof NotOperator)
				return equal((AndOperator) op1, (AndOperator) op2);
			if (op1 instanceof OrOperator && op2 instanceof OrOperator)
				return equal((OrOperator) op1, (OrOperator) op2);
			if (op1 instanceof ForAllOperator && op2 instanceof ForAllOperator)
				return equal((ForAllOperator) op1, (ForAllOperator) op2);
			if (op1 instanceof ExistsOperator && op2 instanceof ExistsOperator)
				return equal((ExistsOperator) op1, (ExistsOperator) op2);
			if (op1 instanceof GreaterOperator && op2 instanceof GreaterOperator)
				return equal((GreaterOperator) op1, (GreaterOperator) op2);
			if (op1 instanceof GreaterEqualOperator && op2 instanceof GreaterEqualOperator)
				return equal((GreaterEqualOperator) op1, (GreaterEqualOperator) op2);
			if (op1 instanceof LessOperator && op2 instanceof LessOperator)
				return equal((LessOperator) op1, (LessOperator) op2);
			if (op1 instanceof LessEqualOperator && op2 instanceof LessEqualOperator)
				return equal((LessEqualOperator) op1, (LessEqualOperator) op2);
			if (op1 instanceof EqualOperator && op2 instanceof EqualOperator)
				return equal((EqualOperator) op1, (EqualOperator) op2);
			return false;
		}
		return false;
	}

	public static boolean equal(NotOperator op1, NotOperator op2) {
		if (op1 != null && op2 != null)
			return Manager.equal(op1.getArgument(), op2.getArgument());
		return false;
	}

	public static boolean equal(AndOperator op1, AndOperator op2) {
		if (op1 != null && op2 != null) {
			if (op1.getArguments() != null && op2.getArguments() != null) {
				if (op1.getArguments().size() == op2.getArguments().size()) {
					for (int i = 0; i < op1.getArguments().size(); i++) {
						if (!Manager.equal(op1.getArguments().get(i), op2.getArguments().get(i)))
							return false;
					}
					return true;
				}
				return false;
			}
			return false;
		}
		return false;
	}

	public static boolean equal(OrOperator op1, OrOperator op2) {
		if (op1 != null && op2 != null) {
			if (op1.getArguments() != null && op2.getArguments() != null) {
				if (op1.getArguments().size() == op2.getArguments().size()) {
					for (int i = 0; i < op1.getArguments().size(); i++) {
						if (!Manager.equal(op1.getArguments().get(i), op2.getArguments().get(i)))
							return false;
					}
					return true;
				}
				return false;
			}
			return false;
		}
		return false;
	}

	public static boolean equal(ExistsOperator op1, ExistsOperator op2) {
		if (op1 != null && op2 != null) {
			if (Manager.equal(op1.getCollection(), op2.getCollection()))
				if (op1.getElement() != null && op2.getElement() != null)
					if (Manager.equal(op1.getElement(), op2.getElement())) {
						if (op1.getArgument() != null && op2.getArgument() != null)
							return Manager.equal(op1.getArgument(), op2.getArgument());
					}
			return false;
		}
		return false;
	}

	public static boolean equal(ForAllOperator op1, ForAllOperator op2) {
		if (op1 != null && op2 != null) {
			if (Manager.equal(op1.getCollection(), op2.getCollection()))
				return Manager.equal(op1.getArgument(), op2.getArgument());
			return false;
		}
		return false;
	}

	public static boolean equal(RelationalOperator op1, RelationalOperator op2) {
		if (op1 != null && op2 != null) {
			if (op1.getClass().equals(op2.getClass()))
				return Manager.equal(op1.getLhs(), op2.getRhs());
			return false;
		}
		return false;
	}

	public static boolean equal(Parameter p1, Parameter p2) {
		if (p1 != null && p2 != null)
			return p1.getResolvingExpr().equals(p2.getResolvingExpr());
		return false;
	}

	// public static boolean equal(SingleValuedParameter p1,
	// SingleValuedParameter p2) {
	// if(p1 != null && p2 != null)
	// return p1.getResolvingExpr().equals(p2.getResolvingExpr());
	// return false;
	// }
	//
	// public static boolean equal(MultipleValuedParameter p1,
	// MultipleValuedParameter p2) {
	// if(p1 != null && p2 != null)
	// return p1.getResolvingExpr().equals(p2.getResolvingExpr());
	// return false;
	// }

	/////// ANTIPATTERN DETECTION METHODS

	public static void completeAntipatternDetection(List<Antipattern> antipatterns, String umlModelUri)
			throws ParserException {
		if (antipatterns != null) {
			if (antipatterns.size() >= 0) {
				List<Element> components = Manager.getInstance().getModel().getNestedPackages().get(0)
						.getNestedPackages().get(0).allOwnedElements();
				System.out.println(
						"\n------------------------------------------ ANTIPATTERN DETECTION ------------------------------------------");
				for (Antipattern ap : antipatterns) {
					System.out.println("\n\t-------------------------------------- " + ap.getName()
							+ " --------------------------------------");

					for (Element c : components) {
						if (c instanceof Component) {
							System.out.print("\n- Is " + ((Component) c).getName() + " a " + ap.getName() + " ? ---> ");
							System.out.println(" = " + Manager.evaluateOperator(
									(AndOperator) ap.getCompleteFormula().getRootOperator(), (Component) c));
						}
					}
					System.out.println(
							"\n\t--------------------------------------------------------------------------------------------------------");
				}
				System.out.println(
						"\n----------------------------------------------------------------------------------------------------------------");

			} else
				System.out.println("No antipatterns to detect");
		} else
			System.out.println("No antipatterns to detect");
	}

	/////////////////////////

	protected static void registerPathmaps(URI baseUri) {
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.LIBRARIES_PATHMAP),
				baseUri.appendSegment("libraries").appendSegment(""));
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.METAMODELS_PATHMAP),
				baseUri.appendSegment("metamodels").appendSegment(""));
		URIConverter.URI_MAP.put(URI.createURI(UMLResource.PROFILES_PATHMAP),
				baseUri.appendSegment("profiles").appendSegment(""));
	}

	public static SingleValuedParameter createSingleValueParameter(String expr) {
		SingleValuedParameter singleValuedParameter = LogicalSpecificationFactory.eINSTANCE
				.createSingleValuedParameter();
		singleValuedParameter.setResolvingExpr(expr);
		return singleValuedParameter;
	}
	
	public static MultipleValuedParameter createMultipleValuedParameter() {
		MultipleValuedParameter multipleValuedParameter = LogicalSpecificationFactory.eINSTANCE
				.createMultipleValuedParameter();
		return multipleValuedParameter;
	}

	public static MultipleValuedParameter createMultipleValuedParameter(String expr) {
		MultipleValuedParameter multipleValuedParameter = LogicalSpecificationFactory.eINSTANCE
				.createMultipleValuedParameter();
		multipleValuedParameter.setResolvingExpr(expr);
		return multipleValuedParameter;
	}
	
	public static Refactoring createRefactoring(){
		Refactoring refactoring = LogicalSpecificationFactory.eINSTANCE.createRefactoring();
		refactoring.setName(Integer.toString(RandomUtils.nextInt(0, 100)));
		return refactoring;
	}

	public static ForAllOperator createForAllOperator(MultipleValuedParameter collection) {
		ForAllOperator forAll = LogicalSpecificationFactory.eINSTANCE.createForAllOperator();
		forAll.setCollection(collection);
		return forAll;
	}

	public static ExistsOperator createExistsOperator(SingleValuedParameter parameter, MultipleValuedParameter collection) {
		ExistsOperator existsOperator = LogicalSpecificationFactory.eINSTANCE.createExistsOperator();
		existsOperator.setElement(parameter);
		existsOperator.setCollection(collection);
		return existsOperator;
	}
	
	public static ExistsOperator createExistsOperator(MultipleValuedParameter collection) {
		ExistsOperator existsOperator = LogicalSpecificationFactory.eINSTANCE.createExistsOperator();
		existsOperator.setCollection(collection);
		return existsOperator;
	}

	public static AndOperator createAndOperator(){
		return LogicalSpecificationFactory.eINSTANCE.createAndOperator();
	}

	public static FOLSpecification createFOLSpectification(String string) {
		FOLSpecification fol = LogicalSpecificationFactory.eINSTANCE.createFOLSpecification();
		fol.setName(string);
		return fol;
	}

	public static NotOperator createNotOperator() {
		return LogicalSpecificationFactory.eINSTANCE.createNotOperator();
	}
	
	public static NotOperator createNotOperator(Operator operator) {
		NotOperator o = LogicalSpecificationFactory.eINSTANCE.createNotOperator();
		o.setArgument(operator);
		return o;
	}

	public static PreCondition createPreCondition() {
		return LogicalSpecificationFactory.eINSTANCE.createPreCondition();
	}

	public static PostCondition createPostCondition() {
		return LogicalSpecificationFactory.eINSTANCE.createPostCondition();
	}
}

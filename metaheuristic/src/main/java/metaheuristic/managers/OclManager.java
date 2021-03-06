package metaheuristic.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.ocl.OCL;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.Query;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.EcoreEnvironmentFactory;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.UMLPackage;

public class OclManager {

	@SuppressWarnings({ "unchecked", "static-access" })
	private static HashSet<Object> getHashSet(String query) {
		HashSet<Object> hashSetQuery = null;
		try {
			hashSetQuery = (HashSet<Object>) evaluateOCL(query, Manager.getInstance().getModel());
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashSetQuery;
	}

	public static HashSet<Object> evaluateQuery(String query) {
		return (HashSet<Object>) getQueryResult(query);
	}

	@SuppressWarnings({ "unchecked", "static-access" })
	private static HashSet<Object> getQueryResult(String query) {
		Object queryResult = null;
		HashSet<Object> hashSet = null;
		try {
			hashSet = new HashSet<Object>();
			queryResult = evaluateOCL(query, Manager.getInstance().getModel());
			if (queryResult instanceof Integer) {
				double intQueryResult = Integer.parseInt(queryResult.toString());
				hashSet.add(intQueryResult);
			} else if (queryResult instanceof Double) {
				double doubleQueryResult = Double.parseDouble(queryResult.toString());
				hashSet.add(doubleQueryResult);
			} else if (queryResult instanceof Model)
				hashSet.add((Model) queryResult);
			else if (queryResult instanceof Package)
				hashSet.add((Package) queryResult);
			else if (queryResult instanceof Component)
				hashSet.add((Component) queryResult);
			else if (queryResult instanceof Operation)
				hashSet.add((Operation) queryResult);
			else if (queryResult instanceof Node)
				hashSet.add((Node) queryResult);
			else if (queryResult instanceof HashSet<?>)
				hashSet = (HashSet<Object>) queryResult;
			else if (queryResult instanceof ArrayList<?>)
				hashSet.addAll((ArrayList<Object>) queryResult);
			
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hashSet;
	}

	public static OCLExpression<EClassifier> getOCLExpression(String query) throws ParserException {
		// create an OCL instance for Ecore
		OCL<?, EClassifier, ?, ?, ?, EParameter, ?, ?, ?, Constraint, EClass, EObject> ocl;
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
		// create an OCL helper object
		OCLHelper<EClassifier, ?, ?, Constraint> helper = ocl.createOCLHelper();
		// set the OCL context classifier
		helper.setContext(UMLPackage.Literals.MODEL);
		OCLExpression<EClassifier> oclExpression = helper.createQuery(query);
		return oclExpression;
	}

	public static Query<EClassifier, EClass, EObject> getOCLQuery(String query) throws ParserException {
		// create an OCL instance for Ecore
		OCL<?, EClassifier, ?, ?, ?, EParameter, ?, ?, ?, Constraint, EClass, EObject> ocl;
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
		// create an OCL helper object
		OCLHelper<EClassifier, ?, ?, Constraint> helper = ocl.createOCLHelper();
		// set the OCL context classifier
		helper.setContext(UMLPackage.Literals.MODEL);
		OCLExpression<EClassifier> oclExpression = helper.createQuery(query);
		Query<EClassifier, EClass, EObject> oclQuery = ocl.createQuery(oclExpression);
		return oclQuery;
	}


	/**
	 * @see http://www.programcreek.com/java-api-examples/index.php?api=org.eclipse.ocl.uml.UMLEnvironmentFactory
	 * @see http://archive.eclipse.org/modeling/mdt/ocl/javadoc/3.0.0/org/eclipse/ocl/Environment.html
	 * @see http://stackoverflow.com/questions/20774594/programmatically-execute-an-ocl-query-on-a-uml-model
	 * 
	 * SONO PARTITO DALL'ULTIMO
	 */
	public static Object evaluateOCL(String query, Object contextualElement) throws ParserException {
		// create an OCL instance for Ecore
		OCL<?, EClassifier, ?, ?, ?, ?, ?, ?, ?, Constraint, EClass, EObject> ocl;
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);

//		UMLEnvironmentFactory factory = new UMLEnvironmentFactory();
//		OCL<?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?> UMLresult = OCL.newInstance(factory);
//		OCLHelper<?, ?, ?, ?> UMLhelper = (OCLHelper<?, ?, ?, ?>) UMLresult.createOCLHelper();

		// create an OCL helper object
		OCLHelper<EClassifier, ?, ?, Constraint> helper = ocl.createOCLHelper();
		
		// set the OCL context classifier
		 if (contextualElement instanceof Model)
		 helper.setContext(UMLPackage.Literals.MODEL);
		 else if (contextualElement instanceof Package)
		 helper.setContext(UMLPackage.Literals.PACKAGE);
		 else if (contextualElement instanceof Component)
		 helper.setContext(UMLPackage.Literals.COMPONENT);
		 else if (contextualElement instanceof Operation)
		 helper.setContext(UMLPackage.Literals.OPERATION);
		 else if (contextualElement instanceof Node)
		 helper.setContext(UMLPackage.Literals.NODE);

		//helper.setInstanceContext(UMLPackage.eINSTANCE.getClass_());
		OCLExpression<EClassifier> oclExpression = helper.createQuery(query);
		Query<EClassifier, EClass, EObject> oclQuery = ocl.createQuery(oclExpression);
		return oclQuery.evaluate(contextualElement);
	}

	public static List<?> evaluateOCL(String query, List<Object> contextualElements) throws ParserException {
		// create an OCL instance for Ecore
		OCL<?, EClassifier, ?, ?, ?, ?, ?, ?, ?, Constraint, EClass, EObject> ocl;
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
		// create an OCL helper object
		OCLHelper<EClassifier, ?, ?, Constraint> helper = ocl.createOCLHelper();
		// set the OCL context classifier
		if (contextualElements.get(0) instanceof Package)
			helper.setContext(UMLPackage.Literals.PACKAGE);
		else if (contextualElements.get(0) instanceof Component)
			helper.setContext(UMLPackage.Literals.COMPONENT);
		else if (contextualElements.get(0) instanceof Operation)
			helper.setContext(UMLPackage.Literals.OPERATION);
		else if (contextualElements.get(0) instanceof Node)
			helper.setContext(UMLPackage.Literals.NODE);
		OCLExpression<EClassifier> oclExpression = helper.createQuery(query);
		Query<EClassifier, EClass, EObject> oclQuery = ocl.createQuery(oclExpression);
		return oclQuery.evaluate(contextualElements);
	}

	public static boolean checkOCL(String query, Object contextualElement) throws ParserException {
		// create an OCL instance for Ecore
		OCL<?, EClassifier, ?, ?, ?, ?, ?, ?, ?, Constraint, EClass, EObject> ocl;
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
		// create an OCL helper object
		OCLHelper<EClassifier, ?, ?, Constraint> helper = ocl.createOCLHelper();
		// set the OCL context classifier
		if (contextualElement instanceof Model)
			helper.setContext(UMLPackage.Literals.MODEL);
		else if (contextualElement instanceof Package)
			helper.setContext(UMLPackage.Literals.PACKAGE);
		else if (contextualElement instanceof Component)
			helper.setContext(UMLPackage.Literals.COMPONENT);
		else if (contextualElement instanceof Operation)
			helper.setContext(UMLPackage.Literals.OPERATION);
		else if (contextualElement instanceof Node)
			helper.setContext(UMLPackage.Literals.NODE);
		OCLExpression<EClassifier> oclExpression = helper.createQuery(query);
		Query<EClassifier, EClass, EObject> oclQuery = ocl.createQuery(oclExpression);
		return oclQuery.check(contextualElement);
	}

	public static boolean checkOCL(String query, List<Object> contextualElements) throws ParserException {
		// create an OCL instance for Ecore
		OCL<?, EClassifier, ?, ?, ?, ?, ?, ?, ?, Constraint, EClass, EObject> ocl;
		ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
		// create an OCL helper object
		OCLHelper<EClassifier, ?, ?, Constraint> helper = ocl.createOCLHelper();
		// set the OCL context classifier
		if (contextualElements.get(0) instanceof Package)
			helper.setContext(UMLPackage.Literals.PACKAGE);
		else if (contextualElements.get(0) instanceof Component)
			helper.setContext(UMLPackage.Literals.COMPONENT);
		else if (contextualElements.get(0) instanceof Operation)
			helper.setContext(UMLPackage.Literals.OPERATION);
		else if (contextualElements.get(0) instanceof Node)
			helper.setContext(UMLPackage.Literals.NODE);
		OCLExpression<EClassifier> oclExpression = helper.createQuery(query);
		Query<EClassifier, EClass, EObject> oclQuery = ocl.createQuery(oclExpression);
		return oclQuery.check(contextualElements);
	}
}

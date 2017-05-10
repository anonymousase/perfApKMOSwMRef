package metaheuristic.managers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.ocl.ParserException;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Node;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;

import logicalSpecification.Action;
import logicalSpecification.AndOperator;
import logicalSpecification.Antipattern;
import logicalSpecification.ExistsOperator;
import logicalSpecification.FOLSpecification;
import logicalSpecification.ForAllOperator;
import logicalSpecification.GreaterEqualOperator;
import logicalSpecification.GreaterOperator;
import logicalSpecification.LessEqualOperator;
import logicalSpecification.LoLa4RAPSRoot;
import logicalSpecification.LogicalSpecificationFactory;
import logicalSpecification.MultipleValuedParameter;
import logicalSpecification.NotOperator;
import logicalSpecification.OrOperator;
import logicalSpecification.Parameter;
import logicalSpecification.PostCondition;
import logicalSpecification.PreCondition;
import logicalSpecification.Refactoring;
import logicalSpecification.SingleValuedParameter;



public class CaseStudy1 {

	public static void main(String[] args) throws ParserException, org.eclipse.ocl.examples.pivot.ParserException {
		// TODO Auto-generated method stub
		LoLa4RAPSRoot root = LogicalSpecificationFactory.eINSTANCE.createLoLa4RAPSRoot();
		
		//REFACTORING ref1
		Refactoring ref1 = LogicalSpecificationFactory.eINSTANCE.createRefactoring();
		ref1.setName("addNode");
		
			//ACTION add
		Action add = LogicalSpecificationFactory.eINSTANCE.createAction();
		
				//ACTION add PARAMETERS
		List<Parameter> addParams = new ArrayList<>();
		SingleValuedParameter n = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String queryN = "Node.allInstances()->select(n | n.name = 'Pippo Node')";
		n.setResolvingExpr(queryN);
		addParams.add(n);
						
		MultipleValuedParameter neighs = LogicalSpecificationFactory.eINSTANCE.createMultipleValuedParameter();
		String queryNeighs = "Node.allInstances()->select(n | n.name = 'BooksDispatcher Node' or n.name = 'MoviesDispatcher Node')";
		neighs.setResolvingExpr(queryNeighs);
		addParams.add(neighs);
		
		MultipleValuedParameter deplComps = LogicalSpecificationFactory.eINSTANCE.createMultipleValuedParameter();
		String queryComps = "Component.allInstances()->select(c | c.name = 'BooksController' or n.name = 'MoviesController')";
		neighs.setResolvingExpr(queryComps);
		addParams.add(deplComps);
		
		MultipleValuedParameter allNodes = LogicalSpecificationFactory.eINSTANCE.createMultipleValuedParameter();
		String queryAllNodes = "Node.allInstances()";
		allNodes.setResolvingExpr(queryAllNodes);
		addParams.add(allNodes);
		
		MultipleValuedParameter allComps = LogicalSpecificationFactory.eINSTANCE.createMultipleValuedParameter();
		addParams.add(allComps);
		
		MultipleValuedParameter n_neighs = LogicalSpecificationFactory.eINSTANCE.createMultipleValuedParameter();
		addParams.add(n_neighs);
		
		MultipleValuedParameter n_deplComps = LogicalSpecificationFactory.eINSTANCE.createMultipleValuedParameter();
		addParams.add(n_deplComps);
		
		add.getParameters().addAll(addParams);
		
				//ACTION add PRECONDITION
		PreCondition addPre = LogicalSpecificationFactory.eINSTANCE.createPreCondition();
		FOLSpecification addPreSpec = LogicalSpecificationFactory.eINSTANCE.createFOLSpecification();
		addPreSpec.setName("AddNodePrecondition");
		AndOperator addPreAnd = LogicalSpecificationFactory.eINSTANCE.createAndOperator();
		
		NotOperator addPreAndNot = LogicalSpecificationFactory.eINSTANCE.createNotOperator();
		ExistsOperator addPreAndNotExists = LogicalSpecificationFactory.eINSTANCE.createExistsOperator();
		addPreAndNotExists.setElement(n);
		addPreAndNotExists.setCollection(allNodes);
		addPreAndNot.setArgument(addPreAndNotExists);
		addPreAnd.getArguments().add(addPreAndNot);
		
		ForAllOperator addPreAndForall = LogicalSpecificationFactory.eINSTANCE.createForAllOperator();
		addPreAndForall.setCollection(neighs);		
		ExistsOperator addPreAndForallExists = LogicalSpecificationFactory.eINSTANCE.createExistsOperator();
		addPreAndForallExists.setCollection(allNodes);
		addPreAndForall.setArgument(addPreAndForallExists);
		addPreAnd.getArguments().add(addPreAndForall);		
		addPreSpec.setRootOperator(addPreAnd);
		addPre.setConditionFormula(addPreSpec);
		add.setPre(addPre);
		
				//ACTION add POSTCONDITION
		PostCondition addPost = LogicalSpecificationFactory.eINSTANCE.createPostCondition();
		FOLSpecification addPostSpec = LogicalSpecificationFactory.eINSTANCE.createFOLSpecification();
		addPostSpec.setName("AddNodePostcondition");
		AndOperator addPostAnd = LogicalSpecificationFactory.eINSTANCE.createAndOperator();		
		
		ExistsOperator addPostAndExists = LogicalSpecificationFactory.eINSTANCE.createExistsOperator();
		addPostAndExists.setElement(n);
		addPostAndExists.setCollection(allNodes);
		addPostAnd.getArguments().add(addPostAndExists);
		
		ForAllOperator addPostAndForallNeighs = LogicalSpecificationFactory.eINSTANCE.createForAllOperator();
		addPostAndForallNeighs.setCollection(neighs);
		AndOperator addPostAndForallNeighsAnd = LogicalSpecificationFactory.eINSTANCE.createAndOperator();
		ExistsOperator addPostAndForallNeighsAndExistsAllNodes = LogicalSpecificationFactory.eINSTANCE.createExistsOperator();
		addPostAndForallNeighsAndExistsAllNodes.setCollection(allNodes);
		addPostAndForallNeighsAnd.getArguments().add(addPostAndForallNeighsAndExistsAllNodes);
		ExistsOperator addPostAndForallNeighsAndExistsNeighs = LogicalSpecificationFactory.eINSTANCE.createExistsOperator();
		addPostAndForallNeighsAndExistsNeighs.setCollection(n_neighs);
		addPostAndForallNeighsAnd.getArguments().add(addPostAndForallNeighsAndExistsNeighs);
		addPostAndForallNeighs.setArgument(addPostAndForallNeighsAnd);
		addPostAnd.getArguments().add(addPostAndForallNeighs);
		
		ForAllOperator addPostAndForallDeplComps = LogicalSpecificationFactory.eINSTANCE.createForAllOperator();
		addPostAndForallDeplComps.setCollection(deplComps);
		AndOperator addPostAndForallDeplCompsAnd = LogicalSpecificationFactory.eINSTANCE.createAndOperator();
		ExistsOperator addPostAndForallDeplCompsAndExistsAllNodes = LogicalSpecificationFactory.eINSTANCE.createExistsOperator();
		addPostAndForallDeplCompsAndExistsAllNodes.setCollection(allComps);
		addPostAndForallDeplCompsAnd.getArguments().add(addPostAndForallDeplCompsAndExistsAllNodes);
		ExistsOperator addPostAndForallDeplCompsAndExistsNeighs = LogicalSpecificationFactory.eINSTANCE.createExistsOperator();
		addPostAndForallDeplCompsAndExistsNeighs.setCollection(n_deplComps);
		addPostAndForallDeplCompsAnd.getArguments().add(addPostAndForallDeplCompsAndExistsNeighs);
		addPostAndForallDeplComps.setArgument(addPostAndForallDeplCompsAnd);
		addPostAnd.getArguments().add(addPostAndForallDeplComps);
		
		addPostSpec.setRootOperator(addPostAnd);
		addPost.setConditionFormula(addPostSpec);
		add.setPost(addPost);
		
		ref1.getActions().add(add);
		root.getRefactorings().add(ref1);
		
				
		
		System.out.println("-------- OPERATOR CONTAINMENTS TESTING --------");
		
		System.out.println("- Is addPreAnd contained within addPreAndNot? " + Manager.guarantees(addPreAnd, addPreAndNot));
		System.out.println("- Is addPreAnd contained within addPreAndNotExists? " + Manager.guarantees(addPreAnd, addPreAndNotExists));
		System.out.println("- Is addPreAnd contained within addPreAndForall? " + Manager.guarantees(addPreAnd, addPreAndForall));
		System.out.println("- Is addPreAnd contained within addPreAndForallExists? " + Manager.guarantees(addPreAnd, addPreAndForallExists));
		System.out.println("- Is addPreAnd contained within addPostAnd? " + Manager.guarantees(addPreAnd, addPostAnd));
		System.out.println("- Is addPreAnd contained within addPostAndExists? " + Manager.guarantees(addPreAnd, addPostAndExists));
		System.out.println("- Is addPreAnd contained within addPostAndForallNeighs? " + Manager.guarantees(addPreAnd, addPostAndForallNeighs));
		System.out.println("- Is addPreAnd contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPreAnd, addPostAndForallNeighsAnd));
		System.out.println("- Is addPreAnd contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPreAnd, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPreAnd contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPreAnd, addPostAndForallNeighsAndExistsNeighs));
		
		System.out.println("- Is addPreAndNot contained within addPreAndNot? " + Manager.guarantees(addPreAndNot, addPreAndNot));
		System.out.println("- Is addPreAndNot contained within addPreAndNotExists? " + Manager.guarantees(addPreAndNot, addPreAndNotExists));
		System.out.println("- Is addPreAndNot contained within addPreAndForall? " + Manager.guarantees(addPreAndNot, addPreAndForall));
		System.out.println("- Is addPreAndNot contained within addPreAndForallExists? " + Manager.guarantees(addPreAndNot, addPreAndForallExists));
		System.out.println("- Is addPreAndNot contained within addPostAnd? " + Manager.guarantees(addPreAndNot, addPostAnd));
		System.out.println("- Is addPreAndNot contained within addPostAndExists? " + Manager.guarantees(addPreAndNot, addPostAndExists));
		System.out.println("- Is addPreAndNot contained within addPostAndForallNeighs? " + Manager.guarantees(addPreAndNot, addPostAndForallNeighs));
		System.out.println("- Is addPreAndNot contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPreAndNot, addPostAndForallNeighsAnd));
		System.out.println("- Is addPreAndNot contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPreAndNot, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPreAndNot contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPreAndNot, addPostAndForallNeighsAndExistsNeighs));
		
		System.out.println("- Is addPreAndNotExists contained within addPreAndNot? " + Manager.guarantees(addPreAndNotExists, addPreAndNot));
		System.out.println("- Is addPreAndNotExists contained within addPreAndNotExists? " + Manager.guarantees(addPreAndNotExists, addPreAndNotExists));
		System.out.println("- Is addPreAndNotExists contained within addPreAndForall? " + Manager.guarantees(addPreAndNotExists, addPreAndForall));
		System.out.println("- Is addPreAndNotExists contained within addPreAndForallExists? " + Manager.guarantees(addPreAndNotExists, addPreAndForallExists));
		System.out.println("- Is addPreAndNotExists contained within addPostAnd? " + Manager.guarantees(addPreAndNotExists, addPostAnd));
		System.out.println("- Is addPreAndNotExists contained within addPostAndExists? " + Manager.guarantees(addPreAndNotExists, addPostAndExists));
		System.out.println("- Is addPreAndNotExists contained within addPostAndForallNeighs? " + Manager.guarantees(addPreAndNotExists, addPostAndForallNeighs));
		System.out.println("- Is addPreAndNotExists contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPreAndNotExists, addPostAndForallNeighsAnd));
		System.out.println("- Is addPreAndNotExists contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPreAndNotExists, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPreAndNotExists contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPreAndNotExists, addPostAndForallNeighsAndExistsNeighs));
		
		System.out.println("- Is addPreAndForall contained within addPreAndNot? " + Manager.guarantees(addPreAndForall, addPreAndNot));
		System.out.println("- Is addPreAndForall contained within addPreAndNotExists? " + Manager.guarantees(addPreAndForall, addPreAndNotExists));
		System.out.println("- Is addPreAndForall contained within addPreAndForall? " + Manager.guarantees(addPreAndForall, addPreAndForall));
		System.out.println("- Is addPreAndForall contained within addPreAndForallExists? " + Manager.guarantees(addPreAndForall, addPreAndForallExists));
		System.out.println("- Is addPreAndForall contained within addPostAnd? " + Manager.guarantees(addPreAndForall, addPostAnd));
		System.out.println("- Is addPreAndForall contained within addPostAndExists? " + Manager.guarantees(addPreAndForall, addPostAndExists));
		System.out.println("- Is addPreAndForall contained within addPostAndForallNeighs? " + Manager.guarantees(addPreAndForall, addPostAndForallNeighs));
		System.out.println("- Is addPreAndForall contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPreAndForall, addPostAndForallNeighsAnd));
		System.out.println("- Is addPreAndForall contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPreAndForall, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPreAndForall contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPreAndForall, addPostAndForallNeighsAndExistsNeighs));
		
		System.out.println("- Is addPreAndForallExists contained within addPreAndNot? " + Manager.guarantees(addPreAndForallExists, addPreAndNot));
		System.out.println("- Is addPreAndForallExists contained within addPreAndNotExists? " + Manager.guarantees(addPreAndForallExists, addPreAndNotExists));
		System.out.println("- Is addPreAndForallExists contained within addPreAndForall? " + Manager.guarantees(addPreAndForallExists, addPreAndForall));
		System.out.println("- Is addPreAndForallExists contained within addPreAndForallExists? " + Manager.guarantees(addPreAndForallExists, addPreAndForallExists));
		System.out.println("- Is addPreAndForallExists contained within addPostAnd? " + Manager.guarantees(addPreAndForallExists, addPostAnd));
		System.out.println("- Is addPreAndForallExists contained within addPostAndExists? " + Manager.guarantees(addPreAndForallExists, addPostAndExists));
		System.out.println("- Is addPreAndForallExists contained within addPostAndForallNeighs? " + Manager.guarantees(addPreAndForallExists, addPostAndForallNeighs));
		System.out.println("- Is addPreAndForallExists contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPreAndForallExists, addPostAndForallNeighsAnd));
		System.out.println("- Is addPreAndForallExists contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPreAndForallExists, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPreAndForallExists contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPreAndForallExists, addPostAndForallNeighsAndExistsNeighs));		
		
		System.out.println("- Is addPostAnd contained within addPreAndNot? " + Manager.guarantees(addPostAnd, addPreAndNot));
		System.out.println("- Is addPostAnd contained within addPreAndNotExists? " + Manager.guarantees(addPostAnd, addPreAndNotExists));
		System.out.println("- Is addPostAnd contained within addPreAndForall? " + Manager.guarantees(addPostAnd, addPreAndForall));
		System.out.println("- Is addPostAnd contained within addPreAndForallExists? " + Manager.guarantees(addPostAnd, addPreAndForallExists));
		System.out.println("- Is addPostAnd contained within addPostAnd? " + Manager.guarantees(addPostAnd, addPostAnd));
		System.out.println("- Is addPostAnd contained within addPostAndExists? " + Manager.guarantees(addPostAnd, addPostAndExists));
		System.out.println("- Is addPostAnd contained within addPostAndForallNeighs? " + Manager.guarantees(addPostAnd, addPostAndForallNeighs));
		System.out.println("- Is addPostAnd contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPostAnd, addPostAndForallNeighsAnd));
		System.out.println("- Is addPostAnd contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPostAnd, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPostAnd contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPostAnd, addPostAndForallNeighsAndExistsNeighs));
		
		System.out.println("- Is addPostAndExists contained within addPreAndNot? " + Manager.guarantees(addPostAndExists, addPreAndNot));
		System.out.println("- Is addPostAndExists contained within addPreAndNotExists? " + Manager.guarantees(addPostAndExists, addPreAndNotExists));
		System.out.println("- Is addPostAndExists contained within addPreAndForall? " + Manager.guarantees(addPostAndExists, addPreAndForall));
		System.out.println("- Is addPostAndExists contained within addPreAndForallExists? " + Manager.guarantees(addPostAndExists, addPreAndForallExists));
		System.out.println("- Is addPostAndExists contained within addPostAnd? " + Manager.guarantees(addPostAndExists, addPostAnd));
		System.out.println("- Is addPostAndExists contained within addPostAndExists? " + Manager.guarantees(addPostAndExists, addPostAndExists));
		System.out.println("- Is addPostAndExists contained within addPostAndForallNeighs? " + Manager.guarantees(addPostAndExists, addPostAndForallNeighs));
		System.out.println("- Is addPostAndExists contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPostAndExists, addPostAndForallNeighsAnd));
		System.out.println("- Is addPostAndExists contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPostAndExists, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPostAndExists contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPostAndExists, addPostAndForallNeighsAndExistsNeighs));
		
		System.out.println("- Is addPostAndForallNeighs contained within addPreAndNot? " + Manager.guarantees(addPostAndForallNeighs, addPreAndNot));
		System.out.println("- Is addPostAndForallNeighs contained within addPreAndNotExists? " + Manager.guarantees(addPostAndForallNeighs, addPreAndNotExists));
		System.out.println("- Is addPostAndForallNeighs contained within addPreAndForall? " + Manager.guarantees(addPostAndForallNeighs, addPreAndForall));
		System.out.println("- Is addPostAndForallNeighs contained within addPreAndForallExists? " + Manager.guarantees(addPostAndForallNeighs, addPreAndForallExists));
		System.out.println("- Is addPostAndForallNeighs contained within addPostAnd? " + Manager.guarantees(addPostAndForallNeighs, addPostAnd));
		System.out.println("- Is addPostAndForallNeighs contained within addPostAndExists? " + Manager.guarantees(addPostAndForallNeighs, addPostAndExists));
		System.out.println("- Is addPostAndForallNeighs contained within addPostAndForallNeighs? " + Manager.guarantees(addPostAndForallNeighs, addPostAndForallNeighs));
		System.out.println("- Is addPostAndForallNeighs contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPostAndForallNeighs, addPostAndForallNeighsAnd));
		System.out.println("- Is addPostAndForallNeighs contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPostAndForallNeighs, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPostAndForallNeighs contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPostAndForallNeighs, addPostAndForallNeighsAndExistsNeighs));
		
		System.out.println("- Is addPostAndForallNeighsAnd contained within addPreAndNot? " + Manager.guarantees(addPostAndForallNeighsAnd, addPreAndNot));
		System.out.println("- Is addPostAndForallNeighsAnd contained within addPreAndNotExists? " + Manager.guarantees(addPostAndForallNeighsAnd, addPreAndNotExists));
		System.out.println("- Is addPostAndForallNeighsAnd contained within addPreAndForall? " + Manager.guarantees(addPostAndForallNeighsAnd, addPreAndForall));
		System.out.println("- Is addPostAndForallNeighsAnd contained within addPreAndForallExists? " + Manager.guarantees(addPostAndForallNeighsAnd, addPreAndForallExists));
		System.out.println("- Is addPostAndForallNeighsAnd contained within addPostAnd? " + Manager.guarantees(addPostAndForallNeighsAnd, addPostAnd));
		System.out.println("- Is addPostAndForallNeighsAnd contained within addPostAndExists? " + Manager.guarantees(addPostAndForallNeighsAnd, addPostAndExists));
		System.out.println("- Is addPostAndForallNeighsAnd contained within addPostAndForallNeighs? " + Manager.guarantees(addPostAndForallNeighsAnd, addPostAndForallNeighs));
		System.out.println("- Is addPostAndForallNeighsAnd contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPostAndForallNeighsAnd, addPostAndForallNeighsAnd));
		System.out.println("- Is addPostAndForallNeighsAnd contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPostAndForallNeighsAnd, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPostAndForallNeighsAnd contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPostAndForallNeighsAnd, addPostAndForallNeighsAndExistsNeighs));
		
		System.out.println("- Is addPostAndForallNeighsAndExistsAllNodes contained within addPreAndNot? " + Manager.guarantees(addPostAndForallNeighsAndExistsAllNodes, addPreAndNot));
		System.out.println("- Is addPostAndForallNeighsAndExistsAllNodes contained within addPreAndNotExists? " + Manager.guarantees(addPostAndForallNeighsAndExistsAllNodes, addPreAndNotExists));
		System.out.println("- Is addPostAndForallNeighsAndExistsAllNodes contained within addPreAndForall? " + Manager.guarantees(addPostAndForallNeighsAndExistsAllNodes, addPreAndForall));
		System.out.println("- Is addPostAndForallNeighsAndExistsAllNodes contained within addPreAndForallExists? " + Manager.guarantees(addPostAndForallNeighsAndExistsAllNodes, addPreAndForallExists));
		System.out.println("- Is addPostAndForallNeighsAndExistsAllNodes contained within addPostAnd? " + Manager.guarantees(addPostAndForallNeighsAndExistsAllNodes, addPostAnd));
		System.out.println("- Is addPostAndForallNeighsAndExistsAllNodes contained within addPostAndExists? " + Manager.guarantees(addPostAndForallNeighsAndExistsAllNodes, addPostAndExists));
		System.out.println("- Is addPostAndForallNeighsAndExistsAllNodes contained within addPostAndForallNeighs? " + Manager.guarantees(addPostAndForallNeighsAndExistsAllNodes, addPostAndForallNeighs));
		System.out.println("- Is addPostAndForallNeighsAndExistsAllNodes contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPostAndForallNeighsAndExistsAllNodes, addPostAndForallNeighsAnd));
		System.out.println("- Is addPostAndForallNeighsAndExistsAllNodes contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPostAndForallNeighsAndExistsAllNodes, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPostAndForallNeighsAndExistsAllNodes contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPostAndForallNeighsAndExistsAllNodes, addPostAndForallNeighsAndExistsNeighs));
		
		System.out.println("- Is addPostAndForallNeighsAndExistsNeighs contained within addPreAndNot? " + Manager.guarantees(addPostAndForallNeighsAndExistsNeighs, addPreAndNot));
		System.out.println("- Is addPostAndForallNeighsAndExistsNeighs contained within addPreAndNotExists? " + Manager.guarantees(addPostAndForallNeighsAndExistsNeighs, addPreAndNotExists));
		System.out.println("- Is addPostAndForallNeighsAndExistsNeighs contained within addPreAndForall? " + Manager.guarantees(addPostAndForallNeighsAndExistsNeighs, addPreAndForall));
		System.out.println("- Is addPostAndForallNeighsAndExistsNeighs contained within addPreAndForallExists? " + Manager.guarantees(addPostAndForallNeighsAndExistsNeighs, addPreAndForallExists));
		System.out.println("- Is addPostAndForallNeighsAndExistsNeighs contained within addPostAnd? " + Manager.guarantees(addPostAndForallNeighsAndExistsNeighs, addPostAnd));
		System.out.println("- Is addPostAndForallNeighsAndExistsNeighs contained within addPostAndExists? " + Manager.guarantees(addPostAndForallNeighsAndExistsNeighs, addPostAndExists));
		System.out.println("- Is addPostAndForallNeighsAndExistsNeighs contained within addPostAndForallNeighs? " + Manager.guarantees(addPostAndForallNeighsAndExistsNeighs, addPostAndForallNeighs));
		System.out.println("- Is addPostAndForallNeighsAndExistsNeighs contained within addPostAndForallNeighsAnd? " + Manager.guarantees(addPostAndForallNeighsAndExistsNeighs, addPostAndForallNeighsAnd));
		System.out.println("- Is addPostAndForallNeighsAndExistsNeighs contained within addPostAndForallNeighsAndExistsAllNodes? " + Manager.guarantees(addPostAndForallNeighsAndExistsNeighs, addPostAndForallNeighsAndExistsAllNodes));
		System.out.println("- Is addPostAndForallNeighsAndExistsNeighs contained within addPostAndForallNeighsAndExistsNeighs? " + Manager.guarantees(addPostAndForallNeighsAndExistsNeighs, addPostAndForallNeighsAndExistsNeighs));
				
		System.out.println("-----------------------------------------------");
		
		
		
		// TRIVIAL PRE CONDITION ATTEMPT
		
		SingleValuedParameter lhs = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String queryLhs = "4";
		lhs.setResolvingExpr(queryLhs);
		
		SingleValuedParameter rhs = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String queryRhs = "3";
		rhs.setResolvingExpr(queryRhs);
		
		
		PreCondition addPre2 = LogicalSpecificationFactory.eINSTANCE.createPreCondition();
		FOLSpecification addPreSpec2 = LogicalSpecificationFactory.eINSTANCE.createFOLSpecification();
		addPreSpec.setName("AddNodePrecondition");
		AndOperator addPreAnd2 = LogicalSpecificationFactory.eINSTANCE.createAndOperator();
		
		GreaterOperator addPreAndGreater2 = LogicalSpecificationFactory.eINSTANCE.createGreaterOperator();
		addPreAndGreater2.setLhs(lhs);
		addPreAndGreater2.setRhs(rhs);
		
		addPreAnd2.getArguments().add(addPreAndGreater2);		
		addPreSpec2.setRootOperator(addPreAnd2);
		addPre2.setConditionFormula(addPreSpec2);
		
		System.out.println("-------- TRIVIAL PRE CONDITION ATTEMPT --------");
		final String modelUri = "src/main/resources/models/UML/ECS_PPAP_extension.uml";
		Manager manager = Manager.getInstance();
		manager.init(modelUri);
		
		System.out.println(" = " + Manager.evaluateFOL(addPre2.getConditionFormula()));
		System.out.println("-----------------------------------------------");
		
		//////////////////////////////////
		
		
		System.out.println("-------- REFACTORINGS POST CONDITIONS TESTING --------");
		for(Refactoring r : root.getRefactorings()) {
			System.out.println("\t---- REFACTORING " + r.getName() + " ----");
			PreCondition rPre = Manager.calculatePreCondition(r);
			System.out.println("\t\tPRE CONDITION: " + rPre.toString());
			PostCondition rPost = Manager.calculatePostCondition(r);
			System.out.println("\t\tPOST CONDITION: " + rPost.toString());
			PostCondition reducedRPost = Manager.reducePostCondition(rPost);
			System.out.println("\t\tREDUCED POST CONDITION: " + reducedRPost.toString());
		}
		System.out.println("-----------------------------------------------");
		
		
		
		
		//final String modelUri = "src/main/resources/models/UML/ECS_PPAP_extension.uml";
		//Manager manager = Manager.getInstance();
		//manager.init(modelUri);
		Model model = Manager.getInstance().getModel();
//		String query = "self.allOwnedElements()->select(el|el.oclIsTypeOf(Component))";
//		Package contextualElement = model.getNestedPackages().get(0);
//		System.out.println(evaluateOCL(query, contextualElement).toString());
//		System.out.println(Manager.evaluateOCL(query, contextualElement).toString());
		//String queryNeighs = "Node.allInstances()->select(n | n.name = 'BooksDispatcher Node' or n.name = 'MoviesDispatcher Node')";
		////neighs.setResolvingExpr(Manager.getOCLQuery(queryNeighs));
		neighs.setResolvingExpr(queryNeighs);
		//String queryAllNodes = "Node.allInstances()";
		////allNodes.setResolvingExpr(Manager.getOCLQuery(queryAllNodes));
		allNodes.setResolvingExpr(queryAllNodes);
//		System.out.println(Manager.evaluateOCL(query, model).toString());
		
		
		//String queryN = "Node.allInstances()->select(n | n.name = 'Pippo Node')";
		HashSet<Object> hashSetQueryN =  (HashSet<Object>) OclManager.evaluateOCL(queryN, Manager.getInstance().getModel());
		List<Object> resQueryN = new ArrayList<Object>();
		for(Object object : hashSetQueryN) {
			if(object instanceof Model)
				resQueryN.add((Model) object);
	        else if(object instanceof Package)
	        	resQueryN.add((Package) object);
	        else if(object instanceof Component)
	        	resQueryN.add((Component) object);
	        else if(object instanceof Operation)
	        	resQueryN.add((Operation) object);
	        else if(object instanceof Node)
	        	resQueryN.add((Node) object);
		}

		
		if(resQueryN.size()!=0)
			System.out.println(" = " + Manager.evaluateOperator(addPreAnd, resQueryN.get(0)));
		else
			System.out.println(" = " + Manager.evaluateOperator(addPreAnd));
		
		
		
		//////// AP SPECIFICATIONS
		Antipattern blob = LogicalSpecificationFactory.eINSTANCE.createAntipattern();
		
		////////////// PARAMS
		List<Parameter> blobParams = new ArrayList<>();
		
		SingleValuedParameter F_numClientConnects = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String F_numClientConnectsQuery = "self.allUsedInterfaces()->size()";
		F_numClientConnects.setResolvingExpr(F_numClientConnectsQuery);		
		blobParams.add(F_numClientConnects);
		
		SingleValuedParameter F_numSupplierConnects = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String F_numSupplierConnectsQuery = "self.allRealizedInterfaces()->size()";
		F_numSupplierConnects.setResolvingExpr(F_numSupplierConnectsQuery);
		blobParams.add(F_numSupplierConnects);
		
		SingleValuedParameter F_numSentMsgs = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String F_numSentMsgsQuery = "Lifeline.allInstances()->select(ll|ll.represents.type=self)->collect(coveredBy)->select(el|el.oclIsTypeOf(MessageOccurrenceSpecification) and Message.allInstances()->exists(m|m.sendEvent=el))->size()";
		F_numSentMsgs.setResolvingExpr(F_numSentMsgsQuery);
		blobParams.add(F_numSentMsgs);
		
		SingleValuedParameter F_numReceivedMsgs = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String F_numReceivedMsgsQuery = "Lifeline.allInstances()->select(ll|ll.represents.type=self)->collect(coveredBy)->select(el|el.oclIsTypeOf(MessageOccurrenceSpecification) and Message.allInstances()->exists(m|m.receiveEvent=el))->size()";
		F_numReceivedMsgs.setResolvingExpr(F_numReceivedMsgsQuery);
		blobParams.add(F_numReceivedMsgs);
		
		SingleValuedParameter F_hwUtil = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String F_hwUtilQuery = "????????????????????????";
		F_hwUtil.setResolvingExpr(F_hwUtilQuery);
		blobParams.add(F_hwUtil);
		
		SingleValuedParameter F_netUtil = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String F_netUtilQuery = "????????????????????????";
		F_netUtil.setResolvingExpr(F_netUtilQuery);
		blobParams.add(F_netUtil);
		
		SingleValuedParameter Th_maxNumConnects = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String Th_maxNumConnectsQuery = "3";
		Th_maxNumConnects.setResolvingExpr(Th_maxNumConnectsQuery);
		blobParams.add(Th_maxNumConnects);
		
		SingleValuedParameter Th_maxNumMsgs = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String Th_maxNumMsgsQuery = "5";
		Th_maxNumMsgs.setResolvingExpr(Th_maxNumMsgsQuery);
		blobParams.add(Th_maxNumMsgs);
		
		SingleValuedParameter Th_maxHwUtil = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String Th_maxHwUtilQuery = "0.8";
		Th_maxHwUtil.setResolvingExpr(Th_maxHwUtilQuery);
		blobParams.add(Th_maxHwUtil);
		
		SingleValuedParameter Th_minNetUtil = LogicalSpecificationFactory.eINSTANCE.createSingleValuedParameter();
		String Th_minNetUtilQuery = "0.5";
		Th_minNetUtil.setResolvingExpr(Th_minNetUtilQuery);
		blobParams.add(Th_minNetUtil);
		
		
		////////////// COMPLETE FORMULA
		FOLSpecification blobCF = LogicalSpecificationFactory.eINSTANCE.createFOLSpecification();
		blobCF.setName("BlobCompleteFormula");
		
		AndOperator blobCFAnd = LogicalSpecificationFactory.eINSTANCE.createAndOperator();
		
		OrOperator blobCFAndOr1 = LogicalSpecificationFactory.eINSTANCE.createOrOperator();
		GreaterEqualOperator blobCFAndOr1Geq1 = LogicalSpecificationFactory.eINSTANCE.createGreaterEqualOperator();
		blobCFAndOr1Geq1.setLhs(F_numClientConnects);
		blobCFAndOr1Geq1.setRhs(Th_maxNumConnects);
		GreaterEqualOperator blobCFAndOr1Geq2 = LogicalSpecificationFactory.eINSTANCE.createGreaterEqualOperator();
		blobCFAndOr1Geq2.setLhs(F_numSupplierConnects);
		blobCFAndOr1Geq2.setRhs(Th_maxNumConnects);
		blobCFAndOr1.getArguments().add(blobCFAndOr1Geq1);
		blobCFAndOr1.getArguments().add(blobCFAndOr1Geq2);
		
		OrOperator blobCFAndOr2 = LogicalSpecificationFactory.eINSTANCE.createOrOperator();
		GreaterEqualOperator blobCFAndOr2Geq1 = LogicalSpecificationFactory.eINSTANCE.createGreaterEqualOperator();
		blobCFAndOr2Geq1.setLhs(F_numSentMsgs);
		blobCFAndOr2Geq1.setRhs(Th_maxNumMsgs);
		GreaterEqualOperator blobCFAndOr2Geq2 = LogicalSpecificationFactory.eINSTANCE.createGreaterEqualOperator();
		blobCFAndOr2Geq2.setLhs(F_numReceivedMsgs);
		blobCFAndOr2Geq2.setRhs(Th_maxNumMsgs);
		blobCFAndOr2.getArguments().add(blobCFAndOr2Geq1);
		blobCFAndOr2.getArguments().add(blobCFAndOr2Geq2);
		
		OrOperator blobCFAndOr3 = LogicalSpecificationFactory.eINSTANCE.createOrOperator();
		GreaterEqualOperator blobCFAndOr3Geq1 = LogicalSpecificationFactory.eINSTANCE.createGreaterEqualOperator();
		blobCFAndOr3Geq1.setLhs(F_hwUtil);
		blobCFAndOr3Geq1.setRhs(Th_maxHwUtil);
		LessEqualOperator blobCFAndOr3Geq2 = LogicalSpecificationFactory.eINSTANCE.createLessEqualOperator();
		blobCFAndOr3Geq2.setLhs(F_netUtil);
		blobCFAndOr3Geq2.setRhs(Th_minNetUtil);
		blobCFAndOr3.getArguments().add(blobCFAndOr3Geq1);
		blobCFAndOr3.getArguments().add(blobCFAndOr3Geq2);
		
		blobCFAnd.getArguments().add(blobCFAndOr1);
		blobCFAnd.getArguments().add(blobCFAndOr2);
		//blobCFAnd.getArguments().add(blobCFAndOr3);
		blobCF.setRootOperator(blobCFAnd);
		blob.setCompleteFormula(blobCF);
		
		
		
		
		List<Element> components = Manager.getInstance().getModel().getNestedPackages().get(0).getNestedPackages().get(0).allOwnedElements();
		System.out.println("\n------------------------------------------ BLOB ANTIPATTERN DETECTION ------------------------------------------");

		for(Element c : components) {			
			if(c instanceof Component) {
				System.out.print("\n- Is " + ((Component) c).getName() + " a Blob? ---> ");
				System.out.println(" = " + Manager.evaluateOperator((AndOperator) blobCF.getRootOperator(), (Component) c));
			}
			
		}
		
		System.out.println("\n----------------------------------------------------------------------------------------------------------------");
		
	}

}

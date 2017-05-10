package metaheuristic.evolutionary;

import java.rmi.UnexpectedException;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.RandomUtils;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.ecore.OCL;
import org.eclipse.papyrus.MARTE.MARTEFactory;
import org.eclipse.papyrus.MARTE.MARTEPackage;
import org.eclipse.papyrus.MARTE.MARTE_AnalysisModel.GQAM.GQAMFactory;
import org.eclipse.papyrus.MARTE.MARTE_AnalysisModel.GQAM.GQAMPackage;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.profile.standard.StandardPackage;
import org.eclipse.uml2.uml.resource.UMLResource;

import logicalSpecification.Action;
import logicalSpecification.LogicalSpecificationFactory;
import logicalSpecification.Refactoring;
import metaheuristic.managers.Manager;
import metaheuristic.managers.uml.UMLManager;

public class RSequence {

	private Refactoring refactoring;

	private Resource modelRefactoredResource;

	public RSequence() {
//		this.refactoring = LogicalSpecificationFactory.eINSTANCE.createRefactoring();
//		this.refactoring.setName(Integer.toString(RandomUtils.nextInt(0, 100)));
		this.refactoring = Manager.createRefactoring();
	}

	public RSequence(int length, int number_of_actions, int allowed_failures)
			throws ParserException, UnexpectedException {

		this.refactoring = LogicalSpecificationFactory.eINSTANCE.createRefactoring();

		assert (this.refactoring.getActions().size() == 0);
		int num_failures = 0;

		while (this.refactoring.getActions().size() < length) {
			if (!this.tryRandomPush(number_of_actions))
				num_failures++;

			if (num_failures >= allowed_failures) {
				// START OVER
				num_failures = 0;
				this.refactoring = null;
//				this.refactoring = LogicalSpecificationFactory.eINSTANCE.createRefactoring();
				this.refactoring = Manager.createRefactoring();
				assert (this.refactoring.getActions().size() == 0);
			}
			// throw new RuntimeException("\nUnable to generate sequence due to
			// preconditions");
		}
		if (this.refactoring.getActions().size() != length) {
			throw new RuntimeException("Unable to fill initial population");
		}

	}

	private boolean tryRandomPush(int n) throws UnexpectedException, ParserException {

		Refactoring temporary_ref = LogicalSpecificationFactory.eINSTANCE.createRefactoring();
		// TODO clone di un refactoring
		temporary_ref = (Refactoring) EcoreUtil.copy(this.refactoring);
		assert (temporary_ref.equals(this.refactoring));

		Action candidate = UMLManager.getUMLRandomAction(n);

		assert (candidate != null);

		temporary_ref.getActions().add(candidate);
		assert (Manager.evaluateFOL(candidate.getPre().getConditionFormula()));
		assert (Manager.evaluateFOL(candidate.getPost().getConditionFormula()));

		if (this.isFeasible(temporary_ref)) {
			// if(Manager.evaluateFOL(Manager.calculatePreCondition(temporary_ref).getConditionFormula())){
			this.insert((Action) EcoreUtil.copy(candidate));
			// System.out.println(candidate.cloneAction().toString());
			temporary_ref = null;
			return true;
		} else {
			candidate = null;
			temporary_ref = null;
			return false;
		}

	}

	private boolean isFeasible(Refactoring tr) throws ParserException {
		boolean fol = Manager.evaluateFOL(Manager.calculatePreCondition(tr).getConditionFormula());
		// System.out.println(" = " + fol);
		return fol;
	}

	public RSequence(RSequence variable) {
		this.refactoring = LogicalSpecificationFactory.eINSTANCE.createRefactoring();
		this.refactoring = (Refactoring) EcoreUtil.copy(variable.getRefactoring());
		this.refactoring.setName(Integer.toString(RandomUtils.nextInt(0, 100)));
		// this.refactoring = variable.getRefactoring().cloneRefactoring();
		assert (this.refactoring.equals(variable.getRefactoring()));

	}

	public Refactoring getRefactoring() {
		return this.refactoring;
	}

	public void setRefactorings(Refactoring r) {
		this.refactoring = r;
	}

	public void insert(Action a) {
		this.refactoring.getActions().add(a);
	}

	public void insert(int i, Action a) {
		this.refactoring.getActions().add(i, a);

	}

	public void replace(int i, Action a) {

		this.refactoring.getActions().remove(i);
		this.refactoring.getActions().add(i, a);

	}

	public Action get(int index) {
		return this.getRefactoring().getActions().get(index);
	}

	public int getLength() {
		return this.getRefactoring().getActions().size();
	}

	public boolean isFeasible() {
		throw new NotImplementedException("TODO");
	}

	public boolean alter(int position, int n) throws UnexpectedException, ParserException {

		Refactoring temporary_ref = LogicalSpecificationFactory.eINSTANCE.createRefactoring();
		temporary_ref = (Refactoring) EcoreUtil.copy(this.refactoring);
		assert (temporary_ref.equals(this.refactoring));

		// Action candidate = Manager.getTautologyRandomAction(n);
		Action candidate = UMLManager.getUMLRandomAction(n);
		assert (candidate != null);
		assert (Manager.evaluateFOL(candidate.getPre().getConditionFormula()));
		assert (Manager.evaluateFOL(candidate.getPost().getConditionFormula()));

		temporary_ref.getActions().add(position, candidate);
		if (this.isFeasible(temporary_ref)) {
			// if(Manager.evaluateFOL(Manager.calculatePreCondition(temporary_ref).getConditionFormula())){
			this.replace(position, (Action) EcoreUtil.copy(candidate));
			// this.replace(position, candidate.cloneAction());
			temporary_ref = null;
			return true;
		} else {
			candidate = null;
			temporary_ref = null;
			return false;
		}

	}

	public void print() {
		for (Action el : this.getRefactoring().getActions()) {
			System.out.println(el);

		}
	}

	public Resource getModelRefactoredResource() {
		return modelRefactoredResource;
	}

	public void setModelRefactoredResource(Resource modelRefactoredResource) {
		this.modelRefactoredResource = modelRefactoredResource;
	}

	public void setModelRefactoredResource(String modelURI) {

		this.modelRefactoredResource = UMLManager.getInstance().createModelResource(modelURI, true);
	}
}

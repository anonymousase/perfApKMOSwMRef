package managers;

import java.rmi.UnexpectedException;

import org.junit.Test;

import logicalSpecification.Action;
import metaheuristic.managers.Manager;
import metaheuristic.managers.uml.UMLManager;

public class MangerTest {
	
	@Test
	public void actionToJsonTest(){
		
		Manager.getInstance().init("src/main/resources/models/BGCS/BGCS.uml");
		
		try {
			Action action = UMLManager.getUMLRandomAction(8);
		} catch (UnexpectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

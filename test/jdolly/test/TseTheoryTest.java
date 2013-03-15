package jdolly.test;

import static org.junit.Assert.*;
import jdolly.JDolly;
import jdolly.JDollyImp;

import org.junit.Test;


public class TseTheoryTest {
	
	@Test
	public void testScopeRenameClass() {
		JDolly jdolly = new JDollyImp("alloyTheory/renameclass_final.als",
				2, 3, 3);

		int numberPrograms = TestUtil.countSolutions(jdolly, false);

		assertEquals(15916, numberPrograms);

	}

	@Test
	public void testScopeRenameMethod() {
		JDolly jdolly = new JDollyImp("alloyTheory/renamemethod_final.als",
				2, 3, 3);

		int numberPrograms = TestUtil.countSolutions(jdolly, false);

		assertEquals(11264, numberPrograms);

	}

	@Test
	public void testPushDownMethod() {
		JDolly jdolly = new JDollyImp(
				"alloyTheory/pushdownmethod_final.als", 2, 3, 4);
		jdolly.setExactMaxPackages(true);
		jdolly.setMaxMethodNames(3);
		jdolly.setExactMaxMethodNames(true);

		int numberPrograms = TestUtil.countSolutions(jdolly, false);

		assertEquals(20544, numberPrograms);

	}
	
	
	@Test
	public void testPushDownMethodOptimized() {
		JDolly jdolly = new JDollyImp("alloyTheory/pushdownmethod_final.als",2, 3, 4);
		jdolly.setExactMaxPackages(true);
		jdolly.setMaxMethodNames(3);
		jdolly.setExactMaxMethodNames(true);
		jdolly.setOptimized(true);
		jdolly.setJump(10);
		
		int numberPrograms = TestUtil.countSolutions(jdolly, false);
		
		assertEquals(2055, numberPrograms);
	}
	
	@Test
	public void testPushDownMethodSetMaximumNumberofPrograms() {
		JDolly jdolly = new JDollyImp("alloyTheory/pushdownmethod_final.als",2, 3, 4);
		jdolly.setExactMaxPackages(true);
		jdolly.setMaxMethodNames(3);
		jdolly.setExactMaxMethodNames(true);
		jdolly.setMaximumPrograms(10);
		
		int numberPrograms = TestUtil.countSolutions(jdolly, false);
		
		assertEquals(10, numberPrograms);
	}

	@Test
	public void testPullUpMethod() {
		JDolly jdolly = new JDollyImp("alloyTheory/pullupmethod_final.als",
				2, 3, 4);
		jdolly.setExactMaxPackages(true);
		jdolly.setMaxClassNames(3);
		jdolly.setExactMaxClassNames(true);
		jdolly.setMaxMethodNames(3);
//		jdolly.setExactMaxMethodNames(true);

		int numberPrograms = TestUtil.countSolutions(jdolly, true);

		assertEquals(8937, numberPrograms);

	}

	@Test
	public void testAddParameter() {
		JDolly jdolly = new JDollyImp("alloyTheory/addparameter_final.als",
				2, 3, 3);

		jdolly.setExactMaxPackages(true);
		jdolly.setMaxClassNames(3);
		jdolly.setExactMaxClassNames(true);

		jdolly.setMaxMethodNames(2);
		jdolly.setExactMaxMethodNames(true);

		int numberPrograms = TestUtil.countSolutions(jdolly, false);

		assertEquals(30186, numberPrograms);

	}
	
	@Test
	public void testRenameField() {
		JDolly jdolly = new JDollyImp("alloyTheory/renamefield_final.als",2, 3, 1,2);
		
		int numberPrograms = TestUtil.countSolutions(jdolly, false);
		
		assertEquals(19424, numberPrograms);
		
	}

	@Test
	public void testPushDownField() {
		JDolly jdolly = new JDollyImp("alloyTheory/pushdownfield_final.als",2, 3, 1,2);
		jdolly.setMaxFieldNames(1);
		
		int numberPrograms = TestUtil.countSolutions(jdolly, false);
		
		assertEquals(11936, numberPrograms);
		
	}
	
	@Test
	public void testPullupField() {
		JDolly jdolly = new JDollyImp("alloyTheory/pullupfield_final.als",2, 3, 1,2);
		jdolly.setMaxFieldNames(1);
		
		int numberPrograms = TestUtil.countSolutions(jdolly, true);
		
		assertEquals(10928, numberPrograms);
		
	}
	

	
	@Test
	public void testEncapsulateField() {
		JDolly jdolly = new JDollyImp("alloyTheory/encapsulatefield_final.als",2, 3, 3,1);
		
		int numberPrograms = TestUtil.countSolutions(jdolly, false);
		
		assertEquals(2000, numberPrograms);
		
	}
	
	@Test
	public void testMoveMethod() {
		JDolly jdolly = new JDollyImp("alloyTheory/movemethod_final.als",2, 3, 3,1);
		jdolly.setMaxMethodNames(2);
		
		int numberPrograms = TestUtil.countSolutions(jdolly, false);
		
		assertEquals(22905, numberPrograms);
		
	}

}

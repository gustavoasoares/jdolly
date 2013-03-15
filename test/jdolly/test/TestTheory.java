package jdolly.test;

import static org.junit.Assert.*;
import jdolly.AlloyRunJava;

import org.junit.Test;


public class TestTheory {

	@Test
	public void testRenameClass() {
		long programs = AlloyRunJava.run("alloyTheory/renameclass_final.als");
		assertEquals(15916, programs + 1);
	}
	
	@Test
	public void testRenameMethod() {
		long programs = AlloyRunJava.run("alloyTheory/renamemethod_final.als");
		assertEquals(11263, programs);
	}
	@Test
	public void testRenameField() {
		long programs = AlloyRunJava.run("alloyTheory/renamefield_final.als");
		assertEquals(19424, programs + 1);
	}
	@Test
	public void testPushDownMethod() {
		long programs = AlloyRunJava.run("alloyTheory/pushdownmethod_final.als");
		assertEquals(20544, programs + 1);
	}
	@Test
	public void testPushDownField() {
		long programs = AlloyRunJava.run("alloyTheory/pushdownfield_final.als");
		assertEquals(11936, programs + 1);
	}
	@Test
	public void testPullUpMethod() {
		long programs = AlloyRunJava.run("alloyTheory/pullupmethod_final.als");
		assertEquals(8937, programs + 1);
	}
	@Test
	public void testPullUpField() {
		long programs = AlloyRunJava.run("alloyTheory/pullupfield_final.als");
		assertEquals(10927, programs);
	}
	@Test
	public void testEncapsulateField() {
		long programs = AlloyRunJava.run("alloyTheory/encapsulatefield_final.als");
		assertEquals(2000, programs + 1);
	}
	@Test
	public void testMoveMethod() {
		long programs = AlloyRunJava.run("alloyTheory/movemethod_final.als");
		assertEquals(22905, programs + 1);
	}
	@Test
	public void testAddParameter() {
		long programs = AlloyRunJava.run("alloyTheory/addparameter_final.als");
		assertEquals(30186, programs + 1);
	}

}

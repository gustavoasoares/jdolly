package jdolly.test;

import static org.junit.Assert.*;

import java.util.List;

import jdolly.JDolly;
import jdolly.JDollyImp;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.Test;

public class JDollyTest {

	@Test
	public void test1() {

		JDolly jdolly = new JDollyImp("alloyTheory/renameclass_final.als",
				2, 3, 3);

		if (jdolly.hasNext()) {
			List<CompilationUnit> cus = jdolly.next();

			String expectedPorgram = "[package Package_1;\n"
					+ "import Package_0.*;\n"
					+ "public class ClassId_1 extends ClassId_0 {\n" + "}\n"
					+ ", package Package_0;\n" + "public class ClassId_0 {\n"
					+ "  protected long methodid_1(  long a){\n"
					+ "    return 1;\n" + "  }\n"
					+ "  public long methodid_0(){\n"
					+ "    return new ClassId_0().methodid_1(2);\n" + "  }\n"
					+ "  public long methodid_1(  int a){\n"
					+ "    return 0;\n" + "  }\n" + "}\n" + "]";

			assertEquals(expectedPorgram, cus.toString());
			
		} else
			fail();
	}

	@Test
	public void test2() {

		JDolly jdolly = new JDollyImp(
				"alloyTheory/renamefield_final.als", 2, 3, 1, 2);

		if (jdolly.hasNext()) {
			List<CompilationUnit> cus = jdolly.next();
			System.out.println("program: " + cus);

			String expectedPorgram = "[package Package_1;\n"
					+ "public class ClassId_2 {\n"
					+ "  public int fieldid_0=11;\n" + "}\n"
					+ ", package Package_0;\n"
					+ "public class ClassId_1 extends ClassId_0 {\n" + "}\n"
					+ ", package Package_0;\n" + "public class ClassId_0 {\n"
					+ "  public long methodid_0(){\n"
					+ "    return fieldid_1;\n" + "  }\n" 
					+ "  public int fieldid_1=10;\n"+ "}\n" + "]";
			assertEquals(expectedPorgram, cus.toString());
		} else
			fail();		
	}

}

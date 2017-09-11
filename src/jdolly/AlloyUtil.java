package jdolly;

import edu.mit.csail.sdg.alloy4.SafeList;
import edu.mit.csail.sdg.alloy4compiler.ast.Sig;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class AlloyUtil {

    private AlloyUtil(){}

    /**
    * Clean a relation by replacing " "(empty spaces) and "$" to "" and "_", respectively,
    * and removing the brackets of the beginning and of the end.
    *
    * For example:
    * Input: {javametamodel_nofield_final3/Class$0->javametamodel_nofield_final3/Package$1}
    * will produce the output: javametamodel_nofield_final3/Class_0->javametamodel_nofield_final3/Package_1
    * */
    public static String cleanName(final String relation) {
        final int beginIndex = 1;
        final int endIndex = relation.length() - 1;

        String removeBraces = relation.substring(beginIndex, endIndex);
        String replaceDollar = removeBraces.replace("$", "_");
        String removeSpaces = replaceDollar.replaceAll(" ", "");

        return removeSpaces;
    }

    public static String removeCrap(final String instance) {
        String aux = instance.replaceAll("[^/]*/", "");
        return aux;
    }

    public static List<String> getNames(final String[] types){
        List<String> result = new ArrayList<String>();

        final char empty = ' ';

        final int beginIndex = 1;
        final int firstPosition = 0;

        for (String typeName : types) {
            if (typeName.charAt(firstPosition) == empty)
                typeName = typeName.substring(beginIndex);
            typeName = typeName.replaceAll("javametamodel(.)*/", "");
            result.add(typeName);
        }
        return result;
    }

    public static List<String> extractInstances(final String labels) {
        List<String> result = new ArrayList<String>();

        String instances = cleanName(labels);

        boolean instancesIsNotEmpty = instances.length() > 0;

        if (instancesIsNotEmpty) {
            String[] types = instances.split(",");
            result = getNames(types);
        }
        return result;
    }

    public static Sig.Field getField(final String key, final SafeList<Sig.Field> compUnitFields) {
        Sig.Field result = null;

        for (Sig.Field field : compUnitFields) {
            String strRepresOfField = field.toString();
            if (strRepresOfField.contains(key)) {
                result = field;
                break;
            }
        }
        return result;
    }

    /*aux method to help with debug and understading*/
    public static void print(final Object o) {
        System.out.println("begins here: " + o + "ends here");
    }

    /*aux method to help with debug and understading*/
    public static void printArray(final Object[] o) {
        System.out.println("begins here: " + Arrays.toString(o) + " ends here");
    }

    /**
     * Before clean the relation, they are splitted by "->". Next, the javametamodel naming
     * until the "/" is going to be replaced by "".
     *
     * For example:
     * Input: javametamodel_nofield_final3/Class_0->javametamodel_nofield_final3/Package_1
     * will produce the output: [Class_0, Package_1]
     * */
    public static String[] cleanMetaModelNaming(final String relation) {

        String[] relatCleaned = relation.split("->");

        relatCleaned[0] = relatCleaned[0].replaceAll("javametamodel(.)*/", "");
        relatCleaned[1] = relatCleaned[1].replaceAll("javametamodel(.)*/", "");

        return relatCleaned;
    }

    public static String setVisValue(Map<String, List<String>> visRel, String field) {
        String vis = "";
        if (visRel.containsKey(field))
            vis = visRel.get(field).get(0);
        return vis;
    }

    public static BodyType setBodyType(String bodySig) {
        BodyType result = null;

        if (bodySig.startsWith("MethodInvocation"))
            result = BodyType.METHOD_INVOCATION;
        else if (bodySig.startsWith("FieldInvocation"))
            result = BodyType.FIELD_INVOCATION;
        else if (bodySig.startsWith("ConstructorFieldInvocation"))
            result = BodyType.CONSTRUCTOR_FIELD_INVOCATION;
        else if (bodySig.startsWith("ConstructorMethodInvocation"))
            result = BodyType.CONSTRUCTOR_METHOD_INVOCATION;

        else if (bodySig.startsWith("ClassMethodInvocation"))
            result = BodyType.CLASS_METHOD_INVOCATION;
        else if (bodySig.startsWith("ClassFieldInvocation"))
            result = BodyType.CLASS_FIELD_INVOCATION;

        else if (bodySig.startsWith("LiteralValue"))
            result = BodyType.INT_CONSTANT_VALUE;

        return result;
    }

    public static void addModifierTo(final Modifier modifier, TypeDeclaration classBody) {
        classBody.modifiers().add(modifier);
    }

    public static void addMethodsTo(final List<MethodDeclaration> methods, TypeDeclaration classBody) {
        for (MethodDeclaration methodDeclaration : methods) {
            classBody.bodyDeclarations().add(methodDeclaration);
        }
    }

    public static void addFieldsTo(final List<FieldDeclaration> fields, TypeDeclaration classBody) {
        for (FieldDeclaration fieldDeclaration : fields) {
            classBody.bodyDeclarations().add(fieldDeclaration);
        }
    }

    public static PrimitiveType.Code getPrimitiveType(final String primitiveTypeName) {
        return PrimitiveType.toCode(primitiveTypeName);
    }

    public static Modifier.ModifierKeyword getModifierByName(final String modifierName) {
        return Modifier.ModifierKeyword.toKeyword(modifierName);
    }
}
import java.lang.reflect.*;
import java.lang.annotation.Annotation;

public class Tester {
	public static void main(String[] args) {
		Class cls = null;
		Package pkg = null;

		try {
			cls = Class.forName(args[0]);
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.err.println("Please provide the full name of the class/package as an argument.");
			System.exit(1);
		} catch (ClassNotFoundException ex) {
			if ((pkg = Package.getPackage(args[0])) == null) {
				System.err.printf("Argument [%s] not recognized.\n", args[0]);
				System.exit(1);
			}
		}

		if (pkg == null) {
			System.out.println("Constructors\n---------------------------------------------------------");
			for (Constructor c : cls.getConstructors())
				System.out.println(c.toGenericString());

			System.out.println("Methods\n--------------------------------------------------------------");
			for (Method m : cls.getMethods())
				System.out.println(m.toGenericString());

			System.out.println("Fields\n------------------------------------------------------------------");
			for (Field f : cls.getFields())
				System.out.println(f);
		} else {
			for (Annotation a : pkg.getAnnotations())
				System.out.println(a);
		}
	}
}
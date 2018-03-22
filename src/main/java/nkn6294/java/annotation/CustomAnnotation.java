package nkn6294.java.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
// @Target(ElementType.METHOD)
@Target(ElementType.TYPE)
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomAnnotation {
	int studentAge() default 18;

	String studentName();

	String stuAddress();

	String stuStream() default "CSE";
}

@CustomAnnotation(stuAddress = "", stuStream = "", studentAge = 18, studentName = "")
class Program {
	public static void main(String[] args) throws Exception {

	}
}
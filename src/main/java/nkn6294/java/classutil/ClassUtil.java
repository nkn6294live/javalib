package nkn6294.java.classutil;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unchecked")
public class ClassUtil
{
    public static ClassLoader GetClassLoader(String className) throws ClassNotFoundException {
        return Class.forName(className).getClassLoader();
    }
    @SuppressWarnings("rawtypes")
	public static InputStream GetResourceClass(Class classObject, String fileName) {
        return classObject.getResourceAsStream(fileName);
    }
    
    public static <T> T CreateObject(Class<T> classO) throws InstantiationException, IllegalAccessException {
        return classO.newInstance();
    }

    public static <T> T CreateObject(Class<T> classO, Object... params) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?>[] classs = new Class<?>[params.length];
        for (int index = 0; index < params.length; index++) {
            classs[index] = params[index].getClass();
        }
        return classO.getConstructor(classs).newInstance(params);
    }

	public static <T> T CreateObject(String className, Object... params) throws NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
        Class<?>[] classs = new Class<?>[params.length];
        for (int index = 0; index < params.length; index++) {
            classs[index] = params[index].getClass();
        }
        return (T)(Class<T>)(Class.forName(className)).getConstructor(classs).newInstance(params);
    }
}

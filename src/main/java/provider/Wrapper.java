package provider;

import util.ClassGenerator;
import util.ReflectUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class Wrapper {
    private static final Map<Class<?>, Wrapper> WRAPPER_MAP = new ConcurrentHashMap<Class<?>, Wrapper>();

    public static Wrapper getWrapper(Class<?> c){
        return WRAPPER_MAP.computeIfAbsent(c, k -> makeWrapper(c));
    }

    abstract public Object invokeMethod(Object instance, String mn, Class<?>[] types, Object[] args) throws NoSuchMethodException, InvocationTargetException;


    private static Wrapper makeWrapper(Class<?> c) {
        if (c.isPrimitive())
            throw new IllegalArgumentException("Can not create wrapper for primitive type: " + c);
        String name = c.getName();
        StringBuilder c3 = new StringBuilder("public Object invokeMethod(Object o, String n, Class[] p, Object[] v) throws " + InvocationTargetException.class.getName() + ", "+ NoSuchMethodException.class.getName() + "{ ");
        c3.append(name).append(" w; try{ w = ((").append(name).append(")o); }catch(Throwable e){ throw new IllegalArgumentException(e); }");
        Method[] methods = c.getMethods();
        boolean hasMethod = hasMethods(methods);
        if (hasMethod) {
            c3.append(" try{");
        }
        for (Method m : methods) {
            if (m.getDeclaringClass() == Object.class) //ignore Object's method.
                continue;

            String mn = m.getName();
            c3.append(" if( \"").append(mn).append("\".equals( n ) ");
            int len = m.getParameterTypes().length;
            c3.append(" && ").append(" p.length == ").append(len);

            boolean override = false;
            for (Method m2 : methods) {
                if (m != m2 && m.getName().equals(m2.getName())) {
                    override = true;
                    break;
                }
            }
            if (override) {
                if (len > 0) {
                    for (int l = 0; l < len; l++) {
                        c3.append(" && ").append(" p[").append(l).append("].getName().equals(\"")
                                .append(m.getParameterTypes()[l].getName()).append("\")");
                    }
                }
            }

            c3.append(" ) { ");

            if (m.getReturnType() == Void.TYPE)
                c3.append(" w.").append(mn).append('(').append(args(m.getParameterTypes(), "v")).append(");").append(" return null;");
            else
                c3.append(" return ($w)w.").append(mn).append('(').append(args(m.getParameterTypes(), "v")).append(");");

            c3.append(" }");
        }
        if (hasMethod) {
            c3.append(" } catch(Throwable e) { ");
            c3.append("     throw new java.lang.reflect.InvocationTargetException(e); ");
            c3.append(" }");
        }
        c3.append(" throw new " + NoSuchMethodException.class.getName() + "(\"Not found method \\\"\"+n+\"\\\" in class " + c.getName() + ".\"); }");
        Class<?> proxyClass = ClassGenerator.newInstance().generateClass(c, c3.toString());
        try {
            return (Wrapper) proxyClass.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean hasMethods(Method[] methods) {
        if (methods == null || methods.length == 0) {
            return false;
        }
        for (Method m : methods) {
            if (m.getDeclaringClass() != Object.class) {
                return true;
            }
        }
        return false;
    }

    private static String args(Class<?>[] cs, String name) {
        int len = cs.length;
        if (len == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            if (i > 0)
                sb.append(',');
            sb.append(arg(cs[i], name + "[" + i + "]"));
        }
        return sb.toString();
    }

    private static String arg(Class<?> cl, String name) {
        if (cl.isPrimitive()) {
            if (cl == Boolean.TYPE)
                return "((Boolean)" + name + ").booleanValue()";
            if (cl == Byte.TYPE)
                return "((Byte)" + name + ").byteValue()";
            if (cl == Character.TYPE)
                return "((Character)" + name + ").charValue()";
            if (cl == Double.TYPE)
                return "((Number)" + name + ").doubleValue()";
            if (cl == Float.TYPE)
                return "((Number)" + name + ").floatValue()";
            if (cl == Integer.TYPE)
                return "((Number)" + name + ").intValue()";
            if (cl == Long.TYPE)
                return "((Number)" + name + ").longValue()";
            if (cl == Short.TYPE)
                return "((Number)" + name + ").shortValue()";
            throw new RuntimeException("Unknown primitive type: " + cl.getName());
        }
        return "(" + ReflectUtils.getName(cl) + ")" + name;
    }


}

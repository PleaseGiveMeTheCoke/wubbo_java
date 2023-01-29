package util;

import javassist.*;
import provider.Wrapper;

public class ClassGenerator {
    private ClassPool mPool;
    private CtClass mCtc;
    private ClassGenerator(ClassPool pool) {
        mPool = pool;
    }

    public static ClassGenerator newInstance() {
        return new ClassGenerator(ClassPool.getDefault());
    }
    public Class<?> generateClass(Class<?> c, String invokeMethod){
        if (mCtc != null)
            mCtc.detach();
        String mClassName = c.getName()+"$wubbo";
        mCtc = mPool.makeClass(mClassName);
        try {
            mCtc.setSuperclass(mPool.get(Wrapper.class.getName()));
            mCtc.addMethod(CtNewMethod.make(invokeMethod, mCtc));
            mCtc.addConstructor(CtNewConstructor.defaultConstructor(mCtc));
            return mCtc.toClass(Thread.currentThread().getContextClassLoader(), getClass().getProtectionDomain());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

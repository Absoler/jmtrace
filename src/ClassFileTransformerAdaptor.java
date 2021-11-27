
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;

public class ClassFileTransformerAdaptor implements ClassFileTransformer {

    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) {

        if(className.startsWith("java")||className.startsWith("sun")||className.startsWith("jdk")){
        //	System.out.println("bingo");
            return null;
        }
        ClassReader classReader = new ClassReader(classfileBuffer);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        ClassVisitor classVisitor = new ClassAdaptor(classWriter);
        classReader.accept(classVisitor, 0);
        return classWriter.toByteArray();
    }

//    public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
//        //instrumentation
//        ClassWriter cw=new ClassWriter(ClassWriter.COMPUTE_MAXS);
//        ClassAdaptor cv=new ClassAdaptor(cw);
//        File classfile=new File("E:\\Assignments\\ISER\\PA2\\out\\production\\PA2\\Test.class");
//        ClassReader cr=new ClassReader(new FileInputStream(classfile));
//        cr.accept(cv,0);
//        byte[] code=cw.toByteArray();
//
//        //load and Instantiate
//        MyClassLoader myClassLoader=new MyClassLoader();
//        Class<?> JmtraceClass=myClassLoader.defineClassPublic("Test",code,0,code.length);
//        Object o=JmtraceClass.newInstance();
//
//        //invoke main-function
//        Method main=JmtraceClass.getMethod("main",String[].class);
//        String[] arg=new String[1];
//        main.invoke(o,arg);
//    }
//    static class MyClassLoader extends ClassLoader{
//        public Class<?> defineClassPublic(String name, byte[] b, int off, int len) throws ClassFormatError {
//            Class<?> cls = defineClass(name, b, off, len);
//            return cls;
//        }
//    }
}

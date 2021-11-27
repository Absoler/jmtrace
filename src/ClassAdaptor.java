import org.objectweb.asm.*;


import static org.objectweb.asm.Opcodes.*;

public class ClassAdaptor extends ClassVisitor {
    public ClassAdaptor(ClassVisitor classVisitor) {
        super(ASM7, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = cv.visitMethod(access, name, descriptor, signature, exceptions);
        return new MethodAdaptor(methodVisitor);
    }


    class MethodAdaptor extends MethodVisitor {
        public MethodAdaptor(MethodVisitor methodVisitor) {
            super(ASM7, methodVisitor);
        }

        @Override
        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
            if(owner.startsWith("java")||owner.startsWith("jdk")||owner.startsWith("sun")){
                mv.visitFieldInsn(opcode,owner,name,descriptor);
                return;
            }
            if (opcode == GETFIELD) {
                // ..., o => ..., o.f
                mv.visitInsn(DUP);
                mv.visitLdcInsn(owner + '.' + name);    // o, owner.name
                mv.visitMethodInsn(INVOKESTATIC, "OutputInfo", "trace_readField", "(Ljava/lang/Object;Ljava/lang/String;)V", false);
            }
            if (opcode == PUTFIELD) {
                // ..., o, v => ...
                if (Type.getType(descriptor).getSize() == 1) {
                    mv.visitInsn(DUP2);
                    mv.visitInsn(POP);  // o, v, o
                    mv.visitLdcInsn(owner + '.' + name);    // o, v, o, owner.name
                    mv.visitMethodInsn(INVOKESTATIC, "OutputInfo", "trace_writeField", "(Ljava/lang/Object;Ljava/lang/String;)V", false);
                } else {
                    // o, w
                    mv.visitInsn(DUP2_X1);  // w, o, w
                    mv.visitInsn(POP2);     // w, o
                    mv.visitInsn(DUP);     // w, o, o
                    mv.visitLdcInsn(owner + '.' + name);    // v, o, o, owner.name
                    mv.visitMethodInsn(INVOKESTATIC, "OutputInfo", "trace_writeField", "(Ljava/lang/Object;Ljava/lang/String;)V", false);   // w, o
                    mv.visitInsn(DUP_X2);   // o, w, o
                    mv.visitInsn(POP);  // o, w
                }
            }
            if (opcode == GETSTATIC) {
                // ... => ..., c.f
                mv.visitLdcInsn(Type.getType("L" + owner + ";"));   // Type(c)
                mv.visitLdcInsn(owner + '.' + name);    // Type(c) owner.name
                mv.visitMethodInsn(INVOKESTATIC, "OutputInfo", "trace_readStatic", "(Ljava/lang/Object;Ljava/lang/String;)V", false);
            }
            if (opcode == PUTSTATIC) {
                // ..., v => ...
                mv.visitLdcInsn(Type.getType("L" + owner + ";"));   // v, Type(c)
                mv.visitLdcInsn(owner + '.' + name);    // v, Type(c), owner.name
                mv.visitMethodInsn(INVOKESTATIC, "OutputInfo", "trace_writeStatic", "(Ljava/lang/Object;Ljava/lang/String;)V", false);
            }
            mv.visitFieldInsn(opcode,owner,name,descriptor);
        }

        @Override
        public void visitInsn(int opcode) {
            if (opcode >= IALOAD && opcode <= SALOAD) {
                // ..., o, i => ..., o[i]
                mv.visitInsn(DUP2); // o, i, o, i
                mv.visitMethodInsn(INVOKESTATIC, "OutputInfo", "trace_readArray", "(Ljava/lang/Object;I)V", false);
            } else if (opcode == DASTORE || opcode == LASTORE) {
                // ..., o, i, w
                mv.visitInsn(DUP2_X2);  // w, o, i, w
                mv.visitInsn(POP2); // w, o, i
                mv.visitInsn(DUP2); // w, o, i, o, i
                mv.visitMethodInsn(INVOKESTATIC, "OutputInfo", "trace_writeArray", "(Ljava/lang/Object;I)V", false);    // w, o, i
                mv.visitInsn(DUP2_X2);  // o, i, w, o, i
                mv.visitInsn(POP2); // o, i, w
            } else if (opcode >= IASTORE && opcode <= SASTORE) {
                // ..., o, i, v
                mv.visitInsn(DUP_X2);   // v, o, i, v
                mv.visitInsn(POP);  // v, o, i
                mv.visitInsn(DUP2); // v, o, i, o, i
                mv.visitMethodInsn(INVOKESTATIC, "OutputInfo", "trace_writeArray", "(Ljava/lang/Object;I)V", false);
                mv.visitInsn(DUP2_X1);  // o, i, v, o, i
                mv.visitInsn(POP2); // o, i, v
            }
            mv.visitInsn(opcode);
        }
    }
}

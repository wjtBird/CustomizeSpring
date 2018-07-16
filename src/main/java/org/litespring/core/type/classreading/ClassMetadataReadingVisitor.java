package org.litespring.core.type.classreading;

import org.litespring.core.type.ClassMetadata;
import org.litespring.util.ClassUtils;
import org.springframework.asm.ClassVisitor;
import org.springframework.asm.Opcodes;
import org.springframework.asm.SpringAsmInfo;

/**
 * Created by jinTao.wang on 2018/7/15
 */
public class ClassMetadataReadingVisitor extends ClassVisitor implements ClassMetadata {


    private String className;

    private boolean isInterface;

    private boolean isAbstract;

    private boolean isFinal;

    private String superClassName;

    private String[] interfaces;


    public ClassMetadataReadingVisitor() {
        super(SpringAsmInfo.ASM_VERSION);
    }

    @Override
    public void visit(int version, int access, String resourcePath, String signature, String superName, String[] interfaces) {
        this.className = ClassUtils.convertResourcePathToClassName(resourcePath);

        this.isAbstract = ((access & Opcodes.ACC_ABSTRACT) != 0);
        this.isInterface = ((access & Opcodes.ACC_INTERFACE) != 0);
        this.isFinal = ((access & Opcodes.ACC_FINAL) != 0);

        if (superName != null) {
            this.superClassName = ClassUtils.convertResourcePathToClassName(superName);
        }

        this.interfaces = new String[interfaces.length];

        for (int i = 0; i < interfaces.length; i++) {
            this.interfaces[i] = ClassUtils.convertResourcePathToClassName(interfaces[i]);
        }

    }

    @Override
    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public boolean isInterface() {
        return isInterface;
    }

    public void setInterface(boolean anInterface) {
        isInterface = anInterface;
    }

    @Override
    public boolean isAbstract() {
        return isAbstract;
    }

    public void setAbstract(boolean anAbstract) {
        isAbstract = anAbstract;
    }

    @Override
    public boolean isFinal() {
        return isFinal;
    }

    @Override
    public boolean hasSuperClass() {
        return this.superClassName != null;
    }

    public void setFinal(boolean aFinal) {
        isFinal = aFinal;
    }

    @Override
    public String getSuperClassName() {
        return superClassName;
    }

    @Override
    public String[] getInterfaceNames() {
        return this.interfaces;
    }

    public void setSuperClassName(String superClassName) {
        this.superClassName = superClassName;
    }

    public String[] getInterfaces() {
        return interfaces;
    }

    public void setInterfaces(String[] interfaces) {
        this.interfaces = interfaces;
    }
}

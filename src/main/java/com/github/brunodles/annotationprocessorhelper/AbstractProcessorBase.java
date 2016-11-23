package com.github.brunodles.annotationprocessorhelper;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

public abstract class AbstractProcessorBase extends AbstractProcessor {

    private static final String TAG = "AbstractProcessorBase";
    protected Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        HashSet<String> strings = new HashSet<String>();

        SupportedAnnotationTypes sat = this.getClass().getAnnotation(SupportedAnnotationTypes.class);
        if (sat != null) {
            String[] value = sat.value();
            Collections.addAll(strings, value);
        }

        SupportedAnnotations sa = this.getClass().getAnnotation(SupportedAnnotations.class);
        if (sa != null) {
            Class<? extends Annotation>[] value = sa.value();
            for (Class<? extends Annotation> aClass : value) {
                String canonicalName = aClass.getCanonicalName();
                strings.add(canonicalName);
            }
        }

        if (strings.isEmpty() && isInitialized())
            warnAnnotationTypeNotFound();

        return Collections.unmodifiableSet(strings);
    }

    private TypeElement asTypeElement(TypeMirror typeMirror) {
        Types TypeUtils = this.processingEnv.getTypeUtils();
        return (TypeElement) TypeUtils.asElement(typeMirror);
    }

    private void writeClass(String className, String packageName, String content) {
        OutputStreamWriter osw = null;
        try {
            JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(packageName + "." + className,
                    elementUtils.getTypeElement(packageName));
            OutputStream os = fileObject.openOutputStream();
            osw = new OutputStreamWriter(os, Charset.forName("UTF-8"));
            osw.write(content, 0, content.length());

        } catch (IOException ex) {
            ex.printStackTrace();
            fatalError(TAG, ex.getMessage());
        } finally {
            try {
                if (osw != null) {
                    osw.flush();
                    osw.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
                fatalError(TAG, ex.getMessage());
            }
        }
    }

    private void warnAnnotationTypeNotFound() {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.WARNING,
                "No SupportedAnnotationTypes annotation " +
                        "found on " + this.getClass().getName() +
                        ", returning an empty set.");
    }

    protected final void log(String tag, String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                String.format("%s - %s\n", tag, msg));
    }

    protected final void fatalError(String tag, String msg) {
        processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                String.format("%s FATAL ERROR - %s\n", tag, msg));
    }
}

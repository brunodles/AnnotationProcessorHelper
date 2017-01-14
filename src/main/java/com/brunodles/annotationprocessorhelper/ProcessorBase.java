package com.brunodles.annotationprocessorhelper;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A Helper class to help to write AnnotationProcessors.
 * <p>
 * The main function of this class is to help to declare the supported annotations,
 * by using {@link SupportedAnnotations} annotation.
 */
public abstract class ProcessorBase extends AbstractProcessor {

    private static final String TAG = "ProcessorBase";
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

    protected TypeElement asTypeElement(TypeMirror typeMirror) {
        Types typeUtils = this.processingEnv.getTypeUtils();
        return (TypeElement) typeUtils.asElement(typeMirror);
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

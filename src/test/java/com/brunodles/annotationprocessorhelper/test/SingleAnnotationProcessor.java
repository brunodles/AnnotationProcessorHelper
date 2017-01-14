package com.brunodles.annotationprocessorhelper.test;

import com.brunodles.annotationprocessorhelper.ProcessorBase;
import com.brunodles.annotationprocessorhelper.SupportedAnnotations;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotations(Annotation1.class)
public class SingleAnnotationProcessor extends ProcessorBase {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

}

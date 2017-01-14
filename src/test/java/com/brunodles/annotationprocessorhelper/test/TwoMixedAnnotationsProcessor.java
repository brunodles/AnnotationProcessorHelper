package com.brunodles.annotationprocessorhelper.test;

import com.brunodles.annotationprocessorhelper.ProcessorBase;
import com.brunodles.annotationprocessorhelper.SupportedAnnotations;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotations(Annotation1.class)
@SupportedAnnotationTypes("com.brunodles.annotationprocessorhelper.test.Annotation2")
public class TwoMixedAnnotationsProcessor extends ProcessorBase {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

}

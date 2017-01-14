package com.brunodles.annotationprocessorhelper.test;

import com.brunodles.annotationprocessorhelper.ProcessorBase;
import com.brunodles.annotationprocessorhelper.SupportedAnnotations;

import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotationTypes("com.brunodles.annotationprocessorhelper.test.Annotation1")
public class OldAnnotationsProcessor extends ProcessorBase {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

}

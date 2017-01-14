package com.brunodles.annotationprocessorhelper.test;

import com.brunodles.annotationprocessorhelper.ProcessorBase;
import com.brunodles.annotationprocessorhelper.SupportedAnnotations;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedAnnotations({Annotation1.class, Annotation2.class})
public class TwoAnnotationsProcessor extends ProcessorBase {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

}

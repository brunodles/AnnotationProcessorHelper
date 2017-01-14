package com.brunodles.annotationprocessorhelper.test;

import com.brunodles.annotationprocessorhelper.ProcessorBase;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.TypeElement;
import java.util.Set;

public class LoggerProcessor extends ProcessorBase {

    private static final String TAG = "LoggerProcessor";

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        return false;
    }

}

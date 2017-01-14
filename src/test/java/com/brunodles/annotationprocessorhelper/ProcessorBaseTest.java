package com.brunodles.annotationprocessorhelper;

import com.brunodles.annotationprocessorhelper.test.*;
import com.brunodles.oleaster.suiterunner.OleasterSuiteRunner;
import org.junit.runner.RunWith;

import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.util.Set;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.after;
import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

@RunWith(OleasterSuiteRunner.class)
public class ProcessorBaseTest {

    private ProcessorBase processor;
    private Set<String> supportedAnnotationTypes;

    private Messager messager;

    {

        describe("Given an AnnotationProcessor", () -> {
            after(() -> processor = null);
            describe("When #getSupportedAnnotationTypes", () -> {
                beforeEach(() -> supportedAnnotationTypes = processor.getSupportedAnnotationTypes());
                describe("With singleAnnotation on @SupportedAnnotations", () -> {
                    before(() -> processor = new SingleAnnotationProcessor());
                    itShouldIncludeAnnotations(Annotation1.class.getCanonicalName());
                });
                describe("With twoAnnotations on @SupportedAnnotations", () -> {
                    before(() -> processor = new TwoAnnotationsProcessor());
                    itShouldIncludeAnnotations(Annotation1.class.getCanonicalName(),
                            Annotation2.class.getCanonicalName());
                });
                describe("with an annotation from @SupportedAnnotations and another from @SupportedAnnotationTypes", () -> {
                    before(() -> processor = new TwoMixedAnnotationsProcessor());
                    itShouldIncludeAnnotations(Annotation1.class.getCanonicalName(),
                            Annotation2.class.getCanonicalName());
                });
                describe("with an annotation on @SupportedAnnotationTypes", () -> {
                    before(() -> processor = new OldAnnotationsProcessor());
                    itShouldIncludeAnnotations(Annotation1.class.getCanonicalName());
                });
                describe("without annotations", () -> {
                    before(this::setupLoggerProcessor);
                    it("should call messager.printMessage", () -> {
                        verify(messager).printMessage(eq(Diagnostic.Kind.WARNING), anyString());
                    });
                });
            });

            describe("context print a message", () -> {
                beforeEach(this::setupLoggerProcessor);
                describe("when #log", () -> {
                    beforeEach(() -> processor.log("TAG", "message"));
                    it("should call messager.printMessage", () -> {
                        verify(messager).printMessage(eq(Diagnostic.Kind.NOTE), eq("TAG - message\n"));
                    });
                });
                describe("when #fatalError", () -> {
                    beforeEach(() -> processor.fatalError("TAG", "error"));
                    it("should call messager.printMessage", () -> {
                        verify(messager).printMessage(eq(Diagnostic.Kind.ERROR), eq("TAG FATAL ERROR - error\n"));
                    });
                });
            });

            describe("when #asTypeElement", () -> {
                it("should call processingEnv to get the typeUtils to get the element", () -> {
                    processor = new LoggerProcessor();
                    ProcessingEnvironment processingEnvironment = mock(ProcessingEnvironment.class);
                    processor.init(processingEnvironment);
                    Types types = mock(Types.class);
                    when(processingEnvironment.getTypeUtils()).thenReturn(types);
                    TypeElement typeElement = mock(TypeElement.class);
                    when(types.asElement(any())).thenReturn(typeElement);

                    assertThat(processor.asTypeElement(mock(TypeMirror.class))).isEqualTo(typeElement);
                });
            });

        });
    }

    private void setupLoggerProcessor() {
        processor = new LoggerProcessor();
        ProcessingEnvironment processingEnvironment = mock(ProcessingEnvironment.class);
        processor.init(processingEnvironment);
        messager = mock(Messager.class);
        when(processingEnvironment.getMessager()).thenReturn(messager);
    }

    private void itShouldIncludeAnnotations(String... annotations) {
        it("should include the annotations annotated with @SupportedAnnotations", () -> {
            assertThat(supportedAnnotationTypes).containsOnly(annotations);
        });
    }

}
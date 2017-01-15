package com.brunodles.annotationprocessorhelper

import com.brunodles.annotationprocessorhelper.test.*
import com.brunodles.oleaster.suiterunner.OleasterSuiteRunner
import com.brunodles.test.helper.once
import com.mscharhag.oleaster.runner.StaticRunnerSupport.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.runner.RunWith
import org.mockito.Matchers
import org.mockito.Mockito.*
import javax.annotation.processing.Messager
import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.TypeElement
import javax.lang.model.type.TypeMirror
import javax.lang.model.util.Types
import javax.tools.Diagnostic

@RunWith(OleasterSuiteRunner::class)
class ProcessorBaseTest {

    private var processor: ProcessorBase? = null
    private var supportedAnnotationTypes: Set<String>? = null

    private var messager: Messager? = null

    init {

        describe("Given an AnnotationProcessor") {
            after { processor = null }
            describe("When #getSupportedAnnotationTypes") {
                beforeEach { supportedAnnotationTypes = processor!!.supportedAnnotationTypes }
                describe("With singleAnnotation on @SupportedAnnotations") {
                    before { processor = SingleAnnotationProcessor() }
                    itShouldIncludeAnnotations(Annotation1::class.java.canonicalName)
                }
                describe("With twoAnnotations on @SupportedAnnotations") {
                    before { processor = TwoAnnotationsProcessor() }
                    itShouldIncludeAnnotations(Annotation1::class.java.canonicalName,
                            Annotation2::class.java.canonicalName)
                }
                describe("with an annotation from @SupportedAnnotations and another from @SupportedAnnotationTypes") {
                    before { processor = TwoMixedAnnotationsProcessor() }
                    itShouldIncludeAnnotations(Annotation1::class.java.canonicalName,
                            Annotation2::class.java.canonicalName)
                }
                describe("with an annotation on @SupportedAnnotationTypes") {
                    before { processor = OldAnnotationsProcessor() }
                    itShouldIncludeAnnotations(Annotation1::class.java.canonicalName)
                }
                describe("without annotations") {
                    before { setupLoggerProcessor() }
                    it("should call messager.printMessage") {
                        verify<Messager>(messager).printMessage(eq(Diagnostic.Kind.WARNING), Matchers.anyString())
                    }
                }
            }

            describe("context print a message") {
                beforeEach { setupLoggerProcessor() }
                describe("when #log") {
                    beforeEach { processor!!.log("TAG", "message") }
                    it("should call messager.printMessage") {
                        verify<Messager>(messager).printMessage(eq(Diagnostic.Kind.NOTE), eq("TAG - message\n"))
                    }
                }
                describe("when #fatalError") {
                    beforeEach { processor!!.fatalError("TAG", "error") }
                    it("should call messager.printMessage") {
                        verify<Messager>(messager).printMessage(eq(Diagnostic.Kind.ERROR), eq("TAG FATAL ERROR - error\n")) }
                }
            }

            describe("when #asTypeElement") {
                it("should call processingEnv to get the typeUtils to get the element") {
                    processor = LoggerProcessor()
                    val processingEnvironment = mock(ProcessingEnvironment::class.java)
                    processor!!.init(processingEnvironment)
                    val types = mock(Types::class.java)
                    once(processingEnvironment.typeUtils).thenReturn(types)
                    val typeElement = mock(TypeElement::class.java)
                    once(types.asElement(any())).thenReturn(typeElement)

                    assertThat(processor!!.asTypeElement(mock(TypeMirror::class.java))).isEqualTo(typeElement)
                }
            }

        }
    }

    private fun setupLoggerProcessor() {
        processor = LoggerProcessor()
        val processingEnvironment = mock(ProcessingEnvironment::class.java)
        processor!!.init(processingEnvironment)
        messager = mock(Messager::class.java)
        once(processingEnvironment.messager).thenReturn(messager)
    }

    private fun itShouldIncludeAnnotations(vararg annotations: String) {
        it("should include the annotations annotated with @SupportedAnnotations") {
            assertThat(supportedAnnotationTypes).containsOnly(*annotations)
        }
    }

}
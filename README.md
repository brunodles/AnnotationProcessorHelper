[![Build Status](https://travis-ci.org/brunodles/AnnotationProcessorHelper.svg?branch=master)](https://travis-ci.org/brunodles/AnnotationProcessorHelper)
# AnnotationProcessorHelper

A helper to create annotation processors.

## Why do I need this lib?
You don't need this lib to create your annotation processor.
I wrote this lib to simplify some of the tasks needed to build an annotation processor.

## What does this lib do?
This lib help to define the *supported annotations*.
The suggested way you to do it is  by add `@SupportedAnnotationTypes`, but the this annotation receives a `String`.
You end up writing `@SupportedAnnotationTypes("all.my.package.to.my.annotation.Annotation");`,
At first time you may not see any problem with it, until you need to move you annotation to another package
or when you need to rename it.

At this point this lib only helps with that, providing a new annotation `@SupportedAnnotations`, with it you can
pass `Annotation.class` to your processor and it will do the job.

## How does it work?
To make it work, you need to extend the `AbstractProcessorBase`.
I know, this is bad, may we figure out another way to do that.

## Installing
Add the repository to your project, by adding the url bellow on your *project level gradle file*
`root/build.gradle`.
```gradle
	allprojects {
		repositories {
			...
			maven { url "https://dl.bintray.com/brunodles/TempRepo" }
		}
	}
```

At your *app level gradle file* add the dependency
```gradle
	dependencies {
	    compile 'com.brunodles:annotationprocessorhelper:1.3.0'
	}
```

## Using
On your annotation processor simply extend `AbstractProcessorBase` and add your annotations as usual.
Now you can add `@SupportedAnnotations` with a array of annotations, not strings.
You still be able to add the old annotations, if you want `@SupportedAnnotationTypes` passing the strings.

## Samples
Single annotation
```java
@SupportedAnnotation(MyAnnotation.class)
Public class MyAnnotationProcessor extend AbstractProccessorBase {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ...
    }
}
```

Multiple annotation
```java
@SupportedAnnotation({MyAnnotation1.class, MyAnnotation2.class, MyAnnotation3.class, ...})
Public class MyAnnotationProcessor extend AbstractProccessorBase {

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        ...
    }
}
```

## Contribute
Send your suggestion, improvements and doubts.

## Useful links
* [The Art Of Metaprogramming in Java](http://pt.slideshare.net/PolymathicCoder/the-art-of-metaprogramming-in-java)
* [Annotation Processing - Processor](https://docs.oracle.com/javase/7/docs/api/javax/annotation/processing/Processor.html)
* [How to Process Java Annotations](https://www.javacodegeeks.com/2015/01/how-to-process-java-annotations.html)
* [Annotation Processing 101](http://hannesdorfmann.com/annotation-processing/annotationprocessing101)
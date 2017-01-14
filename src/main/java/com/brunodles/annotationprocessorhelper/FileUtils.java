package com.brunodles.annotationprocessorhelper;

import javax.tools.FileObject;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

public final class FileUtils {

    private FileUtils() {
    }

    /**
     * Create a file using the {@link javax.annotation.processing.Filer}, look the example bellow
     * <code>
     * JavaFileObject fileObject = processingEnv.getFiler().createSourceFile(fileName, originatingElements);
     * </code>
     *
     * @param fileObject a file object
     * @param content    the file content
     * @throws IOException when failed to write the file
     */
    public static void writeFile(FileObject fileObject, String content) throws IOException {
        OutputStreamWriter osw = null;
        IOException exception = null;
        try {
            OutputStream os = fileObject.openOutputStream();
            osw = new OutputStreamWriter(os, Charset.forName("UTF-8"));
            osw.write(content, 0, content.length());
        } catch (IOException ex) {
            exception = ex;
        } finally {
            try {
                if (osw != null) {
                    osw.flush();
                    osw.close();
                }
            } catch (IOException ex) {
                exception = ex;
            }
        }
        if (exception != null) throw exception;
    }
}

package com.brunodles.annotationprocessorhelper;

import com.brunodles.oleaster.suiterunner.OleasterSuiteRunner;
import com.brunodles.test.helper.OutputStreamTestHelper;
import org.junit.runner.RunWith;

import javax.tools.FileObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.mscharhag.oleaster.runner.StaticRunnerSupport.*;
import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(OleasterSuiteRunner.class)
public class FileUtilsTest {

    private FileObject fileObject;
    private OutputStream outputStream;
    private IOException exception;

    {
        describe("FileUtils, when .writeFile", () -> {
            beforeEach(() -> {
                fileObject = mock(FileObject.class);
                when(fileObject.openOutputStream()).thenReturn(outputStream);
                exception = null;
                try {
                    FileUtils.writeFile(fileObject, "FileContent");
                } catch (IOException e) {
                    exception = e;
                }
            });
            describe("with success", () -> {
                before(() -> outputStream = new ByteArrayOutputStream());
                it("should open a outputStream and write the content", () -> {
                    assertThat(outputStream.toString()).isEqualTo("FileContent");
                });
                it("should not throw any exception", () -> {
                    assertThat(exception).isNull();
                });
            });
            describe("when got some error while write", () -> {
                before(() -> outputStream = OutputStreamTestHelper.failOnWrite());
                itShouldThrowAnIOException();
            });
            describe("when got some error on close", () -> {
                before(() -> outputStream = OutputStreamTestHelper.failOnClose());
                itShouldThrowAnIOException();
            });

        });
    }

    private void itShouldThrowAnIOException() {
        it("should throws an IOException", () -> {
            assertThat(exception).isInstanceOf(IOException.class);
            assertThat(exception).isNotNull();
        });
    }
}
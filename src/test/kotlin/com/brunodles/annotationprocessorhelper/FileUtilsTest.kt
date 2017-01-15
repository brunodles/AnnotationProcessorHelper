package com.brunodles.annotationprocessorhelper

import com.brunodles.oleaster.suiterunner.OleasterSuiteRunner
import com.brunodles.test.helper.OutputStreamTestHelper
import org.junit.runner.RunWith

import javax.tools.FileObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.OutputStream

import com.mscharhag.oleaster.runner.StaticRunnerSupport.*
import org.assertj.core.api.Java6Assertions.assertThat
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

@RunWith(OleasterSuiteRunner::class)
class FileUtilsTest {

    private var fileObject: FileObject? = null
    private var outputStream: OutputStream? = null
    private var exception: IOException? = null

    init {
        describe("FileUtils, when .writeFile") {
            beforeEach {
                fileObject = mock(FileObject::class.java)
                `when`(fileObject!!.openOutputStream()).thenReturn(outputStream)
                exception = null
                try {
                    FileUtils.writeFile(fileObject, "FileContent")
                } catch (e: IOException) {
                    exception = e
                }
            }
            describe("with success") {
                before { outputStream = ByteArrayOutputStream() }
                it("should open a outputStream and write the content") {
                    assertThat(outputStream!!.toString()).isEqualTo("FileContent")
                }
                it("should not throw any exception") { assertThat(exception).isNull() }
            }
            describe("when got some error while write") {
                before { outputStream = OutputStreamTestHelper.failOnWrite() }
                itShouldThrowAnIOException()
            }
            describe("when got some error on close") {
                before { outputStream = OutputStreamTestHelper.failOnClose() }
                itShouldThrowAnIOException()
            }

        }
    }

    private fun itShouldThrowAnIOException() {
        it("should throws an IOException") {
            assertThat(exception).isInstanceOf(IOException::class.java)
            assertThat(exception).isNotNull()
        }
    }
}
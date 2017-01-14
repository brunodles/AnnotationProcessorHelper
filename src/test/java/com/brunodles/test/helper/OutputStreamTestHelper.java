package com.brunodles.test.helper;

import java.io.IOException;
import java.io.OutputStream;

public class OutputStreamTestHelper extends OutputStream {

    private final int whenToFail;
    private static final int FAIL_ON_WRITE = 1;
    private static final int FAIL_ON_CLOSE = 2;

    private OutputStreamTestHelper(int whenToFail) {
        this.whenToFail = whenToFail;
    }

    @Override
    public void write(int b) throws IOException {
        if (isTo(FAIL_ON_WRITE)) throwException();
    }

    @Override
    public void close() throws IOException {
        if (isTo(FAIL_ON_CLOSE)) throwException();
        super.close();
    }

    private void throwException() throws IOException {
        throw new IOException("You should get this exception");
    }

    private boolean isTo(int byteWise) {
        return (whenToFail & byteWise) == byteWise;
    }

    public static OutputStream failOnWrite() {
        return new OutputStreamTestHelper(FAIL_ON_WRITE);
    }

    public static OutputStream failOnClose() {
        return new OutputStreamTestHelper(FAIL_ON_CLOSE);
    }
}

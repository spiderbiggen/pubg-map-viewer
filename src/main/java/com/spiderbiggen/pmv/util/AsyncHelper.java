package com.spiderbiggen.pmv.util;

import java.io.Closeable;
import java.io.IOException;
import java.util.function.Consumer;

public class AsyncHelper {

    public static void closeStream(Closeable closeable, Consumer<IOException> exceptionConsumer) {
        try {
            closeable.close();
        } catch (IOException e) {
            if (exceptionConsumer != null) {
                exceptionConsumer.accept(e);
            }
        }
    }
}

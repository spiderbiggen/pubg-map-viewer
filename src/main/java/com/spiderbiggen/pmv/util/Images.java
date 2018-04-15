package com.spiderbiggen.pmv.util;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import static com.spiderbiggen.pmv.util.AsyncHelper.closeStream;

public class Images {

    private Images() {

    }

    public static Image loadInternal(String path) {
        return loadInternal(path, null);
    }

    public static Image loadInternal(String path, Consumer<IOException> exceptionConsumer) {
        var inputStream = Images.class.getResourceAsStream(path);
        var img = load(inputStream);
        closeStream(inputStream, exceptionConsumer);
        return img;
    }

    public static Image load(InputStream inputStream) {
        return new Image(inputStream);
    }

    public static CompletableFuture<Image> loadInternalAsync(String relativePath) {
        return loadInternalAsync(relativePath, null);
    }

    public static CompletableFuture<Image> loadInternalAsync(String relativePath, Consumer<IOException> exceptionConsumer) {
        InputStream stream = Images.class.getResourceAsStream(relativePath);
        CompletableFuture<Image> future = loadAsync(stream);
        future.thenRun(() -> closeStream(stream, exceptionConsumer));
        return future;
    }


    public static CompletableFuture<Image> loadAsync(InputStream inputStream) {
        return CompletableFuture.supplyAsync(() -> new Image(inputStream));
    }
}

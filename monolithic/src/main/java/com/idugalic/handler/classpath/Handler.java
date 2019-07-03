package com.idugalic.handler.classpath;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class Handler extends URLStreamHandler {

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        String path = u.getPath();

        // Thread context class loader first
        URL classpathUrl = Thread.currentThread().getContextClassLoader().getResource(path);
        if (classpathUrl == null) {
            // This class's class loader if no joy with the tccl
            classpathUrl = Handler.class.getResource(path);
        }

        if (classpathUrl == null) {
            throw new FileNotFoundException("classpathUrlStreamHandler.notFound: " + path);
        }

        return classpathUrl.openConnection();
    }
}

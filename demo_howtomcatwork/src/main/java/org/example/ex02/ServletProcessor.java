package org.example.ex02;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.Servlet;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandler;

import static org.example.common.CommonConstants.WEB_ROOT;

/**
 * Servlet处理器
 */
@Slf4j
public class ServletProcessor {
    public void process(Request request, Response response) {

        String uri = request.getUri();
        String servletName = uri.substring(uri.lastIndexOf("/") + 1);

        // create a URLClassLoader
        URL[] urls = new URL[1];
        URLStreamHandler streamHandler = null;
        File classPath = new File(WEB_ROOT + File.separator + "servlet");

        try {
            // the forming of repository is taken from the createClassLoader method in
            // org.apache.catalina.startup.ClassLoaderFactory
            String repository = (new URL("file", null, classPath.getCanonicalPath() + File.separator)).toString();
            // the code for forming the URL is taken from the addRepository method in
            // org.apache.catalina.loader.StandardClassLoader class.
            urls[0] = new URL(null, repository, streamHandler);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (
           URLClassLoader loader = new URLClassLoader(urls);
        ) {
            Class<?> myClass = loader.loadClass(servletName);
            Servlet servlet = null;
            servlet = (Servlet) myClass.newInstance();
            servlet.service(request, (ServletResponse) response);
        } catch (Exception e) {
           log.error("ClassLoader Servlet error:",e);
        }
    }
}

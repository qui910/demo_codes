package org.example.ex02;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import static org.example.common.CommonConstants.BUFFER_SIZE;

@Slf4j
public class Request implements ServletRequest {

    private final InputStream input;
    @Getter
    private String uri;

    public Request(InputStream input) {
        this.input = input;
    }

    private String parseUri(String requestString) {
        int index1, index2;
        index1 = requestString.indexOf(' ');
        if (index1 != -1) {
            index2 = requestString.indexOf(' ', index1 + 1);
            if (index2 > index1)
                return requestString.substring(index1 + 1, index2);
        }
        return null;
    }

    public void parse() {
        // Read a set of characters from the socket
        StringBuilder request = new StringBuilder(BUFFER_SIZE);
        int i;
        byte[] buffer = new byte[BUFFER_SIZE];
        try {
            i = input.read(buffer);
        } catch (IOException e) {
            log.error("Request read error: " + e);
            i = -1;
        }
        for (int j=0; j<i; j++) {
            request.append((char) buffer[j]);
        }
        System.out.print(request.toString());
        uri = parseUri(request.toString());
    }

    /* implementation of the ServletRequest*/
    @Override
    public Object getAttribute(String attribute) {
        return null;
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return null;
    }
    @Override
    public String getRealPath(String path) {
        return null;
    }

    @Override
    public int getRemotePort() {
        return 0;
    }

    @Override
    public String getLocalName() {
        return null;
    }

    @Override
    public String getLocalAddr() {
        return null;
    }

    @Override
    public int getLocalPort() {
        return 0;
    }

    @Override
    public ServletContext getServletContext() {
        return null;
    }

    @Override
    public AsyncContext startAsync() throws IllegalStateException {
        return null;
    }

    @Override
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse) throws IllegalStateException {
        return null;
    }

    @Override
    public boolean isAsyncStarted() {
        return false;
    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }

    @Override
    public AsyncContext getAsyncContext() {
        return null;
    }

    @Override
    public DispatcherType getDispatcherType() {
        return null;
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }
    @Override
    public boolean isSecure() {
        return false;
    }
    @Override
    public String getCharacterEncoding() {
        return null;
    }
    @Override
    public int getContentLength() {
        return 0;
    }

    @Override
    public long getContentLengthLong() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }
    @Override
    public ServletInputStream getInputStream() throws IOException {
        return null;
    }
    @Override
    public Locale getLocale() {
        return null;
    }
    @Override
    public Enumeration<Locale> getLocales() {
        return null;
    }
    @Override
    public String getParameter(String name) {
        return null;
    }
    @Override
    public Map<String,String[]> getParameterMap() {
        return null;
    }
    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }
    @Override
    public String[] getParameterValues(String parameter) {
        return null;
    }
    @Override
    public String getProtocol() {
        return null;
    }
    @Override
    public BufferedReader getReader() throws IOException {
        return null;
    }
    @Override
    public String getRemoteAddr() {
        return null;
    }
    @Override
    public String getRemoteHost() {
        return null;
    }
    @Override
    public String getScheme() {
        return null;
    }
    @Override
    public String getServerName() {
        return null;
    }
    @Override
    public int getServerPort() {
        return 0;
    }
    @Override
    public void removeAttribute(String attribute) {
    }
    @Override
    public void setAttribute(String key, Object value) {
    }
    @Override
    public void setCharacterEncoding(String encoding)
            throws UnsupportedEncodingException {
    }

}

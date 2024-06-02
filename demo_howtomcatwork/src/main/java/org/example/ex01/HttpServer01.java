package org.example.ex01;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import static org.example.common.CommonConstants.*;

@Slf4j
public class HttpServer01 {

    // the shutdown command received
    private boolean shutdown = false;

    public void await() {
        ServerSocket serverSocket = null;
        int port = PORT;
        log.info("Server is started at port:{}",port);
        log.info("The Web Root is:{}",WEB_ROOT);
        try {
            serverSocket =  new ServerSocket(port, 1, InetAddress.getByName(IP_ADDRESS));
        } catch (IOException e) {
            log.error("Service is error,",e);
            System.exit(1);
        }

        assert serverSocket != null;

        // Loop waiting for a request
        while (!shutdown) {
            try (Socket socket = serverSocket.accept();
                 InputStream input = socket.getInputStream();
                 OutputStream   output = socket.getOutputStream();
                ){
                // create Request object and parse
                Request request = new Request(input);
                request.parse();

                // create Response object
                Response response = new Response(output,request);
                response.sendStaticResource();

                // Close the socket
                socket.close();

                //check if the previous URI is a shutdown command
                shutdown = request.getUri().equals(SHUTDOWN_COMMAND);
            } catch (Exception e) {
                log.error("Service response error,",e);
            }
        }
    }
}

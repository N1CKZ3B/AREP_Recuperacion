package com.mycompany.calcreflex;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Hello world!
 *
 */
public class CalcreflexBEServer
{
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean runnning = true;
        while (runnning) {
            Socket clientSocket = null;
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine;
            boolean isFirstLine=true;
            String firstLine = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if(isFirstLine){
                    firstLine = inputLine;
                    isFirstLine = false;
                    System.out.println(firstLine);
                }
                if (!in.ready()) {
                    break;
                }
            }

            URI reqURL = getReqURI(firstLine);

            if(reqURL.getPath().startsWith("/compreflex")){
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"+"{\"name\":\"John111\"}\n";
            } else {

                outputLine = getDefaultResponse();
            }
            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();
    }

    public static String getDefaultResponse(){
        String htmlcode="HTTP/1.1 200 OK\n"
                + "Content-Type: text/html\r\n"
                + "\r\n" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Form Example</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "Methot not found" +
                "\n" +
                "    </body>\n" +
                "</html>";
        return htmlcode;
    }

    public static URI getReqURI(String reqURI) throws URISyntaxException {
        System.out.println(reqURI);
        String ruri = reqURI.split(" ")[1];
        System.out.println(ruri );
        return new URI(ruri);
    }

    public static String computeMathCommand(String command) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class c = Math.class;
        Class[] parameterTypes ={double.class};

        Method m = c.getDeclaredMethod("abs",parameterTypes);
        Object[] params = {-2.0};
        String resp = m.invoke(null,(Object) params).toString();
        return "";
    }

}
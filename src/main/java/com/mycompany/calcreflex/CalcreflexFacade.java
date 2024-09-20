package com.mycompany.calcreflex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class CalcreflexFacade {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        boolean running = true;
        while (running) {
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
            boolean isFirstLine = true;
            String firstLine = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (isFirstLine) {
                    firstLine = inputLine;
                    isFirstLine = false;
                }
                if (!in.ready()) {
                    break;
                }
            }

            URI requestURI = getRequestURI(firstLine);

            if (requestURI.getPath().startsWith("/computar")) {
                String response = HttpConnectionExample.getResponse("/compreflex?" + requestURI.getQuery());
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + response;

            } else {
                outputLine = getHtmlClient();
            }

            out.println(outputLine);
            out.close();
            in.close();
            clientSocket.close();
        }
        serverSocket.close();

    }



    public static String getHtmlClient() {
        String htmlCode = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n"
                + "<!DOCTYPE html>\n"
                + "<html>\n"
                + "<head>\n"
                + "<meta charset=\"UTF-8\">\n"
                + "<title>CALCULATOR</title>\n"
                + "</head>\n"
                + "<body>\n"
                + "<h1>API CALC</h1>\n"
                + "<form>\n"
                + "<input type=\"text\" id=\"comando\" name=\"comando\" placeholder=\"---\"><br><br>\n"
                + "<input type=\"button\" value=\"Calcular\" onclick=\"loadGetMsg()\">\n"
                + "</form>\n"
                + "<div id=\"getrespmsg\"></div>\n"
                + "<script>\n"
                + "function loadGetMsg() {\n"
                + "let comando = document.getElementById(\"comando\").value;\n"
                + "const xhttp = new XMLHttpRequest();\n"
                + "xhttp.onload = function() {\n"
                + "document.getElementById(\"getrespmsg\").innerHTML = this.responseText;\n"
                + "}\n"
                + "xhttp.open(\"GET\", \"/computar?comando=\" + comando);\n"
                + "xhttp.send();\n"
                + "}\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";
        return htmlCode;
    }

    private static URI getRequestURI(String firstLine) throws URISyntaxException {
        String ruri = firstLine.split(" ")[1];
        return new URI(ruri);
    }
}
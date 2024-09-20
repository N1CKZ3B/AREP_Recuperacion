package com.mycompany.calcreflex;

import java.net.*;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CalcreflexBEServer {
    public static void main(String[] args) throws IOException, URISyntaxException {
        ServerSocket serverSocket = null;

        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 36000.");
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

            if (requestURI.getPath().startsWith("/compreflex")) {

                String query = requestURI.getQuery();
                String command = query.split("=")[1];
                String result = "";
                try {
                    result = computeMathCommand(command);
                } catch (Exception e) {
                    result = "Error: " + e.getMessage();
                }
                outputLine = "HTTP/1.1 200 OK\r\n"
                        + "Content-Type: application/json\r\n"
                        + "\r\n"
                        + "{\"resultado\":\"" + result + "\"}";

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

    private static URI getRequestURI(String firstLine) throws URISyntaxException {
        String ruri = firstLine.split(" ")[1];
        return new URI(ruri);
    }

    public static String computeMathCommand(String command) throws Exception {
        String methodName = command.substring(0, command.indexOf("("));
        System.out.println("Método: " + methodName);
        String paramsString = command.substring(command.indexOf("(") + 1, command.indexOf(")"));
        System.out.println("Parámetros: " + paramsString);

        if (methodName.equals("bbl")) {
            return bubbleSort(paramsString);
        }

        if (paramsString.trim().isEmpty()) {
            Class<?> c = Math.class;
            Method method = c.getMethod(methodName);
            Object result = method.invoke(null);
            return result.toString();
        }

        String[] paramValues = paramsString.split(",");
        Object[] params = new Object[paramValues.length];
        Class<?>[] paramTypes = new Class<?>[paramValues.length];

        for (int i = 0; i < paramValues.length; i++) {
            String param = paramValues[i].trim();
            params[i] = Double.parseDouble(param);
            paramTypes[i] = double.class;
        }

        Class<?> c = Math.class;
        Method method = c.getMethod(methodName, paramTypes);
        Object result = method.invoke(null, params);
        return result.toString();
    }

    public static String getDefaultResponse() {
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
                + "xhttp.open(\"GET\", \"/compreflex\" + comando);\n"
                + "xhttp.send();\n"
                + "}\n"
                + "</script>\n"
                + "</body>\n"
                + "</html>";
        return htmlCode;
    }

    public static String bubbleSort(String list) {
        String[] stringArray = list.split(",");
        int[] array = new int[stringArray.length];

        for (int i = 0; i < stringArray.length; i++) {
            array[i] = Integer.parseInt(stringArray[i].trim());
        }
        boolean swapped = true;
        int n = array.length;
        while (swapped) {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                if (array[i] > array[i + 1]) {
                    int temp = array[i];
                    array[i] = array[i + 1];
                    array[i + 1] = temp;
                    swapped = true;
                }
            }
        }

        StringBuilder sortedList = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            sortedList.append(array[i]);
            if (i < array.length - 1) {
                sortedList.append(",");
            }
        }
        return sortedList.toString();
    }
}
package com.diops.soapwrapper;

import jakarta.xml.ws.Endpoint;

public class SoapServer {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8082/services/getChunk", new GetChunckServiceImpl());
        System.out.println("SOAP Service started at http://localhost:8080/services/getChunk?wsdl");
    }
}

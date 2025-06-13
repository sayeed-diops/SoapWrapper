package com.example.soapwrapper;

import jakarta.xml.ws.Endpoint;

public class SoapWrapperApplication {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/ws/getPdfChunk", new SoapEndpoint());
        System.out.println("SOAP endpoint published at /ws/getPdfChunk");
    }
}
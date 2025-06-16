package com.diops.soapwrapper;

import jakarta.xml.ws.Endpoint;

public class SoapServer {
    public static void main(String[] args) {
        String address = "http://localhost:8082/services/getChunk";
        Endpoint.publish(address, new GetChunckServiceImpl());
        System.out.println("SOAP endpoint published at " + address);
    }
}
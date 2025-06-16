package com.example.soapwrapper;

import com.diops.soapwrapper.GetChunckServiceImpl;

import jakarta.xml.ws.Endpoint;

public class SoapWrapperApplication {
    public static void main(String[] args) {
        String address = "http://localhost:8082/services/getPdfChunk";
        Endpoint.publish(address, new GetChunckServiceImpl());
        System.out.println("SOAP endpoint published at " + address);
    }
}
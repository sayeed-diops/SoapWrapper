package com.example.soapwrapper;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@WebService(targetNamespace = "http://health.fgov.be/telematics/rsw/cosite/2.0/")
public class SoapEndpoint {

    @WebMethod(operationName = "GetChunck")
    public String getPdfChunk(
        @WebParam(name = "TransactionID") String transactionId,
        @WebParam(name = "Lnk") String lnk,
        @WebParam(name = "ChunckID") String chunkId,
        @WebParam(name = "Offset") int offset,
        @WebParam(name = "BufferSize") int bufferSize,
        @WebParam(name = "LnkSize") int lnkSize,
        @WebParam(name = "TimeoutEnSecondes") int timeoutSeconds
    ) {
        try {
            String mirthXml = "<GetChunck>" +
                "<TransactionID>" + transactionId + "</TransactionID>" +
                "<Lnk>" + lnk + "</Lnk>" +
                "<ChunckID>" + chunkId + "</ChunckID>" +
                "<Offset>" + offset + "</Offset>" +
                "<BufferSize>" + bufferSize + "</BufferSize>" +
                "<LnkSize>" + lnkSize + "</LnkSize>" +
                "<TimeoutEnSecondes>" + timeoutSeconds + "</TimeoutEnSecondes>" +
                "</GetChunck>";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("http://localhost:8090/receiveChunk"))
                .header("Content-Type", "application/xml")
                .POST(HttpRequest.BodyPublishers.ofString(mirthXml))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return "<error>" + e.getMessage() + "</error>";
        }
    }
}
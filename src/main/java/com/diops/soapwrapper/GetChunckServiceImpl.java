package com.diops.soapwrapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService()
public class GetChunckServiceImpl {

        @WebMethod
        public String GetChunck(
                        @WebParam(name = "TransactionID") String transactionId,
                        @WebParam(name = "Lnk") String lnk,
                        @WebParam(name = "ChunckID") String chunkId,
                        @WebParam(name = "Offset") int offset,
                        @WebParam(name = "BufferSize") int bufferSize,
                        @WebParam(name = "LnkSize") int lnkSize,
                        @WebParam(name = "TimeoutEnSecondes") int timeout) {
                try {

                        String currentDateTime = ZonedDateTime.now(ZoneOffset.UTC)
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                        "<request>" +
                                        "<transactionId>" + (transactionId != null ? transactionId : "")
                                        + "</transactionId>" +
                                        "<lnk>" + (lnk != null ? lnk : "") + "</lnk>" +
                                        "<chunkId>" + (chunkId != null ? chunkId : "") + "</chunkId>" +
                                        "<offset>" + offset + "</offset>" +
                                        "<bufferSize>" + bufferSize + "</bufferSize>" +
                                        "<lnkSize>" + lnkSize + "</lnkSize>" +
                                        "<timeout>" + timeout + "</timeout>" +
                                        "<timestamp>" + currentDateTime + "</timestamp>" +
                                        "</request>";

                        System.out.println("Forwarding request to Mirth with payload: " + xml);

                        HttpClient client = HttpClient.newBuilder()
                                        .followRedirects(HttpClient.Redirect.ALWAYS)
                                        .build();

                        HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create("http://localhost:8081/api/getChunk"))
                                        .header("Content-Type", "text/xml")
                                        .POST(HttpRequest.BodyPublishers.ofString(xml))
                                        .build();

                        System.out.println("Sending request to Mirth at: http://localhost:8081/api/getChunk");

                        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

                        System.out.println("Received response from Mirth with status code: " + response.statusCode());
                        System.out.println("Response body: " + response.body());

                        String soapResponse = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                                        "<soap:Body>" +
                                        "<ns2:GetChunckResponse xmlns:ns2=\"http://soapwrapper.diops.com/\">" +
                                        "<return>" +
                                        "<status>Success</status>" +
                                        "<timestamp>" + currentDateTime + "</timestamp>" +
                                        "<transactionId>" + (transactionId != null ? transactionId : "")
                                        + "</transactionId>" +
                                        "<responseData>"
                                        + (response.body().isEmpty() ? "Processed Successfully" : response.body())
                                        + "</responseData>" +
                                        "</return>" +
                                        "</ns2:GetChunckResponse>" +
                                        "</soap:Body>" +
                                        "</soap:Envelope>";

                        return soapResponse;

                } catch (Exception e) {
                        String currentDateTime = ZonedDateTime.now(ZoneOffset.UTC)
                                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                                        "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">" +
                                        "<soap:Body>" +
                                        "<soap:Fault>" +
                                        "<faultcode>soap:Server</faultcode>" +
                                        "<faultstring>" + e.getMessage() + "</faultstring>" +
                                        "<timestamp>" + currentDateTime + "</timestamp>" +
                                        "</soap:Fault>" +
                                        "</soap:Body>" +
                                        "</soap:Envelope>";
                }
        }
}
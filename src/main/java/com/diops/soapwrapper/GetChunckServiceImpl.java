package com.diops.soapwrapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebService;

@WebService(endpointInterface = "com.diops.soapwrapper.GetChunckService")
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
            String json = "{"
                    + "\"transactionId\":\"" + transactionId + "\","
                    + "\"lnk\":\"" + lnk + "\","
                    + "\"chunkId\":\"" + chunkId + "\","
                    + "\"offset\":" + offset + ","
                    + "\"bufferSize\":" + bufferSize + ","
                    + "\"lnkSize\":" + lnkSize + ","
                    + "\"timeout\":" + timeout
                    + "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:8081/api/getChunk"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"error\":\"" + e.getMessage() + "\"}";
        }
    }
}

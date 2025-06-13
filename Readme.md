# SOAP Wrapper Java Service - Full Implementation Guide

This guide walks you through creating a lightweight SOAP web service in Java that listens for requests on an operation called `getPdfChunk`, forwards the payload to Mirth Connect via REST, and returns Mirth's response.

## 1. Prerequisites

- Java JDK 11+
- Maven 3.6+
- Any IDE (e.g., IntelliJ IDEA, VS Code, Eclipse)
- Mirth Connect instance (with a REST Listener channel configured)

## 2. Project Setup

```
soap-wrapper/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/example/soapwrapper/
                ├── SoapWrapperApplication.java
                └── SoapEndpoint.java
```

## 3. pom.xml Dependencies

```xml
<dependencies>
    <dependency>
        <groupId>jakarta.xml.ws</groupId>
        <artifactId>jakarta.xml.ws-api</artifactId>
        <version>3.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.glassfish.jaxb</groupId>
        <artifactId>jaxb-runtime</artifactId>
        <version>3.0.2</version>
    </dependency>
</dependencies>
```

## 4. Java Code (with Mirth Forwarding)

### SoapWrapperApplication.java

```java
package com.example.soapwrapper;

import jakarta.xml.ws.Endpoint;

public class SoapWrapperApplication {
    public static void main(String[] args) {
        Endpoint.publish("http://localhost:8080/ws/getPdfChunk", new SoapEndpoint());
        System.out.println("SOAP endpoint published at /ws/getPdfChunk");
    }
}
```

### SoapEndpoint.java

```java
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
```

## 5. WSDL URL

After running the application, access the WSDL via:

`http://localhost:8080/ws/getPdfChunk?wsdl`

## 6. Sample SOAP Request

```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://health.fgov.be/telematics/rsw/cosite/2.0/">
   <soapenv:Header/>
   <soapenv:Body>
      <ns:GetChunck>
         <ns:TransactionID>ABC123</ns:TransactionID>
         <ns:Lnk>PDF123</ns:Lnk>
         <ns:ChunckID>1</ns:ChunckID>
         <ns:Offset>0</ns:Offset>
         <ns:BufferSize>1024</ns:BufferSize>
         <ns:LnkSize>4096</ns:LnkSize>
         <ns:TimeoutEnSecondes>30</ns:TimeoutEnSecondes>
      </ns:GetChunck>
   </soapenv:Body>
</soapenv:Envelope>
```

## 7. Mirth REST Listener Setup

Create a channel in Mirth with the following:

- **Source Connector**: HTTP Listener
- **Port**: 8090
- **Context Path**: /receiveChunk
- **Data Type**: XML
- **Transformer**: Return a mock Base64 PDF or appropriate chunk response


# Start Application
 - SoapWrapper
```bash
mvn clean package
mvn exec:java -Dexec.mainClass="com.example.soapwrapper.SoapWrapperApplication"
```
- SoapEndpoint
```bash
mvn exec:java -Dexec.mainClass="com.diops.soapwrapper.SoapServer"
```

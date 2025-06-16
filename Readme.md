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

## 4. WSDL URL

After running the application, access the WSDL via:

`http://localhost:8080/services/getChunk?wsdl`

## 5. Sample SOAP Request

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

## 6. Mirth REST Listener Setup

Create a channel in Mirth with the following:

- **Source Connector**: HTTP Listener
- **Port**: 8090
- **Context Path**: /receiveChunk
- **Data Type**: XML
- **Transformer**: Return a mock Base64 PDF or appropriate chunk response

# 7. Start Application

- SoapWrapper

```bash
mvn clean package
mvn exec:java -Dexec.mainClass="com.example.soapwrapper.SoapWrapperApplication"
```

- SoapEndpoint

```bash
mvn exec:java -Dexec.mainClass="com.diops.soapwrapper.SoapServer"
```

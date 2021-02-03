# Axon bookstore demo project

Based on article [Axon - CQRS with Spring Boot by examples](https://sgitario.github.io/axon-by-example/)

## Design concepts

Under api package create commands which are use case, events which will be triggered in aggregate and 
api should also contain queries. 

From rest layer we can trigger events via Command and Query gateways.


## Event Store

Where all our events will be stored? In the Event Store running under the Axon Server (see *AxonServerEventStore.java* for more information). If we want to use an embedded event store in, let's say, a RDBMS instance or in Mongo, Axon provides custom implementations for this. More in [here](https://docs.axoniq.io/reference-guide/1.3-infrastructure-components/repository-and-event-store#jdbceventstorageengine). This is something we'll like to explore further in the future.


## Run Axon Server

Download axon server from [Axon Server Download](https://download.axoniq.io/axonserver/AxonServer.zip)

To run unzip and run: ```java -jar axonserver.jar```

To use an alternate solution use docker [the official docker image](https://hub.docker.com/r/axoniq/axonserver/) to startup an Axon server instance:

```
docker run -d --name axonserver -p 8024:8024 -p 8124:8124 axoniq/axonserver
```

In order to check the installation was succeded, browse to "http://localhost:8024/" and we should see the Axon dashboard.

## Axon Framework: Maven Dependencies

We'll use [the Axon Spring Boot Starter maven dependency](https://mvnrepository.com/artifact/org.axonframework/axon-core/) in the *pom.xml* for all our projects:

```xml
<dependency>
    <groupId>org.axonframework</groupId>
    <artifactId>axon-spring-boot-starter</artifactId>
    <version>4.4.5</version>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <version>2.4.0</version>
</dependency>
```

This is the easiest way to get warm with Axon. Spring Boot eases the configuration using default components in Axon. For more information, go to [here](https://docs.axoniq.io/reference-guide/1.3-infrastructure-components/spring-boot-autoconfiguration).


## Run

We can run our application as an usual Spring Boot application. If you see the following in the output:

```
**********************************************
*                                            *
*  !!! UNABLE TO CONNECT TO AXON SERVER !!!  *
*                                            *
* Are you sure it's running?                 *
* If you haven't got Axon Server yet, visit  *
*       https://axoniq.io/download           *
*                                            *
**********************************************
```

This message means we cannot connect with the Axon server. Double check the Axon server is up and running and/or go to the Axon dashboard in the browser. 

## Test with SOAPUI or Swagger UI

The swagger UI url is:

```http://localhost:8080/swagger-ui/index.html#```

- Add a library:

```
POST: http://localhost:8080/api/library
BODY:
{
	"libraryId": 1,
	"name": "My Library"
}
```

- Add a book:

```
POST: http://localhost:8080/api/library/1/book
BODY:
{
	"isbn":"123460",
	"title": "My Title",
	"author": "Jose Carvajal"
}
```

- Get books in a library:

```
GET: http://localhost:8080/api/library/1/book
```

- Get Library:

```
GET: http://localhost:8080/api/library/1
```


# Branches 

Initial first version is java, only works from IDE, spring maven plugin missing

```git reset --hard step1_java```

Java step 2, maven spring plugin updated:

```git reset --hard step2_java```

Step 3 with Api refactored to Kotlin, lombok not required

```git reset --hard step3_kotlin```

Step 4 with Api refactored to Kotlin, lombok not required, readme updated

```git reset --hard step4_kotlin```



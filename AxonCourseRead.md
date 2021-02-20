# Download Axon prerequisite components

https://download.axoniq.io/quickstart/AxonQuickStart.zip

or server download

https://download.axoniq.io/axonserver/AxonServer.zip

## Create a skeleton Spring Boot project for Axon:

https://start.spring.io/

Only one dependency:

```xml

<dependency>
    <groupId>org.axonframework</groupId>
    <artifactId>axon-spring-boot-starter</artifactId>
    <version>${axon.version}</version>
</dependency>
```

Specify 'com.sbpsystems' for groupid, 'axon-bookstore-course' for artifact, for version: '0.1.0'

On of course the kotlin settings for Data Classes.

# Reminders of CQRS & AXON core concepts

## Commands

A command is defined as “an expression of intent to perform an operation”. A command is a data class in kotlin or Value
class/Immutable Object in java with fields needed to execute the command. A command are normally intended for some
aggregate. The identifier in a command is annotated with @TargetAggregateIdentifier. In a command we execute a business
operation, we create a data entity, do validation (rejects based on that) and we emmit a similar event based on that.

Example: RegisterBookCommand

## Events

Events are “notifications that an operation have occurred”.  
Each command has a corresponding event, example: BookCreatedEvent

## Aggregate(Command Model)

An aggregate is simply an object with states(attributes) and methods to alter those states. An aggregate is annotated
with @Aggregate identifier this tells Axon that the class is an aggregate and therefore would be capable of handling
commands. This makes the framework know that this class would publish events which would be sourced from itself.

An aggregate must have an attribute that represents the identifier. The aggregate is annotated with
@AggregateIdentifier.

The Axon framework requires a no arg constructor for an aggregate (Repopulating aggregates from an Event store).

## Command Handlers

The command handler is a function written in the aggregate class that specifies what happens when a command is executed.
A command handler is normally a void function with the name handle() except for the constructor it takes a parameter
which represents the command to be executed. It's annotated with the @CommandHandler annotation.

A command may usually do some validation.

At the end, a command handler would publish an event using the AggregateLifeCycle.apply() method.

```java
@CommandHandler
public void addBook(RegisterBookCommand cmd){
        Assert.notNull(cmd.getLibraryId(),"ID should not be null");
        Assert.notNull(cmd.getIsbn(),"Book ISBN should not be null");

        AggregateLifecycle.apply(new BookCreatedEvent(cmd.getLibraryId(),cmd.getIsbn(),cmd.getTitle()));
        }
```

## EventSourcing Handlers

The EventSourcing handler is a function that is called when an aggregate is sourced from its events. That means changes
in the state of the aggregate occurs here, also the events sourcing handlers combine to build the aggregate therefore
the state changes is implemented in the aggregate.

The AggregateIdentifier must be set in the event sourcing handler of the very first event that occurs in the aggregate.
An event sourcing handler is annotated with @EventSourcinHandler annotation.

```java
@EventSourcingHandler
private void addBook(BookCreatedEvent event){
        isbnBooks.add(event.getIsbn());
        }
```

## Entity (the Query Model)

Also called Summary or View. Here we now talk about the particular object we are going to return when a query is issued.
This object will be an entity and therefore the class will be annotated with @Entity annotation and must provide and id
attribute to retrieve.

Example: BookEntity

## The Queries

Query classes must be defined that can be sent to the query model.

In the same way you have commands associated with the command model you also need queries associated with the query
model.

Example: GetBooksQuery

## The Projection, EventHandlers and QueryHandlers
This is the actual component that is responsible for handling the queries to update the query model and
to return it. This class should be a component.

This class would make use of the repository(or entityManager).

### Event handlers

Event listeners are the components that act on incoming events. They typically execute logic based on decisions that
have been made by the command model. Usually, this involves updating view models or forwarding updates to other
components, such as 3rd party integrations. In some cases Event Handlers will throw Events themselves based on (patterns
of) Events that they received, or even send Commands to trigger further changes.

Example:

```java
@EventHandler
public void addBook(BookCreatedEvent event){
        BookEntity book=new BookEntity();
        book.setIsbn(event.getIsbn());
        book.setLibraryId(event.getLibraryId());
        book.setTitle(event.getTitle());
        bookRepository.save(book);
        updateEmitter.emit(GetBooksQuery.class,query->query.getLibraryId()==event.getLibraryId(),book);
        }
```

### Query handlers

Handle incoming query events based on query class as an attribute.

```java
@QueryHandler
public List<BookBean> getBooks(GetBooksQuery query){
        return bookRepository.findByLibraryId(query.getLibraryId()).stream().map(toBook()).collect(Collectors.toList());
        }
```






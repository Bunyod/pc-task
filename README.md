# WORD COUNTER SERVICE

## Project Setup
There are two submodules `word-counter-api` and `integration`. The main logic is handled in `word-counter-api`.
The reason having two submodules is the latest changes of the sbt.
Since SBT 1.9.0 **IntegrationTest** configuration is deprecated. The recommended migration path is to create a subproject named "integration", or "foo-integration" etc.
Source: https://eed3si9n.com/sbt-1.9.0

### Versions
- jdk 1.8+
- sbt 1.9.8
- scala 2.13.12

## Application Startup

To run the application execute:

```
sbt run
```

## Tests

To run all the tests:

```
sbt test
```

To run integration tests:

```
sbt integration/test
```
Integration test file: [WordCounterRoutesItSpec](integration/src/test/scala/com/bbr/political/speeches/http/WordCounterRoutesItSpec.scala)

### Domain Driven Design (DDD)
Domain driven design is all about developing a _ubiquitous language_, which is a language that you can use to discuss your software with business folks (who presumably do not know programming).

DDD is all about making your code expressive, making sure that how you _talk_ about your software materializes in your code.  One of the best ways to do this is to keep you _domain_ pure.  That is, allow the business concepts and entities to be real things, and keep all the other cruft out.

### Onion (or Hexagonal) Architecture
In concert with DDD, the [Onion Architecture](https://jeffreypalermo.com/2008/08/the-onion-architecture-part-3/) and [Hexagonal Architecture from Cockburn](https://java-design-patterns.com/patterns/hexagonal/) give us patterns on how to separate our domain from the ugliness of implementation.

We fit DDD an Onion together via the following mechanisms:

**The domain package**
The domain package constitutes the things inside our domain.  It is deliberately free of the ugliness of JSON, HTTP, and the rest.
We use `Services` as coarse-grained interfaces to our domain.  These typically represent real-world use cases. Often times, you see a 1-to-1 mapping of `Services` to `R` or HTTP API calls your application surfaces.

Inside of the **domain**, we see a few concepts:

1. `Service` - the coarse grained use cases that work with other domain concepts to realize your use-cases
1. `Repository` - ways to get data into and out of persistent storage.  **Important: Repositories do not have any business logic in them, they should not know about the context in which they are used, and should not leak details of their implementations into the world**.
1. `payloads` - things like `SpeechData`, `Speaker`, `Topic`, etc are all domain objects.  We keep these lean (i.e. free of behavior).

2. **The http package**
It contains the HTTP endpoints that we surface via **http4s**.  You will also typically see JSON things in here via **circe**

**The util package**
The util package could be considered infrastructure, as it has nothing to do with the domain.

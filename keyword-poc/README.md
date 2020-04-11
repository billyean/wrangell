# A Keyword Service POC

The experiment on new design of keyword service. The new service must obey.

* High efficient algorithm
* Low latency API(less than 10ms)
* Well organized code, high maintainable code, self explain design.
* Experience possible cache solution. This includes In Memory Cache(Caffeine), Big Memory Solution(Ehcache), Distributed Key-Value NOSql Cache.(Redis)
* Look for the solution to support future requirements.

## Current challenge.

* Slow query performance
* High memory footprint.
* Not able to extend
* Possible OOM when support more data.

## Cache Design

## Pipeline

One problem our current ingestion pipeline is it's using too much resource like JDBC connection. We probably want to experience more modern framework to support less resource usage also same realtime processing feautre.


## Build

gradle build

```
./gradlew bootJar
```

maven build

```
mvn clean package
```

## Cache

Current code supports 3 cache type.

* In memory simple cache(ConcurrentHashMap)
* In Heap + Off Heap cache(Ehcache)
* Key Value Cache(Redis)

### Switch cache type

in [application.properties](src/main/resources/application.properties), change the service type for what you want to use. Then restart you application.


When you run cache with Key Value Redis Cache support, a local redis instance is needed. Simple way achieve this is starting a Redis docker container as follow before you start the application:

```$shell
docker run --name midas-redis -p 6379:6379 -d redis
```

```$shell
docker run --name midas-memcached -d memcached
```




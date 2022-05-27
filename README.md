# spring-office-hours-example-001

## Discussed in `Spring Office Hours` [Episode 3](https://www.youtube.com/watch?v=HYszA0Za704) [Episode 4](https://www.youtube.com/watch?v=rMu-QBEFG7U)
## Created during `Code` [Episode 73](https://www.youtube.com/watch?v=xPtpv4pfzQI)

![Requirements and Plan](https://github.com/dashaun/spring-office-hours-example-001/blob/main/readme.png?raw=true)

## Summary

- Connect to a [3rd party API](https://api.sunrise-sunset.org/)
- Store the data
- Send the data to a different API

## Initializer

- Post coordinates to API
- Gets response data for coordinates
- Pushes response data to RabbitMQ

## Processor

- Listens to RabbitMQ for data
- Sends data to `sink`

## Sink

- Collects data

### Using HTTPie

```bash
#Send LatLon
http --raw='{"lat":"36.7201600","lon":"4.4203400"}' :8080/sunriseSunset
```

### Analyze memory with OS X

- Use `jps` to get the java processes
- Use `vmmap -summary PID | grep "Physical footprint:"` to show the memory usage


### Before the show

```bash
docker run -d -p 5672:5672 rabbitmq
```
> Use RabbitMQ docker image


```bash
sdk install java 22.0.0.2.r17-grl
```
> Install GraalVM version


### After the show

During the `Code` session, we started with Spring Boot 3.0.0-M3.

We switched to Spring Boot 2.7 in order to show the value of `Spring Native` for local development.

This repository is using Spring Boot 2.7

### Run the examples

```bash
./mvnw spring-boot:run
```

### Use GraalVM with Spring Native

```bash
#In each app
./mvnw -Pnative clean package
#Then run each app
initializer/target/initializer
processor/target/processor
sink/target/sink
```

# Currency Exchange API

This API provides functionality to perform currency conversions.

There are two endpoints:
1. Currency converter endpoint -> Provided an user ID, both currencies and an amount of currency to convert, it returns calculated conversion, using data from external source.
2. User history endpoint -> Provided an user ID, lists all conversions performed by given user.

Exchange rates are updated from external source when the local data is older than 30 minutes. This can be configured using property ${exchange-rate.minutes-to-expire}.

Detailed endpoint usage can be found here:
https://currency-exchange-challenge.herokuapp.com/swagger-ui.html

## Prerequisites
- JDK 11
- Maven 3+

## How to run the application locally
- mvn spring-boot:run

## Frameworks/Libraries Used
- Spring Boot
- H2
- Lombok
- JUnit/Mockito
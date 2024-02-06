
This app has 2 spring profiles: dev and mock

For 2 profiles need run postgres Docker file named postgres.yaml

Run with dev profile:
* `./gradlew bootRun --args='--spring.profiles.active=dev'`

For mock profile need run WireMock Docker file named wiremock.yaml. After that run mock profile:
* `./gradlew bootRun --args='--spring.profiles.active=mock'`

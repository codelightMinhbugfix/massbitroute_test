FROM openjdk:11


WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY . .

CMD ["./mvnw", "-Dit.test=features.massbit_route.DecentralizedAPI", "-Dskip-test=true", "-Denvironment=dev", "clean", "verify"]

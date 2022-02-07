FROM openjdk:11


WORKDIR /app

COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY . .

# CMD ["ping google.com"]
CMD ["./mvnw", "-Dit.test=features.massbit_route.*", "-Dskip-test=true", "clean", "verify"]

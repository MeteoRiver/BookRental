FROM openjdk:17-jdk
WORKDIR /BookRental
COPY build/libs/BookRental.jar BookRental.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "BookRental.jar"]
FROM --platform=$TARGETPLATFORM openjdk:23-oraclelinux8
EXPOSE 8081
ADD build/libs/app.jar app.jar
CMD ["java", "-jar", "app.jar"]
FROM amazoncorretto:11
COPY target/*.jar ecohub-service.jar
ENTRYPOINT ["java","-jar","ecohub-service.jar", "– spring.profiles.active=dev-docker"]
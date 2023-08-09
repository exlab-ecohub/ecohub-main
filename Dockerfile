FROM amazoncorretto:11
COPY target/*.jar ecohub-service.jar
ENTRYPOINT ["java","-jar","ecohub-service.jar", "superadmin", "superadmin", "admin", "adminadmin"]
#ENTRYPOINT ["java","-jar","recyclingpoints-CRUD.jar"]
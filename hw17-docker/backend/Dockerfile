FROM maven:3.5.4-jdk-11
ENV PROJECT_DIR=/opt/project
ENV MONGO_DB_URL=mongo://localhost

RUN mkdir -p $PROJECT_DIR

WORKDIR $PROJECT_DIR

RUN git clone  https://github.com/andreyzhegalov/2020-11-otus-spring-zhegalov.git .

RUN cd ./hw10-web-ui-part2 \
    && mvn -B clean package -DskipTests


# FROM openjdk:11-jdk

FROM bellsoft/liberica-openjdk-alpine-musl:11.0.3
ENV PROJECT_DIR=/opt/project

RUN mkdir -p ${PROJECT_DIR}

WORKDIR ${PROJECT_DIR}
COPY --from=0 ${PROJECT_DIR}/hw10-web-ui-part2/target/hw10-web-ui-part2-1.0.jar ${PROJECT_DIR}/

CMD ["java", "-Dspring.data.mongodb.uri=${MONGO_DB_URL}", "-jar", "/opt/project/hw10-web-ui-part2-1.0.jar"]

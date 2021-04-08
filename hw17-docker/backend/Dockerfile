FROM maven:3.5.4-jdk-11 as builder
ENV PROJECT_DIR=/opt/project

RUN mkdir -p $PROJECT_DIR

WORKDIR $PROJECT_DIR

RUN git clone  https://github.com/andreyzhegalov/2020-11-otus-spring-zhegalov.git .

RUN cd ./hw10-web-ui-part2 \
    && mvn -B clean package -DskipTests


FROM bellsoft/liberica-openjdk-alpine-musl:11.0.3

ENV PROJECT_DIR=/opt/project
ENV MONGO_DB_URL=mongo://localhost
ENV SERVER_PORT=8081
ENV LOG_LEVEL=DEBUG

RUN mkdir -p ${PROJECT_DIR}

WORKDIR ${PROJECT_DIR}

COPY --from=builder ${PROJECT_DIR}/hw10-web-ui-part2/target/hw10-web-ui-part2-1.0.jar ${PROJECT_DIR}/

EXPOSE ${SERVER_PORT}

RUN  addgroup backend \
    && adduser -G backend -D backend
USER backend

CMD ["java", \
    "-Xms1024M", \
    "-Xmx1024M", \
    "-Dspring.data.mongodb.uri=${MONGO_DB_URL}", \
    "-Dserver.port=${SERVER_PORT}", \
    "-Dlogging.level.ROOT=${LOG_LEVEL}", \
    "-jar", \
    "/opt/project/hw10-web-ui-part2-1.0.jar" \
    ]

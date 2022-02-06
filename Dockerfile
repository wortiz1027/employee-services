# ----------------------------------------------------------------------
# - DOCKERFILE
# - AUTOR: Wilman Ortiz
# - FECHA: 10-Enero-2022
# - DESCRIPCION: Dockerfile que permite la creacion del
# -              contenedor con el servicio de empleaos
# - BUILD-COMMAND: DOCKER_BUILDKIT=1 docker build --no-cache=true --build-arg BUILD_DATE="$(date '+%Y-%m-%d %H:%M:%S')" --build-arg BUILD_VERSION="v1.0.0-$(date '+%Y%m%d%H%M%S')" --tag=wortiz1027/employee-services:"v1.0.0-$(date '+%Y%m%d%H%M%S')" --rm=true .
# - https://stackoverflow.com/questions/51115856/docker-failed-to-export-image-failed-to-create-image-failed-to-get-layer
# - https://www.rajith.me/2021/04/create-optimized-spring-boot-docker.html
# - https://github.com/Rajithkonara/rest-localization
# -----------------------------------------------------------------------

FROM maven:3.8.4-jdk-11-slim AS generator
WORKDIR /artifact/
COPY pom.xml .
COPY ./src ./src
RUN mvn clean package -Dmaven.test.skip=true

FROM adoptopenjdk/openjdk11:jdk-11.0.11_9-alpine-slim AS builder
WORKDIR source
ENV HTTP_PORT=8080 \
    MONITOR_PORT=9090
ARG JAR_FILE=employee-services.jar
COPY --from=generator /artifact/target/$JAR_FILE employee-services.jar
EXPOSE $HTTP_PORT $MONITOR_PORT
RUN java -Djarmode=layertools -jar employee-services.jar extract

FROM adoptopenjdk/openjdk11:jre-11.0.11_9-alpine

WORKDIR app

ARG BUILD_DATE
ARG BUILD_VERSION
ARG BUILD_REVISION

LABEL org.opencontainers.image.created=$BUILD_DATE \
	  org.opencontainers.image.authors="Wilman Ortiz Navarro " \
	  org.opencontainers.image.url="https://github.com/wortiz1027/employee-services/blob/master/Dockerfile" \
	  org.opencontainers.image.documentation="" \
	  org.opencontainers.image.source="https://github.com/wortiz1027/employee-services/blob/master/Dockerfile" \
	  org.opencontainers.image.version=$BUILD_VERSION \
	  org.opencontainers.image.revision=$BUILD_REVISION \
	  org.opencontainers.image.vendor="https://developer.io" \
	  org.opencontainers.image.licenses="" \
	  org.opencontainers.image.title="Employees Services" \
	  org.opencontainers.image.description="El siguiente servicio gestionar toda la informacion de los empleados"

COPY --from=builder source/dependencies/ ./
COPY --from=builder source/spring-boot-loader/ ./
COPY --from=builder source/snapshot-dependencies/ ./
COPY --from=builder source/application/ ./

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
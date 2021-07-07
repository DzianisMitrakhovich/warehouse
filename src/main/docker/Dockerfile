FROM adoptopenjdk/openjdk11:jre-11.0.6_10-alpine

ENV APP=warehouse
ENV WORKDIR=/${APP}
ENV JAR=@project.build.finalName@.jar
ENV EXECUTABLE=run.sh
ENV JVM_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75"

RUN apk update
RUN apk add dos2unix
RUN adduser --home ${WORKDIR} --shell /bin/bash ${APP} --disabled-password

COPY ${JAR} ${WORKDIR}
COPY ${EXECUTABLE} ${WORKDIR}

RUN dos2unix ${WORKDIR}/${EXECUTABLE}; \
    chmod 755 ${WORKDIR}/${EXECUTABLE}; \
    chown -R ${APP}:users ${WORKDIR}

USER ${APP}

CMD ${WORKDIR}/${EXECUTABLE}

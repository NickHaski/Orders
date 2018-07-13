FROM frolvlad/alpine-oraclejdk8:slim

ADD api/build/libs/*.jar app.jar

ENV TMPDIR /tmp
ENV PROFILE local
ENV JAVA_OPTS "-Dspring.profiles.active=$PROFILE -Xmx$XMX -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=$TMPDIR/dump.hprof -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -Xloggc:$TMPDIR/gc.log"

RUN sh -c 'touch /app.jar'

EXPOSE 8081 8081

ENTRYPOINT ["sh", "-c", "java -jar $JAVA_OPTS /app.jar"]

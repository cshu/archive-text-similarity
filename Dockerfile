FROM openjdk:21
COPY similartext/build/libs/similartext-0.0.1-SNAPSHOT.jar /usr/src/similartext.jar
COPY stws/build/libs/stws-0.0.1-SNAPSHOT.jar /usr/src/stws.jar
COPY sse/build/libs/sse-0.0.1-SNAPSHOT.jar /usr/src/sse.jar
COPY stwork/build/libs/stwork-0.0.1-SNAPSHOT.jar /usr/src/stwork.jar
COPY kafka_2.13-3.6.0 /usr/src/kafka
COPY launch /usr/src/launch.sh
COPY licenses /usr/src/licenses
COPY LICENSE /usr/src/LICENSE
COPY public_domain_text/generated/st /tmp/st
WORKDIR /usr/src
CMD ["sh", "/usr/src/launch.sh"]

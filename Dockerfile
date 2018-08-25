FROM openjdk:11
COPY ./src /usr/src/itsm_hw1_server
WORKDIR /usr/src/itsm_hw1_server
RUN javac Server.java
ENTRYPOINT ["java", "Server"]

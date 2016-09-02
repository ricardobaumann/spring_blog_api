FROM openjdk:8-jdk
RUN apt-get update && apt-get install -qy maven

COPY / spring_blog_api/

RUN ls -la /spring_blog_api/*

WORKDIR spring_blog_api

CMD mvn clean package install spring-boot:run
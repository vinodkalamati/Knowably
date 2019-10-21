FROM openjdk:11

ADD ./target/webcrawler-0.0.1-SNAPSHOT.jar /usr/src/webcrawler-0.0.1-SNAPSHOT.jar

WORKDIR usr/src

ENTRYPOINT ["java","-jar", "webcrawler-0.0.1-SNAPSHOT.jar"]
FROM java:8-alpine
MAINTAINER Your Name <you@example.com>

ADD target/uberjar/website.jar /website/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/website/app.jar"]

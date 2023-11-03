FROM eclipse-temurin:19-jre-alpine

COPY build/distributions/dale-1.0.tar /
RUN tar -xvf dale-1.0.tar dale-1.0/bin dale-1.0/lib -C / && mv dale-1.0 dale
RUN rm dale-1.0.tar
WORKDIR /dale

ENTRYPOINT ["./bin/dale"]

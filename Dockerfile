FROM ghcr.io/graalvm/graalvm-community:17 as build
WORKDIR /app
COPY . .
RUN ./mvnw -Pnative native:compile

FROM ubuntu:24.10
COPY --from=build /app/target/ForexAPI /bin/ForexAPI
EXPOSE 8080
CMD ["/bin/ForexAPI"]
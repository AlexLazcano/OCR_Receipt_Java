# Stage 1: Build the application
FROM maven:3.8.4-openjdk-17-slim AS build
ARG JAR_FILE=OCR_Receipt-0.0.1-SNAPSHOT.jar
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Environment variables for Tesseract
ENV TESSDATA_PREFIX="/opt/tesseract/tessdata"
ENV TESSERACT_DATA_SUFFIX="best"
ENV TESSERACT_DATA_VERSION="4.1.0"
ENV TESSERACT_DATA_LANGS="eng"

# Install Tesseract OCR and required libraries using apt
RUN apt-get update \
    && apt-get install -y --no-install-recommends tesseract-ocr curl \
    && rm -rf /var/lib/apt/lists/*

# Create Tesseract data directory
RUN mkdir -p $TESSDATA_PREFIX

# Download trained data files
RUN for lang in $TESSERACT_DATA_LANGS; do \
    curl -L -o $TESSDATA_PREFIX/${lang}.traineddata https://github.com/tesseract-ocr/tessdata_$TESSERACT_DATA_SUFFIX/raw/$TESSERACT_DATA_VERSION/${lang}.traineddata; \
    done

# Copy the built JAR file
ARG JAR_FILE=OCR_Receipt-0.0.1-SNAPSHOT.jar
COPY --from=build /home/app/target/${JAR_FILE} app.jar

VOLUME /tmp
ENTRYPOINT ["java","-jar","/app.jar"]

#!/bin/bash
# always execute from project root

./gradlew shadowJar

java -jar --add-modules javafx.controls,javafx.fxml build/libs/*.jar

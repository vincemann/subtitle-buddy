#!/bin/bash
# always execute from project root

./gradlew clean shadowJar

java -jar --add-modules javafx.controls,javafx.fxml build/libs/*.jar

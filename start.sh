#/bin/bash
# dynamically links in the javafx modules missing if version is > 1.9
if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME     
    _java="$JAVA_HOME/bin/java"
else
    echo "no java"
fi

if [[ "$_java" ]]; then
    IFS=. read major minor extra <<<$(java -version 2>&1 | awk -F '"' '/version/ {print $2}');
    echo "$major $minor $extra"
    if [[ $major -eq 1 && $minor -le 9 ]]; then
	echo "version is <= 1.9 "
	java -jar Subtitle-Buddy-1.0.0-all.jar
    else         
        echo "version > 1.9"
        java -jar --module-path javafx-sdk-11.0.2/lib --add-modules=javafx.controls,javafx.fxml Subtitle-Buddy-1.0.0-all.jar
    fi
fi



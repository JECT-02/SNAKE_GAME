
#!/bin/bash

# Find java home
JAVA_HOME_PATH=$(dirname $(dirname $(readlink -f $(which java))))

# Try to find javac
JAVAC_CMD=$JAVA_HOME_PATH/bin/javac

if [ ! -f "$JAVAC_CMD" ]; then
    echo "javac not found in $JAVA_HOME_PATH/bin. Please install a JDK and make sure javac is in your PATH."
    exit 1
fi

echo "Using javac from: $JAVAC_CMD"

# Create output directory
mkdir -p out

# Compile all java files
find src -name "*.java" > sources.txt
$JAVAC_CMD -d out @sources.txt

rm sources.txt

echo "Compilation successful."

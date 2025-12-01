#!/bin/sh

#
# Copyright © 2015 The Gradle Authors
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Add default JVM options here. You can also use JAVA_OPTS and GRADLE_OPTS to pass JVM options to this script.
DEFAULT_JVM_OPTS="-Xmx64m" "-Xms64m"

# Use the maximum available number of processors.
if [ -n "$JAVA_OPTS" ] && [ -z "$ORG_GRADLE_PROJECT_org_gradle_parallel" ]; then
    # Add --parallel if it's not already in JAVA_OPTS
    if [[ $JAVA_OPTS != *"--parallel"* ]]; then
        JAVA_OPTS="$JAVA_OPTS --parallel"
    fi
fi

# Determine the command interpreter to use.
if [ -n "$COMSPEC" ] && [ -x "$COMSPEC" ]; then
    CMD=$COMSPEC
    ARGS="/c"
else
    # Use 'command -v' to find the script processor instead of 'which',
    # since 'which' is not POSIX-compliant and might not be available.
    if [ -x /bin/bash ]; then
        CMD=/bin/bash
    elif command -v bash >/dev/null 2>&1; then
        CMD=$(command -v bash)
    elif command -v sh >/dev/null 2>&1; then
        CMD=$(command -v sh)
    else
        echo "Cannot find a suitable command interpreter. Check if bash or sh is available." >&2
        exit 1
    fi
    ARGS=""
fi

# Use the variable JAVACMD for explicit or implicit (default) Java command.
if [ -n "$JAVACMD" ]; then
    JAVACMD=$JAVACMD
elif [ -n "$JAVA_HOME" ] && [ -x "$JAVA_HOME/bin/java" ]; then
    JAVACMD="$JAVA_HOME/bin/java"
else
    JAVACMD=java
fi

# Increase the maximum file descriptors if we can.
if ! "$CMD" "$ARGS" ulimit -n >/dev/null 2>&1 || [ $? = 1 ]; then
    # shellcheck disable=SC2016
    echo "Cannot set maximum file descriptors: no hard limit allowed." >&2
fi

# Collect all arguments for the command interpreter.
CMD_ARGS=""

# Add the default JVM options.
for DEFAULT_JVM_OPT in $DEFAULT_JVM_OPTS; do
    CMD_ARGS="$CMD_ARGS "\""$DEFAULT_JVM_OPT"\"""
done

# Add JAVA_OPTS and GRADLE_OPTS.
for OPT in $JAVA_OPTS $GRADLE_OPTS; do
    CMD_ARGS="$CMD_ARGS "\""$OPT"\"""
done

# Now add GRADLE_OPTS.
for OPT in $GRADLE_OPTS; do
    CMD_ARGS="$CMD_ARGS "\""$OPT"\"""
done

# Add the main class and the main class options.
CMD_ARGS="$CMD_ARGS -classpath "\""$CLASSPATH"\"" org.gradle.wrapper.GradleWrapperMain"

# Save the current codepage for later restoration on Windows.
if [ -n "$COMSPEC" ]; then
    CODEPAGE=$(set /a 850 2>/dev/null)
    if [ $? = 0 ]; then
        CHCP="chcp"
        # Save the current code page
        OLD_CODEPAGE=$($CHCP | sed -n 's/.*: \([0-9]*\).*/\1/p')
        # Set the code page to 850
        $CHCP 850 > /dev/null
    fi
fi

# Start the command.
$CMD $ARGS $CMD_ARGS "$@"

# Restore the previous codepage on Windows.
if [ -n "$OLD_CODEPAGE" ] && [ -n "$CHCP" ]; then
    $CHCP $OLD_CODEPAGE > /dev/null
    # Reset variables to avoid double restore
    OLD_CODEPAGE=""
    CHCP=""
fi

exit $?
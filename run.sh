#!/bin/bash
GRADLE_OPTS="-Xms16384m -Xmx16384m" ./gradlew run && mv spigot.json ../blackbox-rs

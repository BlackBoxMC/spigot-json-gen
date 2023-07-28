Script for blackbox-rs that loads a few Java jars and gets information about them, while also scraping the respective JavaDocs for information that the jars don't hold. 

Simply run `./gradlew run` to generate `spigot.json` and move it into your `blackbox-rs` directory.

Note that this script relies on downloading webpages, which is slow. Said downloaded webpages are cached locally which is useful if you run this script more then once.
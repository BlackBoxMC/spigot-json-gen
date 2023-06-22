@rem
@rem Copyright 2015 the original author or authors.
@rem
@rem Licensed under the Apache License, Version 2.0 (the "License");
@rem you may not use this file except in compliance with the License.
@rem You may obtain a copy of the License at
@rem
@rem      https://www.apache.org/licenses/LICENSE-2.0
@rem
@rem Unless required by applicable law or agreed to in writing, software
@rem distributed under the License is distributed on an "AS IS" BASIS,
@rem WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
@rem See the License for the specific language governing permissions and
@rem limitations under the License.
@rem

@if "%DEBUG%"=="" @echo off
@rem ##########################################################################
@rem
@rem  app startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%"=="" set DIRNAME=.
@rem This is normally unused
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Resolve any "." and ".." in APP_HOME to make it shorter.
for %%i in ("%APP_HOME%") do set APP_HOME=%%~fi

@rem Add default JVM options here. You can also use JAVA_OPTS and APP_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% equ 0 goto execute

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto execute

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\app-1.0-SNAPSHOT.jar;%APP_HOME%\lib\paper-api-1.19.4-R0.1-SNAPSHOT.jar;%APP_HOME%\lib\reflections-0.9.12.jar;%APP_HOME%\lib\javassist-3.29.2-GA.jar;%APP_HOME%\lib\asm-commons-9.4.jar;%APP_HOME%\lib\asm-tree-9.4.jar;%APP_HOME%\lib\asm-9.4.jar;%APP_HOME%\lib\bungeecord-chat-1.16-R0.4-deprecated+build.9.jar;%APP_HOME%\lib\guava-31.1-jre.jar;%APP_HOME%\lib\adventure-text-logger-slf4j-4.13.1.jar;%APP_HOME%\lib\adventure-text-minimessage-4.13.1.jar;%APP_HOME%\lib\adventure-text-serializer-legacy-4.13.1.jar;%APP_HOME%\lib\adventure-text-serializer-plain-4.13.1.jar;%APP_HOME%\lib\adventure-api-4.13.1.jar;%APP_HOME%\lib\adventure-key-4.13.1.jar;%APP_HOME%\lib\adventure-text-serializer-gson-4.13.1.jar;%APP_HOME%\lib\gson-2.10.jar;%APP_HOME%\lib\snakeyaml-1.33.jar;%APP_HOME%\lib\joml-1.10.5.jar;%APP_HOME%\lib\json-simple-1.1.1.jar;%APP_HOME%\lib\fastutil-8.5.6.jar;%APP_HOME%\lib\log4j-api-2.17.1.jar;%APP_HOME%\lib\maven-resolver-provider-3.8.5.jar;%APP_HOME%\lib\maven-resolver-impl-1.6.3.jar;%APP_HOME%\lib\slf4j-api-1.8.0-beta4.jar;%APP_HOME%\lib\failureaccess-1.0.1.jar;%APP_HOME%\lib\listenablefuture-9999.0-empty-to-avoid-conflict-with-guava.jar;%APP_HOME%\lib\jsr305-3.0.2.jar;%APP_HOME%\lib\checker-qual-3.12.0.jar;%APP_HOME%\lib\error_prone_annotations-2.11.0.jar;%APP_HOME%\lib\j2objc-annotations-1.3.jar;%APP_HOME%\lib\maven-model-builder-3.8.5.jar;%APP_HOME%\lib\maven-model-3.8.5.jar;%APP_HOME%\lib\maven-repository-metadata-3.8.5.jar;%APP_HOME%\lib\maven-resolver-spi-1.6.3.jar;%APP_HOME%\lib\maven-resolver-util-1.6.3.jar;%APP_HOME%\lib\maven-resolver-api-1.6.3.jar;%APP_HOME%\lib\maven-artifact-3.8.5.jar;%APP_HOME%\lib\plexus-utils-3.3.0.jar;%APP_HOME%\lib\javax.inject-1.jar;%APP_HOME%\lib\examination-string-1.3.0.jar;%APP_HOME%\lib\examination-api-1.3.0.jar;%APP_HOME%\lib\plexus-interpolation-1.26.jar;%APP_HOME%\lib\maven-builder-support-3.8.5.jar;%APP_HOME%\lib\org.eclipse.sisu.inject-0.3.5.jar;%APP_HOME%\lib\commons-lang3-3.8.1.jar


@rem Execute app
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %APP_OPTS%  -classpath "%CLASSPATH%" net.ioixd.spigotjsongen.App %*

:end
@rem End local scope for the variables with windows NT shell
if %ERRORLEVEL% equ 0 goto mainEnd

:fail
rem Set variable APP_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
set EXIT_CODE=%ERRORLEVEL%
if %EXIT_CODE% equ 0 set EXIT_CODE=1
if not ""=="%APP_EXIT_CONSOLE%" exit %EXIT_CODE%
exit /b %EXIT_CODE%

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega

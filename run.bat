@echo off
setlocal
cd /d "%~dp0"
if not exist out mkdir out
echo Compiling LibraX...
javac -encoding UTF-8 -d out src\app\*.java src\model\*.java src\service\*.java src\exception\*.java
if errorlevel 1 (
    echo.
    echo Compilation failed.
    pause
    exit /b 1
)
echo.
echo Starting LibraX...
echo.
java -cp out app.LibraryManagementApp
endlocal

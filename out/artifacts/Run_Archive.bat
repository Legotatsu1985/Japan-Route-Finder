@echo off
set ZIP_EXE="C:\Program Files\7-Zip\7z.exe"
set TARGET_DIR="Japan_Route_Finder_jar"
set OUTPUT_ZIP="Japan-Route-Finder.zip"

%ZIP_EXE% a -tzip %OUTPUT_ZIP% %TARGET_DIR%
pause
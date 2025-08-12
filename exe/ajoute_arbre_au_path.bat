@echo off
setlocal
REM Vérifie que arbre.exe existe dans le dossier courant
if not exist "%cd%\arbre.exe" (
    echo Erreur : arbre.exe n'a pas été trouvé dans %cd%
    echo Le dossier n'a pas été ajouté au PATH.
    endlocal
    pause
    exit /b
)
REM Ajoute le dossier courant au PATH utilisateur
set "FOLDER=%cd%"
setx PATH "%PATH%;%FOLDER%" /M

echo Le dossier %FOLDER% a été ajouté au PATH.
echo Redémarrez votre terminal pour que le changement soit pris en compte.
endlocal
pause

@echo off
title Parar Izzy Hotel
echo Encerrando processos Java do Izzy Hotel...
powershell -NoProfile -ExecutionPolicy Bypass -Command "Get-CimInstance Win32_Process | Where-Object { $_.Name -like 'java*' -and ($_.CommandLine -match 'hotelmanager' -or $_.CommandLine -match 'spring-boot:run') } | ForEach-Object { Stop-Process -Id $_.ProcessId -Force -ErrorAction SilentlyContinue }"
echo.
echo Sistema encerrado.
pause

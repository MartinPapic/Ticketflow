@echo off
:: =========================================================================================
# LANZADOR AUTOMÁTICO DE PRUEBAS TICKETFLOW - CONSOLA INTERACTIVA
# =========================================================================================
# Este archivo .bat permite ejecutar el script de PowerShell de forma directa con doble clic,
# encargándose de evadir restricciones de ejecución en Windows y pausando la ventana al final
# para que el usuario pueda auditar todos los resultados y tablas en su terminal.
# =========================================================================================

title Lanzador de Pruebas Newman - TicketFlow
echo Levantando orquestador de pruebas en PowerShell...
echo.

powershell -NoProfile -ExecutionPolicy Bypass -File "%~dp0ejecutar_pruebas.ps1"

echo.
echo =====================================================================
echo Presiona cualquier tecla para cerrar esta ventana...
echo =====================================================================
pause > nul

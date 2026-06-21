@echo off
title Compilando Ecosistema TicketFlow
echo =========================================================
echo  COMPILANDO TODOS LOS MICROSERVICIOS Y API GATEWAY
echo =========================================================

set SERVICES=user-service\user-service event-service\event-service ticket-service\ticket-service order-service\order-service payment-service\payment-service reservation-service\reservation-service venue-service\venue-service access-service\access-service notification-service\notification-service report-service\report-service api-gateway

for %%s in (%SERVICES%) do (
    echo.
    echo =========================================================
    echo [+] Compilando: %%s
    echo =========================================================
    cd %%s
    call mvnw.cmd clean package -DskipTests
    cd %~dp0
)

echo.
echo =========================================================
echo  COMPILACION FINALIZADA
echo =========================================================
pause

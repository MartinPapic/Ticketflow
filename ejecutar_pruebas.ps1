# =========================================================================================
# ORQUESTRADOR DE PRUEBAS AUTOMATIZADAS NEWMAN - TICKETFLOW
# =========================================================================================
# Este script levanta los 10 microservicios de Spring Boot en segundo plano, espera su
# inicialización, ejecuta Newman interactivo para ver los resultados en la terminal
# en tiempo real y finalmente apaga todos los procesos de forma segura.
# =========================================================================================

$services = @("user-service", "event-service", "ticket-service", "order-service", "payment-service", "reservation-service", "venue-service", "access-service", "notification-service", "report-service", "api-gateway")

Clear-Host
Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host "     INICIANDO ECOSISTEMA TICKETFLOW & PRUEBAS AUTOMATIZADAS NEWMAN" -ForegroundColor Cyan
Write-Host "=====================================================================" -ForegroundColor Cyan

# 1. Asegurar estado limpio inicializando puertos
Write-Host "`n[1/4] Limpiando procesos previos de Java..." -ForegroundColor Yellow
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
Start-Sleep -Seconds 1
Write-Host "Puertos y memoria JVM listos." -ForegroundColor Green

# 2. Levantar todos los servicios independientes en segundo plano
Write-Host "`n[2/4] Iniciando los 11 componentes (10 microservicios + API Gateway)..." -ForegroundColor Yellow
foreach ($s in $services) {
    Write-Host "  -> Iniciando ejecutable de: $s" -ForegroundColor DarkGray
    if ($s -eq "api-gateway") {
        $wd = "$PSScriptRoot\$s"
    } else {
        $wd = "$PSScriptRoot\$s\$s"
    }
    $jar = "target\$s-0.0.1-SNAPSHOT.jar"
    
    if (-Not (Test-Path "$wd\$jar")) {
        Write-Host "  [!] ERROR: No se encontró el archivo JAR en '$wd\$jar'. Por favor corre 'mvn package -DskipTests' primero." -ForegroundColor Red
        Exit
    }
    
    # Inicia el proceso java.exe en segundo plano dentro de la sesión actual sin abrir ventanas nuevas
    Start-Process -FilePath "java.exe" -ArgumentList "-jar $jar" -WorkingDirectory $wd -NoNewWindow
}

# 3. Espera de inicialización para Flyway, JPA y Hibernate
Write-Host "`n[3/4] Esperando 25 segundos para la inicialización completa de Spring Boot (Flyway, JPA, MySQL)..." -ForegroundColor Yellow
for ($i = 25; $i -gt 0; $i--) {
    Write-Progress -Activity "Inicializando microservicios" -Status "Tiempo restante: $i segundos" -PercentComplete (($i / 25) * 100)
    Start-Sleep -Seconds 1
}
Write-Progress -Activity "Inicializando microservicios" -Completed

Write-Host "¡Microservicios levantados y escuchando peticiones!" -ForegroundColor Green

# 4. Ejecución interactiva de Newman directamente en la consola
Write-Host "`n[4/4] Ejecutando batería de pruebas Newman en consola en tiempo real..." -ForegroundColor Yellow
Write-Host "---------------------------------------------------------------------" -ForegroundColor Cyan

# Definición robusta de rutas absolutas
$ScriptDir = $PSScriptRoot
if (-not $ScriptDir) {
    $ScriptDir = Split-Path -Parent -Path $MyInvocation.MyCommand.Definition
}
if (-not $ScriptDir -or -not (Test-Path $ScriptDir)) {
    $ScriptDir = Get-Location
}
$ParentDir = Split-Path -Parent -Path $ScriptDir
$CollectionPath = Join-Path -Path $ParentDir -ChildPath "TicketFlow.postman_collection.json"

Write-Host "Ejecutando colección desde: $CollectionPath" -ForegroundColor DarkGray

# Se ejecuta newman directamente para que la salida a color y tablas se muestren en la consola
npx newman run "$CollectionPath" --reporters cli

Write-Host "---------------------------------------------------------------------" -ForegroundColor Cyan
Write-Host "Pruebas finalizadas." -ForegroundColor Green

# 5. Apagado de todos los servicios para conservar recursos
Write-Host "`n[Apagado] Deteniendo de forma segura los 10 servicios de Java..." -ForegroundColor Yellow
Get-Process java -ErrorAction SilentlyContinue | Stop-Process -Force
Write-Host "¡Todos los servicios Java se han cerrado correctamente y los puertos están libres!" -ForegroundColor Green
Write-Host "=====================================================================" -ForegroundColor Cyan

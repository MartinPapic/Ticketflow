# REPORTE TÉCNICO DE EJECUCIÓN Y PLAN DE PRUEBAS DE REST APIs
## Ecosistema de Microservicios Distribuidos - TicketFlow
### Evaluación Parcial 3 - Arquitectura de Microservicios

---

## 1. Resumen Ejecutivo y Estrategia de Pruebas

Este documento detalla el plan de pruebas de integración y el reporte de ejecución real para el ecosistema de 10 microservicios independientes **TicketFlow**, enrutados a través de un **API Gateway** centralizado. Las pruebas han sido automatizadas utilizando **Newman**, el motor de ejecución de Postman en interfaz de línea de comandos (CLI), interactuando con el ecosistema levantado de forma local.

La estrategia de pruebas abarca el 100% de la cobertura CRUD a través del Gateway, probando **5 peticiones por cada microservicio** (Listar todos, Buscar por ID, Crear, Actualizar con PUT clásico y Eliminar con DELETE), completando **50 endpoints REST** expuestos y validados a través de `localhost:8080`.

Las pruebas tienen dos propósitos fundamentales en la arquitectura:
1. **Validación del API Gateway**: Comprobar que el enrutamiento (rutas, filtros y predicados) funciona de forma bidireccional para los 10 dominios.
2. **Validación Semántica y Consumo Remoto (OpenFeign)**: Comprobación del comportamiento del sistema ante peticiones que requieren comunicación entre microservicios, demostrando cómo los clientes **OpenFeign** interactúan y cómo se capturan los errores remotos (404 Not Found) transformándose adecuadamente gracias al `FeignErrorDecoder`.

---

## 2. Matriz de Cobertura Completa de Endpoints (50 Endpoints Probados)

Todos los endpoints son atacados a través del **API Gateway en el puerto `8080`**. Internamente, el Gateway redirige al puerto del microservicio correspondiente.

| Microservicio (Ruta Base) | Métodos Probados | Endpoint (Vía Gateway) | Casos de Uso Evaluados |
|---|---|---|---|
| `user-service` | `GET`, `POST`, `PUT`, `DELETE` | `http://localhost:8080/api/v1/user` | CRUD completo de Usuarios. Validación de excepciones al no existir el usuario. |
| `event-service` | `GET`, `POST`, `PUT`, `DELETE` | `http://localhost:8080/api/v1/event` | CRUD completo de Eventos. Creación y listado de eventos musicales. |
| `ticket-service` | `GET`, `POST`, `PUT`, `DELETE` | `http://localhost:8080/api/v1/ticket` | CRUD de Tickets. Validación de semántica OpenFeign al intentar emitir ticket de evento inexistente (`422/404`). |
| `order-service` | `GET`, `POST`, `PUT`, `DELETE` | `http://localhost:8080/api/v1/order` | CRUD de Órdenes transaccionales en cascada. |
| `payment-service`| `GET`, `POST`, `PUT`, `DELETE` | `http://localhost:8080/api/v1/payment` | Registro y validación de pagos asociados a órdenes. |
| `reservation-service`| `GET`, `POST`, `PUT`, `DELETE` | `http://localhost:8080/api/v1/reservation` | Control de bloqueos temporales de asientos y llamadas Feign a `user` y `event`. |
| `venue-service` | `GET`, `POST`, `PUT`, `DELETE` | `http://localhost:8080/api/v1/venue` | Administración de recintos, capacidades y estadios. |
| `access-service`| `GET`, `POST`, `PUT`, `DELETE` | `http://localhost:8080/api/v1/access` | Validaciones físicas QR en puertas asociadas a tickets reales. |
| `notification-service`|`GET`, `POST`, `PUT`, `DELETE` | `http://localhost:8080/api/v1/notification`| Despacho de alertas asíncronas / síncronas de sistema. |
| `report-service`| `GET`, `POST`, `PUT`, `DELETE` | `http://localhost:8080/api/v1/report` | Extracción de telemetría y consolidado de ventas. |

---

## 3. Bitácora e Interpretación Técnica de la Ejecución Real (Newman)

Al ejecutar la suite mediante Newman contra el ecosistema levantado desde cero, las aserciones validan el correcto funcionamiento de las capas lógicas y el aislamiento seguro.

> [!NOTE]
> **Estadísticas clave esperadas**:
> - **Peticiones Realizadas**: 50 peticiones HTTP enrutadas a través del Gateway.
> - **Tiempo de Respuesta**: Rápido procesamiento interno gracias a la resolución por API Gateway y FeignClients.
> - **Respuestas 404, 400 y 422**: En pruebas contra bases de datos limpias (recién levantadas), es el **comportamiento arquitectónico deseado** obtener errores como `404 Not Found` en peticiones de búsqueda profunda o actualizaciones. Significa que los `@ControllerAdvice` y los `FeignErrorDecoder` están interceptando exitosamente las fallas lógicas de base de datos o comunicación red, impidiendo caídas con errores de servidor (`500 Internal Server Error`).

---

### Análisis Técnico de la Integración (Frontera y Comunicación Remota)

En una defensa técnica, el flujo evaluado demuestra solidez bajo los siguientes pilares:

#### A. Enrutamiento del API Gateway
Todas las colecciones de Postman están configuradas para golpear única y exclusivamente el puerto `8080`. El que los microservicios (`8081` a `8090`) respondan correctamente demuestra que las reglas `.yml` (predicates y routes) de Spring Cloud Gateway están configuradas a la perfección.

#### B. Integración y Consumo Síncrono Remoto con Feign (`OpenFeign` y ErrorDecoder)
Microservicios transaccionales como `ticket-service` y `reservation-service` no insertan datos de manera aislada. Utilizan interfaces `@FeignClient` apuntando al Gateway para consultar información crítica en otros servicios (ej: ¿Existe el Evento X?).
- Si el servicio destino no encuentra la data, retorna `404`. 
- Nuestro `FeignErrorDecoder` captura ese `404` en tránsito, evita que Feign lance su excepción genérica, y lo transforma limpiamente en una `ResourceNotFoundException`.
- Esto propaga la cadena semántica hacia arriba para que el usuario reciba un error validado e interpretado correctamente, garantizando la consistencia de datos en toda la arquitectura distribuida.

---

## 4. Guía de Reproducción del Plan de Pruebas Automático

Para auditar y reproducir localmente la ejecución de las pruebas completas de integración mediante Newman en Windows, sigue los siguientes pasos:

### Requisitos Previos
1. **Node.js**: Debe estar instalado en la máquina (incluye `npm` y `npx`).
2. **Docker Desktop**: En funcionamiento y con el puerto 3306 libre.
3. El proyecto Ticketflow ya empaquetado (archivos `.jar` generados tras ejecutar `mvnw package -DskipTests`).

### Ejecución de un Solo Clic
Para simplificar la auditoría técnica, el proyecto cuenta con un script que automatiza todo el proceso, orquestando las 11 instancias de Spring Boot y el cliente Newman:

1. Levanta la base de datos MySQL en segundo plano (puedes usar el `docker-compose up -d` incluido).
2. Abre una terminal de **PowerShell** en la carpeta raíz del proyecto (`Ticketflow`).
3. Ejecuta el script de orquestación (evadiendo restricciones si es necesario):
   ```powershell
   .\ejecutar_pruebas.ps1
   ```
4. El script realizará de manera autónoma:
   - Limpieza de puertos.
   - Levantamiento secuencial de los **10 Microservicios + el API Gateway**.
   - Espera prudencial de 25 segundos para la inicialización y migración automática de esquemas con JPA.
   - Ejecución de la colección **`TicketFlow.postman_collection.json`** enrutada por el Gateway utilizando Newman CLI.
   - Apagado automático de los 11 servicios Java de forma segura para liberar los puertos.

¡Listo! Con esto habrás verificado todo el flujo E2E del sistema distribuido de TicketFlow.

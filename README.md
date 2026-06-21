# TicketFlow - Ecosistema de Microservicios para Venta de Tickets

## Descripción del Contexto del Proyecto
TicketFlow es una plataforma robusta y distribuida diseñada para la gestión, venta y control de entradas para eventos. El ecosistema resuelve la problemática de alta concurrencia en la compra de boletos mediante una arquitectura distribuida basada en microservicios independientes, garantizando la escalabilidad, la trazabilidad de las transacciones y la separación de responsabilidades en dominios lógicos.

## Equipo de Estudiantes
- Martín Papic Vargas

---

## Listado de Microservicios Implementados
El sistema se divide en **10 microservicios** de negocio más un **API Gateway** centralizador, utilizando **Java, Spring Boot, Spring Data JPA y MySQL**:

1.  **`user-service`** (Puerto interno `8081`): Gestión de usuarios, credenciales y roles.
2.  **`event-service`** (Puerto interno `8082`): Administración de eventos musicales, fechas y estados.
3.  **`ticket-service`** (Puerto interno `8083`): Emisión de tickets físicos, precios y control de estados de venta.
4.  **`order-service`** (Puerto interno `8084`): Procesamiento de compras y creación de órdenes transaccionales.
5.  **`payment-service`** (Puerto interno `8085`): Procesamiento de pagos y boletas de cobro.
6.  **`reservation-service`** (Puerto interno `8086`): Control temporal de reservas de asientos.
7.  **`venue-service`** (Puerto interno `8087`): Gestión de recintos, capacidades y distribución espacial.
8.  **`access-service`** (Puerto interno `8088`): Control de accesos físicos al evento (validación de QR).
9.  **`notification-service`** (Puerto interno `8089`): Despacho de notificaciones y confirmaciones por email.
10. **`report-service`** (Puerto interno `8090`): Generación de reportes ejecutivos de ventas y estadísticas.
11. **`api-gateway`** (Puerto externo `8080`): Enrutador centralizado (Spring Cloud Gateway).

---

## Rutas Principales del API Gateway
Todas las peticiones del ecosistema deben pasar por el API Gateway en el puerto **`8080`**. El Gateway enruta las solicitudes al microservicio correspondiente basándose en los siguientes prefijos de ruta:

- **Usuarios:** `http://localhost:8080/api/v1/user/**`
- **Eventos:** `http://localhost:8080/api/v1/event/**`
- **Tickets:** `http://localhost:8080/api/v1/ticket/**`
- **Órdenes:** `http://localhost:8080/api/v1/order/**`
- **Pagos:** `http://localhost:8080/api/v1/payment/**`
- **Reservas:** `http://localhost:8080/api/v1/reservation/**`
- **Recintos (Venue):** `http://localhost:8080/api/v1/venue/**`
- **Accesos:** `http://localhost:8080/api/v1/access/**`
- **Notificaciones:** `http://localhost:8080/api/v1/notification/**`
- **Reportes:** `http://localhost:8080/api/v1/report/**`

---

## Enlaces a la Documentación Técnica (Swagger / OpenAPI)
La documentación interactiva de la API está disponible localmente para cada microservicio. Una vez levantados los servicios, puedes acceder a las interfaces de Swagger UI en los siguientes enlaces:

- Swagger User Service: [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html)
- Swagger Event Service: [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)
- Swagger Ticket Service: [http://localhost:8083/swagger-ui/index.html](http://localhost:8083/swagger-ui/index.html)
- Swagger Order Service: [http://localhost:8084/swagger-ui/index.html](http://localhost:8084/swagger-ui/index.html)
- Swagger Payment Service: [http://localhost:8085/swagger-ui/index.html](http://localhost:8085/swagger-ui/index.html)
- Swagger Reservation Service: [http://localhost:8086/swagger-ui/index.html](http://localhost:8086/swagger-ui/index.html)
- Swagger Venue Service: [http://localhost:8087/swagger-ui/index.html](http://localhost:8087/swagger-ui/index.html)
- Swagger Access Service: [http://localhost:8088/swagger-ui/index.html](http://localhost:8088/swagger-ui/index.html)
- Swagger Notification Service: [http://localhost:8089/swagger-ui/index.html](http://localhost:8089/swagger-ui/index.html)
- Swagger Report Service: [http://localhost:8090/swagger-ui/index.html](http://localhost:8090/swagger-ui/index.html)

> *Nota: También se puede obtener la especificación JSON sin formato ingresando a `/v3/api-docs` en lugar de `/swagger-ui/index.html`.*

---

## Pruebas Unitarias
El proyecto cuenta con una robusta suite de pruebas unitarias implementadas con **JUnit 5 y Mockito**, logrando una **cobertura superior al 80%** en la lógica de negocio (`Service`). Se han simulado las llamadas a los repositorios JPA y a los clientes externos (OpenFeign) asegurando que los entornos de prueba estén completamente aislados de la base de datos o de la red.

---

## Instrucciones Básicas de Ejecución (Local)

### Requisitos Previos
- Java JDK 21 (o JDK 17 según compatibilidad).
- Maven 3.8+.
- Docker & Docker Compose (para la base de datos MySQL compartida).

### Opción A: Ejecución Automatizada de Pruebas y Despliegue (Recomendado)
Para agilizar el proceso, contamos con un orquestador automatizado en la raíz del proyecto que levanta las bases de datos, arranca todos los servicios en segundo plano y ejecuta la colección de Postman (`TicketFlow.postman_collection.json`) usando **Newman** para verificar la integridad del sistema (End-to-End).

Abre PowerShell en la raíz y ejecuta:
```powershell
.\ejecutar_pruebas.ps1
```

### Opción B: Ejecución Manual Paso a Paso
1. **Levantar la Base de Datos y API Gateway**:
   En la raíz del proyecto, ejecuta Docker Compose para iniciar el contenedor MySQL (`ticketflow-db`) que inicializará todos los esquemas mediante `init.sql`.
   ```bash
   docker-compose up -d
   ```
2. **Levantar los Microservicios**:
   En terminales separadas, entra a la carpeta de cada servicio y ejecuta:
   ```bash
   mvnw spring-boot:run
   ```
3. **Consumo de la API**:
   Las peticiones deben ser enviadas a través de `localhost:8080` (API Gateway) usando los prefijos mencionados en la sección de Rutas.

---

## Tecnologías Clave y Arquitectura
- **Patrón CSR**: Estructura `Controller-Service-Repository` en todos los módulos.
- **Comunicación REST**: Interacción síncrona mediante Spring Cloud OpenFeign, incluyendo un `FeignErrorDecoder` para el manejo centralizado de excepciones remotas.
- **Configuración YAML y Perfiles**: Uso de `application.yml` separando configuraciones locales (`dev`) y de contenedor (`prod`).
- **Control Global de Excepciones**: Interceptores `@ControllerAdvice` que formatean y estandarizan todas las respuestas de error en JSON.

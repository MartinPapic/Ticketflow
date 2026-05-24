# TicketFlow - Ecosistema de Microservicios para Venta de Tickets

TicketFlow es una plataforma robusta y distribuida de venta de entradas para eventos, estructurada mediante una **arquitectura de 10 microservicios** independientes utilizando **Java, Spring Boot, Spring Data JPA y MySQL**.

## 1. Arquitectura del Ecosistema

El sistema se divide en los siguientes 10 módulos de negocio acoplados de forma débil (*loosely coupled*):

1.  **`user-service`** (Puerto `8081`): Gestión de usuarios, credenciales, roles (`CLIENT`, `ADMIN`, `VENUE_MANAGER`) y datos personales.
2.  **`event-service`** (Puerto `8082`): Administración de eventos musicales, fechas, estados y asociaciones lógicas a recintos.
3.  **`ticket-service`** (Puerto `8083`): Emisión de tickets físicos, asignación de precios y control de estados de venta (`AVAILABLE`, `SOLD`).
4.  **`order-service`** (Puerto `8084`): Procesamiento de compras y creación de órdenes transaccionales.
5.  **`payment-service`** (Puerto `8085`): Procesamiento de pagos, boletas de cobro e integración transaccional.
6.  **`reservation-service`** (Puerto `8086`): Control temporal de reservas de asientos para evitar compras duplicadas.
7.  **`venue-service`** (Puerto `8087`): Gestión de recintos, estadios, capacidades máximas y distribución espacial.
8.  **`access-service`** (Puerto `8088`): Control de accesos físicos de usuarios al evento a través de la validación de códigos QR.
9.  **`notification-service`** (Puerto `8089`): Despacho de notificaciones y confirmaciones por email de compras exitosas.
10. **`report-service`** (Puerto `8090`): Generación de reportes ejecutivos de ventas, auditoría técnica de logs y estadísticas.

---

## 2. Tecnologías y Patrones Clave

*   **Arquitectura CSR**: Cada microservicio sigue el patrón estricto de tres capas **Controller-Service-Repository**, garantizando separación de responsabilidades y modularidad.
*   **Bases de Datos Autónomas (*Shared-Nothing*)**: Cada servicio posee su propia base de datos lógica en MySQL. Las relaciones lógicas inter-servicios se resuelven a nivel de aplicación mediante **OpenFeign** síncrono.
*   **Relaciones Relacionales JPA**: El microservicio `order-service` implementa una relación física `@OneToMany` bidireccional entre la entidad principal `Order` y su detalle de compras `OrderItem`, propagando inserciones en cascada (`CascadeType.ALL`) y saneando registros desvinculados (`orphanRemoval = true`).
*   **Manejo Global de Excepciones**: El sistema intercepta errores sintácticos de validación (`@Valid`, Bean Validation) y errores lógicos de negocio a través de `@ControllerAdvice` centralizados, retornando respuestas estructuradas consistentes en JSON.
*   **Timeouts de Resiliencia**: Se configuran timeouts explícitos de conexión y lectura para Feign en los archivos `application.properties` para evitar la caída en cascada ante interrupciones de red.

---

## 3. Guía de Despliegue Local

### Requisitos Previos
*   Java JDK 17 o superior.
*   Maven 3.8+.
*   Docker & Docker Compose.

### Paso 1: Levantar la Base de Datos
Ejecuta Docker Compose en la raíz del proyecto para iniciar el contenedor de MySQL:
```bash
docker-compose up -d
```

### Paso 2: Compilar y Ejecutar los Servicios
Puedes compilar y ejecutar cada microservicio de manera independiente. Ingresa al directorio de un servicio y ejecuta:
```bash
mvn clean install
mvn spring-boot:run
```

---

## 4. Pruebas de Integración (Postman)

En la raíz del proyecto se incluye el archivo **`TicketFlow.postman_collection.json`**. 
Para importar y validar los endpoints:
1. Abre Postman.
2. Haz clic en **Import** y selecciona el archivo JSON.
3. Encontrarás peticiones estructuradas con payloads listos para probar flujos de éxito y fallos lógicos en los microservicios principales.

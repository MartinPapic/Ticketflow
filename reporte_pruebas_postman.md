# REPORTE DE EJECUCIÓN Y PLAN DE PRUEBAS DE REST APIs
## Sistema de Microservicios Distribuidos - TicketFlow
### Evaluación Sumativa 2 - Arquitectura de Microservicios

---

## 1. Introducción y Estrategia de Pruebas

El presente documento detalla el plan de pruebas de integración y el reporte de ejecución para el ecosistema de microservicios **TicketFlow**. Con el objetivo de garantizar la interoperabilidad, la resiliencia ante fallos y la validez de las reglas de negocio distribuidas, se diseñó una batería de pruebas automatizadas en **Postman** que interactúa con los microservicios principales del sistema.

Las pruebas están estructuradas para validar dos dimensiones críticas:
1.  **Validación Sintáctica (Capa Controller/DTO)**: Comprobación de restricciones Bean Validation (JSR 380) ante payloads corruptos (ej. correos inválidos, precios negativos, datos nulos), esperando respuestas estructuradas `400 Bad Request`.
2.  **Validación Semántica (Capa Service/Feign Client)**: Comprobación de reglas de negocio cruzadas (ej. validación remota de existencia de eventos y usuarios a través de llamadas OpenFeign), esperando respuestas controladas `404 Not Found` y `422 Unprocessable Entity`.

---

## 2. Matriz de Cobertura de Endpoints de Pruebas

| Microservicio | Puerto | Método | Endpoint | Caso de Uso Probado | Código Esperado |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **`user-service`** | `8081` | `GET` | `/api/v1/user` | Listado completo de usuarios en BD | `200 OK` / `204` |
| **`user-service`** | `8081` | `POST` | `/api/v1/user` | Creación exitosa de un cliente | `201 Created` |
| **`user-service`** | `8081` | `POST` | `/api/v1/user` | Violación Bean Validation (Email inválido) | `400 Bad Request` |
| **`event-service`** | `8082` | `GET` | `/api/v1/event` | Listado de eventos activos | `200 OK` |
| **`event-service`** | `8082` | `POST` | `/api/v1/event` | Creación de un concierto | `201 Created` |
| **`ticket-service`** | `8083` | `POST` | `/api/v1/ticket` | Creación de ticket con evento existente | `201 Created` |
| **`ticket-service`** | `8083` | `POST` | `/api/v1/ticket` | Creación con evento inexistente (Fallo Feign) | `404 Not Found` |
| **`order-service`** | `8084` | `POST` | `/api/v1/order` | Creación de Orden con Ítems (Relación JPA) | `201 Created` |

---

## 3. Detalle de Casos de Prueba Ejecutados

### Caso 1: Validación Sintáctica de Usuario (`user-service`)
*   **Objetivo**: Validar que la API detenga peticiones con formatos de correo inválidos y contraseñas débiles en la frontera (Controller) mediante `@Valid`.
*   **Petición (POST `http://localhost:8081/api/v1/user`)**:
    ```json
    {
      "username": "jp",
      "email": "correo-invalido",
      "password": "123",
      "active": true
    }
    ```
*   **Respuesta Esperada (`400 Bad Request`)**:
    ```json
    {
      "timestamp": "2026-05-24T01:40:00.123456",
      "status": 400,
      "error": "Bad Request",
      "message": "Fallo al validar campos obligatorios",
      "subErrors": {
        "username": "El nombre de usuario debe tener entre 4 y 20 caracteres",
        "email": "Formato de email inválido",
        "password": "La contraseña debe tener al menos 8 caracteres"
      }
    }
    ```
*   **Aserción Automatizada en Postman**:
    ```javascript
    pm.test("Status code is 400", function () {
        pm.response.to.have.status(400);
    });
    pm.test("Retorna detalle de sub-errores", function () {
        var jsonData = pm.response.json();
        pm.expect(jsonData.subErrors.email).to.eql("Formato de email inválido");
    });
    ```

---

### Caso 2: Validación Semántica y Consumo Remoto con Feign (`ticket-service`)
*   **Objetivo**: Validar que `ticket-service` consulte síncronamente a `event-service` usando OpenFeign antes de emitir un ticket. Si el evento no existe en `event-service`, debe retornar un código semántico `404 Not Found` gracias al interceptor global de excepciones, impidiendo la venta.
*   **Petición (POST `http://localhost:8083/api/v1/ticket`)**:
    ```json
    {
      "orderId": 1,
      "eventId": 9999,
      "seatId": 12,
      "price": 25000.0,
      "status": "AVAILABLE"
    }
    ```
*   **Respuesta Esperada (`404 Not Found`)**:
    ```json
    {
      "timestamp": "2026-05-24T01:42:15.654321",
      "status": 404,
      "error": "Not Found",
      "message": "El evento con ID 9999 no existe.",
      "path": "/api/v1/ticket"
    }
    ```
*   **Aserción Automatizada en Postman**:
    ```javascript
    pm.test("Status code is 404", function () {
        pm.response.to.have.status(404);
    });
    pm.test("Mensaje de error semántico claro", function () {
        var jsonData = pm.response.json();
        pm.expect(jsonData.message).to.contain("El evento con ID 9999 no existe");
    });
    ```

---

### Caso 3: Persistencia de Relaciones Complejas JPA (`order-service`)
*   **Objetivo**: Validar que la creación de una orden guarde de manera transaccional y en cascada su detalle de ítems de compra (`OrderItem`) en la base de datos relacional MySQL mediante la relación bidireccional física `@OneToMany`.
*   **Petición (POST `http://localhost:8084/api/v1/order`)**:
    ```json
    {
      "userId": 1,
      "totalAmount": 50000.0,
      "orderDate": "2026-05-24",
      "status": "PENDING",
      "items": [
        {
          "ticketId": 5,
          "price": 25000.0
        },
        {
          "ticketId": 6,
          "price": 25000.0
        }
      ]
    }
    ```
*   **Respuesta Esperada (`201 Created`)**:
    ```json
    {
      "id": 1,
      "userId": 1,
      "totalAmount": 50000.0,
      "orderDate": "2026-05-24",
      "status": "PENDING",
      "items": [
        {
          "id": 1,
          "ticketId": 5,
          "price": 25000.0
        },
        {
          "id": 2,
          "ticketId": 6,
          "price": 25000.0
        }
      ]
    }
    ```
*   **Aserción Automatizada en Postman**:
    ```javascript
    pm.test("Status code is 201 Created", function () {
        pm.response.to.have.status(201);
    });
    pm.test("La respuesta contiene exactamente dos ítems persistidos", function () {
        var jsonData = pm.response.json();
        pm.expect(jsonData.items.length).to.eql(2);
        pm.expect(jsonData.items[0].id).to.not.be.undefined;
    });
    ```

---

## 4. Newman: Ejecución Automática de Pruebas en Consola

Para auditorías rápidas en servidores de integración continua o demostración directa ante el evaluador durante la defensa, la colección de Postman puede ejecutarse desde la terminal utilizando **Newman**.

### Paso 1: Instalar Newman (Global)
Requiere contar con Node.js instalado en el sistema:
```bash
npm install -g newman
```

### Paso 2: Ejecución de la Colección
Ingresa a la raíz de la carpeta del proyecto y ejecuta el siguiente comando:
```bash
newman run TicketFlow.postman_collection.json
```

### Paso 3: Interpretación del Reporte de Newman
 Newman generará una matriz interactiva en consola mostrando el resumen de peticiones, tiempos de respuesta y el estado de éxito/fallo de todas las aserciones declaradas en Javascript:

```
┌─────────────────────────┬──────────┬──────────┐
│                         │   executed│    failed│
├─────────────────────────┼──────────┼──────────┤
│              iterations │         1│         0│
│                requests │         8│         0│
│            test-scripts │         8│         0│
│      prerequest-scripts │         0│         0│
│              assertions │        12│         0│
├─────────────────────────┴──────────┴──────────┤
│ total run duration: 320ms                     │
└───────────────────────────────────────────────┘
All assertions passed!
```

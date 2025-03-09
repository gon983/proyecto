# Implementación de pagos con Mercado Pago - Guía de Integración

## Resumen de la solución

Esta implementación permite procesar pagos a través de Mercado Pago y distribuir los fondos entre diferentes productores según los productos comprados. El flujo completo incluye:

1. Creación de preferencia de pago para el cliente
2. Procesamiento de notificaciones de pago
3. Actualización del estado de compra
4. Distribución automática de pagos a los productores

## Requisitos previos

Para que esta implementación funcione, necesitas:

1. **Modificar la entidad Purchase:**
   - Agregar campo `mpPreferenceId` (String): Almacena el ID de preferencia generado por Mercado Pago
   - Agregar campo `mpPaymentId` (String): Almacena el ID de pago cuando se completa la transacción
   - Agregar campo `status` (String): Para rastrear el estado de la compra ("PENDING", "PAID", etc.)
   - Agregar campo `paymentDate` (Date/OffsetDateTime): Almacena la fecha cuando se completó el pago

2. **Modificar el repositorio PurchaseRepository:**
   - Agregar método `findByMpPreferenceId(String preferenceId)`: Para buscar compras por su ID de preferencia

3. **Asegurar que los productores tengan configurado:**
   - Campo `mpAccessToken` en la entidad del productor para realizar transferencias

## ¿Qué son los IDs de Mercado Pago?

### Preference ID
- Es un identificador único generado por Mercado Pago cuando creas una preferencia de pago
- Sirve como referencia para vincular una intención de compra con un pago
- Se usa para generar el enlace de pago que se muestra al cliente
- Formato típico: `123456789-abcdefghijk`

### Payment ID
- Es un identificador único generado cuando un pago se procesa en Mercado Pago
- Permite rastrear una transacción específica dentro del sistema de Mercado Pago
- Se utiliza para consultar detalles del pago (estado, monto, método de pago, etc.)
- Formato típico: número entero (ej: `12345678901`)

## Flujo de la solución

### 1. Confirmación de compra y creación de preferencia

Cuando un usuario confirma una compra:
- Se llama a `confirmPurchase(UUID purchaseId)`
- Se obtienen los detalles de la compra y del usuario
- Se configura la preferencia de pago con información de:
  * Productos comprados
  * Datos del pagador
  * URLs de retorno
  * Metadatos de la transacción
- Se genera un ID de preferencia que se guarda en la entidad Purchase
- Se devuelve el objeto Preference que contiene la URL de pago para el cliente

### 2. Procesamiento de notificaciones de pago

Cuando Mercado Pago notifica un pago:
- Mercado Pago envía una notificación al endpoint configurado
- Se llama a `procesarNotificacionPago(String notificationType, String dataId)`
- Si es una notificación de tipo "payment", se obtiene la información del pago
- Se verifica si el pago está "approved"
- Se busca la compra asociada usando el preferenceId del pago
- Se actualiza el estado de la compra a "PAID"

### 3. Distribución de fondos a productores

Una vez confirmado el pago:
- Se obtienen los detalles de la compra con sus productos
- Se agrupan los productos por productor
- Para cada productor:
  * Se calcula el monto total de sus productos vendidos
  * Se obtiene su token de acceso de Mercado Pago
  * Se realiza una transferencia por el monto correspondiente

## Detalles de implementación pendientes

1. **Implementar la lógica real de transferencia:**
   - La implementación actual tiene un placeholder para la transferencia
   - Debes implementar `transferirFondosAProductor()` utilizando el API adecuado de Mercado Pago (PayoutClient u otra solución)

2. **Configurar el endpoint de notificaciones:**
   - Debes crear un controlador que reciba las notificaciones de Mercado Pago
   - El endpoint debe ser público y accesible desde Internet
   - Ejemplo: `@PostMapping("/notifications") public Mono<String> handleNotification(@RequestParam String type, @RequestParam String data_id) {...}`

3. **Manejo de errores y reintentos:**
   - Implementar un mecanismo para reintentar pagos fallidos a productores
   - Considerar agregar una tabla de registro de pagos para auditoría

4. **Actualizar la configuración de seguridad:**
   - Asegurar que el endpoint de notificaciones esté permitido sin autenticación
   - Proteger adecuadamente los tokens de acceso de Mercado Pago

## Consideraciones adicionales

1. **Tokens de acceso:**
   - El token principal (`APP_USR-2552125444382264-...`) debe ser reemplazado por tu token real
   - Debes manejar los tokens de forma segura (considerar usar variables de entorno)

2. **Comisiones:**
   - Esta implementación no considera comisiones de la plataforma
   - Para agregar comisiones, calcula un porcentaje del monto antes de transferir al productor

3. **Estados de compra:**
   - Considera implementar más estados como "PENDING", "PROCESSING", "FAILED", etc.
   - Actualiza la interfaz de usuario para mostrar estos estados

4. **Pruebas:**
   - Primero prueba en el ambiente de sandbox de Mercado Pago
   - Utiliza las herramientas de Mercado Pago para simular pagos y notificaciones

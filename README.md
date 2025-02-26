# proyecto
Notas: Ahora q migre a posgreSQL los UUID se pueden generar en la BD. Para despuees
Como se crean los default_product_week queda pendiente.

Deberia ser algo asi como 

a) si solamente hay un producto de ese tipo disponible q sea ese

b) si la puntuacion de la semana pasada baja del 3.5, que se habilite la votacion por un nuevo producto

1- Stock Movement  

2- Purchase - Purchase Detail

3- Sale - Sale Detail

4- Neigbhorhood Package

Para los endpoints de pedido (purchase) y detalle de pedido (purchasedetail), te conviene seguir los patrones estándar de una API RESTful.

# API de Pedidos (`purchase`) y Detalles de Pedido (`purchasedetail`)

Esta API maneja la gestión de pedidos y sus detalles en un sistema de compras.

---

## 📌 **Endpoints para `Purchase` (Pedidos)**

Estos endpoints permiten gestionar los pedidos.

| Método  | Endpoint              | Descripción |
|---------|-----------------------|-------------|
| **POST**   | `/purchases`            | Crea un nuevo pedido. |
| **GET**    | `/purchases`            | Obtiene la lista de pedidos. |
| **GET**    | `/purchases/{id}`       | Obtiene un pedido específico. |
| **PUT**    | `/purchases/{id}`       | Actualiza un pedido existente. |
| **DELETE** | `/purchases/{id}`       | Elimina un pedido. |

✅ **Notas**:
- Al crear un pedido (`POST`), no se incluyen los detalles de compra, solo información general del pedido.
- Para obtener los detalles de un pedido, se puede usar `GET /purchases/{id}/details`.

---

## 📌 **Endpoints para `PurchaseDetail` (Detalles del Pedido)**

Estos endpoints permiten manejar los productos dentro de un pedido.

| Método  | Endpoint                                  | Descripción |
|---------|-------------------------------------------|-------------|
| **POST**   | `/purchases/{purchaseId}/details`         | Agrega un ítem a un pedido. |
| **GET**    | `/purchases/{purchaseId}/details`         | Obtiene todos los ítems de un pedido. |
| **GET**    | `/purchases/{purchaseId}/details/{id}`    | Obtiene un ítem específico. |
| **PUT**    | `/purchases/{purchaseId}/details/{id}`    | Actualiza un ítem de un pedido. |
| **DELETE** | `/purchases/{purchaseId}/details/{id}`    | Elimina un ítem de un pedido. |

✅ **Notas**:
- Los detalles siempre están vinculados a un pedido (`purchase`).
- Si permites modificar un pedido, es posible que necesites actualizar o eliminar detalles.

---

## 🔄 **Flujo Completo de la API**

1️⃣ **Crear un pedido vacío** → `POST /purchases`  
2️⃣ **Agregar productos al pedido** → `POST /purchases/{id}/details`  
para agregar productos al pedido deberia hacer un endpoint q me devuelva todos los default-product de un collectionPointSuscribed con el precio de acuerdo al nivel de la purchase

3️⃣ **Consultar un pedido** → `GET /purchases/{id}`  
4️⃣ **Ver los detalles de un pedido** → `GET /purchases/{id}/details`  
5️⃣ **Editar o eliminar pedidos/detalles** (Opcional) → `PUT / DELETE`

Con este diseño, la API sigue principios RESTful y permite gestionar pedidos y sus detalles de forma organizada. 🚀


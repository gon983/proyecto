# proyecto
empezar con la logica del negocio.
1- Stock Movement -> afectara el stock del producto
2- Purchase - Purchase Detail
3- Sale - Sale Detail
4- Neigbhorhood Package

Para los endpoints de pedido (purchase) y detalle de pedido (purchasedetail), te conviene seguir los patrones estándar de una API RESTful.

1️⃣ Endpoints Estándar para Purchase (Pedido)
Estos endpoints gestionan la compra en sí.

Método	Endpoint	Descripción
POST	/purchases	Crea un nuevo pedido.
GET	/purchases	Obtiene la lista de pedidos.
GET	/purchases/{id}	Obtiene un pedido específico.
PUT	/purchases/{id}	Actualiza un pedido existente.
DELETE	/purchases/{id}	Elimina un pedido.
✅ Consideraciones:

Al crear un pedido (POST), lo lógico es que no envíes los detalles (PurchaseDetailEntity) directamente, sino solo las referencias a los productos y su cantidad.
Cuando obtienes un pedido (GET /purchases/{id}), puedes incluir los detalles como un objeto anidado o devolverlos aparte con otro endpoint.
2️⃣ Endpoints Estándar para PurchaseDetail (Detalle del Pedido)
Estos endpoints manejan los ítems de un pedido.

Método	Endpoint	Descripción
POST	/purchases/{purchaseId}/details	Agrega un ítem a un pedido.
GET	/purchases/{purchaseId}/details	Obtiene todos los ítems de un pedido.
GET	/purchases/{purchaseId}/details/{id}	Obtiene un ítem específico.
PUT	/purchases/{purchaseId}/details/{id}	Actualiza un ítem de un pedido.
DELETE	/purchases/{purchaseId}/details/{id}	Elimina un ítem de un pedido.
✅ Consideraciones:

Los detalles de un pedido siempre dependen de un pedido, por eso los endpoints van anidados bajo /purchases/{purchaseId}/details.
Si permites que los usuarios editen los pedidos, podrías necesitar actualizar o eliminar ítems.
¿Qué Endpoint Conviene Hacer Primero?
Te recomiendo el siguiente orden:

1️⃣ POST /purchases → Para crear un pedido vacío.
2️⃣ POST /purchases/{purchaseId}/details → Para agregar ítems al pedido.
3️⃣ GET /purchases/{id} → Para obtener un pedido con sus detalles.
4️⃣ GET /purchases/{id}/details → Para obtener los ítems de un pedido.
5️⃣ PUT y DELETE → Solo si permites editar o eliminar pedidos/detalles.

Flujo Completo
El usuario crea un pedido (sin detalles todavía) → POST /purchases
El usuario agrega productos al pedido → POST /purchases/{id}/details
El usuario consulta su pedido → GET /purchases/{id}
El usuario ve los detalles del pedido → GET /purchases/{id}/details
(Opcional) Si necesita editarlo o eliminarlo → Usa PUT o DELETE.

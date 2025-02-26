# proyecto
Notas: Ahora q migre a posgreSQL los UUID se pueden generar en la BD. Ir cambiandolos mientras se desarrolla



# CU A Realizar
## Registrar cambio de stock para un producto por parte del producer

## Registrar una cuenta por parte del productor para recibir los pagos de las ventas

## Registrar una venta del lado del productor 

## Confirmar el pedido.

Este CU pasaría el pedido a partially confirmed, realizando todos los pagos a los detalles y disminuyendo en stock los productos
pero manteniendo la opcion de agregar mas detalles y confirmar de vuelta.

## Finalizar el pedido

Simplemente pasar el pedido a estado confirmed para q no se pueda editar mas y sea tenido en cuenta para la generacion del dia de entrega


## Registrar los productos default en un determinado collection point en una determinada fecha.

Deberia ser algo asi como 

a) si solamente hay un producto de ese tipo disponible q sea ese

b) si la puntuacion de la semana pasada baja del 3.5, que se habilite la votacion por un nuevo producto

## Generar Neightboor Package

Tomara todos los pedidos confirmados realizados en la semana, sumara los totales de productos por cada pedido para un
collection pooint especifico y un dia especifico.

Cada collection point tendrá un dia específico de entrega en el que se pacta siempre (siempre lunes/martes/miercoles/jueves/viernes).
El dia siguiente a ese día específico se reciben ya pedidos.

## Registrar pedido recibido

cuando se recibe un pedido el user tiene q confirmar q lo recibio

## Registrar calificacion. 

para el user que recibio el producto calificar, opcionalmente los productos recibidos

## Registrar votacion

cuando la calificacion de un producto promedio en una entrega es menor a 3.5, se abre la opcion para q los usuarios del collection point voten del mismo standar product otro producto específico.

Lunes -> Usuario recibe el pedido

los dos primeros dias tiene tiempo para calificarlo

Miercoles-> se realizan las votaciones de los productos mal calificados

un dia

Jueves -> Se realizan los pedidos







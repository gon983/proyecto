# Proceso Automático de Renovación de Productos en Puntos de Recolección

## Introducción
Este documento describe el proceso automático de renovación de productos en puntos de recolección dentro del sistema. El objetivo es analizar los productos ofrecidos en semanas anteriores, evaluar su desempeño en base a calificaciones y, según los resultados, renovar automáticamente los productos o habilitar una votación para decidir nuevas opciones.

## Objetivo
Automatizar la gestión de productos en puntos de recolección mediante un proceso programado que:
1. Evalúa los productos de la semana anterior.
2. Calcula su desempeño basado en calificaciones.
3. Renueva los productos si su calificación promedio es aceptable.
4. Activa una votación en caso contrario.

## Reglas del Proceso
- Cada **punto de recolección** tiene un **día fijo** de la semana para realizar la recolección de productos.
- **Cinco días antes** de ese día, se ejecuta el proceso automático.
- Se analiza la **semana anterior** a la fecha actual.
- Si el **promedio de calificaciones** de un producto en la semana pasada es **igual o superior a 3.5**, se **renueva**.
- Si el promedio es **menor a 3.5**, se inicia una **votación** para seleccionar un nuevo producto.

## Flujo del Proceso
1. **Detección de la fecha de recolección**
   - Se identifica el **día de la semana** en que cada punto de recolección realiza su actividad.
   - Se determina la fecha en que debe ejecutarse el proceso (**5 días antes** del día de recolección).

2. **Obtención de productos de la semana anterior**
   - Se buscan todos los productos registrados en el punto de recolección durante la semana anterior.
   - Se extraen las calificaciones asignadas a estos productos.

3. **Cálculo del promedio de calificaciones**
   - Se calcula la calificación promedio de cada producto.
   - Si el promedio es **igual o mayor a 3.5**, el producto se renueva para la siguiente semana.
   - Si el promedio es **menor a 3.5**, se abre una votación para decidir el reemplazo.

4. **Renovación de productos**
   - Se genera un nuevo registro para la siguiente semana con el mismo producto.
   - Se restablecen valores necesarios (por ejemplo, fecha de inicio de la nueva semana).

5. **Apertura de votación (si aplica)**
   - Si el producto no cumple con la calificación mínima, se activa un mecanismo de votación.
   - Este módulo será desarrollado por separado.

## Programación del Proceso
Para automatizar la ejecución, se utiliza una **tarea programada** que corre todos los días a una hora fija (por ejemplo, a las 12:00 PM). Esta tarea:
- Verifica los puntos de recolección y sus fechas asignadas.
- Identifica si faltan exactamente 5 días para la recolección.
- Si es el momento adecuado, inicia el proceso de análisis y renovación.

## Beneficios del Proceso
- **Automatización**: Reduce la necesidad de intervención manual.
- **Eficiencia**: Asegura que los productos con buen desempeño se mantengan en rotación.
- **Participación del usuario**: Permite una decisión participativa cuando un producto no tiene buen rendimiento.
- **Optimización del sistema**: Evita la asignación de productos con baja aceptación.

## Consideraciones Finales
- La hora exacta de ejecución puede ajustarse según la carga del sistema.
- Se podrían agregar métricas adicionales para evaluar la satisfacción del producto más allá de la calificación.
- Se debe garantizar la persistencia de los datos para que las renovaciones sean precisas y no generen inconsistencias.

Este proceso forma parte de una estrategia de mejora continua en la gestión de productos dentro del sistema, asegurando que las decisiones se basen en datos y en la participación de los usuarios.


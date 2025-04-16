📊 Entonces, tu estructura queda así:
✅ ProductEntity
Guarda información de cada producto + desempeño semanal (ventas, ingresos).

✅ RecorridoEntity
Guarda información diaria de rutas: puntos, distancia total, tiempo, repartidor, combustible, etc.

🧠 Ahora… ¿cómo se llama la clase que junta todo para calcular los costos totales de la empresa?
Podés llamarla de varias formas, según tu estilo. Te doy algunas ideas y cuál conviene según enfoque:

1. EmpresaResumenCostos
Ventaja: Claro, castellano, orientado a gestión operativa.

java
Copiar
Editar
public class EmpresaResumenCostos {
    private Double costoTotalSemanal;
    private Double costoLogisticoTotal;
    private Double costoProductosTotal;
    private Double ingresoTotal;
    private Double margenOperativo;
}

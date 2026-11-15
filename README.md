# Microservicios de productos y órdenes

Implementación en Quarkus 3 con Hibernate ORM Panache y PostgreSQL. Los ZIP de referencia y la diapositiva describían servicios en memoria/sin base de datos; esta entrega aplica el cambio solicitado: ambos servicios persisten sus propios datos.

## Proyectos y puertos

- `MSProductos`: catálogo CRUD, puerto **8081**, base lógica `productos_db`.
- `MSOrdenes`: creación y consulta de órdenes, puerto **8082**, base lógica `ordenes_db`.
- PostgreSQL escucha en el puerto **5432**. Los dos servicios no comparten tablas ni base lógica.

Los productos de ejemplo se insertan al iniciar `MSProductos` únicamente si el catálogo está vacío. Al crear una orden, `MSOrdenes` pide los tres productos al servicio de catálogo, calcula el total y guarda una copia del nombre y precio de cada artículo junto con el ID del producto. Así, el detalle histórico permanece aunque luego cambie el catálogo.

## Requisitos

- JDK 17
- Maven
- Docker con Docker Compose

## Iniciar localmente

Desde esta carpeta (`outputs`), inicia PostgreSQL:

```powershell
docker compose up -d db
```

En una terminal, inicia el catálogo:

```powershell
cd MSProductos
mvn quarkus:dev
```

En otra terminal, inicia órdenes:

```powershell
cd MSOrdenes
mvn quarkus:dev
```

Al primer arranque, Quarkus crea/actualiza las tablas. El catálogo carga tres productos de ejemplo, con IDs iniciales 1, 2 y 3.

## Endpoints

| Método | URL | Uso |
| --- | --- | --- |
| GET | `http://localhost:8081/productos` | Listar productos |
| GET | `http://localhost:8081/productos/1` | Consultar un producto |
| POST | `http://localhost:8081/productos` | Crear producto |
| PUT | `http://localhost:8081/productos/1` | Actualizar producto |
| DELETE | `http://localhost:8081/productos/1` | Eliminar producto |
| POST | `http://localhost:8082/ordenes` | Crear orden con tres IDs |
| GET | `http://localhost:8082/ordenes` | Listar órdenes guardadas |
| GET | `http://localhost:8082/ordenes/1` | Consultar una orden |

Crear una orden:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8082/ordenes -ContentType 'application/json' -Body '[1,2,3]'
```

Ejemplo para crear un producto:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8081/productos -ContentType 'application/json' -Body '{"name":"Monitor","price":249.99}'
```

## Prueba desde otra computadora del laboratorio

Los servicios escuchan en `0.0.0.0`. Inicia ambos proyectos en sus equipos y configura en el equipo donde corre `MSOrdenes` la dirección del equipo que corre `MSProductos`:

```powershell
$env:PRODUCTOS_URL = 'http://IP_DEL_EQUIPO_PRODUCTOS:8081'
mvn quarkus:dev
```

Si PostgreSQL corre en otro equipo, configura también `DB_URL` en cada servicio para usar el host/IP del servidor y su base lógica correspondiente. Ajusta `DB_USERNAME` y `DB_PASSWORD` si cambias las credenciales. Los puertos 8081, 8082 y 5432 deben estar disponibles entre los equipos que los usan.

La configuración local usa `app/app` para facilitar el laboratorio; reemplázala por credenciales propias si el entorno lo requiere. Para ambientes reales, sustituye `database.generation=update` por migraciones gestionadas.

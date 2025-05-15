# La Caverna del Cine
## Descripción del Proyecto 

El **Sistema de Administración ** tendrá como objetivo la gestión de una página web sobre películas con dos roles principales: **Usuarios** y **Administrador**. Cada uno de los roles podrá realizar acciones dependiendo de su nivel de acceso.

- **Administrador:** Tendrá acceso a funcionalidades de gestión de todo el sistema, incluyendo la administración de películas y usuarios. Podrá realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre las películas y los usuarios, así como gestionar la parte no visible para los usuarios regulares , Gestion de alquileres y apartado de Estadísticas.
- **Usuario:** Podrá registrarse, iniciar sesión, y acceder a su propio apartado personal donde podrá interactuar con las películas. Los usuarios podrán marcar las películas según el estado en el que se encuentren ( vistas, pendientes o en posesión) , marcarlas como favoritas  comprarlas , alquilarlas y califcarlas y ver el trailer

## Funcionalidades

### Funcionalidades del Administrador

1. **CRUD de Películas:** El administrador podrá agregar, editar y eliminar películas del sistema.
2. **CRUD de Usuarios:** El administrador podrá gestionar los usuarios del sistema, incluyendo la creación, edición y eliminación de cuentas.
3. **Gestión de alquileres:** El administrador podrá gestionar el inventario de películas disponibles para alquilar y comprar
4. **Gestión de Inventario:** El administrador podrá gestionar el inventario de la página;

### Contraseñas
 - **BCrypt:** Hasheadaas con BCrypt

  ### Imágenes
 - **Base64:** Imágenes codificadas en Base 64
### Filtros (Querys)

  1. **Películas vistas por sexo y género** 
  2. **Películas Compradas por Sexo** 
  3. **Películas Alquiladas por Sexo** 
  4. **Puntuación total de un género** 
  5. **Gastos totales de un usuario** 
 
### Funcionalidades del Usuario

1. **Registro y Login:** Los usuarios podrán registrarse en el sistema y acceder con sus credenciales.
2. **Buscar Películas:** Los usuarios podrán buscar películas disponibles en el videoclub.
3. **Apartado Personal:** Los usuarios tendrán un apartado donde podrán ver las películas que han alquilado y su estado.
4. **Marcar Películas por Estado:** Los usuarios podrán marcar las películas según su estado:
   - **Vistas:** Las películas que ya han visto.
   - **Pendientes:** Las películas que desean ver en el futuro.
   - **En Posesión:** Las películas que han alquilado pero aún no han devuelto.
   - **Favorita:** Las películas marcadas como favoritas.
    - **Calificar:** Las películas del 1 al 5.
5. **Comprar:** Podrán Comprar peliculas
6. **Alquilar:** Podrán Alquilar Peliculas
7. . **Ver trailer con link:** Podrán ver los trialers con links
8.  **Filtrar Peliculas:** Dependiento del estado
9.  **Calificar peliculas:** 1 a 5
10.  **Lista Personal:** Podrán filtrar su lista según los valores marcados
   
## Tecnologías
- **Backend:** Spring Boot
- **Frontend:** Thymeleaf, HTML, CSS, JS Bootstrap;

## Estructura del Proyecto

### Service
- Lógica del Proyecto
### Controller

- CRUD (POST,GET,DELETE,PUT.)

### Model

- Definir las clases y enlazarlas con la base de datos.

### Repository

- Interfaz que extiende los métodos del CRUD.

### DTO
- DTO -En este caso lo usamos para el Regex de la tarjeta de crédito

### UTIL
- Apartado para hasehar contraseñas

### Config
- Ya que esta aplicación se puede desplegar , creamos en config un administrador por defecto (Email:admin@gmail.com - Contraseña:admin)

### Static
- Imagenes de las peliculas , Imagenes que no cambia su estado

### Templates
- Vistas Código HTML

### Pom.xml
- Dependencias

### aplication.properties
- Apartado de configuraciones : base de datos, bot , mensajeria , sessiones

## Mensajería

- Creacion de cuenta **Mensaje de creacion de cuenta a tu Correo**.
- Compra Película **Mensaje de compra de pelicula de cuenta a tu Corre**.
- Alquiler Película **Mensaje de Alquiler de cuenta a tu Corre**.
- Ingreso Crédito **Mensaje den Ingreso de cuenta a tu Corre**.
- Recordatorio pago **Mensaje de  recordatorio de devolucion de pelicula de cuenta a tu Corre**.

 ## Chat con bot opensource
 Apartado para hablar con un bot con inteligencia artificial sobre los topicos de peliculas


 ## docker-compose-yml / Dokerfile
Configuracion para dockerizar el proyecto


## Estructura de Base de Datos

### 1. Tabla de Alquileres

## 📌 Estructura de la tabla
| **Columna**        | **Tipo de dato**  | **Descripción**                             |
|-------------------|----------------|-------------------------------------------|
| `id_alquiler`     | `Integer (PK)`  | Identificador único del alquiler.        |
| `id_usuario`      | `Integer (FK)`  | Referencia al usuario que realizó el alquiler. |
| `id_pelicula`     | `Integer (FK)`  | Referencia a la película alquilada.      |
| `fecha_alquiler`  | `LocalDate`     | Fecha en que se realizó el alquiler.     |
| `fecha_devolucion`| `LocalDate`     | Fecha en que se devolvió la película.    |
| `precioAlquiler`  | `Double`        | Precio del alquiler (valor por defecto: `5.0`). |
| `estado_Alquiler` | `String`        | Estado del alquiler (por defecto: `"devuelta"`). |

## 🔗 Relaciones
- `@ManyToOne` con `Usuario`: Relaciona el alquiler con el usuario que lo realizó.
- `@ManyToOne` con `Pelicula`: Relaciona el alquiler con la película alquilada.

### 2. Tabla de Compra
## 📌 Estructura de la tabla
| **Columna**        | **Tipo de dato**  | **Descripción**                             |
|-------------------|----------------|-------------------------------------------|
| `id_compra`       | `Integer (PK)`  | Identificador único de la compra.        |
| `id_usuario`      | `Integer (FK)`  | Referencia al usuario que realizó la compra. |
| `id_pelicula`     | `Integer (FK)`  | Referencia a la película comprada.      |
| `fecha_compra`    | `LocalDate`     | Fecha en que se realizó la compra.     |
| `precioCompra`    | `Double`        | Precio de la compra (valor por defecto: `10.0`). |
| `estado_Compra`   | `String`        | Estado de la compra (por defecto: `"false"`). |

## 🔗 Relaciones
- `@ManyToOne` con `Usuario`: Relaciona la compra con el usuario que la realizó.
- `@ManyToOne` con `Pelicula`: Relaciona la compra con la película adquirida.

---

### 3. Tabla EstadosPeliculas

## 📌 Estructura de la tabla
| **Columna**        | **Tipo de dato**  | **Descripción**                             |
|-------------------|----------------|-------------------------------------------|
| `id_estado`       | `Integer (PK)`  | Identificador único del estado de la película. |
| `id_usuario`      | `Integer (FK)`  | Referencia al usuario asociado al estado de la película. |
| `id_pelicula`     | `Integer (FK)`  | Referencia a la película.                 |
| `devuelta`        | `String`        | Indica si la película fue devuelta (`"si"` por defecto). |
| `fechaDevuelta`   | `LocalDate`     | Fecha en la que se devolvió la película. |
| `estado`          | `String`        | Estado actual de la película. |
| `ultimoEstado`    | `String`        | Último estado registrado de la película. |

## 🔗 Relaciones
- `@ManyToOne` con `Usuario`: Relaciona el estado con el usuario que interactuó con la película.
- `@ManyToOne` con `Pelicula`: Relaciona el estado con la película.

---

### 4. Tabla Peliculas

## 📌 Estructura de la tabla
| **Columna**            | **Tipo de dato**  | **Descripción**                             |
|-----------------------|----------------|-------------------------------------------|
| `id_Pelicula`         | `Integer (PK)`  | Identificador único de la película.       |
| `titulo`             | `String`        | Título de la película.                    |
| `descripcion`        | `String`        | Descripción o sinopsis de la película.    |
| `genero`            | `String`        | Género al que pertenece la película.     |
| `ano`               | `Integer`       | Año de lanzamiento de la película.       |
| `imagen`            | `String (LOB)`  | URL o datos en formato `LONGTEXT` para la imagen. |
| `disponible`        | `Integer`       | Indica si la película está disponible.  |
| `disponibleAlquiler`| `Integer`       | Indica si la película está disponible para alquiler. |
| `formato`           | `String`        | Formato de la película (DVD, digital, etc.). |
| `trailer`           | `String`        | Enlace al tráiler de la película.        |

## 🔗 Relaciones
- `@OneToMany(mappedBy = "pelicula")` con `UsuariosPeliculas`: Relaciona la película con los usuarios que han interactuado con ella.

---



### 5. Tabla Transaccion

## 📌 Estructura de la tabla
| **Columna**            | **Tipo de dato**  | **Descripción**                             |
|-----------------------|----------------|-------------------------------------------|
| `id_Pelicula`         | `Integer (PK)`  | Identificador único de la película.       |
| `titulo`             | `String`        | Título de la película.                    |
| `descripcion`        | `String`        | Descripción o sinopsis de la película.    |
| `genero`            | `String`        | Género al que pertenece la película.     |
| `ano`               | `Integer`       | Año de lanzamiento de la película.       |
| `imagen`            | `String (LOB)`  | URL o datos en formato `LONGTEXT` para la imagen. |
| `disponible`        | `Integer`       | Indica si la película está disponible.  |
| `disponibleAlquiler`| `Integer`       | Indica si la película está disponible para alquiler. |
| `formato`           | `String`        | Formato de la película (DVD, digital, etc.). |
| `trailer`           | `String`        | Enlace al tráiler de la película.        |

## 🔗 Relaciones
- `@OneToMany(mappedBy = "pelicula")` con `UsuariosPeliculas`: Relaciona la película con los usuarios que han interactuado con ella.

---

### 6. Tabla Usuarios
## 📌 Estructura de la tabla
| **Columna**      | **Tipo de dato**  | **Descripción**                             |
|----------------|----------------|-------------------------------------------|
| `id_Usuario`   | `Integer (PK)`  | Identificador único del usuario.         |
| `nombre`       | `String`        | Nombre del usuario.                     |
| `email`        | `String`        | Dirección de correo electrónico (único). |
| `password`     | `String`        | Contraseña del usuario.                 |
| `rol`          | `String`        | Rol del usuario (por defecto: `"USER"`). |
| `credito`      | `Double`        | Crédito disponible del usuario (`0.0` por defecto). |
| `deuda`        | `Integer`       | Deuda del usuario (`0` por defecto). |
| `sexo`         | `String`        | Género del usuario. |

## 🔗 Relaciones
- `@OneToMany(mappedBy = "usuario")` con `UsuariosPeliculas`: Relaciona el usuario con las películas que ha visto o adquirido.

---
### 7. Tabla UsuariosPeliculas
## 📌 Estructura de la tabla
| **Columna**          | **Tipo de dato**  | **Descripción**                              |
|---------------------|----------------|--------------------------------------------|
| `id`               | `Integer (PK)`  | Identificador único de la relación entre usuario y película. |
| `id_usuario`       | `Integer (FK)`  | Referencia al usuario asociado a la película. |
| `id_pelicula`      | `Integer (FK)`  | Referencia a la película asociada al usuario. |
| `fechaCalificacion`| `String`        | Fecha en la que el usuario calificó la película. |
| `calificacion`     | `Integer`       | Puntuación otorgada a la película (`0` por defecto). |
| `favorita`        | `String`        | Indica si la película es favorita (`"false"` por defecto). |
| `fechaFavorita`   | `String`        | Fecha en la que la película fue marcada como favorita. |

## 🔗 Relaciones
- `@ManyToOne` con `Usuario`: Relaciona el usuario con las películas que ha visto o calificado.
- `@ManyToOne` con `Pelicula`: Relaciona la película con los usuarios que han interactuado con ella.

---

## 🔗 Relaciones Entre Clases

- **Usuario (1:N) Alquiler:** Un usuario puede alquilar varias películas.
- **Película (1:N) Alquiler:** Una película puede ser alquilada varias veces.
- **Usuario (1:N) EstadoPelicula:** Un usuario puede marcar varias películas con diferentes estados.
- **Película (1:N) EstadoPelicula:** Una película puede estar en distintos estados según el usuario.
- **Usuario (N:N) Peliculas:** Un usuario puede interactuar con varias películas, y una película puede estar en distintos estados según los usuarios.
- **Usuario (1:N) Compra:** Un usuario puede comprar varias películas.
- **Película (1:N) Compra:** Una película puede ser comprada varias veces.
- **Usuario (1:N) Transaccion:** Un usuario puede realizar múltiples transacciones.
- **Usuario (1:N) UsuariosPeliculas:** Un usuario puede calificar, marcar como favorita o interactuar con varias películas.
- **Película (1:N) UsuariosPeliculas:** Una película puede ser calificada, marcada como favorita o vista por múltiples usuarios.

---

 ## Dockerizar el proyecto
 Para desplegar usamos Docker

 ## Diagrama 

![Image](https://github.com/user-attachments/assets/1933631e-9c77-44f9-a48a-42ad20c759b1)


 

**Gestión de Inventario (CRUD Examen 2)**

Sistema de gestión de productos desarrollado con **Spring Boot (Backend API)** y **Javascript Vanilla (Frontend)**. Permite crear, leer, actualizar, eliminar y buscar productos en tiempo real.

Tecnologías Utilizadas

* **Backend:** Java 17, Spring Boot 3, Spring Data JPA, Lombok.
* **Base de Datos:** MySQL.
* **Frontend:** HTML5, CSS3 (Bootstrap 5), JavaScript (Fetch API).
* **Herramientas:** IntelliJ IDEA, Gradle, Apidog/Postman.

---

**Prerrequisitos**

Antes de ejecutar, asegúrate de tener instalado:
1. **Java JDK 17** o superior.
2. **MySQL Server** (corriendo en el puerto 3306).
3. **Git** (opcional, para clonar).

---

**Instrucciones de Instalación**

**1. Clonar el Repositorio**

git clone https://github.com/majovp-25/GestionCRUD_Examen2.git
cd GestionCRUD_Examen2

**2. Configurar la Base de Datos**

1. Abre tu gestor de base de datos (MySQL Workbench, DBeaver, etc.).
2. Crea la base de datos (verifica el nombre en `application.properties`, por defecto suele ser):
   CREATE DATABASE jpademo_db;

**3. Configurar Credenciales**
Edita el archivo `src/main/resources/application.properties` y actualiza tu usuario y contraseña:

spring.datasource.url=jdbc:mysql://localhost:3306/jpademo_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=TU_CONTRASEÑA_AQUI

**4. Ejecutar el Servidor (Backend)**
Puedes ejecutarlo desde **IntelliJ IDEA** dando clic en `Run` en la clase principal `GestionCrudApplication.java`, o vía terminal:

**En Windows:**
gradlew bootRun

**En Mac/Linux:**
./gradlew bootRun

El servidor iniciará en: http://localhost:8080

---

**Uso del Frontend**

Una vez que el backend esté corriendo:

1. Navega a la carpeta del proyecto.
2. Si el archivo `index.html` está en la carpeta `static`, abre en tu navegador:
   http://localhost:8080/index.html
3. Si tienes el `index.html` separado, simplemente ábrelo con doble clic o usa Live Server.

---

Documentación de la API (Endpoints)

La API responde en la ruta base: `/demoJPA/products`

| Método | URL | Descripción |
| :--- | :--- | :--- |
| GET | /demoJPA/products | Obtiene la lista de todos los productos. |
| GET | /demoJPA/products/{id} | Obtiene un producto por su ID. |
| POST | /demoJPA/products | Crea un nuevo producto (JSON Body). |
| PUT | /demoJPA/products/{id} | Actualiza un producto completo. |
| PATCH | /demoJPA/products/{id} | Actualización parcial. |
| DELETE | /demoJPA/products/{id} | Elimina un producto. |

---

Nota para Entornos Offline

Este proyecto utiliza **Bootstrap 5** para los estilos.
* **Modo Online:** Se carga automáticamente desde el CDN (JSDelivr).
* **Modo Offline:** Si no tienes internet, descarga el archivo `bootstrap.min.css` y colócalo en `src/main/resources/static/css/`, luego actualiza el `<link>` en el `index.html`.

---

**Desarrollado por:** Maria Jose Paredes - Juan Daniel Vasquez - Escuela Politecnica Nacional

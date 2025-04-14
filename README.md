# CastoresAPI

## Descripción

Este proyecto es una API construida con Spring Boot, diseñada para administrar un inventario en un sistema de almacén. Permite la gestión de productos (entradas, salidas, activación y desactivación de productos), y mantiene un historial de movimientos de inventario. También incluye una autenticación basada en roles para los usuarios.

## Requerimientos

- **IDE utilizado**: IntelliJ IDEA Community Edition (CE)
- **Lenguaje de programación**: Java 21
- **DBMS utilizado**: SQL Server (versión recomendada: 2019 o superior)
- **Dependencias**: A continuación, se detallan las principales dependencias utilizadas en este proyecto.

### Dependencias

- **Spring Boot**: Framework principal para el desarrollo del backend.
  - `spring-boot-starter-web`: Para la construcción de servicios web.
  - `spring-boot-starter-data-jpa`: Para la integración con JPA y la base de datos.
  - `spring-boot-starter-security`: Para implementar la seguridad de la aplicación.
  - `spring-boot-starter-validation`: Para validaciones automáticas de los datos.
  - `spring-boot-starter-test`: Para las pruebas unitarias.

- **SQL Server**:
  - `mssql-jdbc`: Controlador JDBC para conectarse a bases de datos SQL Server.

- **JWT (JSON Web Tokens)**:
  - `jjwt-api`, `jjwt-impl`, `jjwt-jackson`: Para manejar la autenticación y autorización basada en JWT.

- **Lombok**:
  - `lombok`: Para simplificar el código eliminando boilerplate en las clases Java.

- **Swagger/OpenAPI**:
  - `springdoc-openapi-starter-webmvc-ui`: Para documentar la API con Swagger UI.

## Pasos para ejecutar la aplicación

1.  **Clonar el repositorio**:
  - Abre tu terminal y ejecuta el siguiente comando:
  ```
    git clone https://github.com/TU_USUARIO/castoresAPI.git
  ```

2.  **Instalar dependencias**:
  - Dirígete a la terminal del proyecto y ejecuta el siguiente comando para instalar las dependencias con Maven:
    `mvn install`
  - Configurar la base de datos:
    En el archivo src/main/resources/application.properties, configura la conexión a la base de datos con los siguientes parámetros (ajusta según tu entorno):
    ```
      spring.datasource.url=jdbc:sqlserver://<hostname>:<port>;databaseName=<db_name>
      spring.datasource.username=<db_user>
      spring.datasource.password=<db_password>
      spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver
      spring.jpa.hibernate.ddl-auto=update
    ```

3.  **Correr la aplicación**:
    Una vez configurado todo, ve al archivo main de la aplicación (src/main/java/com/castores/.../CastoresApiApplication.java) y corre la aplicación desde ahí.
    El puerto predeterminado de la aplicación es el 8080. Puedes acceder a la API en http://localhost:8080.

4.  **Crear roles en la base de datos**:
   Una vez que la aplicación esté corriendo, asegúrate de ejecutar el siguiente script SQL para agregar los roles necesarios:
   -- Agregar Roles --
  ```sql
    INSERT INTO master.dbo.roles (name)
    VALUES
    ('ROLE_ADMIN'),
    ('ROLE_WAREHOUSE');
  ```
  
  


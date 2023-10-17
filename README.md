# 2023-messismo

[![Coverage Status](https://coveralls.io/repos/github/uca-pid/2023-messismo/badge.png?branch=develop)](https://coveralls.io/github/uca-pid/2023-messismo?branch=develop)

# Proyecto Integral de Desarrollo
## Alumnos:
##### Baccari Carla,
####  Guido Martin,
####  Herrmann Karen,
### Año: 2023

## INSTRUCCIONES PARA WINDOWS
1. Descargar e instalar Java SDK 17, se consigue en [Java SDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. Abrir el proyecto en algun IDE (Visual Studio Code, Intellij IDEA, Eclipse,...)
3. En este caso, se utilizo Intellij IDEA, se puede descargar en [Intellij IDEA](https://www.jetbrains.com/idea/download/#section=windows) :
    - Clonar el repositorio publico o descargar el archivo zip que lo contiene
    - Entrar a File > Project Structure y seleccionar en las pestañas Project, Modules and SDK, Java SDK 17 y tambien como nivel de lenguaje
    - Posteriormente, entrar a File > Settings > Build, Execution, Deployment > Build Tools > Maven y seleccionar en Maven Home Path: Bundled (Maven 3)
    - Luego, entrar a File > Settings > Build, Execution, Deployment > Compiler > Java Compiler y seleccionar en Target bytecode Version: 17
    - Abrir el archivo docker-compose.yml y reemplazar en la imagen de springboot, el atributo volumes: por el directorio en la computadora donde se guardó el proyecto hasta el :/app
4. Descargar e instalar Docker Desktop en [Docker Desktop](https://docs.docker.com/desktop/install/windows-install/) y se recomienda instalar la actualizacion del kernel de Linux. En caso de no ternerla, se descarga en [WSL2](https://wslstorestorage.blob.core.windows.net/wslblob/wsl_update_x64.msi)
5. Abrir una terminal en la carpeta del proyecto e insertar el siguiente comando: `./mvnw clean`, cuando termine ejecutar: `./mvnw package`. En caso de resultar en error, se debe ingresar a los archivos mvnw y mvnw.cmd y cambiar su line separator de CRLF a LF.
6. Escribir en la terminal `docker-compose up` para crear el contenedor con el backend, la base de datos y pgAdmin4 (un gestor grafico de la base de datos)
7. Finalmente, para configurar el gestor grafico, debemos dirigirnos a Docker Desktop, en la pestaña Containers, expandir el paquete llamado 2023_messismo  y buscar __pgadmin__. En los 3 puntos de la derecha, tocar Open with Browser.
- Una vez en la pagina de PgAdmin4, ingresar las credenciales name@example.com como usuario y admin como contraseña. Una vez adentro, hacer click en "Add New Server".
- En la primera pestaña, General, solo ingresar en Name: "postgres"
- En la segunda pestaña, Connection, ingresar en Host name/address: "192.168.55.11", en Username: "postgres" y en Password: "postgres"

De esta forma, se configura por unica vez el proyecto, en un futuro solo se requiere entrar a Docker Desktop y correr el paquete de contenedor denominado 2023_messismo. También se puede correr con el siguiente comando desde la terminal: `./mvnw clean package && docker-compose-up`
Los archivos mvnw y mvnw.cmd deben estar en formato LF.
## INSTRUCCIONES PARA MAC OS
1. Descargar e instalar Java SDK 17, se consigue en [Java SDK 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
2. Abrir el proyecto en algun IDE (Visual Studio Code, Intellij IDEA, Eclipse,...)
3. En este caso, se utilizo Intellij IDEA, se puede descargar en [Intellij IDEA](https://www.jetbrains.com/idea/download/#section=windows) :
    - Clonar el repositorio publico o descargar el archivo zip que lo contiene
    - Entrar a File > Project Structure y seleccionar en las pestañas Project, Modules and SDK, Java SDK 17 y tambien como nivel de lenguaje
    - Posteriormente, entrar a File > Settings > Build, Execution, Deployment > Build Tools > Maven y seleccionar en Maven Home Path: Bundled (Maven 3)
    - Luego, entrar a File > Settings > Build, Execution, Deployment > Compiler > Java Compiler y seleccionar en Target bytecode Version: 17
    - Abrir el archivo docker-compose.yml y reemplazar en la imagen de springboot, el atributo volumes: por el directorio en la computadora donde se guardó el proyecto hasta el :/app
4. Descargar e instalar Docker Desktop en [Docker Desktop](https://docs.docker.com/desktop/install/mac-install/)
5. Abrir una terminal en la carpeta del proyecto e insertar el siguiente comando: `./mvnw clean`, cuando termine ejecutar: `./mvnw package`.En caso de resultar en error, se debe ingresar a los archivos mvnw y mvnw.cmd y cambiar su line separator de CRLF a LF.
6. Escribir en la terminal `docker-compose up` para crear el contenedor con el backend, la base de datos y pgAdmin4 (un gestor grafico de la base de datos)
7. Finalmente, para configurar el gestor grafico, debemos dirigirnos a Docker Desktop, en la pestaña Containers, expandir el paquete llamado 2023_messismo  y buscar __pgadmin__. En los 3 puntos de la derecha, tocar Open with Browser.
- Una vez en la pagina de PgAdmin4, ingresar las credenciales name@example.com como usuario y admin como contraseña. Una vez adentro, hacer click en "Add New Server".
- En la primera pestaña, General, solo ingresar en Name: "postgres"
- En la segunda pestaña, Connection, ingresar en Host name/address: "192.168.55.11", en Username: "postgres" y en Password: "postgres"

De esta forma, se configura por unica vez el proyecto, en un futuro solo se requiere entrar a Docker Desktop y correr el paquete de contenedor denominado 2023_messismo. También se puede correr con el siguiente comando desde la terminal: `./mvnw clean package && docker-compose-up`
Los archivos mvnw y mvnw.cmd deben estar en formato LF.

## Otras consideraciones:
Se menciono la forma para correlo con contenedores, sin embargo, se puede tambien correr de forma local. En cuanto al backend, se debe seleccionar el spring.profiles.active = test para usar H2 en vez de PostgreSQL. Y en el front, se debe realizar un npm install previo al npm start

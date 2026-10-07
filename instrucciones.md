# Manual de Instrucciones y Pruebas 🚀

Esta guía detalla los pasos exactos para compilar, levantar y probar el ecosistema completo de microservicios del Banco XYZ.

## 1. Pre-requisitos
*   **Java 17** o superior instalado en el entorno.
*   **Docker Desktop** (o Docker Engine) instalado y ejecutándose.
*   El clúster de **Apache Kafka** levantado y operativo en la nube (Instancia AWS EC2 configurada).

---

## 2. Levantamiento de la Arquitectura
El ecosistema utiliza construcción Multi-etapa (Multi-stage builds) de Docker. Esto significa que **no necesitas tener Maven instalado localmente ni compilar los proyectos a mano**; Docker descargará una imagen de Maven, compilará el código de los 7 microservicios, y luego empaquetará los binarios en contenedores ultraligeros de Java Alpine.

1. Abre una terminal o consola de comandos y navega hasta la carpeta raíz del proyecto (`BancoXYZ_Microservicios`).
2. Verifica que no tengas servicios locales ocupando los puertos que vamos a usar (`8080, 8081, 8082, 8083, 8888, 8761, 9000, 3307`).
3. Ejecuta el comando :
   ```bash
   docker-compose up -d --build
   ```
4. **Espera entre 2 a 4 minutos.** Docker construirá todo el ecosistema.
5. Abre Docker Desktop y verifica que los 8 contenedores se encuentren en estado **Running**.

*(Nota Técnica: Gracias a la regla `restart: on-failure` del `docker-compose` y la implementación de Resilience4j, no importa si un microservicio nace antes que la base de datos o el Config Server; este reintentará conectarse automáticamente hasta lograrlo).*

---

## 3. Pruebas de Servicios y Seguridad (OAuth2)
Los tres canales Backend for Frontend (`bff-web`, `bff-mobile`, `bff-atm`) actúan como Resource Servers de Spring Security. Es **imposible** consumir un servicio de negocio sin antes obtener un Token JWT.

### Paso 1: Obtener el Token de Acceso (Vía Postman)
1. Abre Postman y crea una nueva petición **POST** a `http://localhost:9000/oauth2/token`.
2. Ve a la pestaña **Authorization**, selecciona **Basic Auth** y utiliza las credenciales de la aplicación cliente:
   *   **Username:** `admin-client`
   *   **Password:** `admin123`
3. Ve a la pestaña **Body** (selecciona `x-www-form-urlencoded`) y envía los siguientes parámetros:
   *   `grant_type` = `client_credentials`
   *   `scope` = `cuentas.read cuentas.write`
4. Ejecuta la petición (`Send`). Recibirás un JSON con un `access_token`. Cópialo.

### Paso 2: Probar Lectura Síncrona a través del BFF
1. Crea una petición **GET** a `http://localhost:8081/api/bff-web/estadoCuentas`
2. Ve a la pestaña **Authorization**, selecciona **Bearer Token** y pega el token que copiaste en el Paso 1.
3. Ejecuta la petición. Recibirás un estado `200 OK` con un arreglo JSON que contiene los datos procesados en el Core.

*(Prueba de Resiliencia: Si detienes el contenedor `bank-legacy` desde Docker Desktop y vuelves a ejecutar la petición, notarás que en lugar de arrojar un error 500, el BFF retorna instantáneamente una lista vacía `[]`, demostrando que el Circuit Breaker (Resilience4j) interceptó la caída).*

### Paso 3: Probar Ejecución Asíncrona de Procesos Batch (Kafka a AWS)
Gracias a la última refactorización arquitectónica, ya no se envían los nombres de los procesos en un JSON, sino que cada comando tiene su endpoint estrictamente tipado.

1. Crea una petición **POST** a cualquiera de las siguientes rutas (dependiendo del proceso que quieras ejecutar):
   *   `http://localhost:8081/api/bff-web/kafka/batch/transacciones`
   *   `http://localhost:8081/api/bff-web/kafka/batch/intereses`
   *   `http://localhost:8081/api/bff-web/kafka/batch/estados-cuenta`
   *(Nota: También puedes usar los puertos `8082` (mobile) o `8083` (atm)).*
2. Ve a **Authorization**, selecciona **Bearer Token** y pega tu token.
3. **No es necesario enviar ningún Body.** Simplemente ejecuta la petición.
4. **Verificación:** El BFF responderá con un `200 OK` indicando que la señal fue disparada hacia la nube. Si abres los logs del contenedor `bank-legacy` en Docker Desktop, verás que el Consumidor de Kafka recibió el mensaje desde AWS y activó el proceso de Spring Batch correspondiente.

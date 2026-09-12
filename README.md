# Banco XYZ - Arquitectura Backend for Frontend (BFF) 🏦

## Objetivo del Proyecto 🎯
Este proyecto implementa una arquitectura de microservicios utilizando el patrón **Backend for Frontend (BFF)** para el Banco XYZ. El objetivo principal es optimizar la comunicación entre el sistema central (Core) y los distintos canales de atención (Web, Móvil y Cajeros Automáticos), proporcionando a cada cliente una API adaptada a sus necesidades específicas de ancho de banda, seguridad y experiencia de usuario.

## Estructura del Código 🏗️
El sistema está dividido en un "Workspace Multimódulo" con 4 microservicios independientes:

1. **`bank_legacy` (Core - Puerto 8080):** 
   - Actúa como la fuente de la verdad.
   - Contiene la conexión a la base de datos MySQL y expone los datos crudos a través de endpoints REST internos.

2. **`bff-web` (Puerto 8081 - HTTPS):**
   - **Propósito:** Optimizado para navegadores de escritorio.
   - **Mapeo de Datos:** Proporciona datos completos para soportar interfaces complejas (grids, reportes financieros).
   - **Seguridad:** Protegido con HTTPS (Certificado SSL) y Tokens JWT (JSON Web Tokens). Usuario: `admin_web`.

3. **`bff-mobile` (Puerto 8082 - HTTPS):**
   - **Propósito:** Optimizado para aplicaciones móviles.
   - **Mapeo de Datos:** Respuestas ligeras. Oculta IDs internos y datos no esenciales para ahorrar ancho de banda de red celular.
   - **Seguridad:** Protegido con HTTPS y Tokens JWT. Usuario: `admin_mobile`.

4. **`bff-atm` (Puerto 8083 - HTTPS):**
   - **Propósito:** Cajeros Automáticos.
   - **Mapeo de Datos:** Interfaz ultra-ligera y segura exponiendo únicamente los saldos y montos para operaciones críticas.
   - **Seguridad:** Protegido con HTTPS y Tokens JWT. Usuario: `admin_atm`.

## Patrones Utilizados ⚙️
- **Backend for Frontend (BFF):** Separación de interfaces para clientes.
- **Data Transfer Object (DTO):** Mapeo de `CoreDTO` a `WebDTO/MobileDTO/AtmDTO` para desacoplar el modelo de base de datos de la respuesta al cliente.
- **RestClient:** Cliente HTTP moderno de Spring Boot 3.2+ para la comunicación sincrónica entre los BFFs y el Core.
- **Spring Security (JWT):** Autenticación sin estado (STATELESS) usando `jjwt`, con Filtros personalizados de intercepción de peticiones HTTP.
- **SSL/TLS:** Configuración local con `keytool` para cifrado de red (HTTPS) con certificados PKCS12.

## Instrucciones de Ejecución 🚀

### 1. Pre-requisitos
- Java 17+
- Base de datos MySQL corriendo en el puerto 3306 (con los datos cargados del batch previo).

### 2. Levantamiento de Servicios
Es estrictamente necesario arrancar los servicios en el siguiente orden:
1. Levantar el proyecto `bank_legacy` (Core HTTP).
2. Levantar el proyecto `bff-web` (HTTPS).
3. Levantar el proyecto `bff-mobile` (HTTPS).
4. Levantar el proyecto `bff-atm` (HTTPS).

### 3. Pruebas de APIs (Flujo JWT)
Para consumir cualquier API, primero debes autenticarte en el canal correspondiente para obtener tu Token JWT, y luego inyectarlo como Bearer Token.

**Ejemplo BFF Web:**
1. Haz un **POST** a `https://localhost:8081/auth/bff-web/login` con el siguiente Body JSON:
   ```json
   {
       "username": "admin_web",
       "password": "12345"
   }
   ```
2. Copia el token de la respuesta.
3. Haz un **GET** a `https://localhost:8081/api/bff-web/transacciones` enviando en los Headers HTTP:
   `Authorization: Bearer <TU_TOKEN>`

**Ejemplo BFF Mobile:**
- URL de Login: `https://localhost:8082/auth/bff-mobile/login`
- Usuario: `admin_mobile` / `12345`

**Ejemplo BFF ATM:**
- URL de Login: `https://localhost:8083/auth/bff-atm/login`
- Usuario: `admin_atm` / `12345`

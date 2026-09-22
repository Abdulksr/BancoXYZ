# Banco XYZ - Arquitectura Backend for Frontend (BFF) 🏦

## Objetivo del Proyecto 🎯
Este proyecto implementa una arquitectura de microservicios utilizando el patrón **Backend for Frontend (BFF)** para el Banco XYZ. El objetivo principal es optimizar la comunicación entre el sistema central (Core) y los distintos canales de atención (Web, Móvil y Cajeros Automáticos), proporcionando a cada cliente una API adaptada a sus necesidades específicas de ancho de banda, seguridad y experiencia de usuario.

## Estructura del Código 🏗️
El sistema está dividido en un "Workspace Multimódulo" con los siguientes componentes:

### Ecosistema Spring Cloud
1. **`eureka-server` (Puerto 8761):** Servidor de descubrimiento de servicios.
2. **`config-server` (Puerto 8888):** Servidor de configuración centralizada (obtiene propiedades de la carpeta `config-data`).

### Microservicios de Negocio
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

## Patrones y Tecnologías Utilizadas ⚙️
- **Backend for Frontend (BFF):** Separación de interfaces para clientes.
- **Data Transfer Object (DTO):** Mapeo de `CoreDTO` a `WebDTO/MobileDTO/AtmDTO` para evitar fuga de información.
- **Resiliencia (Resilience4j):** Protección robusta en la comunicación hacia el Core Legacy mediante:
  - **Circuit Breaker:** Corta la conexión si el backend falla, dándole tiempo para recuperarse.
  - **Retry:** Reintenta automáticamente las peticiones ante fallos transitorios.
  - **Rate Limiter:** Limita la tasa de solicitudes hacia el core para evitar saturación.
  - **Fallbacks:** Manejo de errores gracefully (ej. retornando `null` o listas vacías y logueando apropiadamente).
- **RestClient con Timeouts:** Cliente HTTP configurado explícitamente con *Connect Timeout* y *Read Timeout* inyectados por properties, previniendo el agotamiento de hilos.
- **Spring Cloud:** Configuración distribuida (`config-server`) y service discovery (`eureka-server`).
- **Spring Security (JWT):** Autenticación STATELESS con Filtros personalizados.
- **SSL/TLS:** Cifrado de red (HTTPS) con certificados PKCS12 locales.

## Instrucciones de Ejecución 🚀

### 1. Pre-requisitos
- Java 17+
- Base de datos MySQL corriendo en el puerto 3306 (con los datos cargados del batch previo).

### 2. Levantamiento de Servicios
Es estrictamente necesario arrancar los servicios en el siguiente orden para evitar fallos de conexión:
1. Levantar el proyecto `eureka-server`.
2. Levantar el proyecto `config-server`.
3. Levantar el proyecto `bank_legacy` (Core HTTP).
4. Levantar los proyectos BFFs (`bff-web`, `bff-mobile`, `bff-atm`).

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

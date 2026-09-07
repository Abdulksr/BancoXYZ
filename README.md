# Banco XYZ - Arquitectura Backend for Frontend (BFF) 🏦

## Objetivo del Proyecto 🎯
Este proyecto implementa una arquitectura de microservicios utilizando el patrón **Backend for Frontend (BFF)** para el Banco XYZ. El objetivo principal es optimizar la comunicación entre el sistema central (Core) y los distintos canales de atención (Web, Móvil y Cajeros Automáticos), proporcionando a cada cliente una API adaptada a sus necesidades específicas de ancho de banda, seguridad y experiencia de usuario.

## Estructura del Código 🏗️
El sistema está dividido en un "Workspace Multimódulo" con 4 microservicios independientes:

1. **`bank_legacy` (Core - Puerto 8080):** 
   - Actúa como la fuente de la verdad.
   - Contiene la conexión a la base de datos MySQL y expone los datos crudos a través de endpoints REST internos.

2. **`bff-web` (Puerto 8081):**
   - **Propósito:** Optimizado para navegadores de escritorio.
   - **Mapeo de Datos:** Proporciona datos completos (DTOs idénticos a los del Core) para soportar interfaces complejas (grids, reportes financieros).
   - **Seguridad:** Protegido con Spring Security Basic Auth (Usuario: `admin_web`).

3. **`bff-mobile` (Puerto 8082):**
   - **Propósito:** Optimizado para aplicaciones móviles.
   - **Mapeo de Datos:** Respuestas ligeras. Oculta IDs internos y datos no esenciales (como nombres o desglose de transacciones) para ahorrar ancho de banda de red celular.
   - **Seguridad:** Protegido con Spring Security Basic Auth (Usuario: `admin_mobile`).

4. **`bff-atm` (Puerto 8083):**
   - **Propósito:** Cajeros Automáticos.
   - **Mapeo de Datos:** Interfaz ultra-ligera y segura. Oculta nombres y fechas innecesarias, exponiendo únicamente los saldos y montos para operaciones críticas.
   - **Seguridad:** Protegido con Spring Security Basic Auth (Usuario: `admin_atm`).

## Patrones Utilizados ⚙️
- **Backend for Frontend (BFF):** Separación de interfaces para clientes.
- **Data Transfer Object (DTO):** Mapeo de `CoreDTO` a `WebDTO/MobileDTO/AtmDTO` para desacoplar el modelo de base de datos de la respuesta al cliente.
- **RestClient:** Cliente HTTP moderno de Spring Boot 3.2+ para la comunicación sincrónica entre los BFFs y el Core.
- **Spring Security (BCrypt):** Encriptación de contraseñas y autenticación obligatoria e independiente por canal.

## Instrucciones de Ejecución 🚀

### 1. Pre-requisitos
- Java 17+
- Base de datos MySQL corriendo en el puerto 3306 (con los datos cargados del batch previo).

### 2. Levantamiento de Servicios
Es estrictamente necesario arrancar los servicios en el siguiente orden:
1. Levantar el proyecto `bank_legacy` (Core).
2. Levantar el proyecto `bff-web`.
3. Levantar el proyecto `bff-mobile`.
4. Levantar el proyecto `bff-atm`.

### 3. Pruebas de APIs
Puedes consumir las APIs desde el navegador web o herramientas como Postman solicitando autenticación Basic Auth.

**Ejemplo BFF Web:**
- URL: `http://localhost:8081/api/bff-web/transacciones`
- Credenciales: `admin_web` / `12345`

**Ejemplo BFF Mobile:**
- URL: `http://localhost:8082/api/bff-mobile/estadoCuentas`
- Credenciales: `admin_mobile` / `12345`

**Ejemplo BFF ATM:**
- URL: `http://localhost:8083/api/bff-atm/intereses`
- Credenciales: `admin_atm` / `12345`

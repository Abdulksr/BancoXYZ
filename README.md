# Banco XYZ - Arquitectura Híbrida de Microservicios 🚀 (Fase Final - Semana 8)

Este proyecto implementa la arquitectura definitiva del Banco XYZ, evolucionando de un patrón Backend for Frontend (BFF) local a una **Arquitectura Híbrida en la Nube**, cumpliendo con los más altos estándares de orquestación (Docker), seguridad delegada (OAuth2) y procesamiento asíncrono de alto rendimiento (Apache Kafka).

---

## 🏗️ Estructura de la Arquitectura
El ecosistema completo ha sido dockerizado y se compone de **8 contenedores** interconectados:

### Ecosistema de Soporte y Seguridad
1. **mysql-db (Puerto 3307):** Base de datos relacional aislada en Docker para el almacenamiento del Core.
2. **eureka-server (Puerto 8761):** Servidor de descubrimiento de servicios.
3. **config-server (Puerto 8888):** Servidor de configuración centralizada (lee de config-data).
4. **auth-server (Puerto 9000):** NUEVO Servidor de Autorización **OAuth2**. Actúa como el único emisor de tokens JWT (Identity Provider).

### Microservicios de Negocio (Resource Servers)
5. **bank_legacy (Core - Puerto 8080):** 
   - Contiene la conexión a MySQL y expone los datos crudos.
   - Actúa como **Consumidor de Kafka**, procesando lotes de transacciones masivas (Spring Batch) y confirmando con Acks manuales para evitar Poison Pills.

6. **bff-web (Puerto 8081 - HTTPS):**
   - **Propósito:** Optimizado para navegadores. Expone reportes financieros.
   - **Kafka Producer:** Envía eventos de transacciones hacia la nube.
   - **Seguridad:** Configurado como OAuth2 Resource Server. Valida JWTs emitidos por el Auth-server y exige scopes granulares (cuentas.read, cuentas.write).

7. **bff-mobile (Puerto 8082 - HTTPS):** Respuestas ligeras para ahorro de ancho de banda. Protegido con OAuth2.
8. **bff-atm (Puerto 8083 - HTTPS):** Respuestas ultra-ligeras para cajeros automáticos. Protegido con OAuth2.

### La Nube (AWS EC2)
- **Apache Kafka + Zookeeper:** El clúster de mensajería está desplegado externamente en una instancia EC2 de AWS (3.233.6.38:29092), consolidando una arquitectura híbrida (On-Premise Docker + Cloud).

---

## 🛡️ Patrones y Tecnologías Implementadas (Semana 8)

- **Backend for Frontend (BFF):** Separación de canales de atención (Web, Mobile, ATM).
- **OAuth2.0 (Client Credentials Grant):** Se eliminó la seguridad manual. Ahora la autenticación está delegada al Auth-server (Máquina a Máquina). 
  - *Cajero:* Solo lectura (cuentas.read).
  - *Administrador:* Lectura y escritura (cuentas.write).
- **Arquitectura Híbrida de Eventos (Kafka):** Comunicación asíncrona tolerante a fallos. El productor inyecta eventos desde la red local hacia la nube, y el consumidor en el Core los descarga y procesa mediante Spring Batch.
- **Docker Multi-Stage Build:** Creación de imágenes Java súper ligeras. Docker se encarga de compilar el código fuente sin requerir herramientas instaladas en el Host.
- **Docker Compose:** Orquestación de red interna (depends_on, 
estart: on-failure) para auto-curación del clúster sin intervención humana.
- **Resilience4j:** Protección robusta en la comunicación hacia el Core Legacy mediante Circuit Breaker, Retry, Rate Limiter y rutinas de *Fallback* controladas.
- **SSL/TLS:** Cifrado de red (HTTPS) local.

---

## 🚀 Guía Definitiva de Despliegue

### 1. Pre-requisitos
- **Docker Desktop** instalado y corriendo.
- (Opcional) Instancia de Kafka corriendo en AWS.

### 2. Levantamiento de TODO el Ecosistema (Un Solo Clic)
Ya no es necesario levantar proyectos manualmente desde el IDE ni tener MySQL instalado.
Simplemente abre una terminal en la carpeta raíz (BancoXYZ_Microservicios) y ejecuta:

`bash
docker-compose up -d --build
`
*Docker compilará el código de los 7 microservicios, descargará MySQL y encenderá todo en el orden correcto.*

### 3. Pruebas End-to-End en Postman

**A. Obtener el Token (OAuth2 Auth Server)**
Para consumir los BFFs, primero debes pedirle un JWT al servidor de autorización.
- **Endpoint:** POST http://localhost:9000/oauth2/token
- **Auth (Basic):** Usuario:  admin-client / Password:  admin123
- **Body (x-www-form-urlencoded):** 
  - grant_type: client_credentials
  - scope: cuentas.read cuentas.write

**B. Consumir Datos del Core (Circuit Breaker & Scopes)**
- **Endpoint:** GET https://localhost:8081/api/bff-web/estadoCuentas
- **Headers:** Authorization: Bearer <TU_TOKEN>
- *(Requiere permiso cuentas.read. Si bank_legacy se apaga, retornará un JSON vacío [] gracias a Resilience4j).*

**C. Disparar Evento a la Nube (AWS Kafka Producer)**
- **Endpoint:** POST https://localhost:8081/api/bff-web/kafka/transacciones-batch
- **Headers:** Authorization: Bearer <TU_TOKEN>
- *(Requiere permiso cuentas.write)*.
- **Body JSON:**
  `json
  {
      "tipoProceso": "BATCH_TRANSACCIONES"
  }
  `
- *Resultado:* El BFF confirmará (HTTP 200) y enviará el mensaje a AWS. Segundos después, la consola del bank-legacy en Docker imprimirá el procesamiento exitoso del evento.

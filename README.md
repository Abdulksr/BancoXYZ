# Banco XYZ - Arquitectura Híbrida de Microservicios 🏢☁️ (Evaluación Final - Semana 9)

Este repositorio contiene la arquitectura definitiva del **Banco XYZ**, representando la culminación de un proceso de modernización agresiva. El sistema ha evolucionado desde un monolito local cerrado hacia un ecosistema distribuido, tolerante a fallos, asíncrono y desplegado bajo un modelo de **Nube Híbrida**.

---

## 🏗️ Estructura de la Arquitectura (8 Contenedores)
Todo el ecosistema de microservicios está automatizado, dockerizado mediante **Multi-stage builds** y orquestado en una red virtual.

### Capa de Infraestructura y Seguridad Local
1. **mysql-db (Puerto 3307):** Motor de base de datos relacional aislado en Docker.
2. **eureka-server (Puerto 8761):** Service Registry para enrutamiento dinámico.
3. **config-server (Puerto 8888):** Gestión de configuraciones centralizadas (`config-data`).
4. **auth-server (Puerto 9000):** Servidor de Autorización **OAuth2**. Actúa como el único emisor y firmante de tokens JWT del banco.

### Capa de Cómputo Core (Procesamiento Pesado)
5. **bank-legacy (Puerto 8080):** 
   - Conectado a MySQL, expone los datos crudos del banco.
   - **Consumidor Inteligente de Kafka:** Escucha permanentemente el clúster de AWS. Posee un enrutador interno (`switch`) capaz de procesar **3 tipos de Jobs** masivos usando **Spring Batch**:
     - *Transacciones Diarias*
     - *Intereses Mensuales*
     - *Estados de Cuenta Anuales*
   - Implementa **Manual Acks** para prevención de envenenamiento de colas (Poison Pills).

### Capa Frontal (Backend for Frontend - BFFs)
Todos los BFFs son **OAuth2 Resource Servers**. Verifican criptográficamente los JWTs sin llamar al Auth-Server y aplican seguridad granular mediante Scopes (`cuentas.read`, `cuentas.write`).
6. **bff-web (Puerto 8081 - HTTPS):** Respuestas de datos completos para portales web.
7. **bff-mobile (Puerto 8082 - HTTPS):** Respuestas ultra-ligeras para ahorro de batería y datos móviles.
8. **bff-atm (Puerto 8083 - HTTPS):** Interfaces rápidas para terminales de cajero automático.

*(Novedad Final: **Los 3 BFFs actúan como Productores de Kafka**. Proveen endpoints estrictamente tipados para disparar eventos asíncronos hacia la nube de AWS).*

### Capa de Datos en la Nube (AWS EC2)
- **Apache Kafka + Zookeeper:** El bus de eventos principal (`batch-process-topic`) se encuentra alojado en una instancia externa en AWS. Esta separación arquitectónica asegura que los picos de procesamiento asíncrono no saturen la red local de cómputo Docker, logrando un verdadero **despliegue híbrido**.

---

## 🚀 Guía de Instalación y Pruebas
Dado que el proyecto utiliza contenedores con construcción multi-etapa, **no es necesario tener Java, Maven ni MySQL instalados localmente**. 

1. Abre una consola en este directorio raíz.
2. Ejecuta:
   ```bash
   docker-compose up -d --build
   ```
3. Espera 3 minutos a que Docker descargue imágenes, compile código fuente e inicie los 8 servidores.

Para ver el **Manual Completo de Pruebas con Postman** (Circuit Breakers, OAuth2, Generación de Tokens y Ejecución de Spring Batch vía Kafka), consulta el archivo oficial:
👉 [Ver instrucciones.md](instrucciones.md)

Para entender a fondo las decisiones arquitectónicas de nube, consulta el documento técnico:
👉 [Ver despliegue.md](despliegue.md)

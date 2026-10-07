# Estrategia de Despliegue en la Nube ☁️

Para el ecosistema de microservicios del Banco XYZ, se ha diseñado e implementado una **Arquitectura de Despliegue Híbrida**. Esta estrategia combina la flexibilidad, control y portabilidad de los contenedores locales (Docker) con la robustez, resiliencia y escalabilidad infinita de la nube pública de Amazon Web Services (AWS).

## 1. Justificación de la Arquitectura Híbrida
En lugar de desplegar toda la infraestructura en la nube de golpe (lo cual incurriría en altos costos innecesarios para entornos de desarrollo y pruebas), se ha optado por dividir estratégicamente los componentes según su criticidad de datos:

*   **Componentes Críticos de Datos y Eventos (Apache Kafka):** Desplegados en una instancia dedicada en la nube (AWS EC2). Dado que Kafka es el bus vertebral que transporta los comandos de ejecución de procesos pesados (Spring Batch), alojarlo en la nube garantiza alta disponibilidad, procesamiento tolerante a fallos y previene cuellos de botella en la red local.
*   **Microservicios de Cómputo (BFFs, Core, Seguridad):** Dockerizados localmente y orquestados mediante Docker Compose. Mantienen el procesamiento cerrado y seguro en un entorno de fácil administración.

Esta separación asegura que el bus de mensajes asíncrono tenga escalabilidad inmediata, mientras mantenemos un control estricto sobre el procesamiento del Core (`bank_legacy`) y los canales front-end.

---

## 2. Despliegue del Clúster de Kafka en AWS EC2

Para alojar el clúster de mensajería, se utilizó una instancia EC2 configurada de la siguiente manera:

### A. Creación y Configuración de la Instancia
1.  Se aprovisionó una instancia **EC2 t2.medium** (necesaria para soportar los requerimientos de memoria combinados de Kafka y Zookeeper) corriendo Amazon Linux.
2.  Se le asignó una **Elastic IP** estática. Esta IP pública es el puente vital que permite a los microservicios locales saber exactamente a qué servidor en la nube disparar los comandos, incluso si la instancia se reinicia.
3.  Se configuraron los **Security Groups** para permitir tráfico TCP en los puertos clave:
    *   `22` (SSH para administración).
    *   `9092 / 29092` (Puertos del Broker de Kafka, para tráfico interno y externo).
    *   `2181` (Zookeeper).

### B. Levantamiento de los Servicios en la Nube
1.  Dentro de la instancia AWS, se instaló Docker y se configuró un archivo `docker-compose.yml` que orquesta las imágenes oficiales de Confluent (`confluentinc/cp-zookeeper`, `confluentinc/cp-kafka`).
2.  **Configuración Crítica de Enrutamiento:** Para asegurar que los contenedores locales de Spring Boot pudieran alcanzar el clúster, se configuró explícitamente el parámetro `KAFKA_ADVERTISED_LISTENERS` apuntando a la IP Elástica pública.

---

## 3. Conexión de los Microservicios al Entorno Cloud

Con la infraestructura de nube preparada, el despliegue del ecosistema de Spring Boot se reduce a un comando, gracias a la inyección dinámica de variables de entorno.

1.  En la configuración de los microservicios (`config-data`), se actualizó la propiedad de conexión a Kafka:
    `spring.kafka.bootstrap-servers=<ELASTIC_IP_AWS>:29092`
2.  Al ejecutar `docker-compose up -d --build` en la máquina local, los 7 microservicios de Spring Boot (junto con la base de datos MySQL) nacen dentro de una red Docker privada.
3.  Los microservicios **BFF (Web, Mobile, ATM)** exponen de forma segura los endpoints REST hacia el usuario final.
4.  Cuando un BFF recibe un comando de ejecución, sale a internet, se conecta al broker de Kafka alojado en AWS y deposita el `EventoBatchDTO`.
5.  Simultáneamente, el Core del banco (`bank_legacy`), que también está conectado al bus de AWS, consume el evento y detona asíncronamente el trabajo de Spring Batch correspondiente.

---

## 4. Estrategia de Escalabilidad Futura (AWS ECS / EKS)
Dado que el código ha sido desarrollado bajo los patrones de 12-factor apps y está 100% contenerizado (Multi-stage builds), la evolución natural hacia un despliegue 100% Cloud-Native es trivial.

En caso de que el tráfico a los canales digitales aumente exponencialmente:
1.  Las imágenes de Docker de los BFFs se subirían a un registro privado (**AWS ECR**).
2.  Se desplegarían utilizando **Amazon ECS (Elastic Container Service)** o **EKS (Kubernetes)**.
3.  Esto permitiría escalabilidad horizontal automática: si el uso de CPU de `bff-mobile` supera el 70%, ECS lanzará nuevas instancias del contenedor automáticamente, todas respaldadas por nuestro `eureka-server` y conectadas a la misma tubería de Kafka.

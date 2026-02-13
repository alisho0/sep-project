# Sistema Escolar Privado (SEP) | Backend
## 🏫 Descripción del proyecto
Backend para el sistema de gestión escolar desarrollado con Spring Boot.
Permite la administración de alumnos, grados, ciclos lectivos y usuarios con distintos niveles de acceso, implementando control de roles y relaciones complejas entre entidades.
El proyecto fue diseñado siguiendo una arquitectura en capas, priorizando separación de responsabilidades y buenas prácticas de desarrollo backend.

🛠️ Stack Tecnológico
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- Arquitectura en capas

🔐 Seguridad
- Autenticación basada en JWT
- Roles diferenciados: MAESTRO y DIRECTOR
- Restricción de endpoints según permisos

📌 Funcionalidades principales
- Alta, edición y consulta de alumnos
- Gestión de grados y ciclos lectivos
- Relación entre alumnos y grados mediante asociaciones JPA
- Registro de observaciones docentes
- Métricas básicas para análisis académico
- Manejo de errores centralizado

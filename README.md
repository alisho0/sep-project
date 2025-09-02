# Sistema de Gestión Escolar (Backend)
## 🏫 Descripción del proyecto
Este backend implementa un sistema de gestión escolar orientado a registrar alumnos, grados, ciclos lectivos, observaciones docentes y usuarios con roles diferenciados. Está construido con Spring Boot, JPA/Hibernate, y sigue una arquitectura modular basada en entidades claras y relaciones bien definidas.

### 🚀 Tecnologías utilizadas
- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven

### 🔐 Roles y permisos
El sistema contempla los siguientes roles:
- DIRECTOR: puede crear usuarios, ver todos los registros y observaciones.
- MAESTRO: puede registrar observaciones y consultar alumnos asignados.
- ADMIN: acceso completo (gestión técnica).

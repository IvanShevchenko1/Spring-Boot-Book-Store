| Variable | Description | Example |
|-----------|--------------|---------|
| `SPRING_LOCAL_PORT` | Port on your host (localhost) that exposes the Spring Boot app | `8088` |
| `SPRING_DOCKER_PORT` | Internal port used inside the container by the Spring Boot app | `8080` |
| `DEBUG_PORT` | Optional remote debugging port for the Spring container | `5005` |

| Variable | Description | Example |
|-----------|--------------|---------|
| `MYSQLDB_LOCAL_PORT` | Port on your host for accessing MySQL | `3306` |
| `MYSQLDB_DOCKER_PORT` | Internal MySQL port inside the container | `3306` |
| `MYSQLDB_HOST` | Hostname that Spring Boot connects to inside the Docker network (usually the service name) | `db` |
| `MYSQLDB_DATABASE` | Name of the database to be created | `book_store_db` |
| `MYSQLDB_USER` | Application database username (non-root) | `bookuser` |
| `MYSQLDB_PASSWORD` | Password for the above user | `bookpass` |
| `MYSQL_ROOT_PASSWORD` | Root user password (for MySQL initialization) | `supersecretroot` |
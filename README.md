# To-Do List (Spring Boot + PostgreSQL + Lombok)

Ứng dụng to-do list được xây dựng bằng Spring Boot, sử dụng PostgreSQL làm cơ sở dữ liệu và Lombok để giảm boilerplate code.

## Yêu cầu
- JDK 17
- Maven 3.9+
- PostgreSQL (đã tạo database, ví dụ: `todo_db`)

## Cấu hình
Chỉnh sửa `src/main/resources/application.properties` cho thông tin kết nối PostgreSQL:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/todo_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## Chạy ứng dụng
```bash
mvn spring-boot:run
```
Ứng dụng chạy tại `http://localhost:8080`, trang chính chứa giao diện quản lý công việc và gọi API REST:
- `GET    /api/tasks`
- `POST   /api/tasks`
- `GET    /api/tasks/{id}`
- `PUT    /api/tasks/{id}`
- `POST   /api/tasks/{id}/toggle`
- `DELETE /api/tasks/{id}`

## Kiểm thử
```bash
mvn test
```
Hồ sơ `test` sử dụng H2 (chế độ PostgreSQL) để chạy kiểm thử mà không cần PostgreSQL cục bộ.

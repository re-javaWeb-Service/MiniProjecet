

## API

Base URL: `http://localhost:8080/api/students`

| Method | Endpoint | Ket qua |
| --- | --- | --- |
| GET | `/api/students` | Lay danh sach sinh vien |
| GET | `/api/students/{id}` | Lay sinh vien theo id |
| POST | `/api/students` | Them sinh vien moi |
| PUT | `/api/students/{id}` | Cap nhat toan bo |
| PATCH | `/api/students/{id}` | Cap nhat mot phan |
| DELETE | `/api/students/{id}` | Xoa sinh vien |

## Vi du body POST

```json
{
  "fullName": "Nguyen Van A",
  "email": "nguyenvana@example.com",
  "gpa": 3.2
}
```

## Test JSON/XML

Trong Postman, doi header `Accept`:

```text
Accept: application/json
```

hoac:

```text
Accept: application/xml
```

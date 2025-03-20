# 📘 Student-Course Registration System (Spring Boot + MySQL)

A simple web application for managing students and course registrations.

---

## ⚙️ Tech Stack
- Java + Spring Boot
- MySQL
- Maven
- IntelliJ (Optional)

---

## 🚀 Getting Started

### 1. Clone the Repository:
```bash
git clone https://github.com/your-username/your-repo-name.git
cd your-repo-name
```

### 2. Create the Database schema:
use SQL script provided "createDBschema.sql" for creating the database schema.

the DB should be configure on localhost:3306. username = root.

### 3. Build the Project:
```bash
mvn clean install
```

### 4. Run the Application:
The application will start on http://localhost:8080.


## 📌 API Endpoints

### 🧑‍💻 Admin Endpoints (/api/admin)

**Admin Login:** `POST /api/admin/login`

**Create Student:** `POST /api/admin/students`

**Get All Students:** `GET /api/admin/students`

**Get Student:** `GET /api/admin/students/{id}`

**Update Student:** `PUT /api/admin/students/{id}`

**Delete Student:** `DELETE /api/admin/students/{id}`

**Create Course:** `POST /api/admin/courses`

**Get All Courses:** `GET /api/admin/courses`

**Get Courses:** `GET /api/admin/courses/{id}`

**Update Course:** `PUT /api/admin/courses/{id}`

**Delete Course:** `DELETE /api/admin/courses/{id}`


### 🧑‍🎓 Student Endpoints (/api/student)

**Student Login:** `POST /api/student/login`

**Register for Course:** `POST /api/student/register/{courseId}`

**Cancel Course Registration:** `DELETE /api/student/cancel/{courseId}`

**Get All Student Course:** `GET /api/student/myCourses`

### 📝 Notes:
After login, include the Authorization header in all requests.

Students can register for up to 2 courses and each course has a limit of 30 students.

You can find attached Postman collection for testing.


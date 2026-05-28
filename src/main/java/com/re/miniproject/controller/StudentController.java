package com.re.miniproject.controller;

import com.re.miniproject.entity.Student;
import com.re.miniproject.repository.StudentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@RequestBody Student student) {
        if (student.getId() != null) {
            return ResponseEntity.badRequest().build();
        }

        Student createdStudent = studentRepository.save(student);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    existingStudent.setFullName(student.getFullName());
                    existingStudent.setEmail(student.getEmail());
                    existingStudent.setGpa(student.getGpa());

                    Student updatedStudent = studentRepository.save(existingStudent);
                    return ResponseEntity.ok(updatedStudent);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Student> updateStudentPartially(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates
    ) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    applyPatch(existingStudent, updates);
                    Student updatedStudent = studentRepository.save(existingStudent);
                    return ResponseEntity.ok(updatedStudent);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private void applyPatch(Student student, Map<String, Object> updates) {
        if (updates.containsKey("fullName")) {
            student.setFullName((String) updates.get("fullName"));
        }

        if (updates.containsKey("email")) {
            student.setEmail((String) updates.get("email"));
        }

        if (updates.containsKey("gpa")) {
            student.setGpa(toDouble(updates.get("gpa")));
        }
    }

    private Double toDouble(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Number number) {
            return number.doubleValue();
        }

        return Double.valueOf(value.toString());
    }
}

package com.sid.gl.controller;

import com.sid.gl.dto.StudentRequest;
import com.sid.gl.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody StudentRequest studentRequest){
        return ResponseEntity.ok(studentService.addStudent(studentRequest));
    }

    @GetMapping
    public ResponseEntity<?> listStudents(){
        return ResponseEntity.ok(studentService.listStudents());
    }
}

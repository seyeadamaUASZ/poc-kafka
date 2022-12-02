package com.sid.gl.controller;

import com.sid.gl.dto.PaymentRequest;
import com.sid.gl.kafka.PaymentService;
import com.sid.gl.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/payment")
public class PaymentController {
    private PaymentService paymentService;
    private StudentService studentService;

    public PaymentController(PaymentService paymentService, StudentService studentService) {
        this.paymentService = paymentService;
        this.studentService = studentService;
    }


    @GetMapping("/non-pay")
    public ResponseEntity<?> listStudentNonPay(){
        return ResponseEntity.ok(studentService.listStudentNoPay());
    }

    @PostMapping("/{id}")
    public ResponseEntity<?> addPayment(@PathVariable("id") Long id, @RequestBody PaymentRequest paymentRequest){
        return ResponseEntity.ok(paymentService.addPayment(id,paymentRequest));
    }
}

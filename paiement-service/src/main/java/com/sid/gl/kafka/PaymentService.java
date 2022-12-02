package com.sid.gl.kafka;

import com.sid.gl.constants.KafkaConstant;
import com.sid.gl.dto.PaymentRequest;
import com.sid.gl.dto.SubscriptionEvent;
import com.sid.gl.entity.Payment;
import com.sid.gl.entity.Student;
import com.sid.gl.repository.PaymentRepository;
import com.sid.gl.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PaymentService {
    private StudentService studentService;
    private PaymentRepository paymentRepository;

    private PaymentProducer paymentProducer;
    private static final Logger LOGGER =
            LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(StudentService studentService, PaymentRepository paymentRepository, PaymentProducer paymentProducer) {
        this.studentService = studentService;
        this.paymentRepository = paymentRepository;
        this.paymentProducer = paymentProducer;
    }


    @KafkaListener(topics = KafkaConstant.TOPIC_CONSTANT,groupId = "payment")
    public void consume(SubscriptionEvent event){
        LOGGER.info(String.format(" Student subscription received %s",event.toString()));
    }

    public Payment addPayment(Long id, PaymentRequest paymentRequest){
        Optional<Student> optionalStudent = studentService.getStudent(id);
        if(optionalStudent.isEmpty()){
            throw new RuntimeException("student with id not found");
        }else{
            Payment payment = new Payment();
            payment.setAmount(paymentRequest.getAmount());
            payment.setUserId(id);
            //update student pay
            studentService.updateStudentPay(id);
            Payment payment1 = paymentRepository.save(payment);
            SubscriptionEvent event = new SubscriptionEvent();
            event.setStatus("SUCCESS");
            event.setMessage("Successfully payment....");
            event.setData(payment1);
            paymentProducer.sendMessage(event);
            return payment1;
        }

    }
}

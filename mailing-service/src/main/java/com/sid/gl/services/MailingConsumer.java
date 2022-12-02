package com.sid.gl.services;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sid.gl.constants.KafkaConstants;
import com.sid.gl.dto.SubscriptionEvent;
import com.sid.gl.entity.Payment;
import com.sid.gl.entity.Student;
import com.sid.gl.mail.SendMail;
import com.sid.gl.service.StudentService;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MailingConsumer {

    @Autowired
    private SendMail sendMail;

    @Autowired
    private StudentService studentService;

    private static final Logger LOGGER =
            LoggerFactory.getLogger(MailingConsumer.class);

    @KafkaListener(topics = KafkaConstants.TOPIC_CONSTANT,groupId = "mailing")
    public void consume(SubscriptionEvent event){
        LOGGER.info(String.format(" payment student received %s",event.toString()));
        //send email on user pay money
        ObjectMapper mapper = new ObjectMapper();
        Payment payment = mapper.convertValue(event.getData(),Payment.class);
        Optional<Student> optionalStudent = studentService.getStudent(payment.getUserId());
        if(optionalStudent.isEmpty())
            throw new ResourceNotFoundException("Student with id not found ...");

        Student student = optionalStudent.get();
        //sending mail on student to confirm successfully payment
        sendMail.sendEmail(student.getEmail(),"payment made successfully......","Payment student");
    }
}

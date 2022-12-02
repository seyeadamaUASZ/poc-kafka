package com.sid.gl.service;

import com.sid.gl.constants.KafkaConstant;
import com.sid.gl.dto.StudentRequest;
import com.sid.gl.dto.SubscriptionEvent;
import com.sid.gl.entity.Student;
import com.sid.gl.repository.StudentRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(StudentService.class);

    private KafkaTemplate<String, SubscriptionEvent> kafkaTemplate;
    private StudentRepository studentRepository;

    public StudentService(KafkaTemplate<String, SubscriptionEvent> kafkaTemplate, StudentRepository studentRepository) {
        this.kafkaTemplate = kafkaTemplate;
        this.studentRepository = studentRepository;
    }

    public Student addStudent(StudentRequest studentRequest){
        Student student = new Student();
        student.setEmail(studentRequest.getEmail());
        student.setName(studentRequest.getName());
        Student student1 = studentRepository.save(student);
        SubscriptionEvent event = new SubscriptionEvent();
        event.setData(student1);
        event.setMessage("student successfully added.......");
        event.setStatus("SUCCESS");

        //send message on kafka cluster
        sendMessage(event);
        return student1;
    }

    public void sendMessage(SubscriptionEvent event){
        LOGGER.info(
                String.format("order event --- %s",event.toString())
        );
        //create message
        Message<SubscriptionEvent> message =
                MessageBuilder
                        .withPayload(event)
                        .setHeader(KafkaHeaders.TOPIC, KafkaConstant.TOPIC_CONSTANT)
                        .build();
        kafkaTemplate.send(message);

    }

    public List<Student> listStudents(){
        return studentRepository.findAll();
    }

    public List<Student> listStudentNoPay(){
        return studentRepository.findByPayFalse();
    }

    public Student updateStudentPay(Long id){
        Student student = studentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("student not found with id :"+id));
        student.setId(id);
        student.setPay(true);
        return studentRepository.save(student);
    }

    public Optional <Student> getStudent(Long id){
        return  studentRepository.findById(id);
    }
}

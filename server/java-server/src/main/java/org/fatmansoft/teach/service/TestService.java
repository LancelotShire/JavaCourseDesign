package org.fatmansoft.teach.service;

import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestService {
    @Autowired
    private StudentRepository studentRepository;
}

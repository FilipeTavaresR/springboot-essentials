package br.com.devdojo.endpoint;

import br.com.devdojo.error.CustomErrorType;
import br.com.devdojo.error.ResourceNotFoundException;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

//teste
@RestController
@RequestMapping("students")
public class StudentEndpoint {

    private final StudentRepository studentDao;

    @Autowired
    public StudentEndpoint(StudentRepository studentDao){
        this.studentDao = studentDao;
    }

    @GetMapping
    public ResponseEntity<?> listAll(Pageable pageable) {
        return new ResponseEntity<>(studentDao.findAll(PageRequest.of(0,5)), HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable("id") Long id) {
        Optional<Student> student = studentDao.findById(id);
        if (!student.isPresent())
            throw new ResourceNotFoundException("Student not found for ID: "+id);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    @GetMapping(path = "/findByName/{name}")
    public ResponseEntity<?> findStudentsByName(@PathVariable("name") String name) {
        return new ResponseEntity<>(studentDao.findByNameIgnoreCaseContaining(name), HttpStatus.OK);
    }

    @PostMapping
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> save(@Valid @RequestBody Student student) {
        return new ResponseEntity<>(studentDao.save(student), HttpStatus.CREATED);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestBody Student student) {
        studentDao.delete(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody Student student) {
        studentDao.save(student);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

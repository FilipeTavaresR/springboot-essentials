package br.com.devdojo;
import br.com.devdojo.model.Student;
import br.com.devdojo.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void createShouldPersistData(){
        //given
        Student student = new Student("Filipe", "filipe@filipe.com.br");
        //when
        Student studentSalvo = this.studentRepository.save(student);
        //then
        assertThat(studentSalvo.getId()).isNotNull();
        assertThat(studentSalvo.getName()).isEqualTo("Filipe");
        assertThat(studentSalvo.getEmail()).isEqualTo("filipe@filipe.com.br");
    }

    @Test
    public void deleteShouldRemoveData(){
        Student student = new Student("Filipe", "filipe@filipe.com.br");
        this.studentRepository.save(student);
        studentRepository.delete(student);
        assertThat(studentRepository.findById(student.getId())).isEmpty();
    }

    @Test
    public void updateShouldChangeAndPersistData(){
        Student student = new Student("Filipe", "filipe@filipe.com.br");
        student.setName("Filipe2");
        student.setEmail("filipe2@filipe.com.br");
        Student studentSalvado = this.studentRepository.save(student);
        assertThat(studentSalvado.getName()).isEqualTo("Filipe2");
        assertThat(studentSalvado.getEmail()).isEqualTo("filipe2@filipe.com.br");
        assertThat(studentSalvado.getId()).isNotNull();
    }

    @Test
    public void findByNameIgnoreCaseContainingShouldIgnoreCase(){
        Student student = new Student("Filipe", "filipe@filipe.com.br");
        Student student2 = new Student("filipe2", "filipe2@filipe.com.br");
        Student studentSalvado = this.studentRepository.save(student);
        Student studentSalvado2 = this.studentRepository.save(student2);

        List<Student> studentList = studentRepository.findByNameIgnoreCaseContaining("filipe");
        assertThat(studentList.size()).isEqualTo(2);
    }

   /* @Test
    public void createWhenNameIsNullShouldThrowConstraintViolationException(){
        Exception exception = assertThrows(
                RuntimeException.class,
                () -> studentRepository.save(new Student(null, "email@gmail.com")));
        assertTrue(exception.getMessage().contains("Name cannot be null"));
    }*/

}

package br.com.devdojo;

import br.com.devdojo.model.Student;
import br.com.devdojo.model.User;
import br.com.devdojo.repository.StudentRepository;
import br.com.devdojo.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static java.util.Arrays.asList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@EnableAutoConfiguration

public class StudentEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @MockBean
    private StudentRepository studentRepository;

    @Autowired
    private MockMvc mockMvc;

    @TestConfiguration
    static class Config{
        @Bean
        public RestTemplateBuilder restTemplateBuilder(){
            return new RestTemplateBuilder().basicAuthentication("admin","admin");
        }
    }

    @Autowired
    private UserRepository userRepository;

    private static boolean criado = false;

    @BeforeEach
    public void init(){
        if(!criado) {
            User user = new User();
            user.setName("admin");
            user.setUsername("admin");
            user.setPassword("$2a$10$niEoYuoFPZv7PZxAQnjHyOUHVDf7Gb1hy6YdAms.Taa37jgZ1gvoy");
            user.setAdmin(true);
            userRepository.save(user);
            criado = true;
        }
        List<Student> students = asList(new Student("Legolas", "legolas@hotmail.com", 1l),
                new Student("Aragorn", "aragorn@hotmail.com", 2l));
        BDDMockito.when(studentRepository.findAll()).thenReturn(students);
    }


    @Test
    public void listStudentsWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401(){
        restTemplate = restTemplate.withBasicAuth("1","1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void getStudentsByIdWhenUsernameAndPasswordAreIncorrectShouldReturnStatusCode401(){
        restTemplate = restTemplate.withBasicAuth("1","1");
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/1", String.class);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(401);
    }

    @Test
    public void listStudentsWhenUsernameAndPasswordAreCorrectShouldReturnStatusCode200(){
        ResponseEntity<String> response = restTemplate.getForEntity("/v1/protected/students/", String.class);
        System.out.println(response);
        Assertions.assertThat(response.getStatusCodeValue()).isEqualTo(200);
    }


    @Test
    public void deleteWhenUserHasRoleAdminAndStudentExistsShoudReturnStatusCode200(){
        BDDMockito.doNothing().when(studentRepository).deleteById(1L);
        ResponseEntity<String> exchange = restTemplate.exchange("/v1/admin/students/{id}", HttpMethod.DELETE, null, String.class, 1L);
        Assertions.assertThat(exchange.getStatusCodeValue()).isEqualTo(200);
    }

//    @Test
//    @WithMockUser(username = "xx", password = "xx", roles = {"USER","ADMIN"})
//    public void deleteWhenUserHasRoleAdminAndStudentNotExistsShoudReturnStatusCode404() throws Exception {
//        BDDMockito.doNothing().when(studentRepository).deleteById(70L);
//        ResultActions perform = mockMvc.perform(MockMvcRequestBuilders
//                .delete("/v1/admin/students/{id}", 70L));
//        perform
//                        .andExpect(status().isNotFound());
//        System.out.println(perform.andReturn().getRequest().toString());
//    }


}

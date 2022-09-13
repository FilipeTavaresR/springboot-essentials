package br.com.devdojo.javaclient;

import br.com.devdojo.model.Student;

public class JavaSpringClientTest {
    public static void main(String[] args) {


        Student studentPost = new Student();
        studentPost.setName("post1");
        studentPost.setEmail("post1@post.com.br");
        JavaClientDao dao = new JavaClientDao();
        System.out.println(dao.findById(1));
        System.out.println(dao.listAll());
        System.out.println(dao.save(studentPost));



    }

}

 // 1. MySQL Setup
CREATE DATABASE studentdb;

USE studentdb;

CREATE TABLE student (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    email VARCHAR(100)
);
 //2. Student.java
package com.example;

import jakarta.persistence.*;

@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;

    // Constructors
    public Student() {}
    public Student(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
 // 3. hibernate.cfg.xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-configuration PUBLIC
 "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
 "http://hibernate.sourceforge.net/hibernate-configuration-3.0.dtd">

<hibernate-configuration>
 <session-factory>
   <property name="hibernate.connection.driver_class">com.mysql.cj.jdbc.Driver</property>
   <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/studentdb</property>
   <property name="hibernate.connection.username">root</property>
   <property name="hibernate.connection.password">your_password</property>

   <property name="hibernate.dialect">org.hibernate.dialect.MySQLDialect</property>
   <property name="hibernate.hbm2ddl.auto">update</property>
   <property name="show_sql">true</property>

   <mapping class="com.example.Student"/>
 </session-factory>
</hibernate-configuration>
// 4. StudentDAO.java 
  package com.example;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

public class StudentDAO {
    private SessionFactory factory;

    public StudentDAO() {
        factory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    // CREATE
    public void addStudent(Student student) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        session.save(student);
        tx.commit();
        session.close();
    }

    // READ
    public Student getStudent(int id) {
        Session session = factory.openSession();
        Student student = session.get(Student.class, id);
        session.close();
        return student;
    }

    // UPDATE
    public void updateStudent(int id, String newEmail) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Student student = session.get(Student.class, id);
        if (student != null) {
            student.setEmail(newEmail);
            session.update(student);
        }
        tx.commit();
        session.close();
    }

    // DELETE
    public void deleteStudent(int id) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();
        Student student = session.get(Student.class, id);
        if (student != null) {
            session.delete(student);
        }
        tx.commit();
        session.close();
    }

    public void close() {
        factory.close();
    }
}
 // 5. Main.java
package com.example;

public class Main {
    public static void main(String[] args) {
        StudentDAO dao = new StudentDAO();

        // Create
        Student s1 = new Student("Sarthak Rana", "sarthak@example.com");
        dao.addStudent(s1);

        // Read
        Student fetched = dao.getStudent(s1.getId());
        System.out.println("Fetched: " + fetched.getName() + ", " + fetched.getEmail());

        // Update
        dao.updateStudent(fetched.getId(), "updated_email@example.com");

        // Delete
        dao.deleteStudent(fetched.getId());

        dao.close();
    }
}

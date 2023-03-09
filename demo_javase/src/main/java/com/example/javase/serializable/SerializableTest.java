package com.example.javase.serializable;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.*;

/**
 * 序列化测试
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 11:05
 * @since 1.8
 **/
@Slf4j
public class SerializableTest {

    @Test
    public void testOutputFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("teacher.txt"))) {
            EnglishCourse englishCourse = new EnglishCourse("英语",89);
            Student student = new Student("路飞", 20,englishCourse);
            //将对象写入输入流
            oos.writeObject(student);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        }
    }

    @Test
    public void testInput() {
        try (ObjectInputStream ooi = new ObjectInputStream(new FileInputStream("teacher.txt"))) {
            Student student = (Student) ooi.readObject();
            log.info("反序列化对象:{}",student);
        } catch (IOException | ClassNotFoundException e) {
            log.error(e.getMessage(),e);
        }
    }

    @Test
    public void testByteArray() throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        ObjectOutputStream oos=null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(byteArrayOutputStream);

            EnglishCourse englishCourse = new EnglishCourse("英语测试",91);
            Student student = new Student("佐助", 21,englishCourse);
            // 将对象写入输入流
            oos.writeObject(student);
            bytes = byteArrayOutputStream.toByteArray();
            log.info("序列化后的数据:{}",bytes);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally {
            try {
                assert byteArrayOutputStream != null;
                byteArrayOutputStream.close();
                assert oos != null;
                oos.close();
            } catch (IOException e) {
                log.info("序列化后的对象:{}",bytes);
            }
        }

        ByteArrayInputStream bin = null;
        ObjectInputStream oin = null;
        bin = new ByteArrayInputStream(bytes);
        oin = new ObjectInputStream(bin);
        Student person = null;
        try {
            person = (Student) oin.readObject();
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        System.out.println(person);
    }
}

package com.example.javase.inner;

/**
 * @author pangruidong
 * @version 1.0
 * @date 2023-02-08 13:39
 * @since 1.8
 **/
public class PersonA extends Person{

    @Override
    public void doWork() {
        super.doWork();
    }

    private class Name extends Person.Name {
        @Override
        public void work() {
            System.out.println("PersonA NameA Work");
        }
    }
}

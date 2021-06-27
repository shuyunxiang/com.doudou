package com.doudou.rxjava;

import io.reactivex.*;
import io.reactivex.schedulers.Schedulers;


public class RxjavaBackpressure {

    static GetNameService getPersonService = new GetNameService();
    static GetAgeService getAgeService = new GetAgeService();

    public static void main(String[] args) throws InterruptedException {

        long beginTime = System.currentTimeMillis();

        Person person = new Person();

        Observable<Object> initName = Observable.create(emitter -> {
            person.setName(getPersonService.getName());
            emitter.onComplete();
        }).subscribeOn(Schedulers.newThread());

        Observable<Object> initAge = Observable.create(emitter -> {
            person.setAge(getAgeService.getAge());
            emitter.onComplete();
        }).subscribeOn(Schedulers.newThread());

        Observable.merge(initName,initAge).blockingSubscribe();

        System.out.println(person);

        System.out.println(System.currentTimeMillis()-beginTime);

    }
}

class Person {

    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "GetPersonService{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}

class GetNameService {

    public String getName() throws InterruptedException {
        Thread.sleep(3000);
        return "舒云翔";
    }
}

class GetAgeService {

    public Integer getAge() throws InterruptedException {
        Thread.sleep(2000);
        return 18;
    }
}



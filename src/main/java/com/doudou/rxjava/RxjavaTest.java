package com.doudou.rxjava;


import io.reactivex.*;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.Serializable;

public class RxjavaTest {

//    private static String name;

    public static void main(String[] args) throws InterruptedException {

        long startTime = System.currentTimeMillis();

        NameService nameService = new NameService();
        AgeService ageService = new AgeService();

        String[] strings = new String[1];
        Integer[] integers = new Integer[1];
        final String name;

        Flowable stringFlowable = Flowable.create(new FlowableOnSubscribe() {
            @Override
            public void subscribe(@NonNull FlowableEmitter emitter) throws Exception {
                System.out.println(Thread.currentThread().getName());
                strings[0] = nameService.getName();
                if(null!=strings[0]){
                    emitter.onComplete();
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.trampoline());

        Flowable integerFlowable = Flowable.create((FlowableOnSubscribe) emitter -> {
            System.out.println(Thread.currentThread().getName());
            integers[0] = ageService.getAge();
            if(null!=integers[0]){
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.newThread());

        Flowable<? extends Serializable> merge = Flowable.merge(stringFlowable, integerFlowable);
        merge.blockingSubscribe();
        System.out.println(strings[0]);
        System.out.println(integers[0]);
        System.out.println(System.currentTimeMillis()-startTime);

    }
}

class NameService {

    public String getName() throws InterruptedException {
        Thread.sleep(3000);
        return "舒云翔";
    }
}

class AgeService {

    public Integer getAge() throws InterruptedException {
        Thread.sleep(2000);
        return 18;
    }
}
package com.naren.client;

import com.naren.models.Money;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class MoneyStreamingResponse implements StreamObserver<Money> {

    private final CountDownLatch latch;

    public MoneyStreamingResponse(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(Money money) {
        System.out.println("Amount is: "+money.getAmount());
    }

    @Override
    public void onError(Throwable throwable) {

        System.out.println(throwable.getMessage());
        this.latch.countDown();

    }

    @Override
    public void onCompleted() {
        System.out.println("Server is Done");
        this.latch.countDown();
    }
}

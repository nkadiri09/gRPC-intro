package com.naren.client;

import com.naren.models.TransferRequest;
import com.naren.models.TransferServiceGrpc;
import com.naren.service.TransferStreamingResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferClientTest {

    private TransferServiceGrpc.TransferServiceStub transferServiceStub;

    @BeforeAll
    public void setUp() {
        ManagedChannel localhost = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        this.transferServiceStub = TransferServiceGrpc.newStub(localhost);
    }

    @Test
    public void TransferTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        TransferStreamingResponse response = new TransferStreamingResponse(latch);
        StreamObserver<TransferRequest> requestStreamObserver = this.transferServiceStub.transfer(response);
        for (int i = 0; i < 10; i++) {
            TransferRequest request = TransferRequest.newBuilder()
                    .setFromAccountNumber(ThreadLocalRandom.current().nextInt(1, 11))
                    .setToAccountNumber(ThreadLocalRandom.current().nextInt(1, 10))
                    .setAmount(ThreadLocalRandom.current().nextInt(1, 21)).build();
            requestStreamObserver.onNext(request);
        }
        requestStreamObserver.onCompleted();
        latch.await();
    }

}

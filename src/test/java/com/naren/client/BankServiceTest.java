package com.naren.client;

import com.google.common.util.concurrent.Uninterruptibles;
import com.naren.models.*;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankServiceTest {

    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;
    private BankServiceGrpc.BankServiceStub bankServiceStub;

    @BeforeAll
    public void setUp() {
        ManagedChannel localhost = ManagedChannelBuilder
                .forAddress("localhost", 6565)
                .usePlaintext()
                .build();

        this.bankServiceBlockingStub = BankServiceGrpc.newBlockingStub(localhost);
        this.bankServiceStub = BankServiceGrpc.newStub(localhost);
    }

    @Test
    public void balanceTest() {
        BalanceCheckRequest build = BalanceCheckRequest.newBuilder().setAccountNumber(7).build();
        Balance balance = this.bankServiceBlockingStub
                .withDeadline(Deadline.after(2, TimeUnit.SECONDS)).getBalance(build);
        System.out.println("Balance is: " + balance.getAmount());
    }

    @Test
    public void withdrawTest() {

        WithDrawRequest withDrawRequest = WithDrawRequest.newBuilder().setAccountNumber(8).setAmount(50).build();
        Iterator<Money> withdraw = this.bankServiceBlockingStub.withdraw(withDrawRequest);
        System.out.println(withdraw);
    }

    @Test
    public void withDrawAsyncTest(){
        CountDownLatch latch = new CountDownLatch(1);
        WithDrawRequest withDrawRequest = WithDrawRequest.newBuilder().setAccountNumber(8).setAmount(50).build();
        this.bankServiceStub.withdraw(withDrawRequest, new MoneyStreamingResponse(latch));
        Uninterruptibles.sleepUninterruptibly(6, TimeUnit.SECONDS);
    }

    @Test
    public void cashDepositTest() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        StreamObserver<DepositRequest> depositRequestStreamObserver = this.bankServiceStub.cashDeposit(new BalanceStreamObserver(latch));
        for (int i = 0; i < 10; i++) {
            DepositRequest depositRequest = DepositRequest.newBuilder().setAccountNumber(10).setAmount(5).build();
            depositRequestStreamObserver.onNext(depositRequest);
        }
        depositRequestStreamObserver.onCompleted();
        latch.await();
    }

}

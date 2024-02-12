package com.naren.client;

import com.naren.models.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Iterator;

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
    }

    @Test
    public void balanceTest() {
        BalanceCheckRequest build = BalanceCheckRequest.newBuilder().setAccountNumber(7).build();
        Balance balance = this.bankServiceBlockingStub.getBalance(build);
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
        WithDrawRequest withDrawRequest = WithDrawRequest.newBuilder().setAccountNumber(8).setAmount(50).build();
        this.bankServiceStub.withdraw(withDrawRequest, new MoneyStreamingResponse());

    }

}

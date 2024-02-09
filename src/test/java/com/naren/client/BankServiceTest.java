package com.naren.client;

import com.naren.models.Balance;
import com.naren.models.BalanceCheckRequest;
import com.naren.models.BankServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BankServiceTest {

    private BankServiceGrpc.BankServiceBlockingStub bankServiceBlockingStub;

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

}

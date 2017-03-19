package refactula.micro.futuristic;

import refactula.micro.futuristic.frontend.FrontendServiceF;
import refactula.micro.futuristic.model.BillingDetails;
import refactula.micro.futuristic.model.Credits;
import refactula.micro.futuristic.model.Email;
import refactula.micro.futuristic.model.Token;
import refactula.micro.futuristic.model.Username;
import refactula.micro.futuristic.utils.Future;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CountDownLatch;
import java.util.function.Supplier;

public class LoadTestF {
    private final Random random;
    private final int usersAmount;
    private final int messagesAmount;
    private final FrontendServiceF frontend;
    private final Supplier<Username> usernameSupplier;
    private final Supplier<Email> emailSupplier;
    private final Supplier<BillingDetails> billingDetailsSupplier;

    public LoadTestF(
            Random random,
            int usersAmount,
            int messagesAmount,
            FrontendServiceF frontend,
            Supplier<Username> usernameSupplier,
            Supplier<Email> emailSupplier,
            Supplier<BillingDetails> billingDetailsSupplier) {

        this.random = random;
        this.usersAmount = usersAmount;
        this.messagesAmount = messagesAmount;
        this.frontend = frontend;
        this.usernameSupplier = usernameSupplier;
        this.emailSupplier = emailSupplier;
        this.billingDetailsSupplier = billingDetailsSupplier;
    }

    public void run() {
        ConcurrentMap<Username, Token> tokens = new ConcurrentHashMap<>();
        List<Username> users = new ArrayList<>();
        Future<Void> allTokensReceived = null;
        for (int i = 0; i < usersAmount; i++) {
            Username username = usernameSupplier.get();
            Email email = emailSupplier.get();
            users.add(username);
            Future<Void> tokenReceived = frontend.register(username, email).flatMap(token -> {
                tokens.put(username, token);
                return
                    frontend.setBillingDetails(username, token, billingDetailsSupplier.get()) .flatMap(afterDetails ->
                    frontend.deposit(username, token, Credits.withAmount(messagesAmount)));
            });
            allTokensReceived = (allTokensReceived != null)
                    ? allTokensReceived.map2(tokenReceived, (a, b) -> null)
                    : tokenReceived;
        }
        Future<Void> simulationFinished = allTokensReceived.flatMap(tokensReceived -> {
            Future<Void> allTransfersMade = null;
            for (int i = 0; i < messagesAmount; i++) {
                Username sender = users.get(random.nextInt(users.size()));
                Username receiver = users.get(random.nextInt(users.size()));
                Future<Void> transferMade = frontend.transfer(sender, tokens.get(sender), receiver, Credits.withAmount(1));
                allTransfersMade = (allTransfersMade != null)
                        ? allTransfersMade.map2(transferMade, (a, b) -> null)
                        : transferMade;
            }
            return allTransfersMade;
        });
        CountDownLatch latch = new CountDownLatch(1);
        simulationFinished.map(done -> {
            latch.countDown();
            return null;
        });
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

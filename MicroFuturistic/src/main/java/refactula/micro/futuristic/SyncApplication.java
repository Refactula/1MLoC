package refactula.micro.futuristic;

import refactula.micro.futuristic.account.AccountClient;
import refactula.micro.futuristic.account.AccountServer;
import refactula.micro.futuristic.billing.BillingClient;
import refactula.micro.futuristic.billing.BillingServer;
import refactula.micro.futuristic.billing.BillingService;
import refactula.micro.futuristic.email.EmailClient;
import refactula.micro.futuristic.email.EmailServer;
import refactula.micro.futuristic.email.EmailService;
import refactula.micro.futuristic.frontend.FrontendClient;
import refactula.micro.futuristic.frontend.FrontendServer;
import refactula.micro.futuristic.frontend.FrontendService;
import refactula.micro.futuristic.model.BillingDetails;
import refactula.micro.futuristic.model.CVCode;
import refactula.micro.futuristic.model.CreditCardNumber;
import refactula.micro.futuristic.model.Email;
import refactula.micro.futuristic.model.Username;
import refactula.micro.futuristic.utils.Logger;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.function.Supplier;

public class SyncApplication {

    public static void main(String[] args) {
        Random random = new Random(573042751);
        //Logger logger = System.out::println;
        Logger logger = message -> {};

        int usersAmount = 100;
        int messagesAmount = 1000;
        int minNetworkDelay = 1;
        int maxNetworkDelay = 10;
        int executorThreads = 16;

        AccountServer accountServer = new AccountServer();
        AccountClient accountService = new AccountClient(minNetworkDelay, maxNetworkDelay, logger, accountServer);

        BillingServer billingServer = new BillingServer();
        BillingService billingService = new BillingClient(minNetworkDelay, maxNetworkDelay, logger, billingServer);

        EmailServer emailServer = new EmailServer(logger);
        EmailService emailService = new EmailClient(minNetworkDelay, maxNetworkDelay, logger, emailServer);

        FrontendServer frontendServer = new FrontendServer(accountService, billingService, emailService);
        FrontendService frontendService = new FrontendClient(minNetworkDelay, maxNetworkDelay, logger, frontendServer);
        ExecutorService executorService = Executors.newFixedThreadPool(executorThreads);

        Supplier<Username> usernameSupplier = indexedGenerator(i -> new Username("User #" + i));
        Supplier<Email> emailSupplier = indexedGenerator(i -> new Email("email" + i + "@mail.com"));
        Supplier<BillingDetails> billingDetailsSupplier = indexedGenerator(i -> new BillingDetails(
                new CreditCardNumber(String.valueOf(i)),
                new CVCode(String.valueOf(i))));

        LoadTest loadTest = new LoadTest(
                random,
                usersAmount,
                messagesAmount,
                frontendService,
                executorService,
                usernameSupplier,
                emailSupplier,
                billingDetailsSupplier);

        long startAt = System.currentTimeMillis();
        loadTest.run();
        long finishAt = System.currentTimeMillis();

        System.out.println("Sent " + messagesAmount + " messages in " + (finishAt - startAt) + " ms");
        System.exit(0);
    }

    private static <T> Supplier<T> indexedGenerator(Function<Integer, T> indexFunction) {
        return new Supplier<T>() {
            private int index = 0;

            @Override
            public T get() {
                return indexFunction.apply(index++);
            }
        };
    }
}

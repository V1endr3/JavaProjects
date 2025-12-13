package org.example;

import io.grpc.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class ApplicationServer {
    private static final Logger logger = Logger.getLogger(ApplicationServer.class.getName());

    private Server server;

    public void start() throws Exception {
        int port = 27788;

        ExecutorService executorService = Executors.newCachedThreadPool();
        server = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create())
                .executor(executorService)
                .addService(new AnyService())
                .build()
                .start();
        logger.info("Server started, listening on port: " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("=== Shutdown hook triggered, gRPC server shutting down ===");
            try {
                ApplicationServer.this.stop();
            } catch (InterruptedException e) {
                if (server != null) {
                    server.shutdownNow();
                }
                e.printStackTrace(System.err);
            } finally {
                executorService.shutdown();
            }
            System.err.println("=== server shut down ===");
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws Exception {
        final ApplicationServer server = new ApplicationServer();
        server.start();
        server.blockUntilShutdown();
    }

    public static class AnyService implements BindableService {

        @Override
        public ServerServiceDefinition bindService() {
            throw new UnsupportedOperationException();
        }
    }
}

package reservation.system.startup;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import reservation.system.server.SocketServer;

@ApplicationScoped
public class ServerStartup {

    @Inject
    SocketServer socketServer;

    void onStart(@Observes StartupEvent ev) {
        new Thread(socketServer::start, "socket-server-thread").start();
    }
}

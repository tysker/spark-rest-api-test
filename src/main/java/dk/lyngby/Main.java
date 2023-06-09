package dk.lyngby;

import dk.lyngby.config.ApplicationConfig;
import dk.lyngby.controller.Routes;

public class Main {

    public static void main(String[] args) {
        ApplicationConfig.startServer(args.length > 0 ? Integer.parseInt(args[0]) : 7000);
        Routes.initializeRoutes();

    }
}

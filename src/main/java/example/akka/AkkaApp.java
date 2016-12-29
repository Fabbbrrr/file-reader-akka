package example.akka;

import akka.actor.ActorSystem;


public class AkkaApp {

    public static void main(String[] args) {
        // Create actorSystem
        ActorSystem akkaSystem = ActorSystem.create("akkaSystem");

        System.out.println("Hello Akka! ... hey there my name is " + akkaSystem.name());

    }
}

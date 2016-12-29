package example.akka;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.dispatch.OnSuccess;
import akka.pattern.Patterns;
import akka.util.Timeout;
import example.akka.actors.FileAnalysisActor;
import example.akka.messages.FileAnalysisMessage;
import example.akka.messages.FileProcessedMessage;
import example.akka.utils.MapUtil;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AkkaApp {

    public static void main(String[] args) {
        // Create actorSystem
        ActorSystem akkaSystem = ActorSystem.create("akkaSystem");

        // Create first actor based on the specified class
        Props props = Props.create(FileAnalysisActor.class);
        ActorRef coordinator = akkaSystem.actorOf(props);

        // Create a message including the file path
        FileAnalysisMessage msg = new FileAnalysisMessage("data/log.txt");

        // Send a message to start processing the file. This is a synchronous call using 'ask' with a timeout.
        Timeout timeout = new Timeout(5, TimeUnit.SECONDS);
        Future<Object> future = Patterns.ask(coordinator, msg, timeout);

        // Process the results
        final ExecutionContext ec = akkaSystem.dispatcher();
        future.onSuccess(new OnSuccess<Object>() {
            @Override
            public void onSuccess(Object message) throws Throwable {
                if (message instanceof FileProcessedMessage) {
                    printResults((FileProcessedMessage) message);
                }
            }

            private void printResults(FileProcessedMessage message) {
                Map<String, Long> sortedMap = MapUtil.sortByValue(message.getData());
                System.out.println("================================");
                System.out.println("||\tCount\t||\t\tIP");
                System.out.println("================================");
                for (Map.Entry<String, Long> entry : sortedMap.entrySet()) {
                    System.out.println("||\t" + entry.getValue() + "   \t||\t" + entry.getKey());
                }
            }
        }, ec);

    }

}

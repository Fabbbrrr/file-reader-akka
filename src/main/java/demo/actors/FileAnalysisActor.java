package demo.actors;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.messages.FileAnalysisMessage;
import demo.messages.FileProcessedMessage;
import demo.messages.LineProcessingResult;
import demo.messages.LogLineMessage;
import org.apache.commons.io.FileUtils;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * Akka High level actor
 *
 * This actor will be in charge of creating other actors and send them messages to coordinate the work.
 * It also receives the results and prints them once the processing is finished.
 *
 */
public class FileAnalysisActor extends UntypedActor {

    private Map<String, Long> ipMap = new HashMap<>();
    private long fileLineCount;
    private long processedCount;
    private ActorRef analyticsSender = null;

    @Override
    public void onReceive(Object message) throws Exception {
        /*
            This actor can receive two different messages, FileAnalysisMessage or LineProcessingResult, any
            other type will be discarded using the unhandled method
         */
        if (message instanceof FileAnalysisMessage) {

            List<String> lines = FileUtils.readLines(new File(
                    ((FileAnalysisMessage) message).getFileName()));

            fileLineCount = lines.size();
            processedCount = 0;

            // stores a reference to the original sender to send back the results later on
            analyticsSender = this.getSender();

            for (String line : lines) {
                // creates a new actor per each line of the log file
                Props props = Props.create(LogLineProcessor.class);
                ActorRef lineProcessorActor = this.getContext().actorOf(props);

                // sends a message to the new actor with the line payload
                lineProcessorActor.tell(new LogLineMessage(line), this.getSelf());
            }

        } else if (message instanceof LineProcessingResult) {

            // a result message is received after a LogLineProcessor actor has finished processing a line
            String ip = ((LineProcessingResult) message).getIpAddress();

            // increment ip counter
            Long count = ipMap.getOrDefault(ip, 0L);
            ipMap.put(ip, ++count);

            // if the file has been processed entirely, send a termination message to the main actor
            processedCount++;
            if (fileLineCount == processedCount) {
                // send done message
                analyticsSender.tell(new FileProcessedMessage(ipMap), ActorRef.noSender());
            }

        } else {
            // Ignore message
            this.unhandled(message);
        }
    }
}

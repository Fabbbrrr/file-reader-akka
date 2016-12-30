package demo.actors;

import akka.actor.UntypedActor;
import demo.messages.LineProcessingResult;
import demo.messages.LogLineMessage;

public class LogLineProcessor extends UntypedActor {

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof LogLineMessage) {
            // What data each actor process?
//            System.out.println("Line: " + ((LogLineMessage) message).getData());
            // Uncomment this line to see the thread number and the actor name relationship
//            System.out.println("Thread ["+Thread.currentThread().getId()+"] handling ["+ getSelf().toString()+"]");

            // get the message payload, this will be just one line from the log file
            String messageData = ((LogLineMessage) message).getData();

            int idx = messageData.indexOf('-');
            if (idx != -1) {
                // get the ip address
                String ipAddress = messageData.substring(0, idx).trim();

                // tell the sender that we got a result using a new type of message
                this.getSender().tell(new LineProcessingResult(ipAddress), this.getSelf());
            }
        } else {
            // ignore any other message type
            this.unhandled(message);
        }
    }
}

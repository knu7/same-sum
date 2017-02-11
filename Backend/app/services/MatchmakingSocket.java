package services;

import play.mvc.WebSocket;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.LoggerFactory;
 
public class MatchmakingSocket extends WebSocket<String> {
 
   private static List<Out<String>> outputs = new ArrayList<>();
 
   public WebSocket<String> open() {
      return new MatchmakingSocket();
   }
 
   @Override
   public void onReady(In<String> in, final Out<String> out) {
      outputs.add(out);
      in.onMessage(message -> {
    	  LoggerFactory.getLogger(this.getClass()).debug(message);
         outputs.forEach(outt -> outt.write(message));
      });
 
      in.onClose(() -> {
         outputs.remove(out);
      });
   }
}

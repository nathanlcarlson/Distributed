
package HW1;

import java.util.Random;

public class HighLowProtocol {

  private static final int WAITING_TO_START = -1;
  private static final int Q_START = 0;
  private static final int WAITING_FOR_GUESS = 1;
  private static final int SENTCLUE = 2;
  private static final int DONE = 3;

  private static final int MIN = 0;
  private int MAX = 10;

  private int state = WAITING_TO_START;
  int number;
  Random rnd;
  
  public HighLowProtocol (int _MAX) {
    MAX = _MAX;
    rnd = new Random();
    number = rnd.nextInt(MAX);
  }  
  public boolean is_waiting_to_start() {
    return (state == WAITING_TO_START);
  }
  public void start_game(){
    state = WAITING_FOR_GUESS;
  }
  public void end_game(){
    number = rnd.nextInt(MAX);
    state = WAITING_TO_START;
  }
  public String processInput(String input) {
    input = input.toLowerCase();
    String output = "";
    

    if(state == WAITING_FOR_GUESS){
      int guess = Integer.parseInt(input);
      if(guess > number){
        output = "Lower";
      }
      else if(guess < number){
        output = "Higher";
      }
      else {
        state = WAITING_TO_START;
        number = rnd.nextInt(MAX);
        output = "All done.";
      }
    }
    return output;

  }
}

public class HighLowProtocol {

  private static final int WAITING_TO_START = -1;
  private static final int Q_START = 0;
  private static final int WAITING_FOR_GUESS = 1;
  private static final int SENTCLUE = 2;
  private static final int DONE = 3;

  private static final int MIN = 0;
  private static final int MAX = 100;

  private int state = WAITING_TO_START;
  public String processInput(String input) {
    input = input.toLowerCase();
    String output = "";
    if(state == WAITING_TO_START){
      state = Q_START;
      output = "Would you like to play a high low guessing game? [Y/N]";
    }
    else if(state == Q_START){
      if(input.equals("y")){
        state = WAITING_FOR_GUESS;
        output = String.format("Alright, the number is between %d and %d", MIN, MAX);
      }
      else if(input.equals("n")){
        state = DONE;
        output = "All done.";
      }
      else{
        output = "Enter Y or N please.";
      }
    }
    return output;

  }
}
import java.util.ArrayList;

public final class Crossword implements WordPuzzleInterface {
    
	/*
     * fills out a word puzzle defined by an empty board. 
     * The characters in the empty board can be:
     *    '+': any letter can go here
     *    '-': no letter is allowed to go here
     *     a letter: this letter has to remain in the filled puzzle
     *  @param board is a 2-d array representing the empty board to be filled
     *  @param dictionary is the dictinary to be used for filling out the puzzle
     *  @return a 2-d array representing the filled out puzzle
     */
    char[][]original;
    ArrayList<int[]> order = new ArrayList<>();
    int spots = 0;
    char[] letters = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
    // for(int i = 0; i < letters.length; i++)
    //    break;
    public char[][] fillPuzzle(char[][] board, DictInterface dictionary){
      original = new char[board.length][board.length];
      //looking through the board to find pluses
      int i =0;
      for(int row = 0; row < board.length; row ++){
        for(int col = 0; col < board.length; col ++ ){
          original[row][col] = board[row][col];
          //finding all the pluses
          if(board[row][col] == '+'){
            order.add(i++,new int[]{row,col} );
            spots++;
          }

        }
      }
      return fillPuzzleHelper(board,dictionary,0);
      // for (int row = 0; row < board.length; row++){
      //   for (int col = 0; col < board[row].length; col++){
      //     if (board[row][col] == '+'){
      //       for(char letter : letters){
      //         //replacing the + with the a letter, and checking if it is a prefix or word
      //         board[row][col] = letter;
      //         //if its a valid prefix, fill the rest of the puzzle, else turn back into a +
      //         if (check(board,dictionary,row,col)){
      //           //checking if board is full
      //           if(row == board.length-1 && col == board[row].length -1){
      //             return board;
      //           }
      //           fillPuzzle(board, dictionary);
      //         }
      //       }
      //     }
      //   }
      // }
	}
  private char[][]fillPuzzleHelper(char[][]board, DictInterface dictionary, int orderSpot){
    for (char let : letters){
      board[order.get(orderSpot)[0]][order.get(orderSpot)[1]] = let;
      if (check(board, dictionary, order.get(orderSpot)[0], order.get(orderSpot)[1])){
        if (orderSpot == spots -1 ) return board;
        fillPuzzleHelper(board, dictionary,orderSpot +1);
        if(full(board) && check(board, dictionary, order.get(orderSpot)[0], order.get(orderSpot)[1])){
          return board;
        }         
      }
      else if(let == 'z'){
        board[order.get(orderSpot)[0]][order.get(orderSpot)[1]] = '+';
        if(orderSpot == 0) return null;
        return board;
      } 

    }
    return null;
  }

  //checking the row and column to make sure its valid
  private boolean check(char[][] board, DictInterface dictionary, int row, int col){
    StringBuilder rowString = new StringBuilder();
    StringBuilder colString = new StringBuilder();
    //notes if the boxes to side are pre filled int
    int rowAdditional = 0;
    while(row + rowAdditional < board.length - 1 && board[row + rowAdditional + 1][col] != '+' && board[row + rowAdditional + 1][col] != '-')
    rowAdditional++;
    int i = 0;
    //using i so we can perserve the original row
    while(row - i  + rowAdditional>= 0 && board[row +rowAdditional - i][col] != '-'){
      rowString.append(board[row  + rowAdditional- i++][col]);
    }
    i = 0;
    int colAdditional = 0;
    while(col + colAdditional < board.length - 1 && board[row][col + colAdditional + 1] != '+' && board[row][col + colAdditional + 1] != '-')
    colAdditional++;
    //using i so we can perserve the original col
    while(col - i + colAdditional>= 0 && board[row][col + colAdditional - i] != '-'){
      colString.append(board[row][col  + colAdditional- i++]);
    }
    rowString.reverse();
    colString.reverse();
    //if it needs to be a word
    //checking if the letter is by itself
   
     if(!(((row - 1 >= 0 && board[row-1][col] == '-') || row-1 < 0 ) && (((row + 1 <= board.length - 1 && board[row+1][col] == '-')) || row+1 >= board.length ))){
      int rowStringPrefix = dictionary.searchPrefix(rowString);
    if ((row + rowAdditional == board.length -1 || board[row +rowAdditional+ 1][col] == '-')){
      //checking if it is not a word
      if (rowStringPrefix <= 1) return false;
    }
    else{
      if (rowStringPrefix == 0) return false;
    }
    }

    if(!(((col - 1 >= 0 && board[row][col-1] == '-') || col-1 < 0 ) && (((col + 1 <= board.length - 1 && board[row][col+1] == '-')) || col+1 >= board.length ))){
      int colStringPrefix = dictionary.searchPrefix(colString);
    if ((col + colAdditional == board.length -1 || board[row][col + colAdditional+ 1] == '-')){
      //checking if it is not a word
      if (colStringPrefix <= 1) return false;
    }
    else{
      if (colStringPrefix == 0) return false;
    }}
    return true;
  }

    /*
     * checks if filledBoard is a correct fill for emptyBoard
     * @param emptyBoard is a 2-d array representing an empty board
     * @param filledBoard is a 2-d array representing a filled out board
     * @param dictionary is the dictinary to be used for checking the puzzle
     * @return true if rules defined in fillPuzzle has been followed and 
     *  that every row and column is a valid word in the dictionary. If a row
     *  a column has one or more '-' in it, then each segment separated by 
     * the '-' should be a valid word in the dictionary 
     */
    public boolean checkPuzzle(char[][] emptyBoard, char[][] filledBoard, DictInterface dictionary){
      StringBuilder string = new StringBuilder();
      //checking for every row to ensure it is a valid string
      for(char[] row : filledBoard){
        string = new StringBuilder();
        int i = 0;
        for (char let : row){
          //i is used to ensure we dont check the same thing twice
          if(let == '+')
           return false;
          else if(let == '-' && i < filledBoard.length - 1){
            if (dictionary.searchPrefix(string) <= 1 && string.length() > 1){
              return false;
            }
            string = new StringBuilder();
          }
          else{
            string.append(let);
          }
          i++;
        }
        //checking after checking all the letter
        if (dictionary.searchPrefix(string) <= 1 && string.length() > 1){
           return false;
        }
      }
      //making sure the columns are valid
      for (int row = 0; row < filledBoard.length; row++){
        string = new StringBuilder();
        for (int i = 0; i < filledBoard[row].length; i++){
        string = new StringBuilder();
          if(filledBoard[row][i] == '+') return false;
          else if(filledBoard[row][i] == '-' && i < filledBoard.length - 1){
            if (dictionary.searchPrefix(string) <= 1 && string.length() > 1){ 
              return false;
            }
            string = new StringBuilder();
          }
          else{
            string.append(filledBoard[row][i]);
          }

        }
        //checking after checking all the letter
        if (dictionary.searchPrefix(string) <= 1 && string.length() > 1) return false;
      }
		return true;
	}

  private boolean full(char[][] board){
    for(char[] row : board){
      for(char col : row){
        if(col == '+') return false;
      }
    }
    return true;

  }

}

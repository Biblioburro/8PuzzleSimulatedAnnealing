package sample;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;


public class Controller {
    //array to store a representation of the puzzle for searching.
private static int[][] puzzle = {{1,2,3},{8,0,4},{7,6,5}};
//reference array for checking the solution;
private static final int[][] solution = {{1,2,3},{8,0,4},{7,6,5}};
//coordinates for the open slot
public static int[] opencoords = {1,1};
//variables for the simulated annealing algorith,
public float temperature = 20.0f;
public float coolingRate = .1f;

//computational budget
public int iterations = 9999;



//method to implement simulated annealing
@FXML
    private void simulatedAnnealing(ActionEvent event){


    //stores the potential states of the next move
    ArrayList<int[][]> neighbors;
    //store the selected neighbor
    int[][] selectedNeighor;

    //for loop to run the algorithm for a certain number of iterations
    for(int i = 0 ; i<iterations;i++){

        //generate valid neighbors
        neighbors = neighbors(puzzle);
        //randomly select a generated neighbor
        selectedNeighor = neighbors.get((int) (Math.random()*(neighbors.size()-1)));

        if(acceptanceProbability(tilesOutPlace(selectedNeighor),tilesOutPlace(puzzle))>Math.random()){
            move(selectedNeighor);
        }

        //update temperature based on a cooling schedule
        temperature -= .01f;
        iterationLabel.setText(i+"");

        update(event);
        if(correct()){
            return;
        }
    }



}

public static void move(int[][] destState){
    for(int i =0 ; i< destState.length;i++){
        for(int j = 0 ; j<destState[0].length;j++){
            puzzle[i][j] = destState[i][j];
        if(destState[i][j] == 0){
            opencoords[0] = i;
            opencoords[1] = j;
        }
        }
    }


}



//returns an acceptance probability for a given energy
public double acceptanceProbability(int Es ,int E){
    double probability = 0.0f;
    int difference = Es-E;
return Math.exp(-difference/temperature);
}

//return an arraylist containing valid neghbor states given the puzzle
public ArrayList<int[][]> neighbors(int[][] currentState){
    ArrayList<int[][]> neighbors = new ArrayList<>();

    //Inefficient but works and gets around adding references to objects
    int[][] upState = new int[currentState.length][currentState[0].length];
    int[][] downState = new int[currentState.length][currentState[0].length];
    int[][] leftState = new int[currentState.length][currentState[0].length];
    int[][] rightState = new int[currentState.length][currentState[0].length];
    //copy the values of current state to each directional array to remove dealing with java references
    for(int i = 0; i< currentState.length;i++ ){
        for(int j =0 ; j< currentState[i].length;j++){
        upState[i][j] = currentState[i][j];
        }
    }
    for(int i = 0; i< currentState.length;i++ ){
        for(int j =0 ; j< currentState[i].length;j++){
            downState[i][j] = currentState[i][j];
        }
    }
    for(int i = 0; i< currentState.length;i++ ){
        for(int j =0 ; j< currentState[i].length;j++){
            leftState[i][j] = currentState[i][j];
        }
    }
    for(int i = 0; i< currentState.length;i++ ){
        for(int j =0 ; j< currentState[i].length;j++){
            rightState[i][j] = currentState[i][j];
        }
    }
    //check if up is a valid direction
    if(isvalidDirection(opencoords[0]-1,opencoords[1])){
    int temp = upState[opencoords[0]-1][opencoords[1]];
    upState[opencoords[0]][opencoords[1]] = temp;
    upState[opencoords[0]-1][opencoords[1]]=0;
    neighbors.add(upState);
    }
    //check if down is a valid direction
    if(isvalidDirection(opencoords[0]+1,opencoords[1])){
        int temp = downState[opencoords[0]+1][opencoords[1]];
        downState[opencoords[0]][opencoords[1]] = temp;
        downState[opencoords[0]+1][opencoords[1]]=0;
        neighbors.add(downState);
    }

    //check if left is a valid direction
    if(isvalidDirection(opencoords[0],opencoords[1]-1)){
        int temp = leftState[opencoords[0]][opencoords[1]-1];
        leftState[opencoords[0]][opencoords[1]] = temp;
        leftState[opencoords[0]][opencoords[1]-1]=0;
        neighbors.add(leftState);
    }
    //check if right is a valid direction
    if(isvalidDirection(opencoords[0],opencoords[1]+1)){
        int temp = rightState[opencoords[0]][opencoords[1]+1];
        rightState[opencoords[0]][opencoords[1]] = temp;
        rightState[opencoords[0]][opencoords[1]+1]=0;
        neighbors.add(rightState);
    }










    /*
    //store the growing list of potential neighbors
  ArrayList<int[][]> neighbors = new ArrayList<>();
// did dumb copy to get it to stop altering the value of puzzle as a reference value
    //investigate clone function
  int[][] tempState = new int [currentState.length][currentState[0].length];
    for(int i = 0 ;i<currentState.length;i++){
        for(int j =0 ; i< currentState[0].length;i++){
            tempState[i][j] = currentState[i][j];
        }
    }
  //sequence of if statements for
  //check if up is a valid direction
   if(isvalidDirection(opencoords[0]-1,opencoords[1])){
       //perform the swap on the temporary state
       int temp = tempState[opencoords[0]-1][opencoords[1]];
       tempState[opencoords[0]][opencoords[1]] =temp;
       tempState[opencoords[0]-1][opencoords[1]] = 0;
       neighbors.add(tempState);
   }
    for(int i = 0 ;i<currentState.length;i++){
        for(int j =0 ; i< currentState[0].length;i++){
            tempState[i][j] = currentState[i][j];
        }
    }
   //check if below is a valid direction
    if(isvalidDirection(opencoords[0]+1,opencoords[1])){
        //perform the swap on the temporary state
        int temp = tempState[opencoords[0]+1][opencoords[1]];
        tempState[opencoords[0]][opencoords[1]] =temp;
        tempState[opencoords[0]+1][opencoords[1]] = 0;
        //add the current state to the list of neighbors
        neighbors.add(tempState);
    }
    for(int i = 0 ;i<currentState.length;i++){
        for(int j =0 ; i< currentState[0].length;i++){
            tempState[i][j] = currentState[i][j];
        }
    }
    //check if left is a valid direction
    if(isvalidDirection(opencoords[0],opencoords[1]-1)){
        //perfor the swap on the temporary state
        int temp = tempState[opencoords[0]][opencoords[1]-1];
        tempState[opencoords[0]][opencoords[1]] =temp;
        tempState[opencoords[0]][opencoords[1]-1] = 0;
        neighbors.add(tempState);
    }

    for(int i = 0 ;i<currentState.length;i++){
        for(int j =0 ; i< currentState[0].length;i++){
            tempState[i][j] = currentState[i][j];
        }
    }
    //check if right is a valid direction
    if(isvalidDirection(opencoords[0],opencoords[1]+1)){
        //perform the swap on the temporary state
        int temp = tempState[opencoords[0]][opencoords[1]+1];
        tempState[opencoords[0]][opencoords[1]] =temp;
        tempState[opencoords[0]][opencoords[1]+1] = 0;
        neighbors.add(tempState);
    }


*/
return neighbors;

}
//helper function for the neighbor method for determing valid states to add;
public static boolean isvalidDirection(int x,int y){

        if((x<0 || x>solution.length-1) || (y<0 || y>solution.length-1 )){
            return false;
        }else{
            return true;
        }

}

public float energy(int[][] s){

return 0;
}

//goal function to determine the number of misplaced tiles in the puzzle.
public static int tilesOutPlace(int [][] state){
    int numMisplaced = 0;
    for(int i =0 ; i < solution.length;i++){
        for(int j =0 ; j<solution[i].length;j++){
            if(solution[i][j] != state[i][j]){
                numMisplaced++;
            }
        }
    }
    return numMisplaced;
}


//function to determine if the state of the puzzle is correct
public boolean correct() {
    for (int i = 0; i < solution.length; i++) {
        for (int j = 0; j < solution[i].length; j++) {
            if (solution[i][j] != puzzle[i][j]) {
                return false;
            }
        }
    }
    return true;
}

    //function to move left
    @FXML
    private void left(ActionEvent event){
        //boundary check
        if(opencoords[1]>0){
            //swap the open position left
            int temp = puzzle[opencoords[0]][opencoords[1]-1];
            puzzle[opencoords[0]][opencoords[1]] =temp;
            puzzle[opencoords[0]][opencoords[1]-1] = 0;
            //update openspots coordinates
            opencoords[1]--;
            //update the screen
            update(event);
        }
    }
    //function to slide left
    @FXML
    private void right(ActionEvent event){
        //boundary check
        if(opencoords[1]<2){
            //swap the open position right
            int temp = puzzle[opencoords[0]][opencoords[1]+1];
            puzzle[opencoords[0]][opencoords[1]] =temp;
            puzzle[opencoords[0]][opencoords[1]+1] = 0;
            //update openspots coordinates
            opencoords[1]++;
            //update the screen
            update(event);
        }
    }
    //function to slide right
    @FXML
    private void up(ActionEvent event){
        //boundary check
        if(opencoords[0]>0){
            //swap the open position right
            int temp = puzzle[opencoords[0]-1][opencoords[1]];
            puzzle[opencoords[0]][opencoords[1]] =temp;
            puzzle[opencoords[0]-1][opencoords[1]] = 0;
            //update open spots coordinates
            opencoords[0]--;
            //update the screen
            update(event);
        }
    }
    //function to slide down
    @FXML
    private void down(ActionEvent event){
        //boundary check
        if(opencoords[0]<2){
            //swap the open position right
            int temp = puzzle[opencoords[0]+1][opencoords[1]];
            puzzle[opencoords[0]][opencoords[1]] =temp;
            puzzle[opencoords[0]+1][opencoords[1]] = 0;
            //update openspots coordinates
            opencoords[0]++;
            //update the screen
            update(event);
        }
    }


    //function to randomize the puzzle
    @FXML
    private void randomize(ActionEvent event){
        //ArrayList to store the remaining available numbers
        ArrayList<Integer> arr = new ArrayList<Integer>(9);
        //Random number generator to generate random numbers
        Random rand = new Random();
        //loop to populate the array of available numbers
        for(int i =0 ; i < puzzle.length*puzzle[0].length;i++) {
            arr.add(i);
        }
        //nested loop to populate the array with random values
        for(int j = 0 ; j<puzzle.length;j++){
            for(int k =0;k<puzzle[0].length;k++ ){
                //case of empty arraylist
                if(arr.size() == 0){
                    break;
                }
                //generate and assign a random value from the remaining values.
                int num = arr.get(rand.nextInt(arr.size()));
                puzzle[j][k]  = num;
                //update the open coordinates;
                if(num == 0 ){
                    opencoords[0] = j;
                    opencoords[1] = k ;

                }
                //remove the used value from the list of available
                arr.remove(arr.indexOf(num));
            }
        }


        update(event);
    }
    //function to update the visual representation of the graph
    @FXML
    private void update(ActionEvent event){
        //row one
        slot00.setText(puzzle[0][0]+"");
        slot01.setText(puzzle[0][1]+"");
        slot02.setText(puzzle[0][2]+"");
        //row two
        slot10.setText(puzzle[1][0]+"");
        slot11.setText(puzzle[1][1]+"");
        slot12.setText(puzzle[1][2]+"");
        //row Three
        slot20.setText(puzzle[2][0]+"");
        slot21.setText(puzzle[2][1]+"");
        slot22.setText(puzzle[2][2]+"");

        tilesMisplaced.setText(tilesOutPlace(puzzle)+"");
    }

    //Creating the labels for all the slots
    @FXML
    private Label slot00;
    @FXML
    private Label slot01;
    @FXML
    private Label slot02;
    //second row
    @FXML
    private Label slot10;
    @FXML
    private Label slot11;
    @FXML
    private Label slot12;
    //third row
    @FXML
    private Label slot20;
    @FXML
    private Label slot21;
    @FXML
    private Label slot22;

    //Creating label for the tiles misplaced indicator
    @FXML
    private Label tilesMisplaced;
    //Label for iteration count
    @FXML
    private Label iterationLabel;
}

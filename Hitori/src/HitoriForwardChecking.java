import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class HitoriForwardChecking {
	
	public static void main(String[] args) throws InterruptedException{
		
		System.out.println("Enter size: ");
		Scanner s = new Scanner(System.in);

		int size = s.nextInt();
			
		hFC h = new hFC(size);
		
		h.initialize(size);
		h.initializeGUI(size);
		
		Timer time = new Timer();
		time.start();
		if(!h.solve(size, h.createDomain(size))){
			System.out.println("Unsuccessful");
		}
		s.close();
		time.stop();
		System.out.println();
		h.displayResult(size);
		
		time.show();
	}
}

class hFC {
	private HitoriPuzzle[][] board;
	public BoardGUI cb;
	
	public hFC(int size) {
		board = new HitoriPuzzle[size][size];
	}
	
	public void initialize(int size) {
		
		//size 3
		//int[] puzzle = {1,1,2,1,3,2,2,3,1};
		int[] puzzle = {1,1,1,1,3,1,2,3,1}; //unsolvable
		
		//size 4
		//int[] puzzle = {2,2,2,4,1,4,2,3,2,3,2,1,3,4,1,2};
		
		//size 5
		//int[] puzzle = {4,2,2,4,6,4,1,2,5,6,3,3,1,6,4,2,4,5,2,3,4,5,2,3,2};
		//int[] puzzle = {2,2,1,5,3,2,3,1,4,5,1,1,1,3,5,1,3,5,4,2,5,4,3,2,1};
		//int[] puzzle = {2,1,3,3,3,1,4,4,2,3,4,3,3,5,5,5,4,3,1,2,3,3,1,3,5};
		
		//size 6
		//int[] puzzle = {6,5,6,1,6,3,5,3,6,5,2,1,3,1,5,4,2,6,2,1,4,1,6,6,1,4,3,2,5,4,2,4,1,6,2,2};
		
		//size 8
		//int[] puzzle = {7,7,7,4,5,2,2,8,3,8,6,6,1,7,5,2,1,3,3,2,7,6,7,5,8,8,2,4,4,1,3,5,7,4,5,8,4,3,1,6,4,2,8,4,6,7,7,7,4,6,2,1,8,4,2,3,5,1,7,6,3,4,4,3};


		int counter=0;
		for(int i=0; i<size; i++){
			for(int j=0; j<size; j++){
				HitoriPuzzle hp = new HitoriPuzzle(i, j, puzzle[counter], false);
				System.out.print(puzzle[counter] + " ");
				board[i][j]=hp;
				counter++;
			}
			System.out.println();
		}
	}
	
	public ArrayList<HitoriPuzzle> createDomain(int size){
		uncheckedAll(size);
		setChecked(size);
		ArrayList<HitoriPuzzle> domain = new ArrayList<HitoriPuzzle>();
		for(int row=0;row<size;row++){
			for(int col=0;col<size;col++){
				if(repeated(row, col, board[row][col].getValue(), size) && !board[row][col].isShaded() && !board[row][col].isChecked() && !isIsland(row, col, size)){
					HitoriPuzzle hp = board[row][col];
					domain.add(hp);
				}
			}
		}
		return domain;
	}
	
	private boolean repeated(int row, int col, int value, int size) {
		if(repeatedInWholeRow(row, col, size, value) || repeatedInWholeCol(row, col, size, value)){
			return true;
		}
		board[row][col].setChecked(true);
		return false;
	}
	
	private boolean repeatedInWholeRow(int row, int col, int size, int number){
		for(int i=0;i<col;i++){
			if(board[row][i].getValue()==number && !board[row][i].isShaded()){
				return true;
			}
		}
		for(int i=col+1;i<size;i++){
			if(board[row][i].getValue()==number && !board[row][i].isShaded()){
				return true;
			}
		}
		return false;
	}
	
	private boolean repeatedInWholeCol(int row, int col, int size, int number){
		for(int i=0;i<row;i++){
			if(board[i][col].getValue()==number && !board[i][col].isShaded()){
				return true;
			}
		}
		
		for(int i=row+1;i<size;i++){
			if(board[i][col].getValue()==number && !board[i][col].isShaded()){
				return true;
			}
		}
		return false;
	}
	
	public boolean solve(int size, ArrayList<HitoriPuzzle> domain) throws InterruptedException{
			for(HitoriPuzzle d:domain){
				
				
				unvisitAll(size);
				d.setShaded(true);
				d.setChecked(true);
				setChecked(size);
				
				Thread.sleep(150);
				cb.updateBoardWithNumbers(board);
				
				solve(size, createDomain(size));

				if(checkResult(size)){				
					return true;
				}
				
				d.setShaded(false);
			}
			return false;
	}
	
	private void setChecked(int size) {
		for(int row=0;row<size;row++){
			for(int col=0;col<size;col++){
				if(board[row][col].isShaded()){
					if(row>0){
						board[row-1][col].setChecked(true);
					}
					if(col>0){
						board[row][col-1].setChecked(true);
					}
					if(row<size-1){
						board[row+1][col].setChecked(true);
					}
					if(col<size-1){
						board[row][col+1].setChecked(true);
					}
				}
			}
		}		
	}
	
	private void uncheckedAll(int size) {
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				board[i][j].setChecked(false);
			}
		}
	}
	
	private boolean checkResult(int size) {
		int temp;
		HitoriPuzzle[][] result = new HitoriPuzzle[size][size];
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(!board[i][j].isShaded()){
					result[i][j]=board[i][j];
				}
			}
		}
		
		for(int i=0;i<size;i++){ //row
			for(int j=0;j<size;j++){ //col
				
				if(result[i][j]!=null){
					temp=result[i][j].getValue();
					
					for(int k=0;k<j;k++){
						if(result[i][k]!=null){
							if(result[i][k].getValue()==temp){
								return false;
							}
						}
					}
					for(int l=j+1;l<size;l++){
						if(result[i][l]!=null){
							if(result[i][l].getValue()==temp){
								return false;
							}
						}
					}
					for(int m=0;m<i;m++){
						if(result[m][j]!=null){
							if(result[m][j].getValue()==temp){
							return false;
							}
						}
						
					}
					
					for(int n=i+1;n<size;n++){
						if(result[n][j]!=null){
							if(result[n][j].getValue()==temp){
								return false;
							}
						}
					}
				}
			}
		}
		return true;
	}

	private void unvisitAll(int size) {
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				board[i][j].setVisited(false);
				board[i][j].setChecked(false);
			}
		}
	}

	private boolean isIsland(int row, int col, int size) {
		HitoriPuzzle upr = null;
		HitoriPuzzle dr = null;
		HitoriPuzzle upl = null;
		HitoriPuzzle dl = null;
		
		if(row>0 && col<size-1){
			upr = board[row-1][col+1];  //element up-right from the element I'm checking
		}
		if(row<size-1 && col<size-1){
			dr = board[row+1][col+1]; //down-right
		}
		if(row>0 && col>0){
			upl = board[row-1][col-1]; //up-left
		}
		if(row<size-1 && col>0){
			dl = board[row+1][col-1]; //down-left
		}
		
		HitoriPuzzle neighbors[] = {upr, dr, upl, dl};
		// look in each neighbor direction
		for (int i = 0; i < neighbors.length; i++) {
			// skip if can't go that way or already visited or open square
			if (neighbors[i]==null || neighbors[i].isVisited()
					|| !neighbors[i].isShaded()) {
				continue;
			}

			// if the neighbor is an edge, we've traced the path! we have island
			if (isEdgeSquare(neighbors[i], size)) {
				return true;
			}

			// mark square visited and keep walking from neighbor onwards
			neighbors[i].setVisited(true);
			board[neighbors[i].getX()][neighbors[i].getY()].setChecked(true);
			
			if (isIsland(neighbors[i].getX(), neighbors[i].getY(), size)){
				return true;
			}
		}
		return false;
	}

	private boolean isEdgeSquare(HitoriPuzzle h, int size) {
		if(h.getX()==0 || h.getX()==(size-1) || h.getY()==size-1 || h.getY()==0) {
			return true;
		}
		return false;
	}
	
	private boolean neighboursNotShaded(int row, int col, int size) {
		if(row>0){
			if(board[row-1][col].isShaded()){
				return false;
			}
		}
		if(col>0){
			if(board[row][col-1].isShaded()){
				return false;
			}
		}
		if(row<size-1){
			if(board[row+1][col].isShaded()){
				return false;
			}
		}
		if(col<size-1){
			if(board[row][col+1].isShaded()){
				return false;
			}
		}
	return true;
	}
	
	public void displayResult(int size) {
		for(int row=0;row<size;row++){
			for(int col=0;col<size;col++){
				System.out.print(board[row][col].getValue());
				if(board[row][col].isShaded()){
					System.out.print("(s)");
				}
				else{
					System.out.print("(u)");
				}
				System.out.print(" ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void initializeGUI(int size) {
		Runnable r = new Runnable() {
	        @Override
	        public void run() {
            	BoardGUI.n=size;
            	cb = new BoardGUI(size);
                JFrame f = new JFrame("Hitori");
                f.setVisible(false);
                f.getContentPane().add(cb.getGui());
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                f.setLocationByPlatform(true);
                f.pack();
                f.setMinimumSize(f.getSize());
                f.setVisible(true);
	        }
		};
	SwingUtilities.invokeLater(r);	
	}
}

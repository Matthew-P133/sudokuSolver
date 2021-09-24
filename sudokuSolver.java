import java.util.Scanner;

public class sudokuSolver {
	public static void main(String[] args) {
		
		// layout is sudoku[row][column]
		// comment + uncomment to try a different sudoku case
		
		
		// (very) easy sudoku
//		int[][] sudoku = new int[][] {
//			{0, 7, 6, 5, 4, 3, 1, 9, 2},
//			{5, 4, 3, 2, 1, 9, 7, 6, 8},
//			{2, 1, 9, 8, 7, 6, 4, 3, 5},
//			{1, 9, 8, 7, 6, 5, 3, 2, 4},
//			{4, 3, 2, 1, 9, 8, 6, 5, 7},
//			{7, 6, 5, 4, 3, 2, 9, 8, 1},
//			{3, 2, 1, 9, 8, 7, 5, 4, 6},
//			{6, 5, 4, 3, 2, 1, 8, 7, 9},
//			{9, 8, 7, 6, 5, 4, 2, 1, 3}
//		};
		
		// empty sudoku
//		int[][] sudoku = new int[][] {
//			{0, 0, 0, 0, 0, 0, 0, 0, 0},
//			{0, 0, 0, 0, 0, 0, 0, 0, 0},
//			{0, 0, 0, 0, 0, 0, 0, 0, 0},
//			{0, 0, 0, 0, 0, 0, 0, 0, 0},
//			{0, 0, 0, 0, 0, 0, 0, 0, 0},
//			{0, 0, 0, 0, 0, 0, 0, 0, 0},
//			{0, 0, 0, 0, 0, 0, 0, 0, 0},
//			{0, 0, 0, 0, 0, 0, 0, 0, 0},
//			{0, 0, 0, 0, 0, 0, 0, 0, 0}
//		};
//		
		
		// 'fiendish' sudoku
		int[][] sudoku = new int[][] {
			{0, 0, 5, 4, 0, 0, 0, 0, 0},
			{0, 2, 0, 5, 0, 3, 0, 6, 0},
			{0, 0, 0, 9, 0, 0, 0, 0, 7},
			{0, 6, 0, 0, 0, 0, 8, 5, 9},
			{0, 0, 0, 0, 8, 0, 0, 0, 0},
			{3, 4, 8, 0, 0, 0, 0, 1, 0},
			{4, 0, 0, 0, 0, 9, 0, 0, 0},
			{0, 5, 0, 7, 0, 1, 0, 4, 0},
			{0, 0, 0, 0, 0, 2, 9, 0, 0}
		};
		
		printGrid(sudoku);
		Scanner keyboard = new Scanner(System.in);
		System.out.println("\nInstructions:\n\nAdd a number to the grid by entering its row, column and value. For example, "
							+ "\"1 2 3\" adds a 3 to row 1, column 2.\nIf you make a mistake you can just change the number "
							+ "back (enter 0 for blank).\nCheat code for instant solution is \"10 1 1\".");
		
		// allow user to fill sudoku
		while (!full(sudoku)) {
			int row;
			int column;
			int number;
			
			// get user to enter a number
			do {
				System.out.println("\nEnter next instruction:");
				row = keyboard.nextInt();
				column = keyboard.nextInt();
				number = keyboard.nextInt();
				if (row == 10) break;
			} while (!(row > 0 && row < 10 && column > 0 && column < 10 && number > 0 && number < 10));
			
			
			if (row == 10) { // user has entered cheat code
				long start = System.currentTimeMillis();
				solve(sudoku);
				long end = System.currentTimeMillis();
				long duration = end - start;
				System.out.println("\nThat took me: " + duration + " ms");
				System.exit(0);
			}
			else { // add user input to grid
				sudoku[row-1][column-1] = number;
				printGrid(sudoku);
			}
		}
		
		// when user has filled sudoku
		System.out.println("\nGrid full. Checking...");
		if (validSolution(sudoku)) {
			System.out.println("Valid!");
		}
		else {
			System.out.println("Invalid");
		}	
	}
	
	
	public static boolean validSolution(int[][] sudoku) {
		
		// check each element is only present once in its box/row/column
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				int k = sudoku[i][j];
				if (!(validPartial(sudoku, i, j, k))) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	public static boolean solve(int[][] sudoku) {
		
		// recursively solve sudoku
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (sudoku[i][j] == 0) {
					for (int k = 1; k <= 9; k++) {
						sudoku[i][j] = k;
						if (validPartial(sudoku, i, j, k)) {
							if (solve(sudoku)) {
								return true;
							}
							else {
								sudoku[i][j] = 0;
							}
						}
						else {
							sudoku[i][j] = 0;
						}
					}
					return false;
				}
			}
		}
		printGrid(sudoku);
		return true;
	}

	
	public static boolean validPartial(int[][] sudoku, int i, int j, int k) {
		
		// check that number not already present in row, ...
		int[][] row = new int[][] {
			{1, 2, 3, 4, 5, 6, 7, 8, 9},
			{0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
		
		for (int a = 0; a < 9; a++) {
			int x = sudoku[i][a];
			if (x != 0) {
				row[1][x-1] += 1;
			}	
		}
		
		if (row[1][k-1] == 2) {
			return false;
		}
		 
		//... column, ...
		int[][] column = new int[][] {
			{1, 2, 3, 4, 5, 6, 7, 8, 9},
			{0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
		
		for (int a = 0; a < 9; a++) {
			int x = sudoku[a][j];
			if (x != 0) {
				column[1][x-1] += 1;
			}
		}
		if (column[1][k-1] == 2) {
			return false;
		}
		
		// ... and box.
		int[][] box = new int[][] {
			{1, 2, 3, 4, 5, 6, 7, 8, 9},
			{0, 0, 0, 0, 0, 0, 0, 0, 0}
		};
		
		// convert position to centre of its containing 3x3 box
		int boxQuotientHoriz = i / 3;
		int boxQuotientVert = j / 3;
		int boxCentreRow = boxQuotientHoriz + 1 + boxQuotientHoriz*2;
		int boxCentreCol = boxQuotientVert + 1 + boxQuotientVert*2;
		
		for (int m = boxCentreRow - 1; m <= boxCentreRow + 1; m++) {
			for (int l = boxCentreCol - 1; l <= boxCentreCol + 1; l++) {
				int x = sudoku[m][l];
				if (x != 0) {
				box[1][x-1] += 1;
				}
			}
		}
		if (box[1][k-1] == 2) {
			return false;
		}
		return true;
	}
	
	
	public static boolean full(int[][] sudoku) {
		
		// checks if grid is full
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (sudoku[i][j] == 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	public static void printGrid(int[][] sudoku) {
		
		// loop over rows of sudoku 
		for (int i = 0; i < 9; i++) {
			
			//print horizontal borders
			if (i == 0 || i == 3 || i == 6) {
				for (int j = 0; j < 37; j++) {
					System.out.print('═');
				}
			}
			else {
				for (int j = 0; j < 37; j++) {
					System.out.print('—');
				}
			}
			System.out.println();
			
			// print numbers + vertical borders
			for (int j = 0; j < 9; j++) {
				if ( j == 0 || j == 3 || j == 6) {
					if (sudoku[i][j] == 0) {
						System.out.print("║   ");
					}
					else {
						System.out.print("║ " + sudoku[i][j] + " ");
					}
				}
				else {
					if (sudoku[i][j] == 0) {
						System.out.print("|   ");
					}
					else {
						System.out.print("| " + sudoku[i][j] + " ");
					}
				}
			}
			System.out.print("║\n");
		}
		
		// print final horizontal border
		for (int j = 0; j < 37; j++) {
			System.out.print('═');
		}
	}
}


public class Cell {
	private int xcoord, ycoord, terrainType;
	private Cell nNeighbour, eNeighbour, sNeighbour, wNeighbour;
	private Cell[] neighbours;
	private double[] stateVector;
	private double[] nextStateVector;

	public Cell(int xcoord, int ycoord, double population, double food, int terrainType) {
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		stateVector = new double[6];
		stateVector[0] = population;
		stateVector[1] = food;
		nextStateVector = new double[6];
		neighbours = new Cell[4];
		
		for (int i = 0; i < 4; i++) {
			if (neighbours[i] == null) {
				stateVector[i+2] = Double.parseDouble(Raster.DEFAULT_NODATA);
			} else {
				stateVector[i+2] = neighbours[i].getByIndex(i+2);
			}
		}
		
	}

	public double getByIndex(int i) {
		return stateVector[i];
	}
	
	public double getPopulation() {
		return stateVector[0];
	}

	public double getFood() {
		return stateVector[1];
	}

	public void calculateNextState(double[][] functionMatrix) {
		if ( stateVector.length != functionMatrix.length) {
			throw (new IllegalArgumentException("Matrices do not match. Can not multiply " + functionMatrix[0].length + "x" + functionMatrix.length + " by " + stateVector.length + "x1"));
		}
		for (int i = 0; i < functionMatrix.length; i++) {
			for (int j = 0; j < functionMatrix[0].length; j++) {
				nextStateVector[i] += functionMatrix[i][j] * stateVector[j];
			}
		}

	}

	public void updateState() {
		stateVector = nextStateVector;
		
		// TODO: Thresholding Step
	}
	
	public void threshold() {
		if (this)
	}
	
	public void setNorthNeighbour(Cell neighbour) {
		nNeighbour = neighbour;
		neighbours[0] = neighbour;
	}
	
	public void setEastNeighbour(Cell neighbour) {
		eNeighbour = neighbour;
		neighbours[1] = neighbour;
	}
	
	public void setSouthNeighbour(Cell neighbour) {
		sNeighbour = neighbour;
		neighbours[2] = neighbour;
	}
	
	public void setWestNeighbour(Cell neighbour) {
		wNeighbour = neighbour;
		neighbours[3] = neighbour;
	}
	
	public double[] stateVector() {
		return stateVector;
	}
}


public class Cell {
	private int xcoord, ycoord;
	private double[] nNeighbourState, eNeighbourState, sNeighbourState, wNeighbourState;
	private double[] fullStateVector;
	private double[] stateVector;
	private double[] nextFullStateVector;
	private int stateVectorLength = 18;

	public Cell(int xcoord, int ycoord, double population, double food, double terrainType) {
		this.xcoord = xcoord;
		this.ycoord = ycoord;
		stateVector = new double[stateVectorLength];
		stateVector[0] = population;
		stateVector[1] = food;
		stateVector[2] = terrainType;
		if (terrainType == -1) {
			stateVector[3] = 0;
		} else {
			stateVector[3] = 1;
		}
		nextFullStateVector = new double[stateVectorLength];
		fullStateVector = new double[stateVectorLength * 5];
	}



	public void calculateNextState(double[][] functionMatrix) {
		if ( fullStateVector.length != functionMatrix.length) {
			throw (new IllegalArgumentException("Matrices do not match. Can not multiply " + functionMatrix[0].length + "x" + functionMatrix.length + " by " + stateVector.length + "x1"));
		}
		for (int i = 0; i < functionMatrix.length; i++) {
			for (int j = 0; j < functionMatrix[0].length; j++) {
				nextFullStateVector[i] += functionMatrix[i][j] * fullStateVector[j];
			}
		}

	}

	public void updateState() {
		for (int i = 0; i < stateVectorLength; i++) {
			stateVector[i] = nextFullStateVector[i];
		}

	}

	public void threshold() {
		if (stateVector[0] < 0)
			stateVector[0] = 0;
	}



	public double getByIndex(int i) {
		return stateVector[i];
	}

	public double getPopulation() {
		return stateVector[0];
	}

	public void setNorthNeighbour(Cell neighbour) {
		nNeighbourState = neighbour.stateVector();
	}

	public void setEastNeighbour(Cell neighbour) {
		eNeighbourState = neighbour.stateVector();
	}

	public void setSouthNeighbour(Cell neighbour) {
		sNeighbourState = neighbour.stateVector();
	}

	public void setWestNeighbour(Cell neighbour) {
		wNeighbourState = neighbour.stateVector();
	}

	public void makeFullStateVector() {
		for (int i = 0; i < stateVectorLength; i++) {
			fullStateVector[i] = stateVector[i];
			fullStateVector[i + stateVectorLength] = nNeighbourState[i];
			fullStateVector[i + stateVectorLength*2] = sNeighbourState[i];
			fullStateVector[i + stateVectorLength*3] = eNeighbourState[i];
			fullStateVector[i + stateVectorLength*4] = wNeighbourState[i];
		}
	}

	public double[] stateVector() {
		return stateVector;
	}
}

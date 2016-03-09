import java.io.IOException;

public class Grid {

	private int width, height;
	private Cell[][] cells;
	private double[][] populationRaster, foodRaster, terrainRaster, waterRaster;
	private double[][] functionMatrix;
	private double cellsize, xll, yll;
	private int rows, cols;

	public Grid(String populationGridName, String foodGridName, String terrainGridName, double[][] functionMatrix, double[][] logMatrix) throws IOException {
		RasterReader rt = new RasterReader();
		Raster r = rt.readRaster(populationGridName);
		cellsize = r.getCellsize();
		xll = r.getXll();
		yll = r.getYll();
		rows = r.getRows();
		cols = r.getCols();
		
		populationRaster = rt.readRaster(populationGridName).getData();
		foodRaster = rt.readRaster(foodGridName).getData();
		terrainRaster = rt.readRaster(terrainGridName).getData();
		height = populationRaster.length;
		width = populationRaster[0].length;
		//System.out.println("Height: " + height + "\tWidth: " + width);
		this.functionMatrix = functionMatrix;
		this.initCells();
	}


	private void initCells() {
		this.cells = new Cell[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				//System.out.println("HEight: " + height + "\tWidth: " + width);
				//System.out.println("i: " + i + "\tj: " + j);
				cells[i][j] = new Cell(j, i, populationRaster[i][j], foodRaster[i][j], terrainRaster[i][j]);
			}
		}

		// TODO: Make less ugly
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (i == 0) {
					if (j == 0) {
						cells[i][j].setEastNeighbour(cells[i][j+1]);
						cells[i][j].setSouthNeighbour(cells[i+1][j]);
						
						cells[i][j].setNorthNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
						cells[i][j].setWestNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
					} else if (j == (width-1)) {
						cells[i][j].setWestNeighbour(cells[i][j-1]);
						cells[i][j].setSouthNeighbour(cells[i+1][j]);
						
						cells[i][j].setNorthNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
						cells[i][j].setEastNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
					} else {
						cells[i][j].setEastNeighbour(cells[i][j+1]);
						cells[i][j].setWestNeighbour(cells[i][j-1]);
						cells[i][j].setSouthNeighbour(cells[i+1][j]);
						
						cells[i][j].setNorthNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
					}
				} else if (i == (height-1)) {
					if (j == 0) {
						cells[i][j].setNorthNeighbour(cells[i-1][j]);
						cells[i][j].setEastNeighbour(cells[i][j+1]);
						
						cells[i][j].setSouthNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
						cells[i][j].setWestNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
					} else if (j == (width-1)) {
						cells[i][j].setNorthNeighbour(cells[i-1][j]);
						cells[i][j].setWestNeighbour(cells[i][j-1]);
						
						cells[i][j].setSouthNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
						cells[i][j].setEastNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
					} else {
						cells[i][j].setNorthNeighbour(cells[i-1][j]);
						cells[i][j].setEastNeighbour(cells[i][j+1]);
						cells[i][j].setWestNeighbour(cells[i][j-1]);
						
						cells[i][j].setSouthNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
					}
				} else if (j == 0) {
					cells[i][j].setNorthNeighbour(cells[i-1][j]);
					cells[i][j].setEastNeighbour(cells[i][j+1]);
					cells[i][j].setSouthNeighbour(cells[i+1][j]);
					
					cells[i][j].setWestNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
				} else if (j == (width-1)) {
					cells[i][j].setNorthNeighbour(cells[i-1][j]);
					cells[i][j].setSouthNeighbour(cells[i+1][j]);
					cells[i][j].setWestNeighbour(cells[i][j-1]);
					
					cells[i][j].setEastNeighbour(new Cell(-1, -1, 0.0, 0.0, 0));
				} else {
					cells[i][j].setNorthNeighbour(cells[i-1][j]);
					cells[i][j].setEastNeighbour(cells[i][j+1]);
					cells[i][j].setSouthNeighbour(cells[i+1][j]);
					cells[i][j].setWestNeighbour(cells[i][j-1]);
				}

			}
		}
	}

	public void step() {
		for (Cell[] row : cells) {
			for (Cell c: row) {
				c.makeFullStateVector();
				c.calculateNextState(functionMatrix);
			}
		}
		for (Cell[] row : cells) {
			for (Cell c: row) {
				c.updateState();
				c.threshold();
			}
		}
	}

	public double[][] getPop() {
		return this.populationRaster;
	}
	
	public void printGrid() {
		System.out.println("{ " + cells[19][14].stateVector()[0] + ", " + cells[19][14].stateVector()[1] + "}, ");
	}
	
	public void printData(double[][] raster) {
		if (raster.length == 0 || raster[0].length == 0) {
			System.out.println("Could not print raster, it's length is 0.");
			return;
		}
		for (int i = 0; i < raster.length; i++) {
			System.out.println();
			for (int j = 0; j < raster[0].length; j++) {
				System.out.print((int)(raster[i][j]) + " ");
			}
		}
		System.out.println();
	}

	public Raster makeRaster(double[][] data) {
		Raster r = new Raster(data, this.cellsize, this.xll, this.yll);
		return r;
	}
}

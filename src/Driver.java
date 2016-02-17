import java.io.IOException;

public class Driver {
	
	private static final double[][] functionMatrix = {{1.04, -0.9, 0,0,0,0}, {0.1, 1, 0, 0, 0, 0}, {0, 0, 1, 0, 0, 0}, {0, 0, 0, 1, 0, 0}, {0, 0, 0, 0, 1, 0}, {0, 0, 0, 0, 0, 1}};

	public static void main(String[] args) throws IOException {
		Grid world = new Grid("testPopulation.asc", "testFood.asc", "testTerrain.asc", functionMatrix);
		world.printGrid();
		for (int i = 0; i < 5; i++) {
			world.step();
			world.printGrid();
		}
	}
}

import java.io.IOException;

public class Driver {
	
	private static final double[][] functionMatrix = {{1.08}};
	private static final double[][] logFunctionMatrix = {{1.08}};

	public static void main(String[] args) throws IOException {
		Grid world = new Grid("testPopulation.asc", "testFood.asc", "testTerrain.asc", functionMatrix, logFunctionMatrix);
		world.printGrid();
		for (int i = 0; i < 5; i++) {
			world.step();
			world.printGrid();
		}
		
		RasterWriter rw = new RasterWriter();
		rw.writeRaster("output.asc", world.makeRaster(world.getPop()));
	}
}

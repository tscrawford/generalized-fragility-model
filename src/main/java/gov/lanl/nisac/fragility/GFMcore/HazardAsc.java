package gov.lanl.nisac.fragility.GFMcore;

import com.vividsolutions.jts.geom.Coordinate;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.arcgrid.ArcGridReader;
import org.geotools.geometry.jts.JTS;
import org.opengis.coverage.grid.GridCoverageReader;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import java.io.File;
import java.io.IOException;

public class HazardAsc implements HazardField {

    private GFMEngine broker;
    private final String fileLocation;
    private String fileName;
    private GridCoverage2D grid;

    public HazardAsc(String fileLocation, GFMEngine mediator) {
        this.fileLocation = fileLocation;
        this.broker = mediator;
        setFileName(fileLocation);
        openFile();
    }

    private void setFileName(String fileLocation){
        if (fileLocation.contains("\\")){
            int idx = fileLocation.lastIndexOf("\\");
            String fileName = fileLocation.substring(idx+1);
            System.out.println(fileName);
            this.fileName = fileName;
        }else{
            System.out.println("-->"+fileLocation);
        }
    }


    @Override
    public double getExposure(double[] latLon) {

        Coordinate crd = new Coordinate(latLon[0], latLon[1]);
        CoordinateReferenceSystem crs = grid.getCoordinateReferenceSystem2D();
        DirectPosition p = JTS.toDirectPosition(crd, crs);
        double[] r = grid.evaluate(p, new double[1]);

        return r[0];
    }

    @Override
    public String getFileLocation() {
        return fileLocation;
    }

    @Override
    public String getFileName() {
        return this.fileName;
    }


    @Override
    public GridCoverage2D getField() {
        return this.grid;
    }

    private void openFile() {

        File f = new File(fileLocation);

        try {
            GridCoverageReader reader = new ArcGridReader(f);
            grid = (GridCoverage2D) reader.read(null);

//            Coordinate crd = new Coordinate(-72.65, 41.74);
//            CoordinateReferenceSystem crs = grid.getCoordinateReferenceSystem2D();
//            DirectPosition p = JTS.toDirectPosition(crd, crs);
//            double[] r = grid.evaluate(p, new double[1]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

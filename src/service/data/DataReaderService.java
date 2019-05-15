package service.data;

import model.Arc;
import model.Port;
import model.Speed;
import model.Vessel;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DataReaderService {

    private static List<Port> ports = new ArrayList<>();
    private static List<Vessel> vessels = new ArrayList<>();
    private static List<Speed> speeds = new ArrayList<>();
    private static List<Arc> arcsData = new ArrayList<>();

    public static void initializePorts(int numberOfPorts) {
        for (int i = 1; i <= numberOfPorts; i++) {
            Port port = new Port(i);
            ports.add(port);
        }
    }

    public static void initializeVessels(int numberOfVessels) {
        for (int i = 1; i <= numberOfVessels; i++) {
            Vessel vessel = new Vessel(i);
            vessels.add(vessel);
        }
    }

    public static void initializeSpeeds(List<Double> speedList) {
        int speedId = 1;
        for (Double speedValue : speedList) {
            Speed speed = new Speed(speedId, speedValue);
            speedId++;
            speeds.add(speed);
        }
    }

    public static List<Port> getPorts() {
        return ports;
    }

    public static List<Vessel> getVessels() {
        return vessels;
    }

    public static List<Speed> getSpeeds() {
        return speeds;
    }

    public static List<Arc> getArcs() {
        return arcsData;
    }

    public static void setFixedCosts(double[][] cfixArray) {

    }

    public static void readBigInstances(String base, String extension) throws FileNotFoundException{
        initializePorts(59);
        initializeVessels(4);
        try {
            XSSFWorkbook excelBookBase = new XSSFWorkbook(new FileInputStream(base));
            XSSFSheet speedSheet = excelBookBase.getSheet("SPEED");
            List<Double> speedList = getSpeed(speedSheet);
            initializeSpeeds(speedList);
            XSSFSheet vehicleSheet = excelBookBase.getSheet("VEHICLE");
            setVehicle(vehicleSheet);
            XSSFSheet fuelSheet = excelBookBase.getSheet("CONSF1");
            setFuel(fuelSheet);
            XSSFSheet postTimeSheet = excelBookBase.getSheet("DPOST");
            setPreAndPostTime(postTimeSheet);
            XSSFWorkbook excelBookExt = new XSSFWorkbook(new FileInputStream(extension));
            XSSFSheet arcsSheet = excelBookExt.getSheet("A");
            setArcsWithoutRoutes(arcsSheet);
            XSSFSheet cfixSheet = excelBookExt.getSheet("CFIX");
            setCfix(cfixSheet);
            XSSFSheet cppServSheet = excelBookExt.getSheet("CPP_SERV");
            setPrePostCost(cppServSheet);
            XSSFSheet serviceCostSheet = excelBookExt.getSheet("Cs");
            setServiceCost(serviceCostSheet);
            XSSFSheet minServiceTimeSheet = excelBookExt.getSheet("MinS");
            setMinServiceTime(minServiceTimeSheet);

            //double speedValue = row.getCell(0).getNumericCellValue();
            // System.out.println(speedValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readDataFromExcel(String file) throws FileNotFoundException {
        initializePorts(49);
        initializeVessels(4);
        try {
            XSSFWorkbook excelBook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet speedSheet = excelBook.getSheet("SPEED");
            List<Double> speedList = getSpeed(speedSheet);
            initializeSpeeds(speedList);
            XSSFSheet vehicleSheet = excelBook.getSheet("VEHICLE");
            setVehicle(vehicleSheet);
            XSSFSheet cfixSheet = excelBook.getSheet("CFIX");
            setCfix(cfixSheet);
            XSSFSheet fuelSheet = excelBook.getSheet("CONSF1");
            setFuel(fuelSheet);
            XSSFSheet cppServSheet = excelBook.getSheet("CPP_SERV");
            setPrePostCost(cppServSheet);
            XSSFSheet serviceCostSheet = excelBook.getSheet("Cs");
            setServiceCost(serviceCostSheet);
            XSSFSheet minServiceTimeSheet = excelBook.getSheet("MinS");
            setMinServiceTime(minServiceTimeSheet);
            XSSFSheet arcsSheet = excelBook.getSheet("R");
            setArcs(arcsSheet);
            XSSFSheet postTimeSheet = excelBook.getSheet("DPOST");
            setPreAndPostTime(postTimeSheet);


            //double speedValue = row.getCell(0).getNumericCellValue();
            // System.out.println(speedValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Double> getSpeed(XSSFSheet speedSheet) {
        XSSFRow row = speedSheet.getRow(0);
        List<Double> speed = new ArrayList<>();
        Iterator cellIter = row.cellIterator();
        while (cellIter.hasNext()) {
            XSSFCell cell = (XSSFCell) cellIter.next();
            double speedValue = cell.getNumericCellValue();
            speed.add(speedValue);
        }
        return speed;
    }

    private static void setVehicle(XSSFSheet vehicleSheet) {
        int lastRow = vehicleSheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++) {
            XSSFRow row = vehicleSheet.getRow(i);
            int portId = (int) row.getCell(0).getNumericCellValue();
            int vesselId = (int) row.getCell(1).getNumericCellValue();
            Port port = ports.get(portId - 1);
            Vessel vessel = vessels.get(vesselId - 1);
            port.getIncompatibleVessels().add(vessel);
            vessel.getIncompatiblePorts().add(port);
        }
    }

    private static void setCfix(XSSFSheet cfixSheet) {
        //int lastRow = cfixSheet.getLastRowNum();
        int i = 1;
        for (Port port : ports) {
            Map<Integer, Double> fixedCost = new HashMap<>();
            XSSFRow row = cfixSheet.getRow(i);
            Iterator cellIter = row.cellIterator();
            int cellCount = 1;
            while (cellIter.hasNext()) {
                XSSFCell cell = (XSSFCell) cellIter.next();
                double value = cell.getNumericCellValue();
                fixedCost.put(cellCount, value);
                cellCount++;
            }
            port.setFixedCost(fixedCost);
            i++;
        }
    }

    private static void setFuel(XSSFSheet fuelSheet) {
        int lastRow = fuelSheet.getLastRowNum();
        for (int i = 0; i <= lastRow; i++){
            XSSFRow row = fuelSheet.getRow(i);
            Iterator cellIter = row.cellIterator();
            Map<Integer, Double> fuelCost = new HashMap<>();
            int cellCount = 1;
            while (cellIter.hasNext()) {
                XSSFCell cell = (XSSFCell) cellIter.next();
                double value = cell.getNumericCellValue();
                fuelCost.put(cellCount, value);
                cellCount++;
            }
            vessels.get(i).setFuelCost(fuelCost);
        }

    }

    private static void setPrePostCost(XSSFSheet cppServSheet) {
        int lastRow = cppServSheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++){
            XSSFRow row = cppServSheet.getRow(i);
            Iterator cellIter = row.cellIterator();
            Map<Integer, Double> prePostCost = new HashMap<>();
            int cellCount = 1;
            while (cellIter.hasNext()) {
                XSSFCell cell = (XSSFCell) cellIter.next();
                double value = cell.getNumericCellValue();
                prePostCost.put(cellCount, value);
                cellCount++;
            }
            ports.get(i-1).setPreAndPostCost(prePostCost);
        }
    }

    private static void setServiceCost(XSSFSheet serviceCostSheet) {
        int lastRow = serviceCostSheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++) {
            XSSFRow row = serviceCostSheet.getRow(i);
            Iterator cellIter = row.cellIterator();
            Map<Integer, Double> serviceCost = new HashMap<>();
            int cellCount = 1;
            while (cellIter.hasNext()) {
                XSSFCell cell = (XSSFCell) cellIter.next();
                double value = cell.getNumericCellValue();
                serviceCost.put(cellCount, value);
                cellCount++;
            }
            ports.get(i-1).setInServiceCost(serviceCost);
        }
    }

    private static void setMinServiceTime(XSSFSheet minServiceTimeSheet) {
        int lastRow = minServiceTimeSheet.getLastRowNum();
        for (int i = 1; i <= lastRow; i++) {
            XSSFRow row = minServiceTimeSheet.getRow(i);
            Double value = row.getCell(0).getNumericCellValue();
            ports.get(i-1).setMinimumServiceTime(value);
        }
    }

    private static void setArcsWithoutRoutes(XSSFSheet arcsSheet){
        int lastRow = arcsSheet.getLastRowNum();
        List<Arc> arcs = new ArrayList<>();
        int arcId = 1;
        for (int i = 1; i <= lastRow; i++) {
            XSSFRow row = arcsSheet.getRow(i);
            int firstPortIndex = (int) row.getCell(0).getNumericCellValue();
            int secondPortIndex = (int) row.getCell(1).getNumericCellValue();
            Arc newArc = new Arc();
            newArc.setArcId(arcId);
            newArc.setFirstPort(ports.get(firstPortIndex - 1));
            newArc.setSecondPort(ports.get(secondPortIndex - 1));
            newArc.setDistance(row.getCell(2).getNumericCellValue());
            newArc.getAvailableVessels().add(vessels.get(0));
            newArc.getAvailableVessels().add(vessels.get(1));
            newArc.getAvailableVessels().add(vessels.get(2));
            newArc.getAvailableVessels().add(vessels.get(3));
            arcs.add(newArc);
            arcId++;
        }
        arcsData = arcs;
    }

    private static void setArcs(XSSFSheet arcsSheet) {
        int lastRow = arcsSheet.getLastRowNum();
        List<Arc> arcs = new ArrayList<>();
        int start = 0;
        int end = 0;
        int currentVesselId = 0;
        Arc currentArc = new Arc();
        int arcId = 1;
        for (int i = 1; i <= lastRow; i++) {
            XSSFRow row = arcsSheet.getRow(i);
            int firstPortIndex = (int) row.getCell(0).getNumericCellValue();
            int secondPortIndex = (int) row.getCell(1).getNumericCellValue();
            if (start == firstPortIndex && end == secondPortIndex){
                int vesselId = (int)row.getCell(2).getNumericCellValue();
                if (currentVesselId != vesselId) {
                    currentArc.getAvailableVessels().add(vessels.get(vesselId - 1));
                    currentVesselId = vesselId;
                }
            } else {
                Arc newArc = new Arc();
                newArc.setArcId(arcId);
                newArc.setFirstPort(ports.get(firstPortIndex - 1));
                newArc.setSecondPort(ports.get(secondPortIndex - 1));
                newArc.setDistance(row.getCell(5).getNumericCellValue());
                int vesselId = (int)row.getCell(2).getNumericCellValue();
                newArc.getAvailableVessels().add(vessels.get(vesselId-1));
                currentArc = newArc;
                arcs.add(newArc);
                start = firstPortIndex;
                end = secondPortIndex;
                arcId++;
                currentVesselId = vesselId;
            }
        }
        arcsData = arcs;
        /*for (Arc arc : arcs){
            System.out.println(arc.getArcId() + " " + arc.getFirstPort().getPortId() + " " + arc.getSecondPort().getPortId() + " " + arc.getDistance());
        }*/
    }

    private static void setPreAndPostTime(XSSFSheet postTimeSheet){
        int lastRow = postTimeSheet.getLastRowNum();
        for (int i = 0; i <= lastRow; i++){
            XSSFRow row = postTimeSheet.getRow(i);
            double postTime = row.getCell(0).getNumericCellValue();
            Port nextPort = ports.get(i);
            nextPort.setRequiredPostServiceTime(postTime);
            nextPort.setRequiredPreServiceTime(postTime);
        }
    }
}

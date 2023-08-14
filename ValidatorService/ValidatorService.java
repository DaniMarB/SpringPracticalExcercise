package com.example.PracticalExcercise;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class ValidatorService {

    ArrayList<People> peoplelist = new ArrayList<>();
    ArrayList<InjuryReports> reportsList = new ArrayList<>();
    ArrayList<String> validations = new ArrayList<>();
    ArrayList<String> validationsxlsx = new ArrayList<>();
    List<String> jobs = Arrays.asList("Haematologist", "Phytotherapist", "Building surveyor", "Insurance account manager", "Educational psychologist");
    List<String> reportType = Arrays.asList("Near Miss", "Lost Time", "First Aid");



    public ArrayList<People> printPeopleList(){
        return this.peoplelist;
    }


    public String uploadCSVList(String url){
        String fileToRead = url;
        String delimiter = ",";
        String headers;
        String line;
        int validLines = 0;
        int invalidLines = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(fileToRead))) {
            headers = br.readLine();
            while((line = br.readLine()) != null){
                boolean generalValidation = false;
                //Email validation
                boolean emailValidation = false;
                Pattern pattern = Pattern
                        .compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
                String email = Arrays.asList(line.split(delimiter)).get(5);
                Matcher mather = pattern.matcher(email);

                if (mather.find() == true) {
                    emailValidation = true;
                }

                //Date validation
                boolean dateValidation = false;
                Date date1 = new SimpleDateFormat("yyyy-MM-dd").parse(Arrays.asList(line.split(delimiter)).get(7));
                Date date2 = new SimpleDateFormat("yyyy-MM-dd").parse("1980-01-01");
                if(date1.after(date2)){dateValidation = true;}

                //Job validation
                boolean jobValidation = false;
                for(int i = 0; i < jobs.size();i++){
                    if (Arrays.asList(line.split(delimiter)).get(8).equals(jobs.get(i))){
                        jobValidation = true;
                    }
                }

                if(emailValidation == true && dateValidation == true && jobValidation == true){
                    People nPerson = new People(Arrays.asList(line.split(delimiter)).get(0),
                            Arrays.asList(line.split(delimiter)).get(1),
                            Arrays.asList(line.split(delimiter)).get(2),
                            Arrays.asList(line.split(delimiter)).get(3),
                            Arrays.asList(line.split(delimiter)).get(4),
                            Arrays.asList(line.split(delimiter)).get(5),
                            Arrays.asList(line.split(delimiter)).get(6),
                            Arrays.asList(line.split(delimiter)).get(7),
                            Arrays.asList(line.split(delimiter)).get(8));
                    peoplelist.add(nPerson);
                    validLines++;
                    generalValidation = true;
                }else{
                    invalidLines++;
                }
                validations.add(Arrays.asList(line.split(delimiter)).get(0)+" "+String.valueOf(generalValidation));
            }
        } catch (Exception e){
        }
        return (validations+
                "\n\nTotal of valid lines: "+ validLines
        +"\nTotal of invalid lines: "+ invalidLines);
    }

    public String uploadxlsxList(String url) throws IOException {
        int validLinesxlsx = 0;
        int invalidLinesxlsx = 0;
        int index = 1;
        try
        {

            File fileToRead = new File(url);
            FileInputStream fis = new FileInputStream(fileToRead);

            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);
            Iterator<Row> itr = sheet.iterator();
            while (itr.hasNext())
            {
                boolean generalValidationxslx = false;
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                boolean ILValidation = false;
                boolean RPValidation = false;
                ArrayList<String> tempArray = new ArrayList<>();
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();

                    //Injury location validation
                    if(cell.getColumnIndex()==1 && !cell.getStringCellValue().equals("N/A")){
                        ILValidation = true;
                    }

                    //Report type validation
                    if(cell.getColumnIndex()==7){
                        for(int i = 0; i < reportType.size();i++){
                            if (cell.getStringCellValue().equals(reportType.get(i))){
                                RPValidation = true;
                            }
                            }
                    }
                        switch (cell.getCellType())
                        {
                            case STRING:    //field that represents string cell type
                                tempArray.add(cell.getStringCellValue());
                                break;
                            case NUMERIC:    //field that represents number cell type
                                tempArray.add(String.valueOf(cell.getNumericCellValue()));
                                break;
                            default:
                        }

                }
                if(ILValidation == true && RPValidation == true) {
                    InjuryReports newReport = new InjuryReports(
                            LocalDate
                                    .of( 1899 , Month.DECEMBER , 30 ).plusDays((long) Double.parseDouble(tempArray.get(0)))
                                    .toString(),
                            tempArray.get(1),
                            tempArray.get(2),
                            tempArray.get(3),
                            tempArray.get(4),
                            tempArray.get(5),
                            tempArray.get(6),
                            tempArray.get(7),
                            tempArray.get(8),
                            tempArray.get(9),
                            tempArray.get(10));
                    reportsList.add(newReport);
                    validLinesxlsx++;
                    generalValidationxslx = true;
                }else{
                    invalidLinesxlsx++;
                }
                validationsxlsx.add(String.valueOf(index)+" "+String.valueOf(generalValidationxslx));
                index++;
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return (validationsxlsx+
                "\n\nTotal of valid lines: "+ validLinesxlsx
                +"\nTotal of invalid lines: "+ invalidLinesxlsx);
    }
}


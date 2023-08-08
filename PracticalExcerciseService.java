package com.example.PracticalExcercise;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.*;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.text.ParseException;
import java.util.stream.Collectors;
import java.io.File;
import java.io.FileInputStream;
import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class PracticalExcerciseService {
    ArrayList<People> peoplelist = new ArrayList<>();
    ArrayList<String> validations = new ArrayList<>();
    List<String> jobs = Arrays.asList("Haematologist", "Phytotherapist", "Building surveyor", "Insurance account manager", "Educational psychologist");
    List<String> reportType = Arrays.asList("Near Miss", "Lost Time", "First Aid");



    public ArrayList<People> printPeopleList(){
        return this.peoplelist;
    }

    public String uploadCSVList(){
        String file = "C:\\Users\\danie\\OneDrive\\Documentos\\Bootcamp Makaia\\Spring\\PracticalExcercise\\src\\main\\resources\\people.csv";
        String delimiter = ",";
        String headers;
        String line;
        int validLines = 0;
        int invalidLines = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
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
                    //List<String> values = Arrays.asList(line.split(delimiter));
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

    public String uploadxlsxList() throws IOException {
        try
        {
            File file = new File("C:\\Users\\danie\\OneDrive\\Documentos\\Bootcamp Makaia\\Spring\\PracticalExcercise\\src\\main\\resources\\sampledatasafety 2.xlsx");   //creating a new file instance
            FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
            //creating Workbook instance that refers to .xlsx file
            XSSFWorkbook wb = new XSSFWorkbook(fis);
            XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
            Iterator<Row> itr = sheet.iterator();    //iterating over excel file
            while (itr.hasNext())
            {
                Row row = itr.next();
                Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
                while (cellIterator.hasNext())
                {
                    Cell cell = cellIterator.next();
                    System.out.println("\n"+cell.getRowIndex()+","+cell.getColumnIndex());

                    //Injury location validation
                    boolean ILValidation = true;
                    if(cell.getColumnIndex()==1 && cell.getStringCellValue().equals("N/A")){
                        ILValidation = true;
                    }

                    //Report type validation
                    boolean RPValidation = true;
                    if(cell.getColumnIndex()==7){
                        for(int i = 0; i < reportType.size();i++){
                            if (cell.getStringCellValue().equals(reportType.get(i))){
                                RPValidation = true;
                            }
                            }
                    }

                    if(ILValidation == true && RPValidation == true){
                        switch (cell.getCellType())
                        {
                            case STRING:    //field that represents string cell type
                                System.out.print(cell.getStringCellValue());
                                //System.out.print(cell.getStringCellValue() + "\t\t\t");

                                break;
                            case NUMERIC:    //field that represents number cell type
                                System.out.print(cell.getNumericCellValue());
                                //System.out.print(cell.getNumericCellValue() + "\t\t\t");
                                break;
                            default:
                        }
                    }



                }
                System.out.println("");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return "test";
    }
}


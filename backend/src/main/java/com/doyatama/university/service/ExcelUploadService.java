package com.doyatama.university.service;

import com.doyatama.university.model.Lecture;
import com.doyatama.university.model.RPS;
import com.doyatama.university.model.RPSDetail;
import com.doyatama.university.model.Subject;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ExcelUploadService {
    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(),
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }

    public static List<RPS> getRPSDataFromExcel(InputStream inputStream) {
        List<RPS> rpsList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("RPS");

            if (sheet == null) {
                workbook.close();
                throw new IllegalArgumentException("Sheet 'RPS' does not exist in the Excel file.");
            }

            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                RPS rps = new RPS();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 0:
                            rps.setId(cell.getStringCellValue());
                            break;
                        case 1:
                            rps.setName(cell.getStringCellValue());
                            break;
                        case 2:
                            rps.setSks((int) cell.getNumericCellValue());
                            break;
                        case 3:
                            rps.setSemester((int) cell.getNumericCellValue());
                            break;
                        case 4:
                            rps.setCpl_prodi(cell.getStringCellValue());
                            break;
                        case 5:
                            rps.setCpl_mk(cell.getStringCellValue());
                            break;
                        case 6:
                            // Assuming subject name is in a single cell
                            String subjectName = cell.getStringCellValue().trim();
                            Subject subject = new Subject();
                            subject.setName(subjectName);
                            rps.setSubject(subject);
                            break;
                        case 7:
                            // Assuming dev_lecturers are in a single cell, comma-separated
                            String[] devLecturersArray = cell.getStringCellValue().split(";");
                            List<Lecture> devLecturers = new ArrayList<>();
                            for (String lecturerName : devLecturersArray) {
                                Lecture lecture = new Lecture();
                                lecture.setName(lecturerName.trim());
                                devLecturers.add(lecture);
                            }
                            rps.setDev_lecturers(devLecturers);
                            break;

                        default:
                            break;
                    }
                    cellIndex++;
                }
                rpsList.add(rps);
                rowIndex++;
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rpsList;
    }

    public static List<RPSDetail> getRPSDetailDataFromExcel(InputStream inputStream) {
        List<RPSDetail> rpsDetailList = new ArrayList<>();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheet("RPSDetail");

            if (sheet == null) {
                workbook.close();
                throw new IllegalArgumentException("Sheet 'RPSDetail' does not exist in the Excel file.");
            }

            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;
                RPSDetail rpsDetail = new RPSDetail();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 0:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                rpsDetail.setId(String.valueOf((int) cell.getNumericCellValue()));
                            } else {
                                rpsDetail.setId(cell.getStringCellValue());
                            }
                            break;
                        case 1:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                rpsDetail.setWeek((int) cell.getNumericCellValue()); // Set week as Integer
                            } else if (cell.getCellType() == CellType.STRING) {
                                try {
                                    rpsDetail.setWeek(Integer.parseInt(cell.getStringCellValue())); // Parse string to
                                                                                                    // Integer
                                } catch (NumberFormatException e) {
                                    // Handle the case where the string cannot be parsed to an integer
                                }
                            }
                            break;
                        case 2:
                            String rpsId = cell.getStringCellValue().trim();
                            cell = cellIterator.next(); // Move to the next cell for subject name
                            String rpsName = cell.getStringCellValue().trim();
                            RPS rps = new RPS();
                            rps.setId(rpsId);
                            rps.setName(rpsName);
                            rpsDetail.setRps(rps);
                            break;
                        case 3:
                            rpsDetail.setSub_cp_mk(cell.getStringCellValue());
                            break;
                        case 4:
                            if (cell.getCellType() == CellType.NUMERIC) {
                                rpsDetail.setWeight((float) cell.getNumericCellValue());
                            } else {
                                rpsDetail.setWeight(Float.parseFloat(cell.getStringCellValue()));
                            }
                            break;
                        case 5: // Assuming learning materials are in the 7th column
                            List<String> learningMaterials = rpsDetail.getLearning_materials();
                            if (learningMaterials == null) {
                                learningMaterials = new ArrayList<>();
                            }
                            String[] materials = cell.getStringCellValue().split(";"); // Assuming materials are
                                                                                       // semicolon-separated
                            for (String material : materials) {
                                learningMaterials.add(material.trim());
                            }
                            rpsDetail.setLearning_materials(learningMaterials);
                            break;
                        default:
                            break;
                    }
                    cellIndex++;
                }
                rpsDetailList.add(rpsDetail);
                rowIndex++;
            }
            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rpsDetailList;
    }

}
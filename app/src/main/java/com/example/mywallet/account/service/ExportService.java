package com.example.mywallet.account.service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import androidx.core.app.ActivityCompat;

import com.example.mywallet.account.dao.AccountDao;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.core.dao.WalletDbHelper;
import com.example.mywallet.transaction.dao.TransactionDao;
import com.example.mywallet.transaction.model.Transaction;
import com.example.mywallet.util.Utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class ExportService {

    private Context context;
    private AccountDao accountDAO;
    private TransactionDao transactionDao;

    public ExportService(Context context) {
        this.context = context;
        this.accountDAO = new AccountDao();
        this.transactionDao = new TransactionDao();
    }

    public void saveExcelFile() {
        WalletDbHelper dbh = null;
        try {
            dbh = new WalletDbHelper(this.context);

            String fileName = "export_" + System.currentTimeMillis() + ".xls";

            // check if available and not read only
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(
                    Environment.getExternalStorageState())) {
                Utils.toast(this.context, "Storage not available or read only");
                return;
            }

            List<Account> accounts = this.accountDAO.findAll(dbh);
            Map<Integer, Account> accountsMap = new HashMap<>();
            for (Account account : accounts) {
                accountsMap.put(account.getId(), account);
            }

            //New Workbook
            Workbook wb = new HSSFWorkbook();

            Cell c = null;


            // styles >>>
            CellStyle titleCs = wb.createCellStyle();
            titleCs.setFillForegroundColor(HSSFColor.LIME.index);
            titleCs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            CellStyle titleCurrencyCs = wb.createCellStyle();
            titleCurrencyCs.setFillForegroundColor(HSSFColor.LIME.index);
            titleCurrencyCs.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
            titleCurrencyCs.setDataFormat((short)8);

            CellStyle currencyCs = wb.createCellStyle();
            currencyCs.setDataFormat((short)8);
            // styles <<<


            //New Sheet
            Sheet sheet1 = null;
            sheet1 = wb.createSheet("My Wallet");

            int rowNumber = 0;

            for (Account account : accounts) {
                // Generate column headings
                Row row = sheet1.createRow(rowNumber++);

                c = row.createCell(0);
                c.setCellType(Cell.CELL_TYPE_NUMERIC);
                c.setCellValue(account.getId());
                c.setCellStyle(titleCs);

                c = row.createCell(1);
                c.setCellValue(account.getName());
                c.setCellStyle(titleCs);

                c = row.createCell(2);
                c.setCellType(Cell.CELL_TYPE_NUMERIC);
                c.setCellValue(account.getAmount());
                c.setCellStyle(titleCurrencyCs);
            }

            sheet1.createRow(rowNumber++);
            int colNum = 0;

            for (Account account : accounts) {
                // Generate column headings
                Row rowTitle = sheet1.createRow(rowNumber++);

                c = rowTitle.createCell(0);
                c.setCellType(Cell.CELL_TYPE_NUMERIC);
                c.setCellValue(account.getId());
                c.setCellStyle(titleCs);

                c = rowTitle.createCell(1);
                c.setCellValue(account.getName());
                c.setCellStyle(titleCs);

                c = rowTitle.createCell(2);
                c.setCellType(Cell.CELL_TYPE_NUMERIC);
                c.setCellValue(account.getAmount());
                c.setCellStyle(titleCurrencyCs);

                Row rowTitle2 = sheet1.createRow(rowNumber++);

                colNum = 0;

                c = rowTitle2.createCell(colNum++);
                c.setCellValue("Id");
                c.setCellStyle(titleCs);

                c = rowTitle2.createCell(colNum++);
                c.setCellValue("Date");
                c.setCellStyle(titleCs);

                c = rowTitle2.createCell(colNum++);
                c.setCellValue("Before");
                c.setCellStyle(titleCs);

                c = rowTitle2.createCell(colNum++);
                c.setCellValue("From");
                c.setCellStyle(titleCs);

                c = rowTitle2.createCell(colNum++);
                c.setCellValue("Concept");
                c.setCellStyle(titleCs);

                c = rowTitle2.createCell(colNum++);
                c.setCellValue("To");
                c.setCellStyle(titleCs);

                c = rowTitle2.createCell(colNum++);
                c.setCellValue("Amount");
                c.setCellStyle(titleCs);

                c = rowTitle2.createCell(colNum++);
                c.setCellValue("After");
                c.setCellStyle(titleCs);

                List<Transaction> transactions = this.transactionDao.findAllByAccountOrTransfer(dbh, account.getId());
                Double after = account.getAmount();
                Double before = null;

                for (Transaction transaction : transactions) {
                    Row row = sheet1.createRow(rowNumber++);
                    colNum = 0;

                    boolean isTransfer = false;
                    boolean weSent = false;
                    if (transaction.getTransferto() > 0) {
                        isTransfer = true;
                        if (account.getId() == transaction.getAccount()) { // we sent
                            weSent = true;
                            before = after + Math.abs(transaction.getAmount());
                        } else { // we received
                            before = after - Math.abs(transaction.getAmount());
                        }
                    } else {
                        before = after - transaction.getAmount();
                    }

                    c = row.createCell(colNum++);
                    c.setCellType(Cell.CELL_TYPE_NUMERIC);
                    c.setCellValue(transaction.getId());

                    c = row.createCell(colNum++);
                    c.setCellValue(transaction.getStamp());

                    c = row.createCell(colNum++);
                    c.setCellType(Cell.CELL_TYPE_NUMERIC);
                    c.setCellStyle(currencyCs);
                    c.setCellValue(before);

                    c = row.createCell(colNum++);
                    if (isTransfer && !weSent) {
                        c.setCellValue(accountsMap.get(transaction.getAccount()).getName());
                    } else {
                        c.setCellValue("");
                    }

                    c = row.createCell(colNum++);
                    c.setCellValue(transaction.getConcept());

                    c = row.createCell(colNum++);
                    if (isTransfer && weSent) {
                        c.setCellValue(accountsMap.get(transaction.getTransferto()).getName());
                    } else {
                        c.setCellValue("");
                    }

                    c = row.createCell(colNum++);
                    c.setCellType(Cell.CELL_TYPE_NUMERIC);
                    c.setCellStyle(currencyCs);
                    if (isTransfer) {
                        if (weSent) {
                            c.setCellValue(-1d * Math.abs(transaction.getAmount()));
                        } else {
                            c.setCellValue(Math.abs(transaction.getAmount()));
                        }
                    } else {
                        c.setCellValue(transaction.getAmount());
                    }

                    c = row.createCell(colNum++);
                    c.setCellType(Cell.CELL_TYPE_NUMERIC);
                    c.setCellStyle(currencyCs);
                    c.setCellValue(after);

                    after = before;
                }

                sheet1.createRow(rowNumber++);
            }

            sheet1.setColumnWidth(0, 3 * 500);
            sheet1.setColumnWidth(1, 8 * 500);
            sheet1.setColumnWidth(2, 6 * 500);
            sheet1.setColumnWidth(3, 8 * 500);
            sheet1.setColumnWidth(4, 10 * 500);
            sheet1.setColumnWidth(5, 8 * 500);
            sheet1.setColumnWidth(6, 6 * 500);
            sheet1.setColumnWidth(7, 6 * 500);

            int check = ActivityCompat.checkSelfPermission(this.context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (check != PackageManager.PERMISSION_GRANTED) {
                String[] PERMISSIONS = {android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
                ActivityCompat.requestPermissions((Activity) this.context, PERMISSIONS, 0);
            }

            // Create a path where we will place our List of objects on external storage
            File file = new File(
                    Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), fileName);
            FileOutputStream os = null;

            try {
                os = new FileOutputStream(file);
                wb.write(os);
                Utils.toast(this.context, "File saved in Downloads");
            } catch (Exception e) {
                Utils.raise("Writing data", e);
            } finally {
                try {
                    if (null != os)
                        os.close();
                } catch (Exception ex) {
                    Utils.raise("Closing stream", ex);
                }
            }
        } catch (Exception e) {
            Utils.raise("Exporting data", e);
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
    }
}
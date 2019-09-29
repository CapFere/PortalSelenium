package main;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;

public class PortalSelenium {
    public static void main(String[] args) throws InterruptedException {
        System.setProperty("webdriver.gecko.driver", "/usr/bin/geckodriver");
        WebDriver driver;
        driver = new FirefoxDriver();

        //Launch AAiT portal website
        driver.get("https://portal.aait.edu.et/");

        //Find the username input filed and seed it your username
        driver.findElement(By.id("UserName")).sendKeys("your-id");

        //Find the password input filed and seed it your password
        WebElement password = driver.findElement(By.id("Password"));
        password.sendKeys("your-password");

        //Submit the form inorder to login
        password.submit();

        //Initialize web driver to stop the execution until the Grade Report Dropdown in clickable
        WebDriverWait wait = new WebDriverWait(driver, 15);
        wait.until(elementToBeClickable(By.id("m2")));
        Thread.sleep(2000);

        //Find the Grade Report Dropdown and click it
        WebElement dropdown = driver.findElement(By.id("m2"));
        dropdown.click();

        //Find the Grade Report link
        WebElement element = driver.findElement(By.xpath("//*[@id=\"m2\"]/ul/li[1]/a"));

        //Use JavascriptExecutor to scroll through the dropdown
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();",element);

        //After Scrolling Click Grade Report link
        Thread.sleep(2000);
        element.click();

        //Stop the execution until the Updates button in clickable
        wait.until(presenceOfElementLocated(By.tagName("table")));
        Thread.sleep(2000);

        //Find teh container holding the grade results
        WebElement container = driver.findElement(By.tagName("table"));
        List<WebElement> tableRows = container.findElements(By.tagName("tr"));

        //Print Grade Report
        printGradeReport(tableRows);

        //export unread messages
        String filename = "gradeReport" + new Date().getTime() + ".txt";
        exportUnreadMessage(tableRows,filename);

        driver.close();

    }
    public static void printGradeReport(List<WebElement> tableRows){

        for(int i =1;i < tableRows.size();i++){
            List<WebElement> tableData = tableRows.get(i).findElements(By.tagName("td"));
            if(tableData.size()==1){
                System.out.println(tableData.get(0).getText());
                System.out.println();
            }else {

                for(int j=0; j<tableData.size()-1; j++){
                    if(j==1){
                        System.out.format("%-60s", tableData.get(j).getText());
                    }else if(j == 0){
                        System.out.print(tableData.get(j).getText() + "  ");
                    }else {
                        System.out.format("%-10s", tableData.get(j).getText());
                    }

                }
                System.out.println();
            }

        }
    }
    public  static void exportUnreadMessage(List<WebElement> tableRows,String filename){
        FileWriter writer = null;
        try {
            writer = new FileWriter(filename);
            for(int i =1;i < tableRows.size();i++){
                List<WebElement> tableData = tableRows.get(i).findElements(By.tagName("td"));
                if(tableData.size()==1){
                    writer.append(tableData.get(0).getText() + "\n\n");
                }else {

                    for(int j=0; j<tableData.size()-1; j++){
                        if(j==1){
                            writer.append(String.format("%-60s", tableData.get(j).getText()));
                        }else if(j == 0){
                            writer.append(tableData.get(j).getText() + "  ");
                        }else {
                            writer.append(String.format("%-10s", tableData.get(j).getText()));
                        }

                    }
                    writer.append("\n");
                    writer.flush();
                }

            }
        } catch (IOException ex) {

        } finally {
            try {
                writer.close();
            } catch (IOException ex) {

            }
        }
    }
}

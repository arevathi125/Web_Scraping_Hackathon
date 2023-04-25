package Utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;

public class VeganPage {

	@Test(priority = 1)
	public void initializingHypothyroidPage() throws IOException, InterruptedException {
		// Initializing Browser
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--remote-allow-origins=*");
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\areva\\OneDrive\\Desktop\\AllDrivers\\chromedriver.exe");
		ChromeDriver driver = new ChromeDriver(options);

		// WebDriver driver = new ChromeDriver(options);

		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		driver.manage().window().maximize();

		driver.get("https://www.tarladalal.com");

		// Initializing ExcelSheet
		String path = "C:\\Users\\areva\\My Workspace\\web_Scraping_Hackathon\\src\\test\\resources\\excelData\\EliminationList.xlsx";

		Excel_Reader_Writer xlRW = new Excel_Reader_Writer(path);
		List<String> receipeIDList = new ArrayList<>();
		List<String> ingredientsIDList = new ArrayList<>();
		String prepMethod = null;
		String nutriValues = null;
				   
        // using iterators
        Iterator<Map.Entry<String, ExcelPojo>> itr = xlRW.receipyMap().entrySet().iterator();
        String receipySheetKeyValue="";
        String receipyKey = "";
        int eliminationColumnIndex = 0;
        int addColumnIndex = 0;
        
        while(itr.hasNext())
        {
             Entry<String, ExcelPojo> entry = itr.next();
             System.out.println("Key = " + entry.getKey() + 
                                 ", Value = " + entry.getValue());
             receipyKey = entry.getKey(); 
             receipySheetKeyValue = entry.getValue().getSheetName();
             eliminationColumnIndex = entry.getValue().getEliminationColumnIndex();
             addColumnIndex = entry.getValue().getAddColumnIndex();

		// write header in Excel
		xlRW.setCellData(receipySheetKeyValue, 0, 0, "Recipe ID",false);
		xlRW.setCellData(receipySheetKeyValue, 0, 1, "Recipe Name",false);
		xlRW.setCellData(receipySheetKeyValue, 0, 2, "Recipe Category",false);
		xlRW.setCellData(receipySheetKeyValue, 0, 3, "Food Category",false);
		xlRW.setCellData(receipySheetKeyValue, 0, 4, "Ingredients",false);
		xlRW.setCellData(receipySheetKeyValue, 0, 5, "Preparation Time",false);
		xlRW.setCellData(receipySheetKeyValue, 0, 6, "Cooking Time",false);
		xlRW.setCellData(receipySheetKeyValue, 0, 7, "Preparation Method",false);
		xlRW.setCellData(receipySheetKeyValue, 0, 8, "Nutrient values",false);
		xlRW.setCellData(receipySheetKeyValue, 0, 9, "Targetted morbid conditions",false);
		xlRW.setCellData(receipySheetKeyValue, 0, 10, "Recipe URL",false);

		// Finding search button
		WebElement searchButtonInput = driver.findElement(By.xpath("//*[@id='ctl00_txtsearch']"));
		//searchButtonInput.sendKeys("vegan hypothyroidism");
		searchButtonInput.sendKeys(receipyKey);
		WebElement searchButtonSubmit = driver.findElement(By.name("ctl00$imgsearch"));
		searchButtonSubmit.click();

		// Number Of Pages and Number Of Recipes
		int pageSize = driver.findElements(By.xpath("//*[@id='cardholder']/div[2]/a")).size();
		System.out.println("Number Of Pages : " + pageSize);

		List<WebElement> lstReceipes = driver.findElements(By.xpath("html/body//div[@class='rcc_recipecard']"));
		int rowSize = lstReceipes.size();
		receipeIDList = new ArrayList<>();
		for (WebElement w : lstReceipes) {
			// System.out.println("w ell :" +w.getText());
			String recipe = "rcp" + w.getText().substring(8, 13);
			// System.out.println("receipe id :" +recipe);
			receipeIDList.add(recipe);
		}

		// Finding all values of all Pages
		int excelDataRow = 1;
		for (int p = 1; p <= pageSize; p++) {
			//receipeIDList = new ArrayList<>();
			if (p > 1) {

				System.out.println("---------Next Page------------");
				WebElement pageNum = driver.findElement(By.xpath("//*[@id='cardholder']/div[2]/a[" + p + "]"));
				String pagNumber = pageNum.getText();
				pageNum.click();
				System.out.println("Page Number : " + pagNumber);
				lstReceipes = driver.findElements(By.xpath("html/body//div[@class='rcc_recipecard']"));
				rowSize = lstReceipes.size();
				receipeIDList = new ArrayList<>();
				for (WebElement w : lstReceipes) {
					String recipe = "rcp" + w.getText().substring(8, 13);
					receipeIDList.add(recipe);
				}
			}

			for (int r = 0; r < 2; r++) {

				System.out.println("---------Recipe------------");
				System.out.println("---------receipeIDList------------"+receipeIDList.size());
				System.out.println("---------receipeIDList data------------"+receipeIDList);
                if(receipeIDList.size()==0)return;
				WebElement recipNam = driver
						.findElement(By.xpath("//*[@id='" + receipeIDList.get(r) + "']/div[3]/span[1]/a"));
				System.out.println("RecipeIDList : " + receipeIDList.get(r));

				// Receipe Name
				String recipeName = recipNam.getText();
				System.out.println("\t*******");
				System.out.println("Recipe Name : " + recipeName);
				recipNam.click();

				// current URL
				String recipeURL = driver.getCurrentUrl();
				System.out.println("\t*******");
				System.out.println("Recipe URL : " + recipeURL);

				// ReceipeID
				String recipeID = recipeURL.replaceAll("[^0-9]", "");
				System.out.println("\t*******");
				System.out.println("Recipe Id : " + recipeID);

				// String recipeCategory =
				// String foodCategory =

				// Ingredients
				String ingredients = driver.findElement(By.xpath("//div[@id='rcpinglist']")).getText();
				System.out.println("\t*******");
				System.out.println("Ingredients : " + ingredients);

				// Preparation Time
				String prepTime = driver.findElement(By.xpath("//time[@itemprop='prepTime']")).getText();
				System.out.println("\t*******");
				System.out.println("Preparation Time : " + prepTime);

				// Cooking Time
				String cookTime = driver.findElement(By.xpath("//time[@itemprop='cookTime']")).getText();
				System.out.println("\t*******");
				System.out.println("Cooking Time : " + cookTime);
                 
				// Preparation method
				try {
					 prepMethod = driver.findElement(By.xpath("//div[@id='recipe_small_steps']")).getText();
					System.out.println("\t*******");
					System.out.println("Preparation Method : " + prepMethod);
				} catch (Exception e) {
					System.out.println("\t*******");
					System.out.println("There is no Preparation Method");
				}

				// Nutrition Values
				try {
					 nutriValues = driver.findElement(By.xpath("//table[@id='rcpnutrients']")).getText();
					System.out.println("\t*******");
					System.out.println("Nutrition Values : " + nutriValues);
				} catch (Exception e) {
					System.out.println("\t*******");
					System.out.println("There is no Nutrtion Values");
				}

	      List<WebElement> lstIngredients = driver.findElements(By.xpath("//span[@itemprop='recipeIngredient']/a/span")); 
	       int ingridientSize = lstIngredients.size();
	       System.out.println("Ingredient size : " +ingridientSize);
	       WebElement eachIngredient = null; 
	       List<String> ingredientsList = new ArrayList<>();
	       for ( int i=1; i <= ingridientSize; i++) {
	    	   try {
	       //WebElement eachIngredient = driver.findElement(By.xpath("//*[@id='rcpinglist']/div/span["+i+"]/a/span"));
	    		   eachIngredient = driver.findElement(By.xpath("//span[@itemprop='recipeIngredient']["+i+"]/a/span"));
//	    		   System.out.print("Ingredients : " );
//	    	       System.out.println(eachIngredient.getText()); 
	    	       ingredientsList.add(eachIngredient.getText());
	    	   }catch(Exception e) {
	    		   e.printStackTrace();
	    	   }
	    
	       }
	       
	    /* // Elimination list
	       List<String> eliminatedList = xlRW.readColumnValueList("sheet1",eliminationColumnIndex);
	       System.out.println("Elimination List: " + eliminatedList);
	       System.out.println("Before Elimination Ingredient List : " + ingredientsList);
	       System.out.println("----------------");
	        ingredientsList.retainAll(eliminatedList);
	       // ingredientsList = new ArrayList<>();
	       System.out.println("After Elimination Ingredients List : "+ ingredientsList);   */
	       
	       
	    // Elimination list
	      // System.out.println(" updatedList   : " +ingredientsList);
	       
	       List<String> eliminatedList = xlRW.readColumnValueList("sheet1",eliminationColumnIndex);
	       System.out.println("Before Elimination Ingred List ");
	       System.out.println("Ingredients List : " + ingredientsList);
	       System.out.println("\t------------------");
	       System.out.println("Eliminated List  : " + eliminatedList);
	       List<String> duplicateList = xlRW.uniqueAndDuplicateElements(eliminatedList,ingredientsList);
	       List<String> updatedIngredList = new ArrayList<>();
	       updatedIngredList = ingredientsList;
	       System.out.println("copy updatedList   : " + updatedIngredList);
	       updatedIngredList.removeAll(duplicateList);
	       System.out.println("Before Added : ");
	       System.out.println("----------------");
	       System.out.println(" Updated Ingredient List   : " +updatedIngredList);
	       
	       // adding List
	     /*  List<String> addList = xlRW.readColumnValueList("sheet1",addColumnIndex);
	       ingredientsList.addAll(addList);
	       System.out.println("After Added : ");
	       System.out.println("----------------");
           System.out.println(" Updated Ingredient List   : " +ingredientsList);   */

    // adding List
	        List<String> addList = xlRW.readColumnValueList("sheet1",addColumnIndex);
	        updatedIngredList.addAll(addList);
	        System.out.println("After Added : ");
		       System.out.println("----------------");
	       System.out.println(" updatedList   : " +updatedIngredList);
       
    /*   // Allergies List
       List<String> allergyList = xlRW.readColumnValueList("sheet1", 10);
      //System.out.println("Allergy List Removal Ingredients : "); */
      
	       
	       // Allergies List
	       List<String> allergyList = xlRW.readColumnValueList("sheet1", 10);
	       List<String> duplicateList1 = xlRW.uniqueAndDuplicateElements(updatedIngredList, allergyList);
	       
	       // Write values in excel
	       xlRW.setCellData(receipySheetKeyValue, excelDataRow , 0 , recipeID,false);
	       xlRW.setCellData(receipySheetKeyValue, excelDataRow , 1 , recipeName,false);
	       xlRW.setCellData(receipySheetKeyValue, excelDataRow , 2, "Breakfast",false);
	 	   xlRW.setCellData(receipySheetKeyValue, excelDataRow ,3 , "Vegan",false);
	       if (duplicateList1.isEmpty()) {
	       xlRW.setCellData(receipySheetKeyValue, excelDataRow , 4 , updatedIngredList.toString(),false);
	       }
	       else {
	    	   xlRW.setCellData(receipySheetKeyValue, excelDataRow , 4 , updatedIngredList.toString(),true); 
	       }
	       
//	       if (ingredientsList.contains(allergyList) == true) {
//	    	   xlRW.setCellData(receipySheetKeyValue, excelDataRow , 4 , ingredientsList.toString(),true); 
//	       }
//	       else {
//	    	   xlRW.setCellData(receipySheetKeyValue, excelDataRow , 4 , ingredientsList.toString(),false);
//	       }
	       xlRW.setCellData(receipySheetKeyValue, excelDataRow , 5 , prepTime,false);
		   xlRW.setCellData(receipySheetKeyValue, excelDataRow , 6 , cookTime,false);
		   xlRW.setCellData(receipySheetKeyValue, excelDataRow , 7 , prepMethod,false);
		   xlRW.setCellData(receipySheetKeyValue, excelDataRow , 8 , nutriValues,false);
		  // xlRW.setCellData(receipySheetKeyValue, excelDataRow , 9 , Targetted Morbid Condition);
		   xlRW.setCellData(receipySheetKeyValue, excelDataRow , 10 , recipeURL,false);
	      
	       excelDataRow++;
				driver.navigate().back();

			}

		}
		}

	}
}

package semesterProject;

/*
 * This overflowed the default number of lines in the console
 */

import java.util.ArrayList;

import net.sf.jsqlparser.JSQLParserException;

public class DBTest {

	public static void main(String[] args) throws JSQLParserException, IllegalAccessException {

		ArrayList<String> insertQueries = new ArrayList<String>();

		String query1 = "CREATE TABLE YCStudent" + "(" + "BannerID int," + " CurrentStudent boolean DEFAULT  false,"
				+ " Class varchar(255) NOT NULL," + " FirstName varchar(35)," + " LastName varchar(255) NOT NULL UNIQUE,"
				+ " GPA decimal(1,2) DEFAULT 0.00," + " RandomLetter varchar(25) DEFAULT 'b'," + " Happiness INT,"
				+ "RandomNumber INT UNIQUE," + " PRIMARY KEY (BannerID)" + ");";

		String query101 = "CREATE TABLE SymsStudent (BannerID int," + " FirstName varchar(255), "
				+ " LastName varchar(255) NOT NULL,  " + "GPA decimal(1,2) DEFAULT 0.00, "
				+ " CurrentStudent boolean DEFAULT true,  " + "PRIMARY KEY (BannerID))";

		String query302 = "INSERT INTO  SymsStudent  (FirstName, LastName, BannerID)"
				+ " VALUES ('Person','something',  89)";

		String query303 = "Select * FROM SymsStudent";
		String query304 = "CREATE INDEX LastName on SymsStudent (LastName);";
		String query305 = "DELETE FROM SymsStudent";

		String query23 = "INSERT INTO YCStudent (FirstName, LastName, Class, BannerID, RandomNumber, RandomLetter)"
				+ " VALUES ('mr','guy', 'Freshman',80, NULL, 'z')";
		insertQueries.add(query23);

		String query25 = "INSERT INTO YCStudent (CurrentStudent, LastName, FirstName, GPA, Class, BannerID, RandomNumber, RandomLetter)"
				+ " VALUES (true, 'Last','Name', 2, 'Freshman',73, 76, 'c')";
		insertQueries.add(query25);

		String query26 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber, RandomLetter)"
				+ " VALUES (true, 'I dont believe','Names', 3, 'Junior',14, 99, 'd')";
		insertQueries.add(query26);

		String query27 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber, RandomLetter)"
				+ " VALUES (true, 'Benjamin','Cohen', 0, 'Junior',54 , 49, 'f')";

		insertQueries.add(query27);

		String query28 = "INSERT INTO YCStudent (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Dohen', 3, 'Senior',55 , 34)";
		insertQueries.add(query28);
		String query29 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','mohen', 2, 'Super Senior',45, 36)";
		insertQueries.add(query29);

		String query29b = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benin','So', 2, 'Super Senior',23, 41)";
		insertQueries.add(query29b);

		String query30 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Qohen', 3, 'Super Senior',46, 976)";
		insertQueries.add(query30);

		String query31 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Oohen', 0, 'Senior',49, 245)";
		insertQueries.add(query31);

		String query32 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Tohen', 1, 'Super Senior',32 , 327)";
		insertQueries.add(query32);

		String query33 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Sohen', 2, 'Super Senior',48 , 32)";
		insertQueries.add(query33);

		String query34 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Lohen', 9, 'Super Senior',75, 45)";
		insertQueries.add(query34);

		String query51 = "INSERT INTO YCStudent (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjaminnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn','Name', 2, 'Freshman',93, 99)";
		String query52 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Lohen', 9.091, 'Super Senior',798, 99)";
		String query52b = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Sohen', 2, 'Super Senior',48 , 33)";
		String query53 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Lohen', 92, 'Super Senior',798, 99)";
		String query54 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Lohen', 'Apples', 798, 99)";
		String query55 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Lohen', 9, 'Super Senior',798, Apples)";
		String query56 = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, Class, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Pohen', 3.0, 'Super Senior',98)";
		String query56a = "INSERT INTO YCStudent  (CurrentStudent, FirstName, LastName, GPA, BannerID, RandomNumber)"
				+ " VALUES (true, 'Benjamin','Pohen', 3.0, 86, 98)";
		String query57 = "INSERT INTO YCStudent (FirstName, LastName,Class, BannerID, RandomNumber)"
				+ " VALUES ('Benjamin','Name','Freshman',123, 24)";
		String query58 = "INSERT INTO YCStudent (FirstName, LastName,Class, BannerID, RandomNumber)"
				+ " VALUES ('Benjamin','Name','Freshman',NULL, 24)";
		String query59 = "INSERT INTO YCStudent (FirstName, LastName,Class, BannerID, RandomNumber)"
				+ " VALUES ('Benjamin','Name','Freshman',NULL, 76)";
		

		String query37 = "DELETE FROM YCStudent WHERE RandomLetter = 'p'";
		String query37b = "DELETE FROM YCStudent WHERE GPA = 0.0";
		String query37c = "DELETE FROM YCStudent WHERE FirstName <> NULL";
		String query38d = "DELETE FROM YCStudent WHERE Happiness = NULL";

		String query4 = "SELECT * FROM YCStudent";
		String query5 = "SELECT * FROM YCStudent ORDER BY Happiness, GPA, BannerID";
		String query6 = "Select GPA, RandomNumber FROM YCStudent ORDER BY GPA Desc, RandomNumber ASC";
		String query7 = "Select DISTINCT FirstName, GPA FROM YCStudent ORDER BY GPA DESC ";
		String query8 = "Select DISTINCT GPA FROM YCStudent ORDER BY GPA";
		String query8c = "Select GPA FROM YCStudent Order By RandomNumber";
		String query8b = "Select DISTINCT GPA FROM YCStudent WHERE GPA > 2 ";
		String query8d = "Select GPA, RandomNumber FROM YCStudent Order By RandomNumber";


		String query9 = "SELECT * FROM YCStudent WHERE GPA > 2.0";
		String query10 = "SELECT GPA, BannerID, RandomLetter FROM YCStudent WHERE GPA = 2.0";
		String query10b = "SELECT GPA, BannerID, RandomLetter FROM YCStudent WHERE GPA = 7.2 OR GPA = 2";
		String query10c = "SELECT GPA, BannerID, RandomLetter FROM YCStudent WHERE RandomLetter = 'b'";
		String query10d = "SELECT GPA, BannerID, RandomLetter FROM YCStudent WHERE RandomLetter = 'p'";

		String query11 = "SELECT GPA, BannerID FROM YCStudent WHERE GPA < 2.0";

		String query12 = "SELECT GPA, BannerID FROM YCStudent WHERE GPA < 2.0 OR GPA >2.0";
		String query13 = "SELECT GPA, BannerID FROM YCStudent WHERE GPA < 2.0 AND GPA > 2.0";
		String query14 = "SELECT GPA, BannerID FROM YCStudent WHERE GPA <> 2.0";
		String query15c = "SELECT * FROM YCStudent WHERE RandomNumber > 45 ORDER BY LastName";
		String query15 = "SELECT * FROM YCStudent WHERE RandomNumber > 45 ORDER BY BannerID";
		String query15b = "SELECT * FROM YCStudent WHERE RandomNumber > 1";
		String query16 = "SELECT GPA, BannerID, RandomNumber FROM YCStudent WHERE RandomNumber >= 45";
		String query16b = "SELECT GPA, BannerID, RandomNumber, RandomLetter FROM YCStudent WHERE RandomNumber >= 1 ORDER BY RandomLetter";

		String query17 = "SELECT GPA, BannerID FROM YCStudent WHERE BannerID < 46";
		String query18 = "SELECT GPA, BannerID FROM YCStudent WHERE RandomNumber <=45 OR BannerID >=1  ORDER BY GPA";
		String query18b = "SELECT GPA, BannerID FROM YCStudent WHERE RandomNumber < 45 AND BannerID >=1  ORDER BY GPA";

		String query19 = "SELECT GPA, BannerID, RandomNumber FROM YCStudent WHERE (BannerID >= 45) OR (RandomNumber = 99 AND GPA = 3.0)";
		String query20h = "SELECT GPA, BannerID, RandomNumber FROM YCStudent WHERE (RandomNumber <> 976)";

		String query20 = "SELECT GPA, BannerID, RandomNumber FROM YCStudent WHERE (BannerID >= 45 AND BannerID <45) OR (RandomNumber = 99 AND GPA = 3.0)";
		String query20b = "SELECT GPA, BannerID, RandomNumber FROM YCStudent WHERE (BannerID >= 45 AND BannerID <> 80) OR (RandomNumber = 99 AND GPA = 3.0)";
		String query20c = "SELECT RandomLetter FROM YCStudent WHERE (RandomLetter < 'c')";
		String query20d = "SELECT * FROM YCStudent WHERE (RandomLetter <> 'b')";
		String query20e = "SELECT RandomLetter FROM YCStudent WHERE (RandomLetter > 'b')";
		String query20f = "SELECT DISTINCT RandomLetter FROM YCStudent WHERE (RandomLetter >= 'b') ORDER BY RandomLetter";
		String query20g = "SELECT RandomLetter FROM YCStudent WHERE (RandomLetter = 'p')";

		String query21 = "Select SUM(GPA), COUNT(GPA), AVG(GPA),  MAX (GPA), MIN (GPA) FROM YCStudent";
		String query22 = "Select SUM(RandomNumber), COUNT (RandomNumber), AVG(RandomNumber) FROM YCStudent";
		String query22b = "Select COUNT(DISTINCT GPA),SUM(DISTINCT GPA), AVG(DISTINCT GPA),  MAX (BannerID), MIN (BannerID) FROM YCStudent";
		String query22c = "Select COUNT(DISTINCT GPA), SUM(DISTINCT GPA), AVG(DISTINCT GPA),  MAX (BannerID), MIN (BannerID) FROM YCStudent WHERE GPA > 8";
		String query22d = "Select COUNT(Happiness), SUM(Happiness), AVG(Happiness), MAX(Happiness), MIN(Happiness) FROM YCStudent";
		String query22e = "Select COUNT(DISTINCT FirstName) FROM YCStudent";


		String query23a = "Select SUM(CurrentStudent) FROM YCStudent";
		String query23b = "Select SUM(FirstName) FROM YCStudent";
		String query23c = "Select MAX(CurrentStudent) FROM YCStudent";
		String query23d = "Select MIN(CurrentStudent) FROM YCStudent";
		String query23e = "Select SUM(GPA), GPA FROM YCStudent";

		String query60 = "SELECT GPA, BannerID, Class, FirstName, RandomNumber, Happiness FROM YCStudent WHERE RandomNumber = 99";

		String query36 = "UPDATE YCStudent SET Class = 'Fresh' WHERE RandomNumber = 99";
		String query36b = "UPDATE YCStudent SET RandomLetter = 'x', GPA = 7.2  WHERE GPA = 2.0";
		String query36c = "UPDATE YCStudent SET RandomLetter = 'p', GPA = 4.8  WHERE RandomLetter = 'b'";

		String query61 = "UPDATE YCStudent SET FirstName = BENJAMONNNNNNNNNNNNNNNNNNNNNJHKKKKKKKKKKKKKKKKKKKKKKKKKKNNNNNNNNNNNN WHERE RandomNumber = 99";
		String query62 = "UPDATE YCStudent SET GPA = 9.091 WHERE RandomNumber = 99";
		String query63 = "UPDATE YCStudent SET Class = 'news', GPA = 9000.1  WHERE GPA = 0.0";
		String query64 = "UPDATE YCStudent SET BannerID = 15  WHERE GPA= 0.0";
		String query65 = "UPDATE YCStudent SET Happiness  = 20 WHERE RandomNumber = 34";
		String query66 = "UPDATE YCStudent SET RandomLetter = 'l'";
		String query67 = "UPDATE YCStudent SET FirstName = NULL  WHERE RandomLetter = 'p'";
		String query67b = "UPDATE YCStudent SET FirstName = NULL  WHERE RandomLetter = 'b'";
		//String query67c = "UPDATE YCStudent SET RandomNumber = NULL;


		String query68 = "UPDATE YCStudent SET RandomLetter = NULL  WHERE RandomLetter <> 'p'";
		String query70b = "UPDATE YCStudent SET RandomNumber = 89  WHERE RandomLetter <> 'p'";


		String query69 = "SELECT GPA, RandomLetter BannerID, Class, FirstName, RandomNumber, Happiness FROM YCStudent WHERE RandomLetter = NULL";

		String query71 = "UPDATE YCStudent SET BannerID = 15, falseColumn= 99 WHERE RandomNumber = 99";
		String query70 = "SELECT falseColumn FROM YCStudent WHERE RandomNumber = 99";
		String query72 = "CREATE INDEX falsetalebe on YCStudent (falseColumn);";
		String query73 = "UPDATE YCStudent SET falseColumn= 99 WHERE RandomNumber = 99";
		String query74 = "SELECT falsetable FROM YCStudent WHERE RandomNumber = 99";
		String query75 = "CREATE INDEX falseColumn on falsetable (falseColumn);";

		String query77 = "DELETE FROM YCStudent";

		String query35 = "CREATE INDEX RandomNumber on YCStudent (RandomNumber);";
		String query35b = "CREATE INDEX RandomLetter on YCStudent (RandomLetter);";
		String query35c = "CREATE INDEX CurrentStudent on YCStudent (CurrentStudent);";

		String query102 = "SELECT * FROM YCStudent WHERE FirstName <> NULL";
		String query103 = "SELECT * FROM YCStudent WHERE FirstName = NULL";
		String query104 = "SELECT * FROM YCStudent WHERE RandomNumber = NULL ORDER BY RandomNumber";
		String query105 = "SELECT * FROM YCStudent WHERE RandomNumber <> NULL";

		String query700 = "CREATE TABLE MalcheiYehuda (FirstName varchar (10) NOT NULL, father varchar (10), reign decimal (2,2) DEFAULT 0.0, hadBamos BOOLEAN DEFAULT true, num int UNIQUE, PRIMARY KEY (FirstName))";
		String query701 = "INSERT INTO MalcheiYehuda (FirstName, num) VALUES ('Rechavam', 1)";
		String query702 = "INSERT INTO MalcheiYehuda (FirstName, num) VALUES ('Aviah', 2)";
		String query703 = "UPDATE MalcheiYehuda SET father = 'Shlomo'";
		String query704 = "UPDATE MalcheiYehuda SET father = 'Rechavam' WHERE num = 2";
		String query705 = "UPDATE MalcheiYehuda SET reign = 17, hadBamos = true";
		String query706 = "UPDATE MalcheiYehuda SET reign = 3 WHERE father = 'Rechavam'";
		String query707 = "SELECT FirstName, hadBamos FROM MalcheiYehuda WHERE hadBamos = true";
		String query708 = "SELECT DISTINCT COUNT (father) FROM MalcheiYehuda";
		String query709 = "SELECT COUNT (father) FROM MalcheiYehuda";
		String query710 = "CREATE INDEX reign_INDEX on MalcheiYehuda(reign)";
		String query711 = "SELECT * FROM MalcheiYehuda ORDER BY num ASC";
		String query712 = "SELECT SUM (num) FROM MalcheiYehuda";
		String query713 = "SELECT MAX (FirstName), MIN (FirstName) FROM MalcheiYehuda";
		
		String query715 = "Select * FROM MalcheiYehuda";

		Main main = new Main(); // this starts the database
		ResultSet rs = null;
		System.out.printf("%s\n\n", "This query will build the table");
		System.out.printf("query: %s\n\n", query1);
		rs = main.Execute(query1);
		System.out.printf("%s\n\n", "Here are the columns in the result set");
		printTable(rs);

		System.out.println("Just to show these operations are choosing between multiple tables");
		System.out.printf("query: %s\n\n", query101);
		rs = main.Execute(query101);
		System.out.printf("%s\n\n", "Here are the columns in the result set");
		printTable(rs);

		System.out.println();
		System.out.println("These queries will insert some data into the table");
		System.out.println(
				"Note how the first query shows that the order doesn't make a difference order. Also note direclty inserting null");
		for (int i = 0; i < insertQueries.size(); i++) {
			String query = insertQueries.get(i);
			System.out.printf("query: %s\n\n", query);
			rs = main.Execute(query);
			System.out.print("Result: ");
			printData(rs);
			System.out.println("_____________________________________________________________________________");

		}

		System.out.println("_____________________________________________________________________________");
		System.out.printf("\n\n%s\n\n", "This selects all the data in the table");

		rs = main.Execute(query4);
		System.out.printf("query: %s\n\n", query4);
		printTable(rs);

		System.out.println();
		System.out.println("The primary key is automatically indexed. Here is it's Btree");
		System.out.println(
				"It's easier just to draw it out on paper but i recurse through it here. The hieght is on the first line. The 2nd line tells");
		System.out.println(
				"how many elements in the page. The for each entry, it lists the key and if rows is not null(ie- its a leaf) then it prints out the number of rows in it ");
		Table table = (Table) main.getDataBase().get(0);
		BTree btree = (BTree) table.getBtrees().get(0);
		btree.print();

		System.out.println();
		System.out.println("Inserting certain elements with invalid data does nothing.");
		System.out.println();
		System.out.println();

		System.out.println("This is what happens when there are duplicate values in a unique column");
		System.out.println(query52b);
		System.out.println();
		rs = main.Execute(query52b);
		System.out.print("Result: ");
		printTable(rs);
		System.out.println();

		System.out.println("this is what happens when a varchar is too long");

		System.out.println(query51);
		System.out.println();
		main.Execute(query51);
		System.out.print("Result: ");
		printTable(rs);

		System.out.println();
		System.out.println("this is what happens when a decimal is too long");
		System.out.println();
		System.out.println(query52);
		main.Execute(query52);
		System.out.print("Result: ");
		printTable(rs);

		System.out.println();
		System.out.println(query53);
		main.Execute(query53);
		System.out.println();

		System.out.println("Entering false data types will lead to problems when trying to parse");
		System.out.println();

		System.out.println();
		System.out.println(query54);
		main.Execute(query54);
		System.out.println();

		System.out.println("likewise for integers");

		System.out.println();
		System.out.println(query55);
		main.Execute(query55);
		System.out.println();

		System.out.println("Program checks for nulls");
		System.out.println();
		System.out.println(query56);
		main.Execute(query56);
		System.out.println();

		System.out.println("By explicitly setting");
		System.out.println();
		System.out.println(query58);
		main.Execute(query58);
		System.out.println();

		System.out.println("And in a non bannerID");
		System.out.println();
		System.out.println(query56a);
		main.Execute(query56a);
		System.out.println();
		
		
		System.out.println();
		System.out.println(query59);
		main.Execute(query59);
		System.out.println();

		System.out.println(
				"leaving out a default value will insert it automatically wiht the default value. Here current student and GPA will be left off");
		System.out.println();
		System.out.println(query57);
		rs = main.Execute(query57);
		printTable(rs);
		System.out.println();

		System.out.println(
				"Once again I'll print here the entire table to show that nothing has changed except the last added rows with the default values");
		rs = main.Execute(query4);
		System.out.printf("query: %s\n\n", query4);
		printTable(rs);

		System.out.println();
		System.out.println("Select can order the data");
		rs = main.Execute(query5);
		System.out.printf("query: %s\n\n", query5);
		printTable(rs);

		System.out.println();
		System.out.println("Select can select specific columns");
		System.out.println("Order by can be specified to be ascending / descending");
		System.out.println("A null for this function is considered to be the lowest value");
		rs = main.Execute(query6);
		System.out.printf("query: %s\n\n", query6);
		printTable(rs);
		
		System.out.println("ordered by a not present column");
		rs = main.Execute(query8c);
		System.out.printf("query: %s\n\n", query8c);
		printTable(rs);
		
	
		rs = main.Execute(query8d);
		System.out.printf("query: %s\n\n", query8d);
		printTable(rs);

		System.out.println("Select can select distinct pieces of data");
		rs = main.Execute(query8);
		System.out.printf("query: %s\n\n", query8);
		printTable(rs);
		

		

		System.out.println();
		System.out.println("Distinct can select distinct from multiple columns");
		rs = main.Execute(query7);
		System.out.printf("query: %s\n\n", query7);
		printTable(rs);

		System.out.println();
		System.out.println("It is possible to create an index");
		rs = main.Execute(query35);
		System.out.printf("query: %s\n", query35);
		System.out.print("Result: ");
		printTable(rs);

		BTree btree1 = (BTree) table.getBtrees().get(1);

		System.out.println(btree1.getName() + " is the btree's name");
		System.out.println("here is the btree");
		btree1.print();

		System.out.println("Select can select conditionals");
		rs = main.Execute(query9);
		System.out.println();
		System.out.printf("query: %s\n\n", query9);
		printTable(rs);
		rs = main.Execute(query10);
		System.out.println();
		System.out.printf("query: %s\n\n", query10);
		printTable(rs);

		rs = main.Execute(query11);
		System.out.println();
		System.out.printf("query: %s\n\n", query11);
		printTable(rs);

		rs = main.Execute(query12);
		System.out.println();
		System.out.printf("query: %s\n\n", query12);
		printTable(rs);

		rs = main.Execute(query13);
		System.out.println();
		System.out.printf("query: %s\n\n", query13);
		printTable(rs);

		rs = main.Execute(query14);
		System.out.println();
		System.out.printf("query: %s\n\n", query14);
		printTable(rs);

		System.out.println();
		System.out.println("This also works with BTrees");
		System.out.println();
		System.out.printf("query: %s\n\n", query15);
		rs = main.Execute(query15);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query16);
		rs = main.Execute(query16);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query17);
		rs = main.Execute(query17);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query18);
		rs = main.Execute(query18);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query18b);
		rs = main.Execute(query18b);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query19);
		rs = main.Execute(query19);
		System.out.println();
		printTable(rs);
		System.out.println("note how BannerID =14 is present even though its < 45");

		System.out.printf("query: %s\n\n", query20);
		rs = main.Execute(query20);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query20b);
		rs = main.Execute(query20b);
		System.out.println();
		printTable(rs);

		System.out.println();

		System.out.printf("query: %s\n\n", query20h);
		rs = main.Execute(query20h);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query8);
		rs = main.Execute(query8);
		System.out.println();
		printTable(rs);

		System.out.println("SELECT FUNCTIONS");
		System.out.println("----------------------------");

		System.out.println("nulls are not counted for functions as a value");
		System.out.println(
				"the format of the result set: the describers are the column and the row contains the name of function + its value + if it's distinct");
		System.out.println(
				"the directions said 'your result set class must include the names and data types of your columns'");
		System.out.println(
				"Each function gets its own column because each function is its own operation (and its clearer)");

		System.out.printf("query: %s\n\n", query21);
		rs = main.Execute(query21);
		System.out.println();
		printTable(rs);

		System.out.println();
		System.out.printf("query: %s\n\n", query22);
		rs = main.Execute(query22);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query22b);
		rs = main.Execute(query22b);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query22c);
		rs = main.Execute(query22c);
		System.out.println();
		printTable(rs);

		System.out.println("nulls don't count");
		System.out.printf("query: %s\n\n", query22d);
		rs = main.Execute(query22d);
		printTable(rs);
		

		System.out.printf("query: %s\n\n", query22e);
		rs = main.Execute(query22e);
		printTable(rs);

		System.out.println();
		System.out.println("Illegal function calls");

		System.out.printf("query: %s\n\n", query23a);
		rs = main.Execute(query23a);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query23b);
		rs = main.Execute(query23b);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query23c);
		rs = main.Execute(query23c);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query23d);
		rs = main.Execute(query23d);
		System.out.println();
		printTable(rs);

		System.out.println("___________________________________________");
		System.out.println("Create INDEX");

		System.out.printf("query: %s\n\n", query35b);
		rs = main.Execute(query35b);
		System.out.println();
		System.out.print("result: ");
		printTable(rs);

		System.out.println();
		System.out.println();
		System.out.println("Note how it is possible for the btree to store multiple rows in 1 key");
		System.out.println("Also notice comparisons work the same for varchars");
		System.out.println("the sentinel is a blank string so it's blank");
		BTree btree2 = (BTree) table.getBtrees().get(2);
		btree2.print();

		System.out.println();
		System.out.println("It is possible to create an index on a boolean");
		rs = main.Execute(query35c);
		System.out.printf("query: %s\n", query35c);
		System.out.print("Result: ");
		printTable(rs);
		System.out.println("aba");
		BTree btree3 = (BTree) table.getBtrees().get(3);
		btree3.print();

		System.out.println();
		System.out.printf("query: %s\n\n", query20c);
		rs = main.Execute(query20c);
		System.out.println();
		System.out.print("result: ");
		printTable(rs);

		System.out.printf("query: %s\n\n", query20d);
		rs = main.Execute(query20d);
		System.out.println();

		printTable(rs);

		System.out.printf("query: %s\n\n", query20e);
		rs = main.Execute(query20e);
		System.out.println();
		System.out.print("result: ");
		printTable(rs);

		System.out.printf("query: %s\n\n", query20f);
		rs = main.Execute(query20f);
		System.out.println();
		System.out.print("result: ");
		printTable(rs);

		System.out.println("________________________________________________");
		System.out.println("Now for update");
		System.out.println("Here's one row from the table");
		System.out.println();
		System.out.println(query60);
		System.out.printf("query: %s\n\n", query60);
		rs = main.Execute(query60);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query36);
		rs = main.Execute(query36);
		System.out.println();
		printTable(rs);

		System.out.println("After update");
		System.out.println(query60);
		System.out.printf("query: %s\n\n", query60);
		rs = main.Execute(query60);
		System.out.println();
		System.out.println("result: ");
		printTable(rs);

		System.out.println("Observe: class changed to Fresh");
		System.out.println("These changes are made also in the btree. Note how the row comes from the Btree");

		System.out.println("More than one row/column can be updated at a zman");

		rs = main.Execute(query10);
		System.out.println();
		System.out.printf("query: %s\n\n", query10);
		printTable(rs);

		System.out.printf("query: %s\n\n", query36b);
		rs = main.Execute(query36b);
		System.out.println();
		System.out.println("result: ");
		printTable(rs);
		System.out.println();

		System.out.println("And after...");
		rs = main.Execute(query10b);
		System.out.println();
		System.out.printf("query: %s\n\n", query10b);
		printTable(rs);

		rs = main.Execute(query4);
		System.out.printf("query: %s\n\n", query4);
		printTable(rs);

		System.out.printf("query: %s\n\n", query10c);
		rs = main.Execute(query10c);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query36c);
		rs = main.Execute(query36c);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query10c);
		rs = main.Execute(query10c);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.println("but...");
		System.out.printf("query: %s\n\n", query10d);
		rs = main.Execute(query10d);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.println("And from another btree");
		System.out.printf("query: %s\n\n", query16b);
		rs = main.Execute(query16b);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query67);
		rs = main.Execute(query67);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query68);
		rs = main.Execute(query68);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query4);
		rs = main.Execute(query4);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query69);
		rs = main.Execute(query69);
		System.out.println();
		printTable(rs);

		System.out.println("Update also ignores bad queries");
		
	
       System.out.println("changing more than one row in a unique column to the same value");
		System.out.printf("query: %s\n\n", query70b);
		rs = main.Execute(query70b);
		System.out.println();
		printTable(rs);
		


		System.out.printf("query: %s\n\n", query61);
		rs = main.Execute(query61);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query62);
		rs = main.Execute(query62);
		System.out.println();
		printTable(rs);

		System.out.println(
				"next time table is printed it will show that class won't be updated even though that part of query is valid");

		System.out.printf("query: %s\n\n", query63);
		rs = main.Execute(query63);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query64);
		rs = main.Execute(query64);
		System.out.println();
		printTable(rs);
		System.out.println(
				"BannerID = 15 is a legal value however this would change more than 1 row and BannerID is unique");

		System.out.println("no difference inserting into null spots");
		System.out.printf("query: %s\n\n", query65);
		rs = main.Execute(query65);
		System.out.println();
		printTable(rs);

		System.out.println();
		System.out.println("to show nothing changed from those queries except the last");

		System.out.println(query4);
		System.out.printf("query: %s\n\n", query4);
		rs = main.Execute(query4);
		System.out.println();
		printTable(rs);

		System.out.println("distinct with where");
		System.out.printf("query: %s\n\n", query8b);
		rs = main.Execute(query8b);
		System.out.println();
		printTable(rs);

		
		
		System.out.println("Now that we have more nulls in the table here are some tests with selecting for nulls");
		System.out.printf("query: %s\n\n", query102);
		rs = main.Execute(query102);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query103);
		rs = main.Execute(query103);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query104);
		rs = main.Execute(query104);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query105);
		rs = main.Execute(query105);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query15c);
		rs = main.Execute(query15c);
		System.out.println();
		printTable(rs);
		System.out.println("find");

		System.out.println("_______________________________________________");
		System.out.println("And delete...");
		System.out.printf("query: %s\n\n", query37);
		rs = main.Execute(query37);
		System.out.println();
		printTable(rs);

		System.out.println("but...");
		System.out.printf("query: %s\n\n", query10d);
		rs = main.Execute(query10d);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.println("In a non indexed");
		System.out.printf("query: %s\n\n", query4);
		rs = main.Execute(query4);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query37b);
		rs = main.Execute(query37b);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query4);
		rs = main.Execute(query4);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.println("Referencing a false column");
		System.out.printf("query: %s\n\n", query70);
		rs = main.Execute(query70);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query71);
		rs = main.Execute(query71);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query72);
		rs = main.Execute(query72);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query73);
		rs = main.Execute(query73);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.println("And false tablenames");

		System.out.printf("query: %s\n\n", query74);
		rs = main.Execute(query73);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query75);
		rs = main.Execute(query73);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query4);
		rs = main.Execute(query4);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query20d);
		rs = main.Execute(query20d);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.println("updating all values");
		System.out.printf("query: %s\n\n", query66);
		rs = main.Execute(query66);
		System.out.println();
		printTable(rs);

		System.out.printf("query: %s\n\n", query4);
		rs = main.Execute(query4);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.println("And for rm*");
		System.out.printf("query: %s\n\n", query77);
		rs = main.Execute(query77);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query4);
		rs = main.Execute(query4);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query15b);
		rs = main.Execute(query15b);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.println("I need more data in the table to show one more thing");

		for (int i = 0; i < insertQueries.size(); i++) {
			String query = insertQueries.get(i);
			System.out.printf("query: %s\n\n", query);
			rs = main.Execute(query);
			System.out.print("Result: ");
			printData(rs);
			System.out.println("_____________________________________________________________________________");

		}

		System.out.printf("query: %s\n\n", query4);
		rs = main.Execute(query4);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query67b);
		rs = main.Execute(query67b);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query4);
		rs = main.Execute(query4);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.println("Deleting nulls works the same since the condition function works the same");

		System.out.printf("query: %s\n\n", query37c);
		rs = main.Execute(query37c);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query4);
		rs = main.Execute(query4);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query15b);
		rs = main.Execute(query15b);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query38d);
		rs = main.Execute(query38d);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.printf("query: %s\n\n", query15b);
		rs = main.Execute(query15b);
		System.out.println();
		printTable(rs);
		System.out.println();

		System.out.println("borrowing a few testq ueries from piazza to show this works with more tables and to show = with strings");

	
		
		System.out.printf("query: %s\n\n", query700);
		rs = main.Execute(query700);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query701);
		rs = main.Execute(query701);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query702);
		rs = main.Execute(query702);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query703);
		rs = main.Execute(query703);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		
		System.out.printf("query: %s\n\n", query715);
		rs = main.Execute(query715);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query704);
		rs = main.Execute(query704);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query705);
		rs = main.Execute(query705);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		
		System.out.printf("query: %s\n\n", query715);
		rs = main.Execute(query715);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query706);
		rs = main.Execute(query706);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query707);
		rs = main.Execute(query707);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		
		System.out.printf("query: %s\n\n", query715);
		rs = main.Execute(query715);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query708);
		rs = main.Execute(query708);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query709);
		rs = main.Execute(query709);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query710);
		rs = main.Execute(query710);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query711);
		rs = main.Execute(query711);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query712);
		rs = main.Execute(query712);
		System.out.println();
		printTable(rs);
		System.out.println();
		
		System.out.printf("query: %s\n\n", query713);
		rs = main.Execute(query713);
		System.out.println();
		printTable(rs);
		System.out.println();
		
	
		
		
		System.out.println("just to make sure none of my queries get cut off, the first query is " + query1);
		
		
	

	}

	private static void printTable(ResultSet resultSet) {
		printDescribers(resultSet);
		printData(resultSet);

	}

	private static void printData(ResultSet resultSet) {
		if (resultSet.getRows() == null) {
			return;
		}

		if (resultSet.getRows().size() == 0) {
			return;

		}
		ArrayList<Row> rows = resultSet.getRows();

		for (int rowIndex = 0; rowIndex < rows.size(); rowIndex++) {
			Row row = rows.get(rowIndex);

			for (int column = 0; column <= row.getSize(); column++) {
				System.out.printf("%-20.16s", row.getData(column));

			}
			System.out.println();

		}

	}

	private static void printDescribers(ResultSet resultSet) {

		ArrayList<String> descriptions = resultSet.getDescription();
		ArrayList<String> dataType = resultSet.getDataType();

		for (int i = 0; i < descriptions.size(); i++) {
			System.out.printf("%-20s", descriptions.get(i));

		}
		if (descriptions.size() != 0) {
			System.out.println();
			for (int i = 0; i < descriptions.size(); i++) {
				System.out.printf("(%-19s", dataType.get(i) + ")");

			}
			System.out.println();

			for (int i = 0; i < descriptions.size(); i++) {
				System.out.print("___________________");
			}
			System.out.println();

		}
	}

}

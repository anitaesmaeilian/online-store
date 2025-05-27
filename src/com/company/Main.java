package com.company;
import java.sql.Ref;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.ArrayList;
import static com.company.Main.sc;

public class Main {
    static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        while(true)
            Menu.menu();
    }
}

abstract class Menu{
    static boolean loggedIn = false;

    static void menu(){
        System.out.println("Choose where you want to go.");
        System.out.println("1-users section     2-products section     3-admin section");
        int x = sc.nextInt();
        sc.nextLine();
        if(x==1)
            usersSection();
        else if(x==2)
            productsSection();
        else if(x==3)
            adminSection();
    } // safheye asli

    static void usersSection(){
        if (loggedIn) {
            if (Commands.loggedBuyer == null) {
                System.out.println(Commands.loggedSeller.toString());
                sellerCommands.showSellersCommands();
            } else if (Commands.loggedSeller == null) {
                System.out.println(Commands.loggedBuyer.toString());
                buyerCommands.showBuyersCommands();
            }
        }
        else {
            System.out.println("Do you want to log in or make a new account?");
            System.out.println("1-log in     2-make a new account");
            int y = sc.nextInt();
            sc.nextLine();
            if(y==1)
                Commands.logIn();
            else if(y==2)
                Commands.register();
        }
    } // safheye forushande va moshtari ha
    static void productsSection(){
        System.out.println("1-show all products   2-search for products   3-use sort and filters");
        int x = sc.nextInt();
        sc.nextLine();
        switch (x){
            case 1:
                buyerCommands.showProducts();
                break;
            case 2:
                productsGroupingCommands.search(Product.getProductsList());
                break;
            case 3:
                productsGroupingCommands.filter();
                break;
        }
    } // safheye kala ha
    static void adminSection(){
        if (loggedIn && Commands.loggedBuyer == null && Commands.loggedSeller == null) {
            System.out.println(Admin.getAdmin().toString());
            adminCommands.showAdminsCommands();
        } else
            System.out.println("you have to log in first.");
    } // safheye admin
}
abstract class Commands{
    static Seller loggedSeller;
    static Buyer loggedBuyer;

    static void register(){
        // gereftane etelaate sabtenam
        System.out.println("What role are you registering for?");
        System.out.println("1-Seller     2-Buyer");
        int x = sc.nextInt();
        sc.nextLine();
        System.out.println("Please fill the following information.");
        System.out.print("username: ");
        String userName = sc.nextLine();
        System.out.print("first name: ");
        String firstName = sc.nextLine();
        System.out.print("last name: ");
        String lastName = sc.nextLine();
        System.out.print("email: ");
        String email = sc.nextLine();
        System.out.print("phone number: ");
        String phoneNumber = sc.nextLine();
        System.out.print("password: ");
        String password = sc.nextLine();
        // gereftane etelaate motafavete naghsh ha
        if (x==1) { // sabte nam baraye forushande
            System.out.print("company's name: ");
            String companysName = sc.nextLine();
            // check kardane tekrari nabudane username dar forushande ha
            for (Seller i : Seller.getSellersList())
                if (userName.equals(i.getUserName())) {
                    System.out.print("please enter another username:");
                    userName = sc.nextLine();
                }
            // sakhtane yek forushandeye jadid va gharar dadan dar liste darkhast haye modir
            Seller newSeller = new Seller(userName, firstName, lastName, email, phoneNumber, password, companysName);
            Admin.getSellersJoinRequests().add(newSeller);
        }else if(x==2) { // sabte nam baraye moshtari
            // check kardane tekrari nabudane username dar moshtari ha
            for (Buyer i : Buyer.getBuyersList())
                if (userName.equals(i.getUserName())) {
                    System.out.print("please enter another username:");
                    userName = sc.nextLine();
                }
            // sakhtane yek moshtarie jadid va gharar dadan dar liste moshtari ha
            Buyer newBuyer = new Buyer(userName, firstName, lastName, email, phoneNumber, password);
            Buyer.getBuyersList().add(newBuyer);
        }
        System.out.println("registered successfully.you have to log in in order to use your account.");
    } // sabte nam
    static void logIn(){
        // gereftane etelaat
        System.out.println("What is your role?");
        System.out.println("1-Seller     2-Buyer     3-Admin");
        int x = sc.nextInt();
        sc.nextLine();
        System.out.println("Please fill the following information.");
        System.out.print("username: ");
        String userName = sc.nextLine();
        System.out.print("password: ");
        String password = sc.nextLine();
        // jostojo baraye account va vorud
        if(x==1)  //vorud be accounte forushande
            for (Seller i : Seller.getSellersList()) {
                System.out.println("hi");
                if (userName.equals(i.getUserName()) && password.equals(i.getPassword())) {
                    System.out.println("bye");
                    Menu.loggedIn = true;
                    loggedBuyer = null;
                    loggedSeller = i;
                    System.out.println("logged in successfully.");
                    System.out.println(i.toString());
                    sellerCommands.showSellersCommands();
                } else
                    System.out.println("user doesn't exist!");
            }


        else if(x==2)  //vorud be accounte kharidar
            for (Buyer i : Buyer.getBuyersList()) {
                if (userName.equals(i.getUserName()) && password.equals(i.getPassword())) {
                    Menu.loggedIn = true;
                    loggedSeller = null;
                    loggedBuyer = i;
                    System.out.println("logged in successfully.");
                    System.out.println(i.toString());
                } else
                    System.out.println("user doesn't exist!");
            }

        else if(x==3) { //vorud be accounte modir
            Menu.loggedIn=true;
            loggedSeller = null;
            loggedBuyer = null;
            System.out.println("logged in successfully.");
            System.out.println(Admin.getAdmin().toString());
            adminCommands.showAdminsCommands();
        }
    }// vorud be account

    static void changePersonalInfo(Account account){
        System.out.print("username: ");
        String userName = sc.nextLine();
        if(loggedBuyer == null)
            for (Seller i : Seller.getSellersList())
                if (userName.equals(i.getUserName())) {
                    System.out.print("please enter another username:");
                    userName = sc.nextLine();
                }
                else if(loggedSeller == null)
                    for (Buyer j : Buyer.getBuyersList())
                        if (userName.equals(j.getUserName())) {
                            System.out.print("please enter another username:");
                            userName = sc.nextLine();
                        }
        account.setUserName(userName);
        System.out.print("first name: ");
        String firstName = sc.nextLine();
        account.setFirstName(firstName);
        System.out.print("last name: ");
        String lastName = sc.nextLine();
        account.setLastName(lastName);
        System.out.print("email: ");
        String email = sc.nextLine();
        account.setEmail(email);
        System.out.print("phone number: ");
        String phoneNumber = sc.nextLine();
        account.setPhoneNumber(phoneNumber);
        System.out.print("password: ");
        String password = sc.nextLine();
        account.setPassword(password);
    } // taghire etelaate moshtarake se naghsh
}

abstract class productsGroupingCommands{
    static void digitalProductsMenu() {
        System.out.println("1-mobile   2-laptop   3-showAll");
        int y = sc.nextInt();
        sc.nextLine();
        switch (y) {
            case 1:
                for (Product i : Category.mobilesList.getProductsList())
                    System.out.println((i.toString()));
                detailedMenu(Category.mobilesList.getProductsList());
                break;
            case 2:
                for (Product i : Category.laptopsList.getProductsList())
                    System.out.println((i.toString()));
                detailedMenu(Category.laptopsList.getProductsList());
                break;
            case 3:
                for (Product i : Category.digitalProductList.getProductsList())
                    System.out.println((i.toString()));
                detailedMenu(Category.digitalProductList.getProductsList());
                break;
        }
    } // menuye kalahaye digital
    static void clothesMenu() {
        System.out.println("1-dress   2-shoes   3-showAll");
        int y = sc.nextInt();
        sc.nextLine();
        switch (y) {
            case 1:
                for (Product i : Category.dressList.getProductsList())
                    System.out.println((i.toString()));
                detailedMenu(Category.dressList.getProductsList());
                break;
            case 2:
                for (Product i : Category.shoesList.getProductsList())
                    System.out.println((i.toString()));
                detailedMenu(Category.shoesList.getProductsList());
                break;
            case 3:
                for (Product i : Category.clothesList.getProductsList())
                    System.out.println((i.toString()));
                detailedMenu(Category.clothesList.getProductsList());
                break;
        }
    } // menuye lebas ha
    static void homeApplianceMenu(){
        System.out.println("1-TV   2-refrigerator   3-stove   4-showAll");
        int y = sc.nextInt();
        sc.nextLine();
        switch (y) {
            case 1:
                for(Product i:Category.tvsList.getProductsList())
                    System.out.println((i.toString()));
                detailedMenu(Category.tvsList.getProductsList());
                break;
            case 2:
                for(Product i:Category.refrigeratorsList.getProductsList())
                    System.out.println((i.toString()));
                detailedMenu(Category.refrigeratorsList.getProductsList());
                break;
            case 3:
                for(Product i:Category.stovesList.getProductsList())
                    System.out.println((i.toString()));
                detailedMenu(Category.stovesList.getProductsList());
                break;
            case 4:
                for(Product i:Category.homeApplianceList.getProductsList())
                    System.out.println((i.toString()));
                detailedMenu(Category.homeApplianceList.getProductsList());
                break;
        }
    } // menuye kalahaye khanegi
    static void foodsMenu(){
        for(Product i:Category.foodsList.getProductsList())
            System.out.println((i.toString()));
        detailedMenu(Category.foodsList.getProductsList());
    } // menuye ghazaha
    static void detailedMenu(ArrayList<Product> list){
        System.out.println("reach the product through searching in order to buy.");
        System.out.println("1-search   2-sort");
        int x = sc.nextInt();
        sc.nextLine();
        if(x==1)
            search(list);
        else if(x==2) {
            System.out.println("1-by score   2-by price");
            int y = sc.nextInt();
            sc.nextLine();
            if (y == 1)
                scoreSort(list);
            else if (y == 2)
                priceSort(list);
            System.out.println("reach the product through searching in order to buy.");
            System.out.println("1-search   2-remove filters   3-home");
            int z = sc.nextInt();
            sc.nextLine();
            switch (z) {
                case 1:
                    search(list);
                    break;
                case 2:
                    buyerCommands.showProducts();
                    break;
                case 3:
                    break;
            }
        }
    } // kharid be vasileye search va sort
    static void search(ArrayList<Product> list){
        System.out.println("please fill the following information.");
        System.out.println("product ID:");
        int productID = sc.nextInt();
        sc.nextLine();
        for(Product i:list)
            if(productID == i.getProductID()) {
                System.out.println(i.toString());
                System.out.println("1-add to shopping basket   2-comment   3-home");
                int x = sc.nextInt();
                sc.nextLine();
                switch (x){
                    case 1:
                        if (Commands.loggedBuyer == null)
                            System.out.println("you have to log in to your buyer account first");
                        else {
                            Commands.loggedBuyer.getShoppingBasket().add(i);
                            System.out.println("added successfully.");
                        }
                        break;
                    case 2:
                        buyerCommands.comment(i);
                        break;
                    case 3:
                        break;
                }
            }
    } // jostojo dar daste haye mokhtalefe kala
    static void scoreSort(ArrayList<Product> list){
        ArrayList<Product> tempScoresList = list;
        for(int i = tempScoresList.size()-1; i>0; i--)
            for(int j=0; j<i; j++)
                if(tempScoresList.get(j).getAverageScore()>tempScoresList.get(j+1).getAverageScore())
                    Collections.swap(tempScoresList, j, j+1);
        for (Product i : tempScoresList)
            System.out.println((i.toString()));
    } // bubble sort bar asase emtiaz
    static void priceSort(ArrayList<Product> list){
        ArrayList<Product> tempPricesList = list;
        for(int i = tempPricesList.size()-1; i>0; i--)
            for(int j=0; j<i; j++)
                if(tempPricesList.get(j).getPrice()>tempPricesList.get(j+1).getPrice())
                    Collections.swap(tempPricesList, j, j+1);
        for (Product i : tempPricesList)
            System.out.println((i.toString()));
    } // bubble sort bar asase gheymat
    static void filter(){
        System.out.println("What product are you looking for?");
        System.out.println("1-digital products   2-clothes   3-home appliances   4-food");
        int x = sc.nextInt();
        sc.nextLine();
        if(x==1)
            productsGroupingCommands.digitalProductsMenu();
        else if(x==2)
            productsGroupingCommands.clothesMenu();
        else if(x==3)
            productsGroupingCommands.homeApplianceMenu();
        else if(x==4)
            productsGroupingCommands.foodsMenu();
    } // filtere daste ha
}
abstract class buyerCommands{
    static void showBuyersCommands(){
        System.out.println("1-show products   2-filter");
        System.out.println("3-show shopping basket    4-score");
        System.out.println("5-log out");
        int x = sc.nextInt();
        sc.nextLine();
        switch (x){
            case 1:
                showProducts();
                break;
            case 2:
                productsGroupingCommands.filter();
                break;
            case 3:
                showShoppingBasket();
                break;
            case 4:
                score();
                break;
            case 5:
                Commands.loggedBuyer = null;
                Menu.loggedIn = false;
                break;
        }
    } // neshan dadan va ejraye dastorate moshtari
    static void showProducts(){
        int number = 1;
        for(Product i : Product.getProductsList()){
            System.out.println(number + "-" + i.toString());
            number++;
        }
        System.out.println("enter the number of the product in order to comment.");
        int y = sc.nextInt();
        sc.nextLine();
        System.out.println(Product.getProductsList().get(y-1).toString());
        System.out.println("1-comment   2-home");
        int z = sc.nextInt();
        sc.nextLine();
        if(z==1)
            comment(Product.getProductsList().get(y - 1));
    } // neshan dadane hameye kalaha va comment gereftan
    static void showShoppingBasket(){
        System.out.println("items:");
        for(Product i : Commands.loggedBuyer.getShoppingBasket())
            System.out.println(i.toString());
        System.out.println("finished?");
        System.out.println("1-yes     2-no");
        int x = sc.nextInt();
        sc.nextLine();
        if(x==1)
            shipping();
    } // neshan dadane sabade kharid
    static void shipping(){
        System.out.println("please fill the following information.");
        System.out.println("address:");
        String x = sc.nextLine();
        System.out.println("postal code:");
        String y = sc.nextLine();
        System.out.println("phone number");
        String phoneNumber = sc.nextLine();
        String z = sc.nextLine();
        int wholePrice =0;
        for(Product i : Commands.loggedBuyer.getShoppingBasket())
            wholePrice += i.getPrice();
        if (wholePrice < Commands.loggedBuyer.getCredit())
            System.out.println("low credit");
        else{
            System.out.println("please fill the following information.");
            System.out.println("factor ID:");
            int factorID = sc.nextInt();
            sc.nextLine();
            System.out.println("date:");
            String date = sc.nextLine();
            System.out.println("is it received?");
            System.out.println("1-yes     2-no");
            int t = sc.nextInt();
            sc.nextLine();
            boolean isReceived = false;
            if(t==1)
                isReceived = true;
            else if(t==2)
                isReceived=false;
            // dorost kardane factor va add kardan be sabegheye moshtari va forushande
            BuyingFactor tempBuyingFactor = new BuyingFactor(factorID, date, wholePrice, Commands.loggedBuyer.getShoppingBasket(),
                    isReceived);
            SellingFactor tempSellingFactor = new SellingFactor(factorID, date, wholePrice, Commands.loggedBuyer.getShoppingBasket(),
                    Commands.loggedBuyer.getFirstName(),isReceived);
            Commands.loggedBuyer.getBuyingFactors().add(tempBuyingFactor);
            Commands.loggedSeller.getSellingFactors().add(tempSellingFactor);
            Commands.loggedBuyer.setCredit(Commands.loggedBuyer.getCredit() - wholePrice);
            System.out.println("shipping done");
        }
    } // gereftane etelaat va ersale kharid
    static void score(){
        int factorNumber = 1;
        System.out.println("which item do you want to score?");
        for(BuyingFactor i:Commands.loggedBuyer.getBuyingFactors()) {
            int itemNumber = 1;
            for (Product j : i.getBoughtProducts()) {
                int number = factorNumber*10+itemNumber;
                System.out.println(number + "-" + j.toString());
                itemNumber++;
            }
            factorNumber++;
        }
        // dahgane adade mahsol shomareye factore an va yekan shomareye item dar factor ast
        int x = sc.nextInt();
        sc.nextLine();
        System.out.print("score: ");
        int score = sc.nextInt();
        sc.nextLine();
        Score newScore = new Score(Commands.loggedBuyer, score, Commands.loggedBuyer.getBuyingFactors().get(x/10).getBoughtProducts().get(x%10));
        Commands.loggedBuyer.getBuyingFactors().get(x/10).getBoughtProducts().get(x%10).getScoresList().add(newScore);
        // miangin gereftan az hameye score ha
        int scoresSum = 0;
        for(Score i:Commands.loggedBuyer.getBuyingFactors().get(x/10).getBoughtProducts().get(x%10).getScoresList())
            scoresSum += i.getScore();
        int averageScore = scoresSum/Commands.loggedBuyer.getBuyingFactors().get(x/10).getBoughtProducts().get(x%10).getScoresList().size();
        Commands.loggedBuyer.getBuyingFactors().get(x/10).getBoughtProducts().get(x%10).setAverageScore(averageScore);
        System.out.println("the average score was updated");
    } // emtiaz dadan
    static void comment(Product commentedProduct){
        System.out.println("please fill the following information.");
        System.out.print("username: ");
        String username = sc.nextLine();
        System.out.print("comment: ");
        String comment = sc.nextLine();
        Buyer commentingUser = null;
        for(Buyer i:Buyer.getBuyersList())
            if(i.getUserName().equals(username)) {
                commentingUser = i;
                break;
            }
        Boolean haveBought = false;
        for(BuyingFactor i:commentingUser.getBuyingFactors())
            for(Product j: i.getBoughtProducts())
                if(commentedProduct.getProductID() == j.getProductID())
                    haveBought = true;
        Comment newComment = new Comment(commentingUser, commentedProduct, comment, haveBought);
        Admin.getCommentsRequests().add(newComment);
        System.out.println("the request was sent to the admin.");
    } // comment gozashtan
}
abstract class sellerCommands{
    static void showSellersCommands(){
        // printe ekhtiarat va gereftane shomareye dastor
        System.out.println("1-add product request     2-remove product request");
        System.out.println("3-edit product request     4-change personal info");
        System.out.println("5-sell     6-log out");
        int x = sc.nextInt();
        sc.nextLine();
        // ejraye dastor bar asase shomare
        switch(x) {
            case 1:
                addProductRequest();
                break;
            case 2:
                removeProductRequest();
                break;
            case 3:
                editProductRequest();
                break;
            case 4:
                changeSellersInfo(Commands.loggedSeller);
                break;
            case 5:
                sell();
                break;
            case 6:
                Commands.loggedSeller = null;
                Menu.loggedIn = false;
                break;
        }
    } // neshan dadan va ejraye dastorate forushande
    static void removeProductRequest() {
        while(true) {
            System.out.println("which product are you going to remove?");
            for (Product i : Commands.loggedSeller.getOnSaleProducts()) {
                int number = 1;
                System.out.println(i + "-" + i.getName());
                number++;
            }
            int x = sc.nextInt();
            sc.nextLine();
            if (x == 0)
                break;
            System.out.println(Commands.loggedSeller.getOnSaleProducts().get(x - 1).toString());
            Admin.getProductsRemovingRequests().add(Commands.loggedSeller.getOnSaleProducts().get(x - 1));
            System.out.println("the request was sent.");
            System.out.println("type the number 0 when you're done.");
        }

    } // darkhaste hazf kardane kala
    static void addProductRequest() {
        System.out.println("What product are you going to add?");
        System.out.println("1-mobile  2-laptop  3-dress  4-shoes  5-tv  6-refrigerator  7-stove  8-food");
        int x = sc.nextInt();
        sc.nextLine();
        askProductsInfo(x ,1);
        System.out.println("the request was sent.");
    } // darkhaste ezafe kardane kala
    static void editProductRequest() {
        System.out.println("which product are you going to edit?");
        for(Product i:Commands.loggedSeller.getOnSaleProducts()){
            int number=1;
            System.out.println(i+"-"+i.getName());
            number++;
        }
        int x = sc.nextInt();
        sc.nextLine();
        System.out.println(Commands.loggedSeller.getOnSaleProducts().get(x-1));
        // gereftane etelaate jadide mahsol bar asase noe mahsol
        if(Commands.loggedSeller.getOnSaleProducts().get(x-1) instanceof Mobile)
            askProductsInfo(1, 0);
        else if(Commands.loggedSeller.getOnSaleProducts().get(x-1) instanceof Laptop)
            askProductsInfo(2, 0);
        else if(Commands.loggedSeller.getOnSaleProducts().get(x-1) instanceof Dress)
            askProductsInfo(3, 0);
        else if(Commands.loggedSeller.getOnSaleProducts().get(x-1) instanceof Shoes)
            askProductsInfo(4, 0);
        else if(Commands.loggedSeller.getOnSaleProducts().get(x-1) instanceof TV)
            askProductsInfo(5, 0);
        else if(Commands.loggedSeller.getOnSaleProducts().get(x-1) instanceof Refrigerator)
            askProductsInfo(6, 0);
        else if(Commands.loggedSeller.getOnSaleProducts().get(x-1) instanceof Stove)
            askProductsInfo(7, 0);
        else if(Commands.loggedSeller.getOnSaleProducts().get(x-1) instanceof Food)
            askProductsInfo(8, 0);
        System.out.println("the request was sent.");
    } // darkhaste edit kardane kala
    static void askProductsInfo(int x, int y){
        int productID = 0;
        if(y==1) {
            System.out.print("product ID: ");
            productID = sc.nextInt();
            sc.nextLine();
            for (Product i : Product.getProductsList())
                if (productID == i.getProductID()) {
                    System.out.print("please enter another ID:");
                    productID = sc.nextInt();
                }
        } // taghire username hengame edit emkan pazir nist
        System.out.print("name: ");
        String name = sc.nextLine();
        System.out.print("brand: ");
        String brand = sc.nextLine();
        System.out.print("price: ");
        int price = sc.nextInt();
        sc.nextLine();
        System.out.print("description: ");
        String description = sc.nextLine();

        if(x==1 || x==2){ // gereftane etelaate kalaye digital
            System.out.print("memory capacity: ");
            int memoryCapacity = sc.nextInt();
            sc.nextLine();
            System.out.print("ram: ");
            int ram = sc.nextInt();
            sc.nextLine();
            System.out.print("operating system: ");
            String operatingSystem = sc.nextLine();
            System.out.print("weight: ");
            int weight = sc.nextInt();
            sc.nextLine();
            System.out.print("dimensions: ");
            String dimensions = sc.nextLine();

            if(x==1) { // gereftane etelaate mobile
                System.out.print("simcards number: ");
                int simcardsNumber = sc.nextInt();
                sc.nextLine();
                System.out.print("camera resolution: ");
                int cameraResolution = sc.nextInt();
                sc.nextLine();
                Mobile newMobile = new Mobile(productID, name, brand, price, Commands.loggedSeller, description,
                        memoryCapacity, ram, operatingSystem, weight, dimensions, simcardsNumber, cameraResolution);
                if(y==0)  // add kardane mobile be darkhasti haye edite kalaye modir
                    Admin.getProductsEditRequests().add(newMobile);
                else if(y == 1) // add kardane mobile be darkhasti haye ezafe kardane kalaye modir
                    Admin.getProductsAddingRequests().add(newMobile);
            } else if(x==2){ // gereftane etelaate laptop
                System.out.print("cpu type: ");
                String cpuType = sc.nextLine();
                System.out.println("is it for gaming?");
                System.out.println("1-yes     2-no");
                int z = sc.nextInt();
                sc.nextLine();
                Boolean gamingLaptop = null;
                if(z==1) // sakhtane laptop ba ghabeliate gaming
                    gamingLaptop = true;
                else if(z==2) // sakhtane laptop bedone ghabeliate gaming
                    gamingLaptop = false;

                Laptop newLaptop = new Laptop(productID, name, brand, price, Commands.loggedSeller, description,
                        memoryCapacity, ram, operatingSystem, weight, dimensions, cpuType, gamingLaptop);
                if(y==0)  // add kardane laptop be darkhasti haye edite kalaye modir
                    Admin.getProductsEditRequests().add(newLaptop);
                else if(y == 1) // add kardane laptop be darkhasti haye ezafe kardane kalaye modir
                    Admin.getProductsAddingRequests().add(newLaptop);
            }
        }
        else if(x==3 || x==4){ // gereftane etelaate poshak
            System.out.print("producer country: ");
            String producerCountry = sc.nextLine();
            System.out.print("material: ");
            String material = sc.nextLine();
            if(x==3){ // gereftane etelaate lebas
                System.out.print("what type of dress is it?(write in capital) ");
                String clothesType = sc.nextLine();
                System.out.println("size: ");
                int size = sc.nextInt();
                sc.nextLine();

                Dress newDress = new Dress(productID, name, brand, price, Commands.loggedSeller, description, producerCountry,
                        material, Dress.type.valueOf(clothesType), size);
                if(y==0)  // add kardane lebas be darkhasti haye edite kalaye modir
                    Admin.getProductsEditRequests().add(newDress);
                else if(y == 1) // add kardane lebas be darkhasti haye ezafe kardane kalaye modir
                    Admin.getProductsAddingRequests().add(newDress);
            }else if(x==4){ // gereftane etelaate kafsh
                System.out.print("what type of shoes is it?(write in capital) ");
                String shoesType = sc.nextLine();
                System.out.println("size: ");
                int size = sc.nextInt();
                sc.nextLine();

                Shoes newShoes = new Shoes(productID, name, brand, price, Commands.loggedSeller, description, producerCountry,
                        material, Shoes.type.valueOf(shoesType), size);
                if(y==0)  // add kardane kafsh be darkhasti haye edite kalaye modir
                    Admin.getProductsEditRequests().add(newShoes);
                else if(y == 1) // add kardane lebas be darkhasti haye ezafe kardane kalaye modir
                    Admin.getProductsAddingRequests().add(newShoes);
            }
        }
        else if(x==5 || x==6 || x==7){ // gereftane etelaate kalaye khanegi
            System.out.print("energy consumption degree: ");
            int energyConsumptionDegree = sc.nextInt();
            sc.nextLine();
            System.out.println("does it have guarantee?");
            System.out.println("1-yes     2-no");
            int t = sc.nextInt();
            sc.nextLine();
            boolean tempGuarantee = false;
            if (t == 1)
                tempGuarantee = true;
            else if(t==2)
                tempGuarantee = false;

            if(x==5){ // gereftane etelaate television
                System.out.print("resolution: ");
                int resolution = sc.nextInt();
                sc.nextLine();
                System.out.print("screen size: ");
                int screenSize = sc.nextInt();
                sc.nextLine();
                TV newTV = new TV(productID, name, brand, price, Commands.loggedSeller, description, energyConsumptionDegree,
                        tempGuarantee, resolution, screenSize);
                if(y==0)  // add kardane television be darkhasti haye edite kalaye modir
                    Admin.getProductsEditRequests().add(newTV);

                else if(y == 1) // add kardane television be darkhasti haye ezafe kardane kalaye modir
                    Admin.getProductsAddingRequests().add(newTV);
            } else if (x == 6) { // gereftane etelaate yakhchal
                System.out.print("capacity: ");
                int capacity = sc.nextInt();
                sc.nextLine();
                System.out.print("type: ");
                String type = sc.nextLine();
                System.out.println("does it have fridge?");
                System.out.println("1-yes     2-no");
                int z = sc.nextInt();
                sc.nextLine();

                Boolean fridge = false;
                if(z==1)
                    fridge = true;
                else if(z==2)
                    fridge=false;

                Refrigerator newRefrigerator = new Refrigerator(productID, name, brand, price, Commands.loggedSeller,
                        description, energyConsumptionDegree, tempGuarantee, capacity, type, fridge);
                if(y==0)  // add kardane yakhchal be darkhasti haye edite kalaye modir
                    Admin.getProductsEditRequests().add(newRefrigerator);
                else if(y == 1) // add kardane television be darkhasti haye ezafe kardane kalaye modir
                    Admin.getProductsAddingRequests().add(newRefrigerator);

            }else if(x == 7){ // gereftane etelaate gaz
                System.out.print("burners number: ");
                int burnersNumber = sc.nextInt();
                sc.nextLine();
                System.out.print("material: ");
                String material = sc.nextLine();
                System.out.println("does it have oven?");
                System.out.println("1-yes     2-no");
                int z = sc.nextInt();
                sc.nextLine();
                Boolean oven = false;
                if(z==1)
                    oven=true;
                else if(z==2)
                    oven=false;

                Stove newStove = new Stove(productID, name, brand, price, Commands.loggedSeller, description,
                        energyConsumptionDegree, tempGuarantee, burnersNumber, material, oven);
                if(y==0) // add kardane gaz be darkhasti haye edite kalaye modir
                    Admin.getProductsEditRequests().add(newStove);
                else if(y == 1) // add kardane gaz be darkhasti haye ezafe kardane kalaye modir
                    Admin.getProductsAddingRequests().add(newStove);
            }
        }
        else if(x==8){ // gereftane etelaate ghaza
            System.out.print("production date: ");
            String productionDate = sc.nextLine();
            System.out.print("expiration date: ");
            String expirationDate = sc.nextLine();
            Food newFood = new Food(productID, name, brand, price, Commands.loggedSeller, description, productionDate, expirationDate);
            if(y==0)  // add kardane ghaza be darkhasti haye edite kalaye modir
                Admin.getProductsEditRequests().add(newFood);
            else if(y == 1) // add kardane ghaza be darkhasti haye ezafe kardane kalaye modir
                Admin.getProductsAddingRequests().add(newFood);
        }
    } // gereftane etelaate mahsol
    static void changeSellersInfo(Seller seller) {
        Commands.changePersonalInfo(seller);
        System.out.print("company's name: ");
        String companysName = sc.nextLine();
        seller.setCompanysName(companysName);
    } // taghire etelaate forushande
    static void sell(){
        for(Product i : Commands.loggedSeller.getOnSaleProducts())
            System.out.println(i.toString());
    } // forushe kala
}
abstract class adminCommands {
    static void showAdminsCommands() {
        // printe ekhtiarat va gereftane shomareye dastor
        System.out.println("1-manage sellers join requests   2-remove user  ");
        System.out.println("3-manage products add requests   4-manage products remove requests");
        System.out.println("5-manage products edit requests   6-manage comments");
        System.out.println("7-show users   8-change personal info   9-log out ");
        int x = sc.nextInt();
        sc.nextLine();
        // ejraye dastor bar asase shomare
        switch (x) {
            case 1:
                manageSellersJoinRequests();
                break;
            case 2:
                removeUser();
                break;
            case 3:
                manageProductAddRequests();
                break;
            case 4:
                manageProductRemoveRequests();
                break;
            case 5:
                manageProductEditRequests();
                break;
            case 6:
                manageComments();
                break;
            case 7:
                showUsers();
                break;
            case 8:
                Commands.changePersonalInfo(Admin.getAdmin());
                break;
            case 9:
                Menu.loggedIn = false;
                break;
        }
    } // neshan dadan va ejraye dastorate modir

    static void manageSellersJoinRequests() {
        for (Seller i : Admin.getSellersJoinRequests()) {
            System.out.println("do you want to let this seller join?");
            System.out.println(i.toString());
            System.out.println("1-yes     2-no");
            int x = sc.nextInt();
            sc.nextLine();
            if (x == 1)  // ezafe kardane forushande
                Seller.getSellersList().add(i);
        }
        Admin.getSellersJoinRequests().clear();
    } // residegi be darkhast haye sabte name forushande

    static void manageComments() {
        for (Comment i : Admin.getCommentsRequests()) {
            System.out.println("do you want to approve this comment?");
            System.out.println(i.toString());
            System.out.println("1-yes   2-no");
            int x = sc.nextInt();
            sc.nextLine();
            if (x == 1) {
                i.setCommentStatus(Comment.commentStatus.APPROVED);
                i.getCommentedProduct().getCommentsList().add(i);
            } else if (x == 2)
                i.setCommentStatus(Comment.commentStatus.UNAPPROVED);
        }
        Admin.getCommentsRequests().clear();
    } // residegi be comment ha

    static void manageProductAddRequests() {
        for (Product i : Admin.getProductsAddingRequests()) {
            System.out.println("do you want to let this product join?");
            System.out.println(i.toString());
            System.out.println("1-yes     2-no");
            int x = sc.nextInt();
            sc.nextLine();
            if (x == 1) { // ezafe kardane kala
                i.getSeller().getOnSaleProducts().add(i);
                Product.getProductsList().add(i);
                putInRightCategory(i);
            }
        }
        Admin.getProductsAddingRequests().clear();
    } // residegi be darkhast haye ezafe kardane mahsol

    static void manageProductRemoveRequests() {
        for (Product i : Admin.getProductsRemovingRequests()) {
            System.out.println("do you want to let this product be removed?");
            System.out.println(i.toString());
            System.out.println("1-yes     2-no");
            int x = sc.nextInt();
            if (x == 1) { // hazfe kala
                i.getSeller().getOnSaleProducts().remove(i);
                Product.getProductsList().remove(i);
                removeFromCategory(i);
            }
        }
        Admin.getProductsRemovingRequests().clear();
    } // residegi be darkhast haye hazfe mahsol

    static void manageProductEditRequests() {
        Product tempOldProduct = null;
        Product tempNewProduct = null;
        int x = 0;
        for (Product i : Admin.getProductsEditRequests()) {
            System.out.println("do you want to let this product be edited?");
            for (Product j : i.getSeller().getOnSaleProducts()) // printe etelaate kala ghabl va bade edit
                if (i.getProductID() == j.getProductID()) {
                    System.out.println("before:");
                    System.out.println(j.toString());
                    System.out.println("after:");
                    System.out.println(i.toString());
                    tempOldProduct = j;
                }
            System.out.println("1-yes     2-no");
            x = sc.nextInt();
            sc.nextLine();
            if (x == 1) {// hazfe kalaye ghable edit va ezafe kardane kalaye jadid
                tempNewProduct = i;
                Product.getProductsList().remove(tempOldProduct);
                Product.getProductsList().add(tempNewProduct);
                tempNewProduct.getSeller().getOnSaleProducts().remove(tempOldProduct);
                tempNewProduct.getSeller().getOnSaleProducts().add(tempNewProduct);
                removeFromCategory(tempOldProduct);
                putInRightCategory(tempNewProduct);
            }
        }
        Admin.getProductsEditRequests().clear();
    }// residegi be darkhast haye edite mahsol

    static void putInRightCategory(Product product) {
        if (product instanceof Mobile) {
            Category.mobilesList.getProductsList().add(product);
            Category.digitalProductList.getProductsList().add(product);
        } else if (product instanceof Laptop) {
            Category.laptopsList.getProductsList().add(product);
            Category.digitalProductList.getProductsList().add(product);
        } else if (product instanceof Dress) {
            Category.dressList.getProductsList().add(product);
            Category.clothesList.getProductsList().add(product);
        } else if (product instanceof Shoes) {
            Category.shoesList.getProductsList().add(product);
            Category.clothesList.getProductsList().add(product);
        } else if (product instanceof TV) {
            Category.tvsList.getProductsList().add(product);
            Category.homeApplianceList.getProductsList().add(product);
        } else if (product instanceof Refrigerator) {
            Category.refrigeratorsList.getProductsList().add(product);
            Category.homeApplianceList.getProductsList().add(product);
        } else if (product instanceof Stove) {
            Category.stovesList.getProductsList().add(product);
            Category.homeApplianceList.getProductsList().add(product);
        } else if (product instanceof Food)
            Category.foodsList.getProductsList().add(product);
    } // gharar dadane kala dar daste haye mortabet

    static void removeFromCategory(Product product) {
        if (product instanceof Mobile) {
            Category.mobilesList.getProductsList().remove(product);
            Category.digitalProductList.getProductsList().remove(product);
        } else if (product instanceof Laptop) {
            Category.laptopsList.getProductsList().remove(product);
            Category.digitalProductList.getProductsList().remove(product);
        } else if (product instanceof Dress) {
            Category.dressList.getProductsList().remove(product);
            Category.clothesList.getProductsList().remove(product);
        } else if (product instanceof Shoes) {
            Category.shoesList.getProductsList().remove(product);
            Category.clothesList.getProductsList().remove(product);
        } else if (product instanceof TV) {
            Category.tvsList.getProductsList().remove(product);
            Category.homeApplianceList.getProductsList().remove(product);
        } else if (product instanceof Refrigerator) {
            Category.refrigeratorsList.getProductsList().remove(product);
            Category.homeApplianceList.getProductsList().remove(product);
        } else if (product instanceof Stove) {
            Category.stovesList.getProductsList().remove(product);
            Category.homeApplianceList.getProductsList().remove(product);
        } else if (product instanceof Food)
            Category.foodsList.getProductsList().remove(product);
    } // remove kardane kala az daste bad az hazf

    static void removeUser() {
        showUsers();
        System.out.println("enter the role of the user you want to remove.");
        System.out.println("1-Seller    2-Buyer");
        int x = sc.nextInt();
        sc.nextLine();
        System.out.println("enter the username of the user you want to remove.");
        String username = sc.nextLine();
        Seller tempSeller = null;
        Buyer tempBuyer = null;
        if (x == 1) {
            for (Seller i : Seller.getSellersList())
                if (username.equals(i.getUserName()))
                    tempSeller = i;
            Seller.getSellersList().remove(tempSeller);
            System.out.println("removed successfully.");
        } else if (x == 2) {
            for (Buyer i : Buyer.getBuyersList())
                if (username.equals(i.getUserName()))
                    tempBuyer = i;
            Buyer.getBuyersList().remove(tempBuyer);
            System.out.println("removed successfully.");
        }
    } // ekhraje karbar

    static void showUsers() {
        System.out.println("Sellers:");
        for (Seller i : Seller.getSellersList())
            System.out.println(i.toString());

        System.out.println("Buyers:");
        for (Buyer i : Buyer.getBuyersList())
            System.out.println(i.toString());
    } // neshan dadane karbaran
}


abstract class Account{
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;

    public Account(String userName, String firstName, String lastName, String email, String phoneNumber, String password, String role) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", role='" + role + '\'' ;
    }
}
class Buyer extends Account{
    private ArrayList<Product> shoppingBasket;
    private ArrayList<BuyingFactor> buyingFactors;
    private int credit;
    private static ArrayList<Buyer> buyersList = new ArrayList<Buyer>();;

    public Buyer(String userName, String firstName, String lastName, String email, String phoneNumber, String password) {
        super(userName, firstName, lastName, email, phoneNumber, password, "Buyer");
        this.shoppingBasket = new ArrayList<Product>();
        this.buyingFactors = new ArrayList<BuyingFactor>();
        this.credit = 0;
    }

    public ArrayList<Product> getShoppingBasket() {
        return shoppingBasket;
    }

    public void setShoppingBasket(ArrayList<Product> shoppingBasket) {
        this.shoppingBasket = shoppingBasket;
    }

    public ArrayList<BuyingFactor> getBuyingFactors() {
        return buyingFactors;
    }

    public void setBuyingFactors(ArrayList<BuyingFactor> buyingFactors) {
        this.buyingFactors = buyingFactors;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public static ArrayList<Buyer> getBuyersList() {
        return buyersList;
    }

    public void setBuyersList(ArrayList<Buyer> buyersList) {
        this.buyersList = buyersList;
    }

    @Override
    public String toString() {
        return super.toString()+
                "shoppingBasket=" + shoppingBasket +
                ", buyingFactors=" + buyingFactors +
                ", credit=" + credit ;
    }
}
class Seller extends Account{
    private String companysName;
    private ArrayList<SellingFactor> sellingFactors;
    private int credit;
    private ArrayList<Product> onSaleProducts;
    private static ArrayList<Seller> sellersList = new ArrayList<Seller>();

    public Seller(String userName, String firstName, String lastName, String email, String phoneNumber, String password,
                  String companysName) {
        super(userName, firstName, lastName, email, phoneNumber, password, "Seller");
        this.companysName = companysName;
        this.sellingFactors = new ArrayList<SellingFactor>();
        this.credit = 0;
        this.onSaleProducts = new ArrayList<Product>();
    }

    public static ArrayList<Seller> getSellersList() {
        return sellersList;
    }

    public static void setSellersList(ArrayList<Seller> sellersList) {
        Seller.sellersList = sellersList;
    }

    public String getCompanysName() {
        return companysName;
    }

    public void setCompanysName(String companysName) {
        this.companysName = companysName;
    }

    public ArrayList<SellingFactor> getSellingFactors() {
        return sellingFactors;
    }

    public void setSellingFactors(ArrayList<SellingFactor> sellingFactors) {
        this.sellingFactors = sellingFactors;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public ArrayList<Product> getOnSaleProducts() {
        return onSaleProducts;
    }

    public void setOnSaleProducts(ArrayList<Product> onSaleProducts) {
        this.onSaleProducts = onSaleProducts;
    }

    @Override
    public String toString() {
        return  super.toString()+
                "companysName='" + companysName + '\'' +
                ", sellingFactors=" + sellingFactors +
                ", credit=" + credit +
                ", onSaleProducts=" + onSaleProducts ;
    }
}
class Admin extends Account{
    private static Admin admin;
    private static ArrayList<Seller> sellersJoinRequests = new ArrayList<Seller>();
    private static ArrayList<Product> productsEditRequests = new ArrayList<Product>();
    private static ArrayList<Product> productsAddingRequests = new ArrayList<Product>();
    private static ArrayList<Product> productsRemovingRequests = new ArrayList<Product>();
    private static ArrayList<Comment> commentsRequests = new ArrayList<Comment>();

    private Admin() {
        super("admin", "admin", "admin", "admin", "admin", "admin", "admin");
    }

    public static Admin getAdmin(){
        if(admin == null)
            admin = new Admin();
        return admin;
    }


    public static void setAdmin(Admin admin) {
        Admin.admin = admin;
    }

    public static ArrayList<Seller> getSellersJoinRequests() {
        return sellersJoinRequests;
    }

    public static void setSellersJoinRequests(ArrayList<Seller> sellersJoinRequests) {
        Admin.sellersJoinRequests = sellersJoinRequests;
    }

    public static ArrayList<Product> getProductsEditRequests() {
        return productsEditRequests;
    }

    public static void setProductsEditRequests(ArrayList<Product> productsEditRequests) {
        Admin.productsEditRequests = productsEditRequests;
    }

    public static ArrayList<Product> getProductsAddingRequests() {
        return productsAddingRequests;
    }

    public static void setProductsAddingRequests(ArrayList<Product> productsAddingRequests) {
        Admin.productsAddingRequests = productsAddingRequests;
    }

    public static ArrayList<Product> getProductsRemovingRequests() {
        return productsRemovingRequests;
    }

    public static void setProductsRemovingRequests(ArrayList<Product> productsRemovingRequests) {
        Admin.productsRemovingRequests = productsRemovingRequests;
    }

    public static ArrayList<Comment> getCommentsRequests() {
        return commentsRequests;
    }

    public static void setCommentsRequests(ArrayList<Comment> commentsRequests) {
        Admin.commentsRequests = commentsRequests;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}


class BuyingFactor{
    private int factorID;
    private String date;
    private int paidPrice;
    private ArrayList<Product> boughtProducts;
    private boolean isReceived;

    public BuyingFactor(int factorID, String date, int paidPrice, ArrayList<Product> boughtProducts, boolean isReceived) {
        this.factorID = factorID;
        this.date = date;
        this.paidPrice = paidPrice;
        this.boughtProducts = boughtProducts;
        this.isReceived = isReceived;
    }

    public int getFactorID() {
        return factorID;
    }

    public void setFactorID(int factorID) {
        this.factorID = factorID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPaidPrice() {
        return paidPrice;
    }

    public void setPaidPrice(int paidPrice) {
        this.paidPrice = paidPrice;
    }

    public ArrayList<Product> getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(ArrayList<Product> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public boolean getIsReceived() {
        return isReceived;
    }

    public void setIsReceived(boolean isReceived) {
        this.isReceived = isReceived;
    }
}
class SellingFactor{
    private int factorID;
    private String date;
    private int receivedPrice;
    private ArrayList<Product> soldProducts;
    private String buyersName;
    private boolean isSent;

    public SellingFactor(int factorID, String date, int receivedPrice, ArrayList<Product> soldProducts, String buyersName, boolean isSent) {
        this.factorID = factorID;
        this.date = date;
        this.receivedPrice = receivedPrice;
        this.soldProducts = soldProducts;
        this.buyersName = buyersName;
        this.isSent = isSent;
    }

    public int getFactorID() {
        return factorID;
    }

    public void setFactorID(int factorID) {
        this.factorID = factorID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getReceivedPrice() {
        return receivedPrice;
    }

    public void setReceivedPrice(int receivedPrice) {
        this.receivedPrice = receivedPrice;
    }

    public ArrayList<Product> getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(ArrayList<Product> soldProducts) {
        this.soldProducts = soldProducts;
    }

    public String getBuyersName() {
        return buyersName;
    }

    public void setBuyersName(String buyersName) {
        this.buyersName = buyersName;
    }

    public boolean getIsSent() {
        return isSent;
    }

    public void setSent(boolean isSent) {
        this.isSent = isSent;
    }
}


class Category{
    private String name;
    private ArrayList<Product> productsList;
    static Category digitalProductList = new Category("digital products list");
    static Category mobilesList = new Category("mobiles list");
    static Category laptopsList = new Category("laptops list");
    static Category clothesList = new Category("Clothes list");
    static Category dressList = new Category("dress list");
    static Category shoesList = new Category("shoes list");
    static Category homeApplianceList = new Category("home appliance list");
    static Category tvsList = new Category("TVs list");
    static Category refrigeratorsList = new Category("refrigerators list");
    static Category stovesList = new Category("stoves list");
    static Category foodsList = new Category("foods list");

    public Category(String name) {
        this.name = name;
        this.productsList = new ArrayList<Product>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<Product> productsList) {
        this.productsList = productsList;
    }
}


abstract class Product{
    private int productID;
    private String name;
    private String brand;
    private int price;
    private Seller seller;
    private boolean availability;
    private String description;
    private int averageScore;
    static ArrayList<Score> scoresList = new ArrayList<>();
    private ArrayList<Comment> commentsList;
    private static ArrayList<Product> productsList = new ArrayList<>();

    public Product(int productID, String name, String brand, int price, Seller seller, String description) {
        this.productID = productID;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.seller = seller;
        this.availability = true;
        this.description = description;
        this.averageScore = 0;
        this.commentsList = new ArrayList<Comment>();
    }

    public static ArrayList<Score> getScoresList() {
        return scoresList;
    }

    public static void setScoresList(ArrayList<Score> scoresList) {
        Product.scoresList = scoresList;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public boolean getAvailability() {
        return availability;
    }

    public void setAvailability(boolean availability) {
        this.availability = availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public ArrayList<Comment> getCommentsList() {
        return commentsList;
    }

    public void setCommentsList(ArrayList<Comment> commentsList) {
        this.commentsList = commentsList;
    }

    public boolean isAvailability() {
        return availability;
    }

    public static ArrayList<Product> getProductsList() {
        return productsList;
    }

    public void setProductsList(ArrayList<Product> productsList) {
        this.productsList = productsList;
    }

    @Override
    public String toString() {
        return "productID=" + productID +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", price=" + price +
                ", seller=" + seller.getFirstName() +
                ", availability=" + availability +
                ", description='" + description + '\'' +
                ", averageScore=" + averageScore +
                ", commentsList=" + commentsList ;
    }
}

abstract class DigitalProduct extends Product{
    private int memoryCapacity;
    private int ram;
    private String operatingSystem;
    private int weight;
    private String dimensions;

    public DigitalProduct(int productID, String name, String brand, int price, Seller seller, String description,
                          int memoryCapacity, int ram, String operatingSystem, int weight, String dimensions) {
        super(productID, name, brand, price, seller, description);
        this.memoryCapacity = memoryCapacity;
        this.ram = ram;
        this.operatingSystem = operatingSystem;
        this.weight = weight;
        this.dimensions = dimensions;
    }

    public int getMemoryCapacity() {
        return memoryCapacity;
    }

    public void setMemoryCapacity(int memoryCapacity) {
        this.memoryCapacity = memoryCapacity;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "memoryCapacity=" + memoryCapacity +
                ", ram=" + ram +
                ", operatingSystem='" + operatingSystem + '\'' +
                ", weight=" + weight +
                ", dimensions='" + dimensions ;
    }
}
class Mobile extends DigitalProduct{
    private int simcardsNumber;
    private int cameraResolution;

    public Mobile(int productID, String name, String brand, int price, Seller seller, String description,
                  int memoryCapacity, int ram, String operatingSystem, int weight, String dimensions,
                  int simcardsNumber, int cameraResolution) {
        super(productID, name, brand, price, seller, description, memoryCapacity, ram, operatingSystem, weight, dimensions);
        this.simcardsNumber = simcardsNumber;
        this.cameraResolution = cameraResolution;
    }

    public int getSimcardsNumber() {
        return simcardsNumber;
    }

    public void setSimcardsNumber(int simcardsNumber) {
        this.simcardsNumber = simcardsNumber;
    }

    public int getCameraResolution() {
        return cameraResolution;
    }

    public void setCameraResolution(int cameraResolution) {
        this.cameraResolution = cameraResolution;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "simcardsNumber=" + simcardsNumber +
                ", cameraResolution=" + cameraResolution ;
    }
}
class Laptop extends DigitalProduct{
    private String cpuType;
    private boolean isGaming;

    public Laptop(int productID, String name, String brand, int price, Seller seller, String description,
                  int memoryCapacity, int ram, String operatingSystem, int weight, String dimensions, String cpuType,
                  boolean isGaming) {
        super(productID, name, brand, price, seller, description, memoryCapacity, ram, operatingSystem, weight, dimensions);
        this.cpuType = cpuType;
        this.isGaming = isGaming;
    }

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    public boolean getIsGaming() {
        return isGaming;
    }

    public void setIsGaming(boolean isGaming) {
        this.isGaming = isGaming;
    }

    @Override
    public String toString() {
        return super.toString() +
                "cpuType='" + cpuType + '\'' +
                ", isGaming=" + isGaming ;
    }
}

abstract class ClothingProduct extends Product{
    private String producerCountry;
    private String material;

    public ClothingProduct(int productID, String name, String brand, int price, Seller seller, String description,
                           String producerCountry, String material) {
        super(productID, name, brand, price, seller, description);
        this.producerCountry = producerCountry;
        this.material = material;
    }

    public String getProducerCountry() {
        return producerCountry;
    }

    public void setProducerCountry(String producerCountry) {
        this.producerCountry = producerCountry;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "producerCountry='" + producerCountry + '\'' +
                ", material='" + material ;
    }
}
class Dress extends ClothingProduct{
    enum type
    {SHIRT, JEANS, SKIRT, PANTS}
    private type clothesType;
    private int size;

    public Dress(int productID, String name, String brand, int price, Seller seller, String description, String producerCountry,
                 String material, type clothesType, int size) {
        super(productID, name, brand, price, seller, description, producerCountry, material);
        this.clothesType = clothesType;
        this.size = size;
    }

    public type getClothesType() {
        return clothesType;
    }

    public void setClothesType(type clothesType) {
        this.clothesType = clothesType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "clothesType=" + clothesType +
                ", size=" + size ;
    }
}
class Shoes extends ClothingProduct{
    enum type
    {BOOTS, SNEAKERS, HEELS, SANDALS}
    private type shoesType;
    private  int size;

    public Shoes(int productID, String name, String brand, int price, Seller seller, String description, String producerCountry,
                 String material, type shoesType, int size) {
        super(productID, name, brand, price, seller, description, producerCountry, material);
        this.shoesType = shoesType;
        this.size = size;
    }

    public type getShoesType() {
        return shoesType;
    }

    public void setShoesType(type shoesType) {
        this.shoesType = shoesType;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return super.toString() +
                "shoesType=" + shoesType +
                ", size=" + size ;
    }
}

abstract class HomeAppliance extends Product{
    private int energyConsumptionDegree;
    private boolean haveGuarantee;

    public HomeAppliance(int productID, String name, String brand, int price, Seller seller, String description,
                         int energyConsumptionDegree, boolean haveGuarantee) {
        super(productID, name, brand, price, seller, description);
        this.energyConsumptionDegree = energyConsumptionDegree;
        this.haveGuarantee = haveGuarantee;
    }

    public int getEnergyConsumptionDegree() {
        return energyConsumptionDegree;
    }

    public void setEnergyConsumptionDegree(int energyConsumptionDegree) {
        this.energyConsumptionDegree = energyConsumptionDegree;
    }

    public boolean getHaveGuarantee() {
        return haveGuarantee;
    }

    public void setHaveGuarantee(boolean haveGuarantee) {
        this.haveGuarantee = haveGuarantee;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "energyConsumptionDegree=" + energyConsumptionDegree +
                ", haveGuarantee=" + haveGuarantee ;
    }
}
class TV extends HomeAppliance{
    private int resolution;
    private int screenSize;

    public TV(int productID, String name, String brand, int price, Seller seller, String description,
              int energyConsumptionDegree, boolean haveGuarantee, int resolution, int screenSize) {
        super(productID, name, brand, price, seller, description, energyConsumptionDegree, haveGuarantee);
        this.resolution = resolution;
        this.screenSize = screenSize;
    }

    public int getResolution() {
        return resolution;
    }

    public void setResolution(int resolution) {
        this.resolution = resolution;
    }

    public int getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(int screenSize) {
        this.screenSize = screenSize;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "resolution=" + resolution +
                ", screenSize=" + screenSize ;
    }
}
class Refrigerator extends HomeAppliance{
    private int capacity;
    private String type;
    private boolean haveFridge;

    public Refrigerator(int productID, String name, String brand, int price, Seller seller, String description,
                        int energyConsumptionDegree, boolean haveGuarantee, int capacity, String type, boolean haveFridge) {
        super(productID, name, brand, price, seller, description, energyConsumptionDegree, haveGuarantee);
        this.capacity = capacity;
        this.type = type;
        this.haveFridge = haveFridge;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean getHaveFridge() {
        return haveFridge;
    }

    public void setHaveFridge(boolean haveFridge) {
        this.haveFridge = haveFridge;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "capacity=" + capacity +
                ", type='" + type + '\'' +
                ", haveFridge=" + haveFridge ;
    }
}
class Stove extends HomeAppliance{
    private int burnersNumber;
    private String material;
    private boolean haveOven;

    public Stove(int productID, String name, String brand, int price, Seller seller, String description,
                 int energyConsumptionDegree, boolean haveGuarantee, int burnersNumber, String material, boolean haveOven) {
        super(productID, name, brand, price, seller, description, energyConsumptionDegree, haveGuarantee);
        this.burnersNumber = burnersNumber;
        this.material = material;
        this.haveOven = haveOven;
    }

    public int getBurnersNumber() {
        return burnersNumber;
    }

    public void setBurnersNumber(int burnersNumber) {
        this.burnersNumber = burnersNumber;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public boolean getHaveOven() {
        return haveOven;
    }

    public void setHaveOven(boolean haveOven) {
        this.haveOven = haveOven;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "burnersNumber=" + burnersNumber +
                ", material='" + material + '\'' +
                ", haveOven=" + haveOven ;
    }
}

class Food extends Product{
    private String productionDate;
    private String expirationDate;

    public Food(int productID, String name, String brand, int price, Seller seller, String description,
                String productionDate, String expirationDate) {
        super(productID, name, brand, price, seller, description);
        this.productionDate = productionDate;
        this.expirationDate = expirationDate;
    }

    public String getProductionDate() {
        return productionDate;
    }

    public void setProductionDate(String productionDate) {
        this.productionDate = productionDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return  super.toString() +
                "productionDate='" + productionDate + '\'' +
                ", expirationDate='" + expirationDate ;
    }
}


class Comment{
    enum commentStatus
    {WAITING, APPROVED, UNAPPROVED}
    private commentStatus commentStatus;
    private  Account commentingUser;
    private  Product commentedProduct;
    private String commentText;
    private boolean haveBought;

    public Comment(Account commentingUser, Product commentedProduct, String commentText, boolean haveBought) {
        this.commentStatus = commentStatus.WAITING;
        this.commentingUser = commentingUser;
        this.commentedProduct = commentedProduct;
        this.commentText = commentText;
        this.haveBought = haveBought;
    }

    public Comment.commentStatus getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(Comment.commentStatus commentStatus) {
        this.commentStatus = commentStatus;
    }

    public Account getCommentingUser() {
        return commentingUser;
    }

    public void setCommentingUser(Account commentingUser) {
        this.commentingUser = commentingUser;
    }

    public Product getCommentedProduct() {
        return commentedProduct;
    }

    public void setCommentedProduct(Product commentedProduct) {
        this.commentedProduct = commentedProduct;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public boolean getHaveBought() {
        return haveBought;
    }

    public void setHaveBought(boolean haveBought) {
        this.haveBought = haveBought;
    }

    @Override
    public String toString() {
        return  "commentStatus=" + commentStatus +
                ", commentingUser=" + commentingUser +
                ", commentedProduct=" + commentedProduct +
                ", commentText='" + commentText + '\'' +
                ", haveBought=" + haveBought ;
    }

}


class Score{
    private Account scoringUser;
    private int score;
    private Product scoredProduct;

    public Score(Account scoringUser, int score, Product scoredProduct) {
        this.scoringUser = scoringUser;
        this.score = score;
        this.scoredProduct = scoredProduct;
    }

    public Account getScoringUser() {
        return scoringUser;
    }

    public void setScoringUser(Account scoringUser) {
        this.scoringUser = scoringUser;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Product getScoredProduct() {
        return scoredProduct;
    }

    public void setScoredProduct(Product scoredProduct) {
        this.scoredProduct = scoredProduct;
    }
}

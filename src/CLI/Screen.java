package CLI;

import BusinessLayer.Market;

import java.time.LocalDate;
import java.util.Scanner;


public abstract class Screen implements Runnable{
    protected final static Scanner scanner = new Scanner(System.in);
    protected final Market market;
    private final String[] menuOptions;
    protected final Screen caller;


    public Screen(Market market, String[] menuOptions){
        this.market = market;
        this.menuOptions = menuOptions;
        caller = null;
    }

    public Screen(Screen caller, String[] menuOptions){
        this.market = caller.market;
        this.menuOptions = menuOptions;
        this.caller = caller;
    }

    protected void printMenu(){
        for (int i = 0 ; i < menuOptions.length; i++)
            System.out.println((i + 1) + " -- " + menuOptions[i]);
    }

    protected int runMenu(){
        System.out.println("\nWhat would you like to do?");
        printMenu();
        int option = 0;
        try {
            option = getNumber(1, menuOptions.length);
        } catch (RuntimeException ignored) {
        }
        return option;
    }

    protected void endRun(){
        if (caller != null)
            new Thread(caller).start();
        else
            System.out.println("Thanks for using software by Group_L!\nBye bye!");
    }

    //Input returns only for int!
    protected int getInput(){
        boolean stopWait = true;
        int input = -1;

        while(stopWait){
            if(scanner.hasNextInt()) {
                input = scanner.nextInt();
                stopWait = false;
            }
            else{
                System.out.println("Enter an integer please!");
                scanner.nextLine();

            }
        }
        scanner.nextLine();

        return input;
    }

    //Input returns only for float!
    protected float getInputFloat(){
        boolean stopWait = true;
        float input = -1;

        while(stopWait){
            if(scanner.hasNextFloat()) {
                input = scanner.nextFloat();
                stopWait = false;
            }
            else{
                System.out.println("Enter an integer please!");
                scanner.nextLine();

            }
        }
        scanner.nextLine();

        return input;
    }

    protected LocalDate getDate() {
        while (true) {
            try {
                System.out.println("Please insert date in format: DD/MM/YYYY");
                String dateInput = scanner.nextLine();
                if (dateInput.equals("c")) {
                    System.out.println("Cancelling command");
                    return null;
                }
                String[] date = dateInput.split("/");
                return LocalDate.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]));
            } catch (Exception p) {
                System.out.println("Date in wrong format! please try again. c to cancel command");
            }
        }
    }

    protected int getStoreID() {
        System.out.println("Please insert store ID of store you are interested in.");
        System.out.println("Current store IDs are:");
        return scanner.nextInt();
    }

    public static int getNumber() {
        while (true) {
            try {
                int number = Integer.parseInt(getLine());
                if (number == -1) {
                    throw new RuntimeException("Inserted -1, System Closing");
                } else {
                    return number;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter an integer");
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception ex) {
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
    }

    public static int getNumber(int min) {
        while (true) {
            try {
                int number = Integer.parseInt(getLine());
                if (number == -1) {
                    throw new RuntimeException("Inserted -1, System Closing");
                } else if (number < min) {
                    System.out.println("Please enter an integer bigger or equal to " + min);
                } else {
                    return number;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter an integer");
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception ex) {
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
    }

    public static int getNumber(int min, int max) {
        while (true) {
            try {
                int number = Integer.parseInt(getLine());
                if (number == -1) {
                    throw new RuntimeException("Inserted -1, System Closing");
                } else if (number < min || number > max) {
                    System.out.println("Please enter an integer between " + min + " and " + max);
                } else {
                    return number;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter an integer");
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception ex) {
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
    }

    public static int getNumber(int min, String because) {
        while (true) {
            try {
                int number = Integer.parseInt(getLine());
                if (number == -1) {
                    throw new RuntimeException("Inserted -1, System Closing");
                } else if (number < min) {
                    System.out.println("Please enter an integer bigger or equal to " + min + " because: " + because);
                } else {
                    return number;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Please enter an integer");
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception ex) {
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
    }

    public static String getString() {
        while (true) {
            try {
                String string = getLine();
                if (string.equals("-1"))
                    throw new RuntimeException("Inserted -1, System Closing");
                else
                    return string;
            }
            catch (RuntimeException e){
                throw e;
            }
            catch (Exception e) {
                System.out.println("Unexpected error occurred");
                System.out.println("Please try again");
            }
        }
    }

    private static String getLine() {
        return scanner.nextLine().trim();
    }


}
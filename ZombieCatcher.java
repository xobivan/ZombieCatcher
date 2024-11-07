import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class ZombieCatcher{
    static int zombieIn, zombieOut;
    static Scanner scanner = new Scanner(System.in);

    public static int fileReader(String fileName){
        int potentialZombies = 0;
        
        try{
            File file = new File(fileName);
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()){
                String line = reader.nextLine().trim();
                String[] parts = line.split(" ");
                
                String visitorName = parts[0];
                int arrival = Integer.parseInt(parts[1]);
                int departure = Integer.parseInt(parts[2]);

                if (overlappingDayAndNightPeriods(zombieIn, zombieOut, arrival, departure)){
                    potentialZombies++;
                    System.out.println(visitorName + "needs to be quarantined");
                }else {
                    System.out.println(visitorName + " does not need to be quarantined.");
                }  
                
            }
    
        } catch(FileNotFoundException e){
            System.out.println("WARNING: "+fileName+" not found.");
        } catch (NumberFormatException e) {
            System.out.println("WARNING: Number format issue encountered in " + fileName + ". Check your data.");
        }

        return potentialZombies;

    }
    public static boolean overlappingDayAndNightPeriods(int start_1, int end_1, int start_2, int end_2) {
        // Here I looked at all possible types of overlapping and wrote a block of code for every one.
        // Basic logic is that in case of crossing midnight I split the time span in before and after midnight(7 - 24 and 00 - 6 respectfully)
        // In this way I brough all crossing midnight cases to to the basic case described in the method overlappingPeriods()

        // Case 1: both are crossing midnight
        if(start_1>end_1 && start_2>end_2){
            return overlappingPeriods(start_1, 24, start_2, 24) || overlappingPeriods(0, end_1, 0, end_2);
        }

        //Case 2: Only zombie is crossing midnight
        if (start_1>end_1){
            return overlappingPeriods(start_1, 24, start_2, end_2) || overlappingPeriods(0, end_1, start_2, end_2);
        }

        //Case 3: Only visitor crosses midnight
        if(start_2 > end_2){
            return overlappingPeriods(start_1, end_1, start_2, 24) || overlappingPeriods(start_1, end_1, 0, end_2);
        }
        //Case 5: None of them is crossing midnight
        if(start_1<end_2 && start_2<end_1){
            return overlappingPeriods(start_1, end_1, start_2, end_2);
        }

        return false;

    }
    
    // Two periods of time intersect iff start_1<end_2 AND start_2<end_1
    public static boolean overlappingPeriods (int start_1, int end_1, int start_2, int end_2){
        return start_1<end_2 && start_2<end_1; 
    }

    // Check if time input satisfies the rquirement
    public static boolean checkTimeFormat(int start, int end){
        if(start == end){
            return false;
        } else if(start > 23 || end > 23) {
            return false;
        }else if(start < 0 ||  end < 0){
            return false;
        }
        return true;
    }
    public static int getVisitors(int zombieIn, int zombieOut){
        int quarantinedVisitors = 0;
        System.out.println("Enter the number of visitors: ");
        int numOfVisit = Integer.parseInt(scanner.nextLine());
        
        for (int i = 0; i < numOfVisit; i++){
            while(true){
                System.out.println("Enter the visitor's name: ");
                String visitorName = scanner.nextLine();
                System.out.println("Enter the arrival time: ");
                int arrival = Integer.parseInt(scanner.nextLine());
                System.out.println("Enter the departure time: ");
                int departure = Integer.parseInt(scanner.nextLine());
                
                if(!checkTimeFormat(arrival, departure)){
                    System.out.println("Please, enter correct time format. Try again");
                    continue;
                }
                
                if(overlappingDayAndNightPeriods(zombieIn, zombieOut, arrival, departure)){
                    System.out.println(visitorName + " needs to be quarantined.");
                    quarantinedVisitors++;
                } else {
                    System.out.println(visitorName + " does not need to be quarantined.");
                }

                break;
            }
        }
        return quarantinedVisitors;
    }
    
    public static void main (String[] args){

        while(true){
            System.out.println("Enter the start time: ");
            zombieIn = Integer.parseInt(scanner.nextLine());
            System.out.println("Enter the end time: ");
            zombieOut = Integer.parseInt(scanner.nextLine());

            if (!checkTimeFormat(zombieIn, zombieOut)) {
                System.out.println("Please, enter correct time format! Try again.");
                continue;
            }
            break;
        }

        int totalQuarantinedVisitors = 0;

        if (args.length == 0) {
            totalQuarantinedVisitors = getVisitors(zombieIn, zombieOut);
    
        } else {
            for(int i = 0; i < args.length; i ++){
                String fileName = args[i];
                int quarantined = fileReader(fileName);
                totalQuarantinedVisitors += quarantined;

            }
        }
            

        System.out.println("Number of potential zombies: " + totalQuarantinedVisitors);

            
    }

    


}

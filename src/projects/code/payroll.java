import java.util.*;
import java.text.*;
import java.io.*;


public class payroll {
  public static void main(String[] args) throws IOException {
    File outFile = new File ("Payroll.txt");
    FileWriter fWriter = new FileWriter (outFile, true);
    PrintWriter pWriter = new PrintWriter (fWriter, true);
    DateFormat dateFormat = new SimpleDateFormat ("MM/dd/yyyy");
    Date date = new Date();
    Scanner jake = new Scanner(System.in);
    DecimalFormat f = new DecimalFormat("0.00");
    double hour, extra, hourly, overtime, labor, pay, gross, ssm, net, incomegross, incometax;
    int percent, income, report, marriage, allowance, repeat;
    String worker;
    System.out.println(); 
    System.out.println(" -------------------------------------");
    System.out.println(" Welcome to Jake's Payroll Calculator!");
    System.out.println(" -------------------------------------");
    System.out.println("              " + dateFormat.format(date));
    do {
      System.out.println();
          System.out.print(" What is the employee's name? ");
          worker = jake.next();
          do {
            System.out.print(" How many hours has " + worker +
            " worked this week? ");
            hour = jake.nextDouble();
          } while ( hour < 0);

          if (hour > 0) {
        do {
          System.out.print(" How much does " + worker + " make a hour? ");
          hourly = jake.nextDouble();
          if (hour > 40) {
            extra = hour - 40;
                    overtime = (hour - 40) * (hourly * 1.5);
                    hour = hour - extra;
                } else {
                    overtime = 0;
                    extra = 0;
                }
        } while (hourly < 0);
      } else {
        overtime = 0;
        hour = 0;
        hourly = 0;
        extra = 0;
      }

          do {
        System.out.print(" How much total mechanic labor has " + worker + " done? ");
        labor = jake.nextDouble();
        
        if (labor > 0) {
          do {
            System.out.println(" What labor percentage does " + worker + " take in? ");
                    System.out.print(" Enter percent as a  whole number  ");
                    percent = jake.nextInt();
                 } while (percent < 0);
                
          pay = (labor * percent) / 100;
          gross = (hour * hourly + overtime + pay);
        } else {
          gross = (hour * hourly + overtime);
          pay = 0;
          percent = 0;
        }
      } while (labor < 0);
          
      System.out.println(" " + worker + "'s gross pay is -- " + f.format(gross) + " --");
          ssm = gross * .0765;

          do {
        System.out.print(" Is " + worker + " single(1) or married(2)? "); 
            marriage = jake.nextInt();
          } while (marriage != 1 &&  marriage != 2);
        
      do {
        System.out.print(" How many allowances does " + worker + " have? ");
        allowance = jake.nextInt();
          } while ( allowance < 0 );
        
      incomegross = gross - (allowance * 76);
        
      if (marriage == 1) {
            if (incomegross < 218 && incomegross >= 43) {
                incometax = (((incomegross - 43) * 10) / 100);
            } else if (incomegross < 753 && incomegross >= 218) {
                incometax = ((((incomegross - 218) * 15) / 100) + 17.5);
            } else if (incomegross < 1762 && incomegross >= 753) {
                incometax = ((((incomegross - 753) * 25) / 100) + 97.75);
            } else if (incomegross < 3627 && incomegross >= 1762) {
                incometax = ((((incomegross - 1762) * 28) / 100) + 350);
            } else if (incomegross < 7834 && incomegross >= 3627) {
                incometax = ((((incomegross - 3627) * 33) / 100) + 872.2);
            } else if (incomegross < 7865 && incomegross >= 7834) {
                incometax = ((((incomegross - 7834) * 35) / 100) + 2260.51);
            } else if (incomegross >= 7865) {
                incometax = ((((incomegross - 7865) * 39.6) / 100) + 2271.36);
            } else {
                incometax = 0;
            }
          } else {
            if (incomegross < 512 && incomegross >= 163) {
                incometax = (((incomegross - 163) * 10) / 100);
            } else if (incomegross < 1582 && incomegross >= 512) {
                incometax = ((((incomegross - 512) * 15) / 100) + 34.9);
            } else if (incomegross < 3025 && incomegross >= 1582) {
                incometax = ((((incomegross - 1582) * 25) / 100) + 195.4);
            } else if (incomegross < 4525 && incomegross >= 3025) {
                incometax = ((((incomegross - 3025) * 28) / 100) + 556.15);
            } else if (incomegross < 7953 && incomegross >= 4525) {
                incometax = ((((incomegross - 4525) * 33) / 100) + 976.15);
            } else if (incomegross < 8963 && incomegross >= 7953) {
                incometax = ((((incomegross - 7953) * 35) / 100) + 2107.39);
            } else if (incomegross >= 8963) {
                incometax = ((((incomegross - 7865) * 39.6) / 100) + 2460.89);
            } else {
                incometax = 0;
            }
          }
          
      int incometaxwhole = (int) incometax;
          net = gross - ssm - incometaxwhole;
          System.out.println(" " + worker + "'s paycheck is:");
          System.out.println();
          System.out.println(" $$ " + f.format(net) + " $$ ");
          System.out.println();
          System.out.println(" Thank you for using Jake's Payroll Calculator!!");
          System.out.println("   :-) :-) :-) :-) :-) :-) :-) :-) :-) :-) :-)  ");
          System.out.println();
          System.out.print(" Press 1 for a full report or any other key " + "to exit program: ");
        
      report = jake.nextInt();
        
      if (report == 1) {
            System.out.println();
            System.out.println("         " + worker);
            System.out.println(" ________________");
            System.out.println("       Gross Pay:   " + f.format(gross));
            System.out.println("        Overtime:   " + f.format(overtime));
            System.out.println("  Mechanic Labor:   " + f.format(pay));
            System.out.println(" Social Security:   " + f.format(ssm));
            System.out.println("    and Medicare   ");
            System.out.println("      Income Tax:   " + incometaxwhole);
            System.out.println("      Net Income:   " + f.format(net));
            System.out.println();
            System.out.println();
            System.out.println(" " +worker + " has worked " +hour+ " hours for " +hourly+ " dollars per hour ");
            System.out.println(" " +worker + " worked " + extra + " hours overtime for " +overtime );
            System.out.println(" " +worker + " total mechanic labor is " + labor + " earning " + percent + " percent");
            System.out.println(" Single(1) Married(2): " + worker + " is " + marriage);
            System.out.println(" " + worker + " has " + allowance + " allowances ");
            System.out.println();
            System.out.println();
        System.out.print (" Want to save information? Press(1) ");
            
        int save = jake.nextInt();
        
        if ( save == 1) {
          pWriter.println("----" + worker + "----");
                pWriter.println("              " + dateFormat.format(date));
                pWriter.println(" ________________");
                pWriter.println("       Gross Pay:   " + f.format(gross));
                pWriter.println("        Overtime:   " + f.format(overtime));
                pWriter.println("  Mechanic Labor:   " + f.format(pay));
                pWriter.println(" Social Security:   " + f.format(ssm));
                pWriter.println("    and Medicare   ");
                pWriter.println("      Income Tax:   " + incometaxwhole);
                pWriter.println("      Net Income:   " + f.format(net));
                pWriter.println();
                pWriter.println();
                pWriter.println(" " +worker + " has worked " +hour+ " hours for " +hourly+ " dollars per hour ");
                pWriter.println(" " +worker + " worked " + extra + " hours overtime for " +overtime );
                pWriter.println(" " +worker + " total mechanic labor is " + labor + " earning " + percent + " percent");
                pWriter.println(" Single(1) Married(2): " + worker + " is " + marriage);
              pWriter.println(" " + worker + " has " + allowance + " allowances ");
                pWriter.println();
            }

        } else {
            System.out.println(" System Terminated");
            System.out.println();
            System.exit(0);
        }
 
    System.out.print(" Have another employee? Press(3) ");
      repeat = jake.nextInt();
     } while (repeat == 3); 
          
  
    pWriter.close();
   }
}

/*public class PayrollSearch
{
  public static void main (String[] args) throws IOException
  {
  FileReader freader = new FileReader ("Payroll.txt");
  BufferedReader buff = new BufferedReader (freader);
  Scanner sc = new Scanner (System.in);

  System.out.print ("Enter employee name: ");
  String name = sc.next();

  String line = buff.readLine();

  while (line != null)
  {

    if (line.indexOf(name) > 0)
    {
      for (int i = 0; i < 17; i++)
      {
        System.out.println(line);
      line = buff.readLine();
      }
      System.out.println();
    }
    
    line = buff.readLine();
  }
  buff.close();
  }
}*/


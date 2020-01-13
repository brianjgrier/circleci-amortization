import java.util.ArrayList;
import java.util.List;

public class amort {

    public class LoanTerms{
        Double principal;
        Double apr;
        Integer numPayments;
        Integer startYear;
        Integer startMonth;
        Double monthlyPayment;
        Double monthlyInt;

        private LoanTerms(double initialPrinc, double yapr, int numPaymnts, int year, int month){
            principal = initialPrinc;

            apr = yapr ;
            numPayments = numPaymnts;
            startYear = year;
            startMonth = month;

            monthlyInt = apr/100.0/12.0;
            Double mnthIntP1 = Math.pow(monthlyInt + 1, numPayments);
            monthlyPayment = principal * ( (monthlyInt * mnthIntP1)/(mnthIntP1-1));
            long tmpAmt = Math.round(100.0 * monthlyPayment);
            monthlyPayment = tmpAmt / 100.0;
        }

        private String getTerms(){
            String Result = "Principal: " + principal.toString() + "; ";
            Result += "Interest Rate: " + apr.toString() + "; ";
            Result += "Number Payments: " + numPayments.toString() + "; ";
            Result += "Monthly Payment: " + monthlyPayment.toString();
            return Result;
        }

        private void process_loan(ExtraPayments extras){
            Double remainingPrincipal = principal;
            Double interestPayment = 0.0;
            Double principalPayment = 0.0;
            Double interestPaid = 0.0;
            Double currentPayment = monthlyPayment;
            Integer currentYear = startYear;
            Integer currentMonth = startMonth;

            while (remainingPrincipal > 0.0){
                interestPayment = remainingPrincipal * monthlyInt;
                principalPayment = monthlyPayment - interestPayment;
                if (principalPayment > remainingPrincipal ){
                    principalPayment = remainingPrincipal;
                    currentPayment = principalPayment + interestPayment;
                }
                interestPaid += interestPayment;
               double  extraPrincipal = extras.getExtraPrincipal(currentMonth, currentYear);
                remainingPrincipal -= (principalPayment + extraPrincipal);
                if (remainingPrincipal > 0){
                    currentMonth += 1;
                    if ( currentMonth > 12){
                        currentMonth = 1;
                        currentYear += 1;
                    }
                }
            }
            System.out.println("Last Payment Date: " + currentMonth.toString() + "/" + currentYear.toString() );
            System.out.println(String.format("Final payment: %.2f", currentPayment));
            System.out.println(String.format("Total Interest Paid: %.2f", interestPaid));
        }
    }


    public class ExtraPayment{
        Integer yearStart;
        Integer yearLast;
        Integer monthStart;
        double extraPrincipal;

        private ExtraPayment(Integer yearBegin, Integer month, Integer yearEnd, double principal){
            this.yearStart = yearBegin;
            this.monthStart = month;
            this.yearLast = yearEnd;
            this.extraPrincipal = principal;
        }
    }


    public class ExtraPayments{
        List<ExtraPayment> monthly;
        List<ExtraPayment> annually;
        List<ExtraPayment> oneTime;

        private ExtraPayments(){
            monthly = new ArrayList<>();
            annually = new ArrayList<>();
            oneTime = new ArrayList<>();
        }

        private double getExtraPrincipal(Integer month, Integer year){
            double result = 0.0;
            int numMonthly = this.monthly.size();
            for (int i=0; i<numMonthly; i++){
                ExtraPayment it = this.monthly.get(i);
                if (year > it.yearStart && year <= it.yearLast){
                    result += it.extraPrincipal;
                }
                if (year.equals(it.yearStart) && month >= it.monthStart){
                    result += it.extraPrincipal;
                }
            }

            int entries = this.annually.size();
            for (int i=0; i<entries; i++){
                ExtraPayment it = this.annually.get(i);
                if (year >= it.yearStart && year <= it.yearLast && month.equals(it.monthStart)){
                    result += it.extraPrincipal;
                }
            }

            return result;
        }
    }

    private void doIt(){
        LoanTerms loan;
        loan = new LoanTerms(168000.0, 4.0, 360, 2012, 8);
        ExtraPayments extras = new ExtraPayments();
        extras.monthly.add(new ExtraPayment(2016, 8, 2018, 200));
        extras.monthly.add(new ExtraPayment(2019, 2, 2045, 2000));
//        extras.monthly.add(new ExtraPayment(2020, 1, 2020, 400));
//        extras.monthly.add(new ExtraPayment(2021, 1, 2045, 500));
        extras.annually.add(new ExtraPayment(2019, 3, 2045, 5000));
        extras.annually.add(new ExtraPayment(2019, 9, 2045, 5000));
        System.out.println(loan.getTerms());
        loan.process_loan(extras);
    }


    public static void main(String[] args) {
        System.out.println("Running");
        amort dasProgram = new amort();
        dasProgram.doIt();


    }
}

package info.modprobe.crdt.demo.us2024elections;

import static java.util.concurrent.TimeUnit.SECONDS;

public class RunElection {
    public static void main(String[] args) throws InterruptedException {
        final RegionalNode california = new RegionalNode("California");
        final RegionalNode newYork = new RegionalNode("New York");
        final RegionalNode everythingElse = new RegionalNode("Everything Else");

        california.voteForTrump();
        california.voteForKamala();
        california.voteForKamala();

        newYork.voteForTrump();
        newYork.voteForKamala();
        newYork.voteForKamala();

        everythingElse.voteForTrump();
        everythingElse.voteForTrump();
        everythingElse.voteForTrump();
        everythingElse.voteForTrump();
        everythingElse.voteForKamala();


        int attempt = 1;
        final int max = 10;
        while(!((california.getNationalResults().kamala() == newYork.getNationalResults().kamala()
                && everythingElse.getNationalResults().kamala() == california.getNationalResults().kamala())

            && (california.getNationalResults().trump() == newYork.getNationalResults().trump()
                && everythingElse.getNationalResults().trump() == california.getNationalResults().trump()))) {

            System.out.println("National results are still synchronizing... " + attempt);
            attempt++;

                                                                                                                                                     california.sync();
            newYork.sync();
            everythingElse.sync();
            SECONDS.sleep(1);

            if(attempt > max) {
                System.out.println("National results did not converge after " + attempt +" attempts");
                break;
            }

        }

    }
}

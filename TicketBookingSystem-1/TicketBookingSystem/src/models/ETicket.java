package models;

public class ETicket {
    private Transaction transaction;

    public ETicket(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public String toString() {
        String ticket = String.format("ETicket-%d\n", this.transaction.getPnr());
        for(Passenger p: transaction.getPassenger()){
            ticket += p.getName()+" "+p.getAge()+" - "+p.getSeatNumber()+"\n";
        }
        return ticket;
    }
}

package ch.makery.address;

/**
 * Created by truposchet on 07.06.16.
 */
public class SaveCarrier {
    public double coefficient;
    public String filename;
    public int compareTo(SaveCarrier saveCarrier) {
        if (this.coefficient == saveCarrier.coefficient) return 0;
        if (this.coefficient > saveCarrier.coefficient) return 1;
        return -1;
    }
}
package Keygen;

import java.math.BigInteger;

public class Keyfile{
    BigInteger e;
    BigInteger n;

    public Keyfile(){}

    public Keyfile(BigInteger e, BigInteger n){
        this.e = e;
        this.n = n;
    }

    public BigInteger getE() {
        return e;
    }

    public BigInteger getN() {
        return n;
    }
}

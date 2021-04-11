package Keygen;

import java.math.BigInteger;

public class Keygen {
    private final BigInteger p;
    private final BigInteger q;
    private final BigInteger n;
    private final BigInteger t;
    private final BigInteger e;
    private final BigInteger d;
    private final Key publicKey;
    private final Key privateKey;

    public Keygen(BigInteger p, BigInteger q) {
        this.p = p;
        this.q = q;
        this.n = p.multiply(q);
        this.t = p.subtract(BigInteger.ONE).multiply(this.q.subtract(BigInteger.ONE));
        this.e = this.getCoPrime(this.t);
        this.d = this.e.modInverse(this.t);
        this.publicKey = new Key(n, this.e);
        this.privateKey = new Key(n, this.d);
    }

    public boolean isCoPrime(BigInteger c, BigInteger n) {
        BigInteger one = new BigInteger("1");
        return c.gcd(n).equals(one);
    }

    public BigInteger getCoPrime(BigInteger n) {
        BigInteger result = new BigInteger(n.toString());
        BigInteger one = new BigInteger("1");
        BigInteger two = new BigInteger("2");

        for(result = result.subtract(two); result.intValue() > 1 && !result.gcd(n).equals(one); result = result.subtract(one)) {
        }

        return result;
    }

    public BigInteger getP() {
        return this.p;
    }

    public BigInteger getQ() {
        return this.q;
    }

    public BigInteger getN() {
        return this.n;
    }

    public BigInteger getT() {
        return this.t;
    }

    public BigInteger getE() {
        return this.e;
    }

    public BigInteger getD() {
        return this.d;
    }

    public Key getPublicKey() {
        return this.publicKey;
    }

    public Key getPrivateKey() {
        return this.privateKey;
    }
}

package Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PaillierCypher {
    public long p, q, n, lambda, g, mu, rand;

    public boolean isPrime(long x) {
        if (x % 2 == 0) {
            return false;
        } else {
            for (int i = 3; i < Math.sqrt(x); i++) {
                if (x % i == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public long lcm(long p, long q) {
        long tmpp = p, tmpq = q;
        while (tmpp != tmpq) {
            if (tmpp < tmpq) {
                tmpp += p;
            } else {
                tmpq += q;
            }
        }
        return tmpp;
    }

    public long mod(long a, long b, long m) {
        long res = 0, x = a, y = b;
        x = x % m;
        while (y > 0) {
            if (y % 2 == 1) {
                res = (res + x) % m;
            }
            x = (x*2) % m;
            y /= 2;
        }
        return res % m;
    }

    public long modularPower(long m, long e, long b) {
        long res = 1,base = m, exp = e;
        base = base % b;
        while (exp > 0){
            if (exp % 2 == 1){
                res = mod(res, base, b);
            }
            exp = exp / 2;
            base = mod(base,base,b);
        }
        return res;
    }

    public long modInverse(long a, long m){
        for (int x = 1; x < m; x++)
            if (((a%m) * (x%m)) % m == 1)
                return x;
        return 1;
    }

    public long lFunction(long x){
        return (x-1)/n;
    }

    public void generateAttribute() {
        long[] pq = {0, 0};
        Random random = new Random();

        for (int i = 0; i < 2; i++) {
            long tmpl = 0;
            while ((tmpl < 1000) || !(isPrime(tmpl))) {
                tmpl = random.nextLong() % 9999;
            }
            pq[i] = tmpl;
        }
        p = pq[0]; q = pq[1];
        n = pq[0] * pq[1];
        lambda = lcm(pq[0]-1,pq[1]-1);

        g = (long) ((n*n) * random.nextDouble());

        long x = modularPower(g,lambda,n*n);
        mu = modInverse(lFunction(x), n);

        rand = (long) (n * random.nextDouble());
        while (!isCoprime(rand, n)){
            rand = (long) (n * random.nextDouble());
        }
    }

    public boolean isCoprime(long x, long y) {
        return (x * y) == lcm(x, y);
    }

    public int longLength(long x) {
        int l = 0;
        long ten = 1;
        while (x / ten != 0) {
            l += 1;
            ten *= 10;
        }
        return l;
    }

    public List<Long> blockify(String text, long base){
        List<Long> blocks = new ArrayList<>();
        long m = 0;
        int i = 0;
        long tmpl = 0;
        while (i < text.length()){
            int tmp = text.charAt(i);
            int l = longLength(m);
            if (l < 16){
                tmpl = m * 1000 + tmp;
            }
            if ((tmpl < base) && (l < 16) && (i < text.length()-1)){
                m = tmpl;
            } else {
                if (i != text.length()-1){
                    i -= 1;
                } else {
                    if ((tmpl < base) && (l < 16)){
                        m = tmpl;
                    } else {
                        blocks.add(m);
                        blocks.add((long)tmp);
                        return blocks;
                    }
                }
                blocks.add(m);
                m = 0;
            }
            i += 1;
        }
        return blocks;
    }

    public String deblockify(List<Long> blocks){
        StringBuilder out = new StringBuilder();
        for (int i = blocks.size()-1; i >= 0; i--){
            long block = blocks.get(i);
            while (block != 0){
                out.append((char) (block % 1000));
                block = block / 1000;
            }
        }
        return out.reverse().toString();
    }

    public String encrypt(String plaintext){
        List<Long> blocks = blockify(plaintext, n);
        List<Long> cipher = new ArrayList<>();
        for (Long block : blocks){
            long tmpg = modularPower(g,block,n*n);
            long tmpr = modularPower(rand,n,n*n);
            cipher.add(mod(tmpg,tmpr,n*n));
        }
        return deblockify(cipher);
    }

    public String decrypt(String cipher){
        List<Long> blocks = blockify(cipher, n*n);
        List<Long> plain = new ArrayList<>();
        for (Long block : blocks){
            long tmpl = lFunction(modularPower(block,lambda,n*n));
            plain.add(mod(tmpl,mu,n));
        }
        return deblockify(plain);
    }

    public static void main(String[] args) {
        PaillierCypher test = new PaillierCypher();
        test.generateAttribute();
        String cipher = test.encrypt("aku ingin terbang dan menari");
        System.out.println(cipher);
        System.out.println(test.decrypt(cipher));
    }
}

package Engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RSACypher {
    public long PUBLIC_KEY = 65537;
    public long PRIVATE_KEY;
    public long N;

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

    public long calculatePublic(long[] pair) {
        return pair[0] * pair[1];
    }

    public long calculateD(long cot) {
        long t = 0, newt = 1, r = cot, newr = PUBLIC_KEY;
        long tmp, quotient;
        while (newr != 0) {
            quotient = r / newr;
            tmp = t - quotient * newt;
            t = newt;
            newt = tmp;

            quotient = r / newr;
            tmp = r - quotient * newr;
            r = newr;
            newr = tmp;
        }
        t = t + cot;

        return t;
    }

    public long cotient(long p, long q) {
        return lcm(p - 1, q - 1);
    }

    public long[] generatePair() {
        long[] tmp = {0, 0};
        Random random = new Random();

        for (int i = 0; i < 2; i++) {
            long tmpl = 0;
            while ((tmpl < 50000000) || !(isPrime(tmpl))) {
                tmpl = random.nextLong() % 999999999;
            }
            tmp[i] = tmpl;
        }
        return tmp;
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

    public int longLength(long x) {
        int l = 0;
        long ten = 1;
        while (x / ten != 0) {
            l += 1;
            ten *= 10;
        }
        return l;
    }

    public List<Long> blockify(String text){
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
            if ((tmpl < N) && (l < 16) && (i < text.length()-1)){
                m = tmpl;
            } else {
                if (i != text.length()-1){
                    i -= 1;
                } else {
                    if ((tmpl < N) && (l < 16)){
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
    public String encrypt(String plaintext) {
//        susun jadi blok-blok 18 digit
        List<Long> blocks = blockify(plaintext);
//        encrypt
        List<Long> ciphers = new ArrayList<>();
        for (Long block : blocks) {
            ciphers.add(modularPower(block, PUBLIC_KEY, N));
        }
//        balikin jadi string
        return deblockify(ciphers);
    }

    public String decrypt(String cipher){
//        susun jadi blok 18 digit
        List<Long> blocks = blockify(cipher);
//        decrypt
        List<Long> plains = new ArrayList<>();
        for (Long block : blocks){
            plains.add(modularPower(block, PRIVATE_KEY, N));
        }
//        balikin jadi string
        return deblockify(plains);
    }

    public static void main(String[] args) {
        RSACypher test = new RSACypher();
        String cipher = test.encrypt("aku ingin tinggitwe");
        System.out.println(test.decrypt(cipher));
    }
}

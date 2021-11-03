package Engine;

import java.util.*;

public class ElGamalCypher {
    private Map<Character,Long> map = new HashMap<>();


    public void setAttribute(Character key, Long value){
        map.put(key,value);
    }
    public Long getAttribute(Character key) {
        return map.get(key);
    }

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

    public void generateAttribute() {
        long p = 0;
        Random random = new Random();
        while ((p < 50000000) || !(isPrime(p))) {
            p = random.nextLong() % 999999999;
        }
        map.put('P',p);

        long g = (long) (p * random.nextDouble());
        map.put('G', g);

        long x = 0;
        while ((x < 1) || (x > p-2)){
            x = (long) (p * random.nextDouble());
        }
        map.put('X', x);

        long y = modularPower(g, x, p);
        map.put('Y',y);

        this.map = map;
    }

    public long mod(long a, long b, long m) {
        long res = 0, x = a, y = b;
        x = x % m;
        while (y > 0) {
            if (y % 2 == 1) {
                res = (res + x) % m;
            }
            x = (x * 2) % m;
            y /= 2;
        }
        return res % m;
    }

    public long modularPower(long m, long e, long b) {
        long res = 1, base = m, exp = e;
        base = base % b;
        while (exp > 0) {
            if (exp % 2 == 1) {
                res = mod(res, base, b);
            }
            exp = exp / 2;
            base = mod(base, base, b);
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
            if ((tmpl < map.get('P')) && (l < 16) && (i < text.length()-1)){
                m = tmpl;
            } else {
                if (i != text.length()-1){
                    i -= 1;
                } else {
                    if ((tmpl < map.get('P')) && (l < 16)){
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

    public List<String> encrypt(String plaintext){
        List<String> ciphers = new ArrayList<>();

        List<Long> blocks = blockify(plaintext);

        Random random = new Random();
        long k = (long) (map.get('P') * random.nextDouble());
        while ((k < 1) && (k > map.get('P') - 2)){
            k = (long) (map.get('P') * random.nextDouble());
        }

        List<Long> cipherA = new ArrayList<>();
        List<Long> cipherB = new ArrayList<>();
        for(Long block : blocks){
            cipherA.add(modularPower(map.get('G'), k, map.get('P')));
            long yk = modularPower(map.get('Y'), k, map.get('P'));
            cipherB.add(mod(yk, block % map.get('P'), map.get('P')));
        }
        ciphers.add(deblockify(cipherA));
        ciphers.add(deblockify(cipherB));

        return ciphers;
    }

    public String decrypt(String cipherA, String cipherB){
        List<Long> plain = new ArrayList<>();
//        Cipher A
        List<Long> blocksA = blockify(cipherA);

        long invA = modularPower(blocksA.get(0), map.get('P') - 1 - map.get('X'), map.get('P'));

//        Cipher B
        List<Long> blocksB = blockify(cipherB);
        for (Long block : blocksB) {
            plain.add(mod(block, invA, map.get('P')));
        }

        return deblockify(plain);
    }


    public static void main(String[] args) {
        ElGamalCypher test = new ElGamalCypher();
        test.generateAttribute();
        List<String> ciphers = test.encrypt("aku ingin terbang dan menari");
        System.out.println(ciphers);
        System.out.println(test.decrypt(ciphers.get(0), ciphers.get(1)));
    }
}

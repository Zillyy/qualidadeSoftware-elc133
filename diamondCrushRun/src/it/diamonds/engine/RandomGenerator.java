package it.diamonds.engine;


import java.util.Random;


public class RandomGenerator implements RandomGeneratorInterface
{
    private Random rand;

    private long seed;


    public RandomGenerator(long seed)
    {
        rand = new Random();
        setSeed(seed);
    }


    public RandomGenerator()
    {
        this(System.nanoTime());
    }


    public void setSeed(long newSeed)
    {
        seed = newSeed;
        rand.setSeed(seed);
    }


    public long getSeed()
    {
        return seed;
    }


    public int extract(int module)
    {
        return rand.nextInt(module);
    }


    public RandomGeneratorInterface clone()
    {
        return new RandomGenerator(seed);
    }

}

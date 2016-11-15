package it.diamonds.engine;


public interface RandomGeneratorInterface
{
    int extract(int module);


    long getSeed();


    RandomGeneratorInterface clone();
}
